package com.voting.service;

import com.voting.model.request.LogInRequest;
import com.voting.model.request.RegisterRequest;
import com.voting.model.response.LogInResponse;
import com.voting.model.response.RegisterResponse;

public interface IWalletService {
    RegisterResponse register(String logId, RegisterRequest request);

    LogInResponse login(String logId, LogInRequest request);
}
