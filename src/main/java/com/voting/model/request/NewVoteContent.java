package com.voting.model.request;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class NewVoteContent {
    private String requestId;
    private Long requestTime;
    private String content;
    private long startDate;
    private long endDate;
    private String description;

    public boolean isValidData() {
        try {
            return !(StringUtils.isBlank(this.requestId)
                    || StringUtils.isBlank(this.content)
                    || this.startDate < 0 //System.currentTimeMillis()
                    || this.startDate >= this.endDate
                    || this.requestTime < 0); //System.currentTimeMillis() - 600000);
        }
        catch (Exception ex) {
            return false;
        }
    }
}
