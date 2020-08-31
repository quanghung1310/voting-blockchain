package com.voting.repository;

import com.voting.dto.ElectorDTO;
import org.springframework.data.repository.CrudRepository;

public interface IElectorRepository extends CrudRepository<ElectorDTO, Long> {
    ElectorDTO findFirstByContentIdAndWalletId(String contentId, String walletId);
}
