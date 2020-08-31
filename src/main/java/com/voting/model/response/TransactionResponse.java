package com.voting.model.response;

import io.vertx.core.json.jackson.DatabindCodec;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class TransactionResponse {
    private Long id;
    private String transId;
    private Boolean isMined;
    private String from;
    private String to;
    private Integer value;
    private String currency;
    private Timestamp createDate;
    private String signature;
    private String description;
    private String contentId;

    @Override
    public String toString() {
        try {
            return DatabindCodec.mapper().writeValueAsString(this);
        } catch (Exception e) {
            return "{}";
        }
    }
}
