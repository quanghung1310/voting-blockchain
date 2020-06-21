package com.voting.model.request;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class VotingRequest {
    private String requestId;
    private Long requestTime = System.currentTimeMillis();
    private String  sender;
    private String reciepient;
    private Integer value;
    private String description;
    private String contentId;
    private String currency;

    public boolean isValidData() {
        try {
            return !(StringUtils.isBlank(this.requestId)
                    || this.requestTime < 0
                    || StringUtils.isBlank(this.sender)
                    || StringUtils.isBlank(this.reciepient)
                    || this.value <= 0
                    || StringUtils.isBlank(this.contentId));
        }
        catch (Exception ex) {
            return false;
        }
    }
}
