package com.voting.model.response;

import io.vertx.core.json.jackson.DatabindCodec;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WalletResponse {
    private String walletId;
    private String walletAddress;
    private String walletPrimary;
    private String createDate;
    private String lastName;
    private String firstName;
    private String mail;
    private int sex;

    @Override
    public String toString() {
        try {
            return DatabindCodec.mapper().writeValueAsString(this);
        } catch (Exception e) {
            return "{}";
        }
    }
}
