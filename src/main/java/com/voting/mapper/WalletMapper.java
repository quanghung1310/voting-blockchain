package com.voting.mapper;

import com.voting.dto.WalletDTO;
import com.voting.model.response.RegisterResponse;

public final class WalletMapper {
    public static RegisterResponse toModel(WalletDTO walletDTO) {
        if (walletDTO == null) {
            return null;
        }
        return RegisterResponse.builder()
                .responseTime(System.currentTimeMillis())
                .walletId(walletDTO.getWalletId())
                .walletAddress(walletDTO.getPublicKey())
                .walletPrimary(walletDTO.getPrivateKey())
                .createDate(walletDTO.getCreateDate())
                .build();

    }
}
