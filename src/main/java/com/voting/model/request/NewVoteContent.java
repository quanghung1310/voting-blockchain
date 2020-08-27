package com.voting.model.request;

import com.voting.util.DataUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class NewVoteContent {
    private String requestId = DataUtil.createRequestId();
    private Long requestTime = System.currentTimeMillis();
    private String content;
    private String startDate;
    private String endDate;
    private String description;

    public boolean isValidData() {
        try {
            return !(StringUtils.isBlank(this.requestId)
                    || StringUtils.isBlank(this.content)
                    || StringUtils.isBlank(this.startDate)
                    || StringUtils.isBlank(this.startDate)
                    || this.requestTime < 0);
        }
        catch (Exception ex) {
            return false;
        }
    }
}
