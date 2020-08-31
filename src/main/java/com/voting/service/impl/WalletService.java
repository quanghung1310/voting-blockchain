package com.voting.service.impl;

import com.voting.dto.ElectorDTO;
import com.voting.dto.VoteContentDTO;
import com.voting.dto.WalletDTO;
import com.voting.mapper.WalletMapper;
import com.voting.model.request.RegisterRequest;
import com.voting.model.response.ElectorResponse;
import com.voting.model.response.RegisterResponse;
import com.voting.process.WalletProcess;
import com.voting.repository.IElectorRepository;
import com.voting.repository.IVoteContentRepository;
import com.voting.repository.IWalletRepository;
import com.voting.service.IWalletService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WalletService implements IWalletService {

    private static final Logger logger = LogManager.getLogger(WalletService.class);

    private IWalletRepository walletRepository;
    private IElectorRepository electorRepository;
    private IVoteContentRepository voteContentRepository;

    @Autowired
    public WalletService(IWalletRepository walletRepository
            , IElectorRepository electorRepository
            , IVoteContentRepository voteContentRepository) {
        this.walletRepository = walletRepository;
        this.electorRepository = electorRepository;
        this.voteContentRepository = voteContentRepository;
    }

    @Override
    public RegisterResponse register(String logId, RegisterRequest request) {
        try {
            WalletDTO walletExisted = walletRepository.findFirstByEmail(request.getEmail());
            if (walletExisted != null) {
                logger.warn("{}| Email - {} was existed!", logId, request.getEmail());
                return null;
            }
            WalletDTO walletDTO = WalletProcess.register(logId, request.getEmail(), request.getPassword(), request.getFirstName(), request.getLastName(), request.getType(), request.getSex());
            if (walletDTO == null) {
                logger.warn("{}| System generate fail!", logId);
                return null;
            }

            WalletDTO wallet = walletRepository.save(walletDTO);
            logger.info("{}| Add new wallet success with id: {}", logId, walletDTO.getId());
            return WalletMapper.toModelRegister(wallet);
        } catch (Exception exception) {
            logger.error("{}| Register catch exception: ", logId, exception);
            return null;
        }

    }

    @Override
    public List<ElectorResponse> getElector(String logId, String contentId, String walletId) {
        List<ElectorResponse> response = new ArrayList<>();
        try {

           List<WalletDTO> wallets;
           if (StringUtils.isBlank(contentId)) {
               wallets = walletRepository.findAllElector();
               logger.info("{}| Found all elector success with size: {}", logId, wallets.size());
               List<ElectorDTO> electorDTOS = (List<ElectorDTO>) electorRepository.findAll();
               if(electorDTOS.size() > 0) {
                   for (ElectorDTO electorDTO : electorDTOS) {
                       WalletDTO walletDTO = walletRepository.findFirstByWalletIdAndActive(electorDTO.getWalletId(), 1);
                       response.add(WalletMapper.toModelElector(walletDTO, electorDTO.getContentId(), electorDTO.getWalletId().equals(walletId)));
                   }
               }
           } else {
               wallets = walletRepository.findAllByContentId(contentId);
               logger.info("{}| Found elector by content - {} success with size: {}", logId, contentId, wallets.size());
               wallets.forEach(wallet -> response.add(WalletMapper.toModelElector(wallet, contentId, wallet.getWalletId().equals(walletId))));
           }

           if (wallets.size() <= 0) {
               logger.warn("{}| Data not found!", logId);
           }
           return response;
        } catch (Exception ex) {
            logger.error("{}| Get elector catch exception: ", logId, ex);
            return null;
        }
    }

    @Override
    public ElectorResponse saveElector(String logId, WalletDTO dto, ElectorDTO electorDTO) {
        VoteContentDTO voteContentDTO = voteContentRepository.findFirstByContentId(electorDTO.getContentId());
        if (voteContentDTO == null) {
            logger.warn("{}| Content id - {} not existed!", logId, electorDTO.getContentId());
            return null;
        }
        ElectorDTO elector = electorRepository.save(electorDTO);
        return WalletMapper.toModelElector(dto, elector.getContentId(), true);
    }

    @Override
    public WalletDTO findByEmail(String email) {
        return walletRepository.findFirstByEmail(email);
    }

    @Override
    public WalletDTO findByWalletId(String walletId) {
        return walletRepository.findFirstByWalletIdAndActive(walletId, 1);
    }
}
