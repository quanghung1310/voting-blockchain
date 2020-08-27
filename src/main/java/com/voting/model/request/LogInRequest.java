package com.voting.model.request;

import com.voting.util.DataUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class LogInRequest {
    private String requestId = DataUtil.createRequestId();
    private Long requestTime = System.currentTimeMillis();
    private String email;
    private String password;


    public boolean isValidData() {
        try {
            return !(StringUtils.isBlank(this.requestId)
                    || StringUtils.isBlank(this.email)
                    || StringUtils.isBlank(this.password)
                    || requestTime < 0);
        }
        catch (Exception ex) {
            return false;
        }
    }
}
