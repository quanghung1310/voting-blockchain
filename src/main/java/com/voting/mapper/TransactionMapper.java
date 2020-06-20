package com.voting.mapper;

import com.voting.dto.TransactionDTO;
import com.voting.model.response.TransactionResponse;

public final class TransactionMapper {
    public static TransactionResponse toModelTransaction(TransactionDTO dto, String walletId) {
        if (dto == null) {
            return null;
        }
        return TransactionResponse.builder()
                .id(dto.getId())
                .createDate(dto.getCreateDate())
                .currency(dto.getCurrency())
                .from(dto.getSender())
                .isMine(dto.getIsMine() == 1)
                .to(dto.getReceipt())
                .transId(dto.getTransId())
                .value(dto.getValue())
                .walletId(walletId)
                .build();
    }
}
