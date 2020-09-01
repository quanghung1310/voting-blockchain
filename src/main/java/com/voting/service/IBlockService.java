package com.voting.service;

import com.voting.dto.WalletDTO;
import com.voting.model.request.MineTransactionRequest;
import com.voting.model.response.BlockResponse;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface IBlockService {
    JsonObject mineTransaction(String logId, WalletDTO mineWallet, MineTransactionRequest request);

    List<BlockResponse> getBlocks(String logId, String walletId);
}
