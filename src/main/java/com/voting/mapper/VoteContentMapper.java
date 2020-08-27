package com.voting.mapper;

import com.voting.constants.StringConstant;
import com.voting.dto.VoteContentDTO;
import com.voting.model.response.VoteContentResponse;
import com.voting.util.DataUtil;

public final class VoteContentMapper {
    public static VoteContentResponse toModelVoteContent(VoteContentDTO dto) {
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
                .build();
    }
}
