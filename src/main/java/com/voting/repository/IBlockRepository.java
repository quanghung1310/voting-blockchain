package com.voting.repository;

import com.voting.dto.BlockDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface IBlockRepository extends CrudRepository<BlockDTO, Long> {
    BlockDTO findAllByMinerIdAndTransId(String minerId, String transId);

    BlockDTO findByIdIs(Long id);

    Long countByTransId(String transId);
}
