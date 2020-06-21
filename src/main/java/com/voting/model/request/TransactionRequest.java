package com.voting.model.request;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class TransactionRequest {
    private String requestId;
    private Long requestTime = System.currentTimeMillis();
    private String walletId;

    public boolean isValidData() {
        try {
            return !(StringUtils.isBlank(this.requestId)
                    || this.requestTime < 0);
        }
        catch (Exception ex) {
            return false;
        }
    }
}
