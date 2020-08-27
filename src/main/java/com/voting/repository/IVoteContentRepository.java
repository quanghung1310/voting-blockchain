package com.voting.repository;

import com.voting.dto.VoteContentDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IVoteContentRepository extends CrudRepository<VoteContentDTO, Long> {
    @Query(value = "select * from vote_content where START_DATE >= cast(:startDate as timestamp) OR END_DATE <= cast(:endDate as timestamp)", nativeQuery = true)
    List<VoteContentDTO> findAllContentByDate(@Param("startDate") String startDate,
                                                     @Param("endDate") String endDate);

    VoteContentDTO findFirstByContentId(String contentId);


}
