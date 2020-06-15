package com.voting.model.request;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class NewWalletReq {
    private String requestId;
    private Long requestTime;
    private String email;
    private String password;

    public boolean isValidData() {
        try {
            return StringUtils.isNotBlank(this.requestId)
                    && StringUtils.isNotBlank(this.email)
                    && StringUtils.isNotBlank(this.password)
                    && requestTime >= 0;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
