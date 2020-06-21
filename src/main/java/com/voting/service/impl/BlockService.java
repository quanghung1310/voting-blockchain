package com.voting.service.impl;

import com.voting.constants.ErrorConstant;
import com.voting.dto.BlockChainDTO;
import com.voting.dto.BlockDTO;
import com.voting.dto.TransactionDTO;
import com.voting.model.request.MineTransactionRequest;
import com.voting.model.response.TransactionResponse;
import com.voting.process.MineProcess;
import com.voting.repository.IBlockChainRepository;
import com.voting.repository.IBlockRepository;
import com.voting.repository.ITransactionRepository;
import com.voting.repository.IWalletRepository;
import com.voting.service.IBlockService;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BlockService implements IBlockService {
    private static final Logger logger = LogManager.getLogger(BlockService.class);

    private final String RESULT_CODE    = "resultCode";
    private final String TRANS_RESPONSE = "transactionResponse";
    private final int DIFFICULTY        = 5;
    @Autowired
    public ITransactionRepository transactionRepository;

    @Autowired
    public IBlockRepository blockRepository;

    @Autowired
    public IBlockChainRepository blockChainRepository;

    @Autowired
    public IWalletRepository walletRepository;

    @Override
    public JsonObject mineTransaction(String logId, MineTransactionRequest request) {
        JsonObject response = new JsonObject();
        TransactionResponse transactionResponse = TransactionResponse.builder().build();
        try {
            String transId = request.getTransId();
            String walletId = request.getWalletId();
            //Step 1: Verify signature
            TransactionDTO transaction = transactionRepository.findByTransId(transId);
            if (transaction == null) {
                logger.warn("{}| Transaction - {} is not existed!", logId, transId);
                return response.put(RESULT_CODE, ErrorConstant.TRANS_NOT_FOUND);
            }
            logger.warn("{}| Transaction - {} is existed!", logId, transId);

            if (!MineProcess.verifySignature(logId, transaction)) {
                logger.warn("{}| Verify sig - {} fail!", logId, transaction.getSignature());
                return response.put(RESULT_CODE, ErrorConstant.CHECK_SIGNATURE_FAIL);
            }
            //Step 2: Create block
            BlockDTO block = new BlockDTO();
            //Step 2.1: trans can mine? [was mined success >= 50%]
            TransactionDTO transactionDTO = transactionRepository.findByTransId(transId);
            if (transactionDTO.getIsMine() == 1) {
                logger.warn("{}| Transaction was add to blockchain", logId);
                return response.put(RESULT_CODE, ErrorConstant.TRANS_PAID);
            }
            //Step 2.2: wallet can mine ? [ walletId not fount, walletId is sender, walletId was mined]
            BlockDTO blockDTO = blockRepository.findAllByMinerIdAndTransId(walletId, transId);
//            if (blockDTO != null || walletId.equals(transactionDTO.getSender())) {
//                logger.warn("{}| User can't mine", logId);
//                return response.put(RESULT_CODE, ErrorConstant.CANT_MINE);
//            }
            //Step 2.3: Get prevHash
            List<BlockChainDTO> blockchains = blockChainRepository.findByOrderByIdDesc();
            String prevHash = "";
            int index = 0;
            long currentTime = System.currentTimeMillis();
            long nonce = 0;
            if (blockchains.size() <= 0) { //First block -> not valid
                logger.info("{}| This is first block!", logId);
                prevHash = "0";
            } else {
                logger.info("{}| PrevHash is: {}", logId, prevHash);
                index = blockchains.size();
                BlockDTO blockDto = blockRepository.findByIdIs(blockchains.get(index - 1).getBlockId());
                prevHash = blockDto.getHash();
                String realPrevHash = MineProcess.mineBlock(logId, blockDto.getPreviousHash(), Long.parseLong(blockDto.getTimeHash()), blockDto.getNonce(), blockDto.getTransId(), blockDto.getDifficulty(), (int) blockDto.getTotal(), walletId).getHash();
                if (!prevHash.equals(realPrevHash)) {
                    logger.warn("{}| Hash in block id - {}: Invalid!", logId, blockDto.getBlockId());
                    return response.put(RESULT_CODE, ErrorConstant.HASH_NOT_VALID);
                }
            }

            block = MineProcess.mineBlock(logId, prevHash, currentTime, nonce, transId, DIFFICULTY, index, walletId);
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

            //Step 3: Add block to blockchain
            //check mined = 50% ?
            Long totalWallet = walletRepository.countByActiveAndType(1, 0);
            Long totalMine = blockRepository.countByTransId(transId);

            if (totalMine >= totalWallet/2) {
                try {
                    Timestamp lastModify = new Timestamp(System.currentTimeMillis());
                    int status = 1;
                    blockChainRepository.save(BlockChainDTO.builder()
                            .blockId(block.getId())
                            .createDate(lastModify)
                            .isActive(1)
                            .walletId(walletId)
                            .build());
                    transactionRepository.setIsMineByTransId(status, lastModify, transId);
                } catch (Exception e) {
                    logger.error("{}| Add block to blockchain catch exception: ", logId, e);
                    return response.put(RESULT_CODE, ErrorConstant.SYSTEM_ERROR);
                }
            }

            //Step 4: Build response
//            "blockId": "BLOCK_1555472829580",
//                    "difficulty": 5,
//                    "nonce": 1245123,
//                    "prevHash": "0",
//                    "hash": "2e53daeba42cc615f082373cfe6a62b30ca76ecc4815866ae11d5c0cbc991698"
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
}
