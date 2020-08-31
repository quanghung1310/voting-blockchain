package com.voting.mapper;

import com.voting.constants.StringConstant;
import com.voting.dto.ElectorDTO;
import com.voting.dto.VoteContentDTO;
import com.voting.model.response.ElectorResponse;
import com.voting.model.response.VoteContentResponse;
import com.voting.util.DataUtil;

import java.util.ArrayList;
import java.util.List;

public final class VoteContentMapper {
    public static VoteContentResponse toModelVoteContent(VoteContentDTO dto, boolean isRegister, List<ElectorResponse> electorResponses) {
        if (dto == null) {
            return VoteContentResponse.builder().build();
        }
        return VoteContentResponse.builder()
                .contentId(dto.getContentId())
                .content(dto.getContent())
                .description(dto.getDescription())
                .endDate(DataUtil.convertTimeWithFormat(dto.getEndDate().getTime(), StringConstant.FORMAT_ddMMyyyyTHHmmss))
                .startDate(DataUtil.convertTimeWithFormat(dto.getStartDate().getTime(), StringConstant.FORMAT_ddMMyyyyTHHmmss))
                .creator(dto.getWalletId())
                .isRegister(isRegister)
                .electors(electorResponses)
                .build();
    }
}
