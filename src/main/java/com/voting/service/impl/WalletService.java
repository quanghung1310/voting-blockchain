package com.voting.service.impl;

import com.voting.dto.WalletDTO;
import com.voting.mapper.WalletMapper;
import com.voting.model.request.ElectorRequest;
import com.voting.model.request.LogInRequest;
import com.voting.model.request.RegisterRequest;
import com.voting.model.response.ElectorResponse;
import com.voting.model.response.LogInResponse;
import com.voting.model.response.RegisterResponse;
import com.voting.process.WalletProcess;
import com.voting.repository.IWalletRepository;
import com.voting.service.IWalletService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class WalletService implements IWalletService {

    private static final Logger logger = LogManager.getLogger(WalletService.class);

    @Autowired
    IWalletRepository walletRepository;

    @Override
    public RegisterResponse register(String logId, RegisterRequest request) {
        try {
            WalletDTO walletDTO = WalletProcess.register(logId, request.getEmail(), request.getPassword(), request.getFirstName(), request.getLastName(), request.getType(), request.getSex());

            WalletDTO wallet = walletRepository.save(walletDTO);
            logger.info("{}| Add new wallet success with id: {}", logId, walletDTO.getId());
            return WalletMapper.toModelRegister(wallet);
        } catch (Exception exception) {
            logger.error("{}| Register catch exception: ", logId, exception);
            return null;
        }

    }

    @Override
    public LogInResponse login(String logId, LogInRequest request) {
        try {
            WalletDTO walletDTO = walletRepository.getAllByWalletId(request.getWalletId());
            if (walletDTO == null || walletDTO.getActive() == 0) {
                logger.warn("{}| Wallet not existed!", logId);
                return null;
            }
            logger.info("{}| Wallet is existed with id: {}", logId, walletDTO.getId());

            //Validate password
            if (!walletDTO.getPassword().matches(request.getPassword())) {
                logger.warn("{}| Wallet not existed!", logId);
                return null;
            }
            logger.info("{}| Validate password success!", logId);

            return WalletMapper.toModelLogIn(walletDTO);
        } catch (Exception ex) {
            logger.error("{}| Login catch exception: ", logId, ex);
            return null;
        }
    }

    @Override
    public List<ElectorResponse> getElector(String logId, ElectorRequest reqquest) {
        List<ElectorResponse> response = new ArrayList<>();
        try {
           List<WalletDTO> wallets = walletRepository.findAllByContentId(reqquest.getContentId());
           if (wallets.size() <= 0) {
               logger.warn("{}| Data not found!", logId);
               return response;
           }

            logger.info("{}| Found elector success with size: {}", logId, wallets.size());
            wallets.forEach(wallet -> response.add(WalletMapper.toModelElector(wallet)));
           return response;
        } catch (Exception ex) {
            logger.error("{}| Get elector catch exception: ", logId, ex);
            return null;
        }
    }
}
