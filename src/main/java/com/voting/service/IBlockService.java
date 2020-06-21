package com.voting.service;

import com.voting.model.request.BlockRequest;
import com.voting.model.request.MineTransactionRequest;
import com.voting.model.response.BlockResponse;
import com.voting.model.response.MineTransactionResponse;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface IBlockService {
    JsonObject mineTransaction(String logId, MineTransactionRequest request);

    List<BlockResponse> getBlocks(String logId, BlockRequest request);
}
