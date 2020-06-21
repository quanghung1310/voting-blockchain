package com.voting.mapper;

import com.voting.dto.VoteContentDTO;
import com.voting.model.response.VoteContentResponse;

public final class VoteContentMapper {
    public static VoteContentResponse toModelVoteContent(VoteContentDTO dto) {
        if (dto == null) {
            return VoteContentResponse.builder().build();
        }
        return VoteContentResponse.builder()
                .id(dto.getId())
                .contentId(dto.getContentId())
                .content(dto.getContent())
                .description(dto.getDescription())
                .build();
    }
}
