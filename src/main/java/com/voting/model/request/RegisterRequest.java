package com.voting.model.request;

import com.voting.util.DataUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class RegisterRequest {
    private String requestId = DataUtil.createRequestId();
    private Long requestTime = System.currentTimeMillis();
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private int sex;
    private int type;

    public boolean isValidData() {
        try {
            return !(StringUtils.isBlank(this.requestId)
                    || StringUtils.isBlank(this.email)
                    || StringUtils.isBlank(this.password)
                    || StringUtils.isBlank(this.lastName)
                    || StringUtils.isBlank(this.firstName)
                    || requestTime <= 0
                    || type < 0
                    || sex < 0);

        }
        catch (Exception ex) {
            return false;
        }
    }
}
