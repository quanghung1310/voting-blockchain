package com.voting.service.impl;

import com.voting.dto.VoteContentDTO;
import com.voting.model.request.NewContentVote;
import com.voting.process.TransactionProcess;
import com.voting.repository.ITransactionRepository;
import com.voting.repository.IVoteContentRepository;
import com.voting.service.ITransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService {
    private static final Logger logger = LogManager.getLogger(TransactionService.class);

    @Autowired
    public ITransactionRepository transactionRepository;

    @Autowired
    public IVoteContentRepository voteContentRepository;

    @Override
    public String createContentVote(String logId, NewContentVote request) {
        try {
            VoteContentDTO voteContent = TransactionProcess.createVoteContent(logId, request.getContent(), request.getStartDate(), request.getEndDate(), request.getDescription());
            voteContentRepository.save(voteContent);
            return voteContent.getContentId();
        } catch (Exception exception) {
            logger.error("{}| Create content vote catch exception: ", logId, exception);
            return null;
        }

    }
}
