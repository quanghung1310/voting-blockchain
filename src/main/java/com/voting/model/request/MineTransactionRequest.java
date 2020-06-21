package com.voting.model.request;

import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class MineTransactionRequest {
    private String requestId;
    private long requestTime = System.currentTimeMillis();
    private String walletId;
    private String transId;

    public boolean isValidData() {
        try {
            return !(StringUtils.isBlank(this.requestId)
                    || this.requestTime < 0
                    || StringUtils.isBlank(this.walletId)
                    || StringUtils.isBlank(this.transId));
        }
        catch (Exception ex) {
            return false;
        }
    }
}
