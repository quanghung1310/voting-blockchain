package com.voting.process;

import com.voting.dto.BlockDTO;
import com.voting.dto.TransactionDTO;
import com.voting.util.DataUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;

public class MineProcess {
    private static final Logger logger = LogManager.getLogger(MineProcess.class);

    public static Boolean verifySignature(String logId, TransactionDTO transaction, String senderAddress) {
        String data = transaction.getSender() + transaction.getReceiver() + transaction.getValue() + transaction.getContentId();
        //Verifies the data we signed hasn't been tampered with
        boolean isVerify = DataUtil.verifySignatureBase64(transaction.getSignature(), data, senderAddress);
        if (!isVerify) {
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
            String hash = DataUtil.calculateHash(blockDTO.getPreviousHash(), currentTime, nonce, transId, index);
            String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
            while (!hash.substring(0, difficulty).equals(target)) {
                nonce++;
                hash = DataUtil.calculateHash(blockDTO.getPreviousHash(), currentTime, nonce, transId, index);
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

//    public static BlockDTO createBlock(String prevHash, long currentTime, String transId, int difficulty, int index, String minerId) {
//        //Create new Block
//        BlockDTO blockDTO = new BlockDTO();
//        blockDTO.setTransId(transId);
//        blockDTO.setMinerId(minerId);
//        blockDTO.setBlockId("BLOCK_" + System.currentTimeMillis());
//        blockDTO.setDifficulty(difficulty);
//        blockDTO.setTimeHash(String.valueOf(currentTime));
//        blockDTO.setPreviousHash(prevHash);
//        blockDTO.setTotal(index);
//        blockDTO.setCreateDate(new Timestamp(currentTime));
//        blockDTO.setLastModify(new Timestamp(currentTime));
//        blockDTO.setParentId(0);
//        blockDTO.setIsActive(0);
//        blockDTO.setStatusBlock(1);
//        return blockDTO;
//    }

    public static boolean validateHash(String logId, List<BlockDTO> blockDTOS) {
        if (blockDTOS.size() == 1) {
            BlockDTO blockDTO = blockDTOS.get(0);
            String currentHash = DataUtil.calculateHash(blockDTO.getPreviousHash(), Long.parseLong(blockDTO.getTimeHash()),
                    blockDTO.getNonce(), blockDTO.getTransId(), blockDTO.getTotal());
            if (StringUtils.isNotBlank(blockDTO.getPreviousHash()) || !blockDTO.getHash().equals(currentHash)) {
                logger.warn("{}|Current hash - {} not compare Block hash - {}", logId, currentHash, blockDTO.getHash());
                return false;
            }
        } else {
            for (int i = 1; i < blockDTOS.size(); i++) {
                BlockDTO currentBlock = blockDTOS.get(i);
                BlockDTO previousBlock = blockDTOS.get(i - 1);
                // compare registered hash and calculated hash:
                String currentHash = currentBlock.calculateHash();
                if (!currentBlock.getHash().equals(currentHash)) {
                    logger.warn("{}|Current hash - {} not compare Block hash - {}", logId, currentHash, currentBlock.getHash());
                    return false;
                }

                // compare previous hash and registered previous hash
                if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                    logger.warn("{}|Previous hash - {} not compare Block hash - {}", logId, currentBlock.getPreviousHash(), previousBlock.getHash());
                    return false;
                }
            }
        }
        return true;
    }
}
