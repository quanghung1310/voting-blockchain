package com.voting.service.impl;

import com.voting.constants.ActionConstant;
import com.voting.dto.BlockDTO;
import com.voting.dto.TransactionDTO;
import com.voting.dto.WalletDTO;
import com.voting.mapper.TransactionMapper;
import com.voting.model.request.VotingRequest;
import com.voting.model.response.TransactionResponse;
import com.voting.model.response.VotingResponse;
import com.voting.process.TransactionProcess;
import com.voting.repository.IBlockRepository;
import com.voting.repository.ITransactionRepository;
import com.voting.repository.IWalletRepository;
import com.voting.service.ITransactionService;
import com.voting.util.DataUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class TransactionService implements ITransactionService {
    private static final Logger logger = LogManager.getLogger(TransactionService.class);

    private ITransactionRepository transactionRepository;
    private IWalletRepository walletRepository;
    private IBlockRepository blockRepository;

    @Autowired
    public TransactionService(ITransactionRepository transactionRepository
            , IWalletRepository walletRepository, IBlockRepository blockRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.blockRepository = blockRepository;
    }

    @Override
    public VotingResponse voting(String logId, VotingRequest request, WalletDTO sendWallet) {
        String senderWalletId = sendWallet.getWalletId();
        String receiverWalletId = request.getReceiverWallet();
        VotingResponse response = new VotingResponse();
        try {
            //Step 1: Validate sender
            List<Integer> status = new ArrayList<>();
            status.add(ActionConstant.INIT.getValue());
            status.add(ActionConstant.CONFIRM.getValue());
            status.add(ActionConstant.COMPLETED.getValue());

            int voted = transactionRepository.countBySenderAndReceiverAndContentIdAndCreateDateAfterAndStatusIn(senderWalletId, receiverWalletId, request.getContentId(), Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)), status);
            if(!TransactionProcess.isValidateSender(logId, sendWallet, request, voted)) {
                logger.warn("{}| Sender wallet can't voting", logId);
                return response;
            }
            logger.info("{}| Validate sender wallet success!", logId);

            //Step 2: Validate receiver
            WalletDTO receiverWallet =  walletRepository.findFirstByWalletIdAndActive(request.getReceiverWallet(), 1);
            if (receiverWallet == null) {
                logger.warn("{}| Receiver wallet is not existed!", logId);
                return response;
            }
            logger.info("{}| Receiver wallet is existed with wallet id - {}", logId, receiverWallet.getWalletId());

            //Step 3: Genera signature
            String senderPrivateKey = sendWallet.getPrivateKey();
            String data = sendWallet.getWalletId() + receiverWalletId + request.getValue() + request.getContentId();
            String signature = Base64.getEncoder().encodeToString(DataUtil.applyRSASig(senderPrivateKey, data));

            if (signature == null) {
                logger.warn("{}| Genera signature fail!", logId);
                return response;
            }
            logger.info("{}| Apply RSA signature success: {}", logId, signature);

            //Step 4: Insert transaction
            String transId = "TRANS_" + System.currentTimeMillis();
            int totalWallet = walletRepository.countByActive(1);
            TransactionDTO transaction = TransactionProcess.buildTransaction(logId, transId, senderWalletId, receiverWalletId, request, signature, totalWallet);
            if (transaction == null) {
                return response;
            }
            logger.info("{}| Save new transaction with transId: {}", logId, transId);
            transactionRepository.save(transaction);

            TransactionDTO newTrans = transactionRepository.findByTransId(transId);
            if (newTrans == null) {
                logger.warn("{}| Save transaction - {} fail!", logId, transId);
                return response;
            }
            logger.info("{}| Save transaction - {} success!", logId, newTrans.getId());

            //Step 5: Build response
            response.setTransId(transId);
            response.setSignature(signature);
            return response;
        } catch (Exception exception) {
            logger.error("{}| Voting catch exception: ", logId, exception);
            return null;
        }
    }

    @Override
    public List<TransactionResponse> getTransactions(String logId, String walletId, String rootWalletId) {
        List<TransactionResponse> responses = new ArrayList<>();
        List<TransactionDTO> transactions;
        try {
            //Get all transaction of 1 wallet
            if (StringUtils.isNotBlank(walletId)) {
                transactions = transactionRepository
                        .findAllBySenderOrReceiver(walletId, walletId);
            } else {
                transactions = (List<TransactionDTO>) transactionRepository.findAll();
            }

            if(transactions.size() <= 0) {
                logger.warn("{}| Not found transaction!", logId);
                return responses;
            }

            transactions.forEach(transaction -> {
                boolean canMine = true;
                BlockDTO blockDTO = blockRepository.findAllByMinerIdAndTransIdAndIsActive(rootWalletId, transaction.getTransId(), 1);
                if (blockDTO != null || rootWalletId.equals(transaction.getSender())) {
                    canMine = false;
                }
                responses.add(TransactionMapper.toModelTransaction(transaction, canMine));
            });
            return responses;
        } catch (Exception e) {
            logger.error("{}| Get transactions catch exception: ", logId, e);
            return null;
        }
    }
}
