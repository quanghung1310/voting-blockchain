package com.voting.repository;

import com.voting.dto.BlockChainDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IBlockChainRepository extends CrudRepository<BlockChainDTO, Long> {
    List<BlockChainDTO> findByOrderByIdDesc();
}
