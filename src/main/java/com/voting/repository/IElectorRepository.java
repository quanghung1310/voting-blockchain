package com.voting.repository;

import com.voting.dto.ElectorDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IElectorRepository extends CrudRepository<ElectorDTO, Long> {
    List<ElectorDTO> findAllByContentIdAndWalletId(String contentId, String walletId);
    List<ElectorDTO> findAllByContentId(String contentId);
}
