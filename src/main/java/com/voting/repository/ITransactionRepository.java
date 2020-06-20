package com.voting.repository;

import com.voting.dto.TransactionDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ITransactionRepository extends CrudRepository<TransactionDTO, Long> {
    Integer countBySender(String sender);

    TransactionDTO findByTransId(String transId);

}
