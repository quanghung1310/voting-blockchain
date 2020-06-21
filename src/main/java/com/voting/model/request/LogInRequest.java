package com.voting.model.request;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class LogInRequest {
    private String requestId;
    private Long requestTime = System.currentTimeMillis();
    private String walletId;
    private String password;


    public boolean isValidData() {
        try {
            return !(StringUtils.isBlank(this.requestId)
                    || StringUtils.isBlank(this.walletId)
                    || StringUtils.isBlank(this.password)
                    || requestTime < 0);
        }
        catch (Exception ex) {
            return false;
        }
    }
}
