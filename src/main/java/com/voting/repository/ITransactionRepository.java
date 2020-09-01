package com.voting.repository;

import com.voting.dto.TransactionDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface ITransactionRepository extends CrudRepository<TransactionDTO, Long> {
    int countBySenderAndReceiverAndContentIdAndCreateDateAfterAndStatusIn(String sender, String receiver, String contentId, Timestamp createDate, List<Integer> status);

    TransactionDTO findByTransId(String transId);

    TransactionDTO findFirstByTransId(String transId);

    List<TransactionDTO> findAllBySenderOrReceiver(String sender, String receiver);

    List<TransactionDTO> findAllByStatus(int active);

    @Query(value = "UPDATE transaction SET IS_MINE = :status, LAST_MODIFY = :lastModify WHERE TRANS_ID = :transId", nativeQuery = true)
    void setIsMineByTransId(@Param("status") int status,
                            @Param("lastModify") Timestamp lastModify,
                            @Param("transId") String transId);

    int countAllByContentIdAndStatusAndReceiver(String contentId, int status, String receiverWallet);
}
