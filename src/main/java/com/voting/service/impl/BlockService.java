package com.voting.service.impl;

import com.voting.constants.ActionConstant;
import com.voting.constants.ErrorConstant;
import com.voting.dto.BlockChainDTO;
import com.voting.dto.BlockDTO;
import com.voting.dto.TransactionDTO;
import com.voting.dto.WalletDTO;
import com.voting.mapper.BlockMapper;
import com.voting.mapper.TransactionMapper;
import com.voting.model.request.BlockRequest;
import com.voting.model.request.MineTransactionRequest;
import com.voting.model.response.BlockResponse;
import com.voting.model.response.TransactionResponse;
import com.voting.process.MineProcess;
import com.voting.repository.IBlockChainRepository;
import com.voting.repository.IBlockRepository;
import com.voting.repository.ITransactionRepository;
import com.voting.repository.IWalletRepository;
import com.voting.service.IBlockService;
import com.voting.util.DataUtil;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlockService implements IBlockService {
    private static final Logger logger = LogManager.getLogger(BlockService.class);

    private final String RESULT_CODE    = "resultCode";
    private final String TRANS_RESPONSE = "transactionResponse";
    private final int DIFFICULTY        = 5;

    public ITransactionRepository transactionRepository;

    public IBlockRepository blockRepository;

    public IBlockChainRepository blockChainRepository;

    public IWalletRepository walletRepository;

    @Autowired
    public BlockService(ITransactionRepository transactionRepository
            , IBlockRepository blockRepository
            , IBlockChainRepository blockChainRepository
            , IWalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.blockRepository = blockRepository;
        this.blockChainRepository = blockChainRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public JsonObject mineTransaction(String logId, WalletDTO mineWallet, MineTransactionRequest request) {
        JsonObject response = new JsonObject();
        try {
            String transId = request.getTransId();
            //Step 1: Verify signature
            TransactionDTO transaction = transactionRepository.findByTransId(transId);
            if (transaction == null) {
                logger.warn("{}| Transaction - {} is not existed!", logId, transId);
                return response.put(RESULT_CODE, ErrorConstant.TRANS_NOT_FOUND);
            }
            logger.warn("{}| Transaction - {} is existed!", logId, transId);

            WalletDTO senderWallet = walletRepository.findFirstByWalletIdAndActive(transaction.getSender(), 1);
            if (senderWallet == null) {
                logger.warn("{}| Sender wallet - {} is not existed!", logId, transaction.getSender());
                return response.put(RESULT_CODE, ErrorConstant.NOT_EXISTED);
            }

            if (!MineProcess.verifySignature(logId, transaction, senderWallet.getPublicKey())) {
                logger.warn("{}| Verify sig - {} fail!", logId, transaction.getSignature());
                return response.put(RESULT_CODE, ErrorConstant.CHECK_SIGNATURE_FAIL);
            }
            //Step 2: Validate block
            //Step 2.1: trans can mine? [was mined success >= 50%]
            if (transaction.getStatus() == ActionConstant.COMPLETED.getValue()) {
                logger.warn("{}| Transaction was add to block chain", logId);
                return response.put(RESULT_CODE, ErrorConstant.TRANS_PAID);
            }
            //Step 2.2: wallet can mine ? [walletId is sender, walletId was mined]
            BlockDTO blockDTO = blockRepository.findAllByMinerIdAndTransIdAndIsActive(mineWallet.getWalletId(), transId, 1);
            if (blockDTO != null || mineWallet.getWalletId().equals(transaction.getSender())) {
                logger.warn("{}|Wallet - {} is sender or mined", logId, mineWallet.getWalletId());
                return response.put(RESULT_CODE, ErrorConstant.CANT_MINE);
            }

            //Step 2.3: Validate hash
//            List<BlockChainDTO> blockChains = blockChainRepository.findByOrderByIdDesc();
            List<BlockDTO> blockDTOS = blockRepository.findAllByIsActiveOrderByIdAsc(1);
            String prevHash;
            int index = blockDTOS.size();
            long currentTime = System.currentTimeMillis();
            long nonce = 0;

            if (index <= 0) { //First block -> not valid
                logger.info("{}| This is first block!", logId);
                prevHash = "0";
            } else {
                if (!MineProcess.validateHash(logId, blockDTOS)) {
                    logger.warn("{}|Validate blocks fail!", logId);
                    return response.put(RESULT_CODE, ErrorConstant.SYSTEM_ERROR);
                }
                prevHash = blockDTOS.get(index - 1).getPreviousHash();
            }
            logger.info("{}| Validate Previous hash success!", logId);

            //Step 3: Mine
            BlockDTO block = MineProcess.mineBlock(logId, prevHash, currentTime, nonce, transId, DIFFICULTY, index, mineWallet.getWalletId());
            if (StringUtils.isBlank(block.getHash())) {
                return response.put(RESULT_CODE, ErrorConstant.SYSTEM_ERROR);
            }

            try {
                blockRepository.save(block);
                logger.info("{}| Save block success with block id: {}", logId, block.getBlockId());
            } catch (Exception e) {
                logger.error("{}| Save new block catch exception: ", logId, e);
                return response.put(RESULT_CODE, ErrorConstant.SYSTEM_ERROR);
            }

            //Step 4: Update transaction
            //check mined = 50% ?
            int totalWallet = transaction.getTotalWallet();
            int totalMined = blockRepository.countByTransId(transId);

            transaction.setMined(totalMined);
            transaction.setLastModify(new Timestamp(currentTime));
            if (totalMined > totalWallet/2) {
                try {
                    transaction.setStatus(ActionConstant.COMPLETED.getValue());
                } catch (Exception e) {
                    logger.error("{}| Add block to blockChain catch exception: ", logId, e);
                    return response.put(RESULT_CODE, ErrorConstant.SYSTEM_ERROR);
                }
            }
            transactionRepository.save(transaction);

            //Step 5: Kết thúc đau thương ở đây!
            JsonObject transObject = new JsonObject()
                    .put("blockId", block.getBlockId())
                    .put("difficulty", block.getDifficulty())
                    .put("nonce", block.getNonce())
                    .put("prevHash", block.getPreviousHash())
                    .put("hash", block.getHash());
            return response.put(RESULT_CODE, ErrorConstant.SUCCESS)
                            .put(TRANS_RESPONSE, transObject.toString());

        } catch (Exception exception) {
            logger.error("{}| Mine catch exception: ", logId, exception);
            return response.put(RESULT_CODE, ErrorConstant.SYSTEM_ERROR);
        }
    }

    @Override
    public List<BlockResponse> getBlocks(String logId, BlockRequest request) {
        List<BlockResponse> responses = new ArrayList<>();
        List<BlockDTO> blockDTOS = new ArrayList<>();
        try {
            String walletId = request.getWalletId();

            //Get all transaction of 1 wallet
            if (StringUtils.isNotBlank(walletId)) {
                blockDTOS = blockRepository.findAllByMinerIdOrderByIdDesc(walletId);
            } else {
                blockDTOS = blockRepository.findAll();
            }

            if(blockDTOS.size() <= 0) {
                logger.warn("{}| Not found block!", logId);
                return responses;
            }

            blockDTOS.forEach(block -> responses.add(BlockMapper.toModelBlock(block, transactionRepository.findFirstByTransId(block.getTransId()))));
            return responses;
        } catch (Exception e) {
            logger.error("{}| Get transactions catch exception: ", logId, e);
            return null;
        }
    }
}
