package com.voting.service;

import com.voting.model.request.NewVoteContent;
import com.voting.model.response.VoteContentResponse;

import java.util.List;

public interface IVoteContentService {
    VoteContentResponse createContentVote(String logId, NewVoteContent request, String walletId);

    List<VoteContentResponse> getContent(String logId, String startDate, String endDate, String walletId);
}
