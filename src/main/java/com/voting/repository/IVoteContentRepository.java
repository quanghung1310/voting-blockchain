package com.voting.repository;

import com.voting.dto.VoteContentDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface IVoteContentRepository extends CrudRepository<VoteContentDTO, Long> {
    @Query(value = "select * from vote_content where START_DATE >= :startDate and END_DATE <= :endDate", nativeQuery = true)
    public List<VoteContentDTO> findAllContentByDate(@Param("startDate") Timestamp startDate,
                                                     @Param("endDate") Timestamp endDate);
}
