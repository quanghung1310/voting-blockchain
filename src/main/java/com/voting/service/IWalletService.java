package com.voting.service;

import com.voting.dto.ElectorDTO;
import com.voting.dto.WalletDTO;
import com.voting.model.request.RegisterRequest;
import com.voting.model.response.ElectorResponse;
import com.voting.model.response.RegisterResponse;

import java.util.List;

public interface IWalletService {
    RegisterResponse register(String logId, RegisterRequest request);

    List<ElectorResponse> getElector(String logId, String contentId);

    ElectorResponse saveElector(String logId, WalletDTO dto, ElectorDTO electorDTO);

    WalletDTO findByEmail(String email);

    WalletDTO findByWalletId(String walletId);
}
