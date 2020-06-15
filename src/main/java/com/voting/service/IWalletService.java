package com.voting.service;

import com.voting.dto.WalletDTO;
import com.voting.model.response.RegisterResponse;

public interface IWalletService {
    RegisterResponse addNewWallet(String logId, WalletDTO walletDTO);
}
