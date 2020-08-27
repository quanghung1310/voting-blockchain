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
//        "id": 5,
//                "blockId": "BLOCK_1590917257748",
//                "transId": "TRANS_1590823490583",
//                "miner": "1590820605329_480114a2f9ad4543ba2c869873ff9993",
//                "value": 1,
//                "currency": "vote",
//                "createDate": "2020-05-30 14:24:51"
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
