package com.voting.service;

import com.voting.model.request.NewVoteContent;
import com.voting.model.request.VoteContentRequest;
import com.voting.model.response.VoteContentResponse;

import java.util.List;

public interface IVoteContentService {
    public String createContentVote(String logId, NewVoteContent request);

    public List<VoteContentResponse> getContent(String logId, VoteContentRequest request);
}
