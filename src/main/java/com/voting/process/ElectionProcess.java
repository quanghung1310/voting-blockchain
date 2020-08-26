package com.voting.process;

import com.voting.dto.VoteContentDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;

public class ElectionProcess {
    private static final Logger logger = LogManager.getLogger(ElectionProcess.class);

    public static VoteContentDTO createVoteContent(String logId, String content, Timestamp startDate, Timestamp endDate, String description, String walletId) {
        VoteContentDTO voteContent = new VoteContentDTO();
        voteContent.setContent(content);
        voteContent.setCreateDate(new Timestamp(System.currentTimeMillis()));
        voteContent.setStartDate(startDate);
        voteContent.setEndDate(endDate);
        voteContent.setDescription(description);
        voteContent.setContentId("CONTENT_" + System.currentTimeMillis());
        voteContent.setWalletId(walletId);
        return voteContent;
    }

}
