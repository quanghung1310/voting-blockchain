package com.voting.repository;

import com.voting.dto.TransactionDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ITransactionRepository extends CrudRepository<TransactionDTO, Long> {
    Integer countBySender(String sender);

    TransactionDTO findByTransId(String transId);

    List<TransactionDTO> findAllBySenderAndActive(String sender, int active);

    List<TransactionDTO> findAllByActive(int active);
}
