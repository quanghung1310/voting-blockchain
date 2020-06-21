package com.voting.service;

import com.voting.model.request.MineTransactionRequest;
import com.voting.model.response.MineTransactionResponse;
import io.vertx.core.json.JsonObject;

public interface IBlockService {
    JsonObject mineTransaction(String logId, MineTransactionRequest request);
}
