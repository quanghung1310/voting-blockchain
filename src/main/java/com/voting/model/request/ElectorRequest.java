package com.voting.model.request;

import com.voting.util.DataUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class ElectorRequest {
    private String requestId = DataUtil.createRequestId();
    private Long requestTime = System.currentTimeMillis();
    private String contentId;
    private String walletId;

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
