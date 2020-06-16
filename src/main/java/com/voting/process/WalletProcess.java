package com.voting.process;

import com.voting.constants.StringConstant;
import com.voting.dto.WalletDTO;
import com.voting.util.DataUtil;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;

public class WalletProcess {
    private static final Logger logger = LogManager.getLogger(WalletProcess.class);

    public static WalletDTO register(String logId, String email, String password, String fistName, String lastName, int type, int sex) {
        WalletDTO walletDTO= new WalletDTO();
        walletDTO.setEmail(email);
        walletDTO.setPassword(password);
        walletDTO.setFirstName(fistName);
        walletDTO.setLastName(lastName);
        walletDTO.setType(type);
        walletDTO.setSex(sex);

        JsonObject keyPair = DataUtil.generateKeyPair(logId);
        String privateKey = keyPair.getString(StringConstant.WALLET_PRIMARY);
        String publicKey = keyPair.getString(StringConstant.WALLET_ADDRESS);
        logger.info("{}| public key: {}", logId, publicKey);
        logger.info("{}| private key: {}", logId, privateKey);

        String walletId = DataUtil.generateWalletId(logId);

        logger.info("{}| walletId: {}", logId, walletId);
        if (privateKey.isEmpty() || publicKey.isEmpty() || walletId.isEmpty()) {
            logger.warn("{}| Generate ketPair - {}, {}, walletId - {}", logId, privateKey, publicKey, walletId);
            return null;
        }
        walletDTO.setWalletId(walletId);
        walletDTO.setPrivateKey(privateKey);
        walletDTO.setPublicKey(publicKey);
        walletDTO.setActive(1);
        walletDTO.setCreateDate(new Timestamp(System.currentTimeMillis()));
        walletDTO.setLastModify(new Timestamp(System.currentTimeMillis()));
        return walletDTO;
    }
}
