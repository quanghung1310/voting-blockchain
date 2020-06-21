package com.voting.model.response;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MineTransactionResponse {
    private String blockId;
    private int difficulty;
    private long nonce;
    private String prevHash;
    private String hash;

    public JsonObject toJson() {
        return new JsonObject(this.toString());
    }

    @Override
    public String toString() {
        try {
            return DatabindCodec.mapper().writeValueAsString(this);
        } catch (Exception e) {
            return "{}";
        }
    }
}
