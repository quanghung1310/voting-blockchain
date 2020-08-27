package com.voting.model.request;

import com.voting.util.DataUtil;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class MineTransactionRequest {
    private String requestId = DataUtil.createRequestId();
    private long requestTime = System.currentTimeMillis();
    private String transId;

    public boolean isValidData() {
        try {
            return !(StringUtils.isBlank(this.requestId)
                    || this.requestTime < 0
                    || StringUtils.isBlank(this.transId));
        }
        catch (Exception ex) {
            return false;
        }
    }
}
