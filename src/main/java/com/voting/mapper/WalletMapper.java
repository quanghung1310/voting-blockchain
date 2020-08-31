package com.voting.mapper;

import com.voting.constants.StringConstant;
import com.voting.dto.WalletDTO;
import com.voting.model.response.ElectorResponse;
import com.voting.model.response.LogInResponse;
import com.voting.model.response.RegisterResponse;
import com.voting.util.DataUtil;

public final class WalletMapper {
    public static RegisterResponse toModelRegister(WalletDTO walletDTO) {
        if (walletDTO == null) {
            return null;
        }
        return RegisterResponse.builder()
                .walletId(walletDTO.getWalletId())
                .walletAddress(walletDTO.getPublicKey())
                .walletPrimary(walletDTO.getPrivateKey())
                .createDate(DataUtil.convertTimeWithFormat(walletDTO.getCreateDate().getTime(), StringConstant.FORMAT_ddMMyyyyTHHmmss))
                .build();

    }

    public static LogInResponse toModelLogIn(WalletDTO walletDTO) {
        if (walletDTO == null) {
            return LogInResponse.builder().build();
        }
        return LogInResponse.builder()
                .lastName(walletDTO.getLastName())
                .firstName(walletDTO.getFirstName())
                .sex(walletDTO.getSex())
                .email(walletDTO.getEmail())
                .type(walletDTO.getType())
                .build();
    }

    public static ElectorResponse toModelElector(WalletDTO walletDTO, String contentId, boolean isRegister) {
        if (walletDTO == null) {
            return ElectorResponse.builder().build();
        }
        return ElectorResponse.builder()
                .walletId(walletDTO.getWalletId())
                .walletAddress(walletDTO.getPublicKey())
                .lastName(walletDTO.getLastName())
                .firstName(walletDTO.getFirstName())
                .sex(walletDTO.getSex())
                .email(walletDTO.getEmail())
                .type(walletDTO.getType())
                .active(walletDTO.getActive())
                .contentId(contentId)
                .isRegister(isRegister)
                .build();
    }
}
