package com.voting.service.impl;

import com.voting.dto.TransactionDTO;
import com.voting.dto.WalletDTO;
import com.voting.mapper.TransactionMapper;
import com.voting.model.request.TransactionRequest;
import com.voting.model.request.VotingRequest;
import com.voting.model.response.TransactionResponse;
import com.voting.model.response.VotingResponse;
import com.voting.process.TransactionProcess;
import com.voting.repository.ITransactionRepository;
import com.voting.repository.IWalletRepository;
import com.voting.service.ITransactionService;
import com.voting.util.DataUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class TransactionService implements ITransactionService {
    private static final Logger logger = LogManager.getLogger(TransactionService.class);

    @Autowired
    public ITransactionRepository transactionRepository;
    @Autowired
    public IWalletRepository walletRepository;

    @Override
    public VotingResponse voting(String logId, VotingRequest request) {
        String senderPublicKey = request.getSender();
        String reciepientPublicKey = request.getReciepient();
        VotingResponse response = new VotingResponse();
        try {
            //Step 1: Validate sender
            WalletDTO sendWallet = walletRepository.findAllByPublicKeyAndActive(senderPublicKey, 1);
            int voted = transactionRepository.countBySender(sendWallet.getPublicKey());
            if(!TransactionProcess.isValidateSender(logId, sendWallet, request, voted)) {
                logger.warn("{}| Sender wallet can't voting", logId);
                return response;
            }
            logger.info("{}| Validate sender wallet success!", logId);

            //Step 2: Validate receipt
            WalletDTO recipWallet =  walletRepository.findAllByPublicKeyAndActiveAndType(reciepientPublicKey, 1, 1);
            if (recipWallet == null) {
                logger.warn("{}| Receipt wallet is not existed!", logId);
                return response;
            }
            logger.info("{}| Receipt wallet is existed with wallet id", recipWallet.getWalletId());

            //Step 3: Genera signature
            String senderPrivateKey = sendWallet.getPrivateKey();
            String data = senderPublicKey + reciepientPublicKey + request.getValue();
            String signature = Base64.getEncoder().encodeToString(DataUtil.applyRSASig(senderPrivateKey, data));

            if (signature == null) {
                logger.warn("{}| Genera signature fail!", logId);
                return response;
            }
            logger.info("{}| Apply RSA signature success: {}", logId, signature);

            //Step 4: Upsert db
            String transId = "TRANS_" + System.currentTimeMillis();
            TransactionDTO transaction = TransactionProcess.buildTransaction(logId, transId, senderPublicKey, reciepientPublicKey, request, signature);
            if (transaction == null) {
                return response;
            }
            logger.info("{}| Save new transaction with transId: {}", logId, transId);
            transactionRepository.save(transaction);

            Long id = transactionRepository.findByTransId(transId).getId();
            if (id == null) {
                logger.warn("{}| Save transaction - {} fail!", logId, transId);
                return response;
            }
            logger.info("{}| Save transaction - {} success!", logId, transId);

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
    public List<TransactionResponse> getTransactions(String logId, TransactionRequest request) {
        List<TransactionResponse> responses = new ArrayList<>();
        List<TransactionDTO> transactions = new ArrayList<>();
        try {
            String walletId = request.getWalletId();

            //Get all transaction of 1 wallet
            if (StringUtils.isNotBlank(walletId)) {
                transactions = transactionRepository
                        .findAllBySenderAndActive(
                                walletRepository.findAllByWalletId(walletId).getPublicKey(), 1);
            } else {
                transactions = transactionRepository.findAllByActive(1);
            }

            if(transactions.size() <= 0) {
                logger.warn("{}| Not found transaction!", logId);
                return responses;
            }

            transactions.forEach(transaction -> responses.add(TransactionMapper.toModelTransaction(transaction, walletId)));
            return responses;
        } catch (Exception e) {
            logger.error("{}| Get transactions catch exception: ", logId, e);
            return null;
        }
    }
}
