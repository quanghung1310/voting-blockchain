package com.voting.repository;

import com.voting.dto.BlockDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IBlockRepository extends CrudRepository<BlockDTO, Long> {
    BlockDTO findAllByMinerIdAndTransIdAndIsActive(String minerId, String transId, int isActive);

    List<BlockDTO> findByIdIs(Long id);

    int countByTransId(String transId);

    List<BlockDTO> findAllByMinerIdOrderByIdDesc(String minerId);

    List<BlockDTO> findAll();

    List<BlockDTO> findAllByIsActiveOrderByIdAsc(int active);

}
