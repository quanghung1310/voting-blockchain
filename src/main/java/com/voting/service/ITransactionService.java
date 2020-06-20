package com.voting.service;

import com.voting.model.request.VotingRequest;
import com.voting.model.response.VotingResponse;

public interface ITransactionService {

    public VotingResponse voting(String logId, VotingRequest request);

}
