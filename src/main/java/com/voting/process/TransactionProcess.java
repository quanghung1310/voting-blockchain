package com.voting.process;

import com.voting.dto.VoteContentDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;

public class TransactionProcess {
    private static final Logger logger = LogManager.getLogger(TransactionProcess.class);

    public static VoteContentDTO createVoteContent(String logId, String content, long startDate, long endDate, String description) {
        VoteContentDTO voteContent = new VoteContentDTO();
        voteContent.setContent(content);
        voteContent.setCreateDate(new Timestamp(System.currentTimeMillis()));
        voteContent.setStartDate(new Timestamp(startDate));
        voteContent.setEndDate(new Timestamp(endDate));
        voteContent.setDescription(description);
        voteContent.setContentId("CONTENT_" + System.currentTimeMillis());
        return voteContent;
    }

}
