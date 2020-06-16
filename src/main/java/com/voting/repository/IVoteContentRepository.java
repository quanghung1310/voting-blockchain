package com.voting.repository;

import com.voting.dto.VoteContentDTO;
import org.springframework.data.repository.CrudRepository;

public interface IVoteContentRepository extends CrudRepository<VoteContentDTO, Long> {
}
