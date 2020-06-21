package com.voting.model.request;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class ElectorRequest {
    private String requestId;
    private Long requestTime = System.currentTimeMillis();
    private String contentId;

    public boolean isValidData() {
        try {
            return !(StringUtils.isBlank(this.requestId)
                    || this.requestTime < 0
                    || StringUtils.isBlank(this.contentId));
        }
        catch (Exception ex) {
            return false;
        }
    }
}
