package com.voting.service;

import com.voting.model.request.NewContentVote;

public interface ITransactionService {
    String createContentVote(String logId, NewContentVote request);
}
