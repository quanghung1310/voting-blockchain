package com.voting.process;

import com.voting.dto.BlockDTO;
import com.voting.dto.TransactionDTO;
import com.voting.util.DataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;

public class MineProcess {
    private static final Logger logger = LogManager.getLogger(MineProcess.class);

    public static Boolean verifySignature(String logId, TransactionDTO transaction) {
        String data = transaction.getSender() + transaction.getReceiver() + transaction.getValue();
        //Verifies the data we signed hasnt been tampered with
        boolean isVerify =  DataUtil.verifySignatureBase64(transaction.getSignature(), data, transaction.getSender());
        if(!isVerify) {
            logger.warn("{}| Transaction Signature failed to verify!", logId);
            return false;
        }
        logger.info("{}| Transaction Signature verify success!", logId);

        return true;
    }

    public static BlockDTO mineBlock(String logId, String prevHash, long currentTime, long nonce, String transId, int difficulty, int index, String minerId) {
        //Create new Block
        BlockDTO blockDTO = new BlockDTO();
        try {
            blockDTO.setTransId(transId);
            blockDTO.setMinerId(minerId);
            blockDTO.setBlockId("BLOCK_" + System.currentTimeMillis());
            blockDTO.setDifficulty(difficulty);
            blockDTO.setTimeHash(String.valueOf(currentTime));
            blockDTO.setPreviousHash(prevHash);
            blockDTO.setTotal(index);
            blockDTO.setCreateDate(new Timestamp(currentTime));
            blockDTO.setLastModify(new Timestamp(currentTime));
            blockDTO.setParentId(0);
            blockDTO.setIsActive(0);
            blockDTO.setStatusBlock(1);

            //Mine
            long timeHash = Long.parseLong(blockDTO.getTimeHash());
            String hash = DataUtil.calculateHash(blockDTO.getPreviousHash(), timeHash, nonce, transId, index);
            String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
            while(!hash.substring( 0, difficulty).equals(target)) {
                nonce++;
                hash = DataUtil.calculateHash(blockDTO.getPreviousHash(), timeHash, nonce, transId, index);
            }
            blockDTO.setNonce(nonce);
            blockDTO.setHash(hash);
            logger.info("{}| Block Mined: {}", logId, hash);
            return blockDTO;
        } catch (Exception ex) {
            logger.error("{}| Mine block catch exception: ", logId);
            return blockDTO;
        }
    }
}
