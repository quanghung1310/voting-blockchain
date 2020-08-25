package com.voting.service.impl;

import com.voting.dto.VoteContentDTO;
import com.voting.mapper.VoteContentMapper;
import com.voting.model.request.NewVoteContent;
import com.voting.model.request.VoteContentRequest;
import com.voting.model.response.VoteContentResponse;
import com.voting.process.ElectionProcess;
import com.voting.process.TransactionProcess;
import com.voting.repository.IVoteContentRepository;
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

    public IVoteContentRepository voteContentRepository;

    public VoteContentService(IVoteContentRepository voteContentRepository) {
        this.voteContentRepository = voteContentRepository;
    }

    @Override
    public String createContentVote(String logId, NewVoteContent request) {
        try {
            VoteContentDTO voteContent = ElectionProcess.createVoteContent(logId, request.getContent(), request.getStartDate(), request.getEndDate(), request.getDescription());
            voteContentRepository.save(voteContent);
            return voteContent.getContentId();
        } catch (Exception exception) {
            logger.error("{}| Create content vote catch exception: ", logId, exception);
            return null;
        }

    }

    @Override
    public List<VoteContentResponse> getContent(String logId, String startDate, String endDate) {
        List<VoteContentResponse> responseData = new ArrayList<>();
        try {
            //Get all
            if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
                logger.info("{}| Get all content", logId);
                voteContentRepository.findAll()
                        .forEach(vote -> responseData.add(VoteContentMapper.toModelVoteContent(vote)));
            } else {
                //Get by time
                voteContentRepository.findAllContentByDate(startDate, endDate)
                        .forEach(vote -> responseData.add(VoteContentMapper.toModelVoteContent(vote)));
            }
            logger.info("{}| Get data from db with size: {}", logId, responseData.size());
            return responseData;
        } catch (Exception exception) {
            logger.error("{}| Get content vote catch exception: ", logId, exception);
            return responseData;
        }
    }
}
