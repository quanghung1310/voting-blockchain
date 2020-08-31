package com.voting.mapper;

import com.voting.constants.ActionConstant;
import com.voting.dto.TransactionDTO;
import com.voting.model.response.TransactionResponse;

public final class TransactionMapper {
    public static TransactionResponse toModelTransaction(TransactionDTO dto) {
        if (dto == null) {
            return null;
        }
        return TransactionResponse.builder()
                .id(dto.getId())
                .createDate(dto.getCreateDate())
                .currency(dto.getCurrency())
                .from(dto.getSender())
                .isMined(dto.getStatus() == ActionConstant.COMPLETED.getValue())
                .to(dto.getReceiver())
                .transId(dto.getTransId())
                .value(dto.getValue())
                .contentId(dto.getContentId())
                .signature(dto.getSignature())
                .description(dto.getDescription())
                .build();
    }
}
