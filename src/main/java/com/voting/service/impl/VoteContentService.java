package com.voting.service.impl;

import com.voting.constants.ActionConstant;
import com.voting.dto.ElectorDTO;
import com.voting.dto.VoteContentDTO;
import com.voting.dto.WalletDTO;
import com.voting.mapper.VoteContentMapper;
import com.voting.mapper.WalletMapper;
import com.voting.model.request.NewVoteContent;
import com.voting.model.response.ElectorResponse;
import com.voting.model.response.VoteContentResponse;
import com.voting.process.ElectionProcess;
import com.voting.repository.IElectorRepository;
import com.voting.repository.ITransactionRepository;
import com.voting.repository.IVoteContentRepository;
import com.voting.repository.IWalletRepository;
import com.voting.service.IVoteContentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoteContentService implements IVoteContentService {
    private static final Logger logger = LogManager.getLogger(VoteContentService.class);

    private IVoteContentRepository voteContentRepository;
    private IElectorRepository electorRepository;
    private IWalletRepository walletRepository;
    private ITransactionRepository transactionRepository;

    @Autowired
    public VoteContentService(IVoteContentRepository voteContentRepository, IElectorRepository electorRepository, IWalletRepository walletRepository, ITransactionRepository transactionRepository) {
        this.voteContentRepository = voteContentRepository;
        this.electorRepository = electorRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public VoteContentResponse createContentVote(String logId, NewVoteContent request, String walletId) {
        try {
            Timestamp startDate = Timestamp.valueOf(request.getStartDate());
            Timestamp endDate = Timestamp.valueOf(request.getEndDate());
            if (startDate.after(endDate)) {
                logger.error("{}| Start date - {} < End date - {}", logId, startDate, endDate);
                return null;
            }
            VoteContentDTO voteContent = ElectionProcess.createVoteContent(logId, request.getContent(), startDate, endDate, request.getDescription(), walletId);
            voteContent = voteContentRepository.save(voteContent);
            return VoteContentMapper.toModelVoteContent(voteContent, true, new ArrayList<>());
        } catch (Exception exception) {
            logger.error("{}| Create content vote catch exception: ", logId, exception);
            return null;
        }

    }

    @Override
    public List<VoteContentResponse> getContent(String logId, String startDate, String endDate, String walletId) {
        List<VoteContentResponse> responseData = new ArrayList<>();
        try {
            //Get all
            if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
                logger.info("{}| Get all content", logId);
                voteContentRepository.findAll()
                        .forEach(vote -> {
                            List<ElectorDTO> electorDTOS = electorRepository.findAllByContentId(vote.getContentId());
                            long register = electorDTOS.stream().filter(electorDTO1 -> electorDTO1.getWalletId().equals(walletId)).count();

                            //Build List<Elector>
                            List<ElectorResponse> electorResponses = new ArrayList<>();
                            for (ElectorDTO elector : electorDTOS) {
                                WalletDTO walletDTO = walletRepository.findFirstByWalletIdAndActive(elector.getWalletId(), 1);
                                int voted = transactionRepository.countAllByContentIdAndStatus(elector.getContentId(), ActionConstant.COMPLETED.getValue());
                                electorResponses.add(WalletMapper.toModelElector(walletDTO, elector.getContentId(), walletId.equals(elector.getWalletId()), voted));
                            }

                            responseData.add(VoteContentMapper.toModelVoteContent(vote, register > 0, electorResponses));
                        });
            } else {
                //Get by time
                voteContentRepository.findAllContentByDate(startDate, endDate)
                        .forEach(vote -> {
                            List<ElectorDTO> electorDTOS = electorRepository.findAllByContentIdAndWalletId(vote.getContentId(), walletId);
                            long register = electorDTOS.stream().filter(electorDTO1 -> electorDTO1.getWalletId().equals(walletId)).count();
                            //Build List<Elector>
                            List<ElectorResponse> electorResponses = new ArrayList<>();
                            for (ElectorDTO elector : electorDTOS) {
                                WalletDTO walletDTO = walletRepository.findFirstByWalletIdAndActive(elector.getWalletId(), 1);
                                int voted = transactionRepository.countAllByContentIdAndStatus(elector.getContentId(), ActionConstant.COMPLETED.getValue());
                                electorResponses.add(WalletMapper.toModelElector(walletDTO, elector.getContentId(), walletId.equals(elector.getWalletId()), voted));
                            }

                            responseData.add(VoteContentMapper.toModelVoteContent(vote, register > 0, electorResponses));
                        });
            }
            logger.info("{}| Get data from db with size: {}", logId, responseData.size());
            return responseData;
        } catch (Exception exception) {
            logger.error("{}| Get content vote catch exception: ", logId, exception);
            return responseData;
        }
    }
}
