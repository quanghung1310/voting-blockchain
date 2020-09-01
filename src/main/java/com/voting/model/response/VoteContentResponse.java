package com.voting.model.response;

import io.vertx.core.json.jackson.DatabindCodec;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class VoteContentResponse {
    private String contentId;
    private String content;
    private String description;
    private String startDate;
    private String endDate;
    private String creator;
    private boolean isRegister;
    List<ElectorResponse> electors;

    @Override
    public String toString() {
        try {
            return DatabindCodec.mapper().writeValueAsString(this);
        } catch (Exception e) {
            return "{}";
        }
    }
}
