package com.voting.service;

import com.voting.model.request.ElectorRequest;
import com.voting.model.request.RegisterRequest;
import com.voting.model.response.ElectorResponse;
import com.voting.model.response.RegisterResponse;

import java.util.List;

public interface IWalletService {
    RegisterResponse register(String logId, RegisterRequest request);

    List<ElectorResponse> getElector(String logId, ElectorRequest request);
}
