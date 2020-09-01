package com.voting.model.response;

import io.vertx.core.json.jackson.DatabindCodec;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ElectorResponse {
    private String walletId;
    private String walletAddress;
    private String email;
    private String firstName;
    private String lastName;
    private int sex;
    private int type;
    private int active;
    private String contentId;
    private boolean isRegister;
    private int voted;

    @Override
    public String toString() {
        try {
            return DatabindCodec.mapper().writeValueAsString(this);
        } catch (Exception e) {
            return "{}";
        }
    }
}
