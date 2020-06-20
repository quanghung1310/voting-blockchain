package com.voting.process;

import com.voting.dto.TransactionDTO;
import com.voting.dto.VoteContentDTO;
import com.voting.dto.WalletDTO;
import com.voting.model.request.VotingRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.transaction.Transaction;
import java.sql.Timestamp;

public class TransactionProcess {
    private static final Logger logger = LogManager.getLogger(TransactionProcess.class);

    public static Boolean isValidateSender(String logId, WalletDTO sendWallet, VotingRequest request, int voted) {
        // Step 1: Valid wallet existed
        if (sendWallet == null ) {
            logger.warn("{}| Sender wallet is not existed!", logId);
            return false;
        }
        logger.info("{}| Sender wallet is existed with wallet id: {}", logId, sendWallet.getWalletId());

        // Step 2: Valid max per date
        int maxPerDate = sendWallet.getMaxPerDate();
        int value = request.getValue();
        if (maxPerDate < value
            || voted >= maxPerDate
            || maxPerDate - voted < value) {
            logger.warn("{}| Sender wallet is cash limit: max per date - {} < value - {} or balance - {} < voted - {}", logId, maxPerDate, value, maxPerDate, voted);
            return false;
        }
        logger.info("{}| Sender can voting with balance: {}", logId, maxPerDate - voted);
        return true;
    }

    public static TransactionDTO buildTransaction(String logId, String transId, String sendPublicKey, String recieptPublicKey, VotingRequest request, String signature) {
       try {
           return TransactionDTO.builder()
                   .active(1)
                   .contentId(request.getContentId())
                   .createDate(new Timestamp(request.getRequestTime()))
                   .currency(request.getCurrency())
                   .description(request.getDescription())
                   .lastModify(new Timestamp(request.getRequestTime()))
                   .receipt(recieptPublicKey)
                   .sender(sendPublicKey)
                   .signature(signature)
                   .transId(transId)
                   .value(request.getValue())
                   .build();
       } catch (Exception exception) {
           logger.error("{}| Build transaction catch exception: ", logId);
           return null;
       }
    }
}
