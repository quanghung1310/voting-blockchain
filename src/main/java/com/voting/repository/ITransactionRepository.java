package com.voting.repository;

import com.voting.dto.WalletDTO;
import org.springframework.data.repository.CrudRepository;

public interface ITransactionRepository extends CrudRepository<WalletDTO, Long> {
}
