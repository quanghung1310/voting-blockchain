package com.voting.model.response;

import io.vertx.core.json.jackson.DatabindCodec;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class RegisterResponse {
    private Long responseTime;
    private String walletId;
    private String walletAddress;
    private String walletPrimary;
    private Timestamp createDate;

    @Override
    public String toString() {
        try {
            return DatabindCodec.mapper().writeValueAsString(this);
        } catch (Exception e) {
            return "{}";
        }
    }
}
