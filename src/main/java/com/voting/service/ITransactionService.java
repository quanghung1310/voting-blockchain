package com.voting.service;

import com.voting.model.request.TransactionRequest;
import com.voting.model.request.VotingRequest;
import com.voting.model.response.TransactionResponse;
import com.voting.model.response.VotingResponse;

import java.util.List;

public interface ITransactionService {

    VotingResponse voting(String logId, VotingRequest request);

    List<TransactionResponse> getTransactions(String logId, TransactionRequest request);
}
