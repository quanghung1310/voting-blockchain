package com.voting.repository;

import com.voting.dto.BlockDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IBlockRepository extends CrudRepository<BlockDTO, Long> {
    BlockDTO findAllByMinerIdAndTransId(String minerId, String transId);

    BlockDTO findByIdIs(Long id);

    Long countByTransId(String transId);

    List<BlockDTO> findAllByMinerIdOrderByIdDesc(String minerId);

    List<BlockDTO> findAll();

}
