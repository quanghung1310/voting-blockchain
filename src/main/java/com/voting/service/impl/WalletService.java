package com.voting.service.impl;

import com.voting.constants.StringConstant;
import com.voting.dto.WalletDTO;
import com.voting.mapper.WalletMapper;
import com.voting.model.response.RegisterResponse;
import com.voting.repository.IWalletRepository;
import com.voting.service.IWalletService;
import com.voting.util.DataUtil;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class WalletService implements IWalletService {

    private static final Logger logger = LogManager.getLogger(WalletService.class);

    @Autowired
    IWalletRepository walletRepository;

    @Override
    public RegisterResponse addNewWallet(String logId, WalletDTO walletDTO) {
        try {
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

            WalletDTO wallet = walletRepository.save(walletDTO);
            logger.info("{}| Add new wallet success with id: {}", logId, walletDTO.getId());
            return WalletMapper.toModel(wallet);
        } catch (Exception exception) {
            logger.error("{}| Add new wallet catch exception: ", logId, exception);
            return null;
        }

    }
}
