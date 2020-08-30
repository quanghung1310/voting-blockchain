package com.voting.mapper;

import com.voting.dto.BlockDTO;
import com.voting.dto.TransactionDTO;
import com.voting.model.response.BlockResponse;
import com.voting.model.response.TransactionResponse;

public final class BlockMapper {
    public static BlockResponse toModelBlock(BlockDTO dto, TransactionDTO transaction) {
        if (dto == null) {
            return null;
        }
        return BlockResponse.builder()
                .id(dto.getId())
                .createDate(dto.getCreateDate())
                .currency(transaction.getCurrency())
                .blockId(dto.getBlockId())
                .miner(dto.getMinerId())
                .transId(dto.getTransId())
                .value(transaction.getValue())
                .build();
    }
}
