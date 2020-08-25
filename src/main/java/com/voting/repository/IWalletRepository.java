package com.voting.repository;

import com.voting.dto.WalletDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IWalletRepository extends CrudRepository<WalletDTO, Long> {
    WalletDTO findAllByWalletId(String walletId);

    @Query(value = "select w.*" +
            " FROM elector e" +
            " left join wallet w on w.wallet_id = e.WALLET_ID" +
            " left join vote_content v on v.CONTENT_ID = e.CONTENT_ID" +
            " where e.CONTENT_ID = :contentId and START_DATE >= current_date OR END_DATE <= current_date and w.type = 1", nativeQuery = true)
    List<WalletDTO> findAllByContentId(@Param("contentId") String contentId);

    WalletDTO findAllByPublicKeyAndActive(String publicKey, int active);

    WalletDTO findAllByPublicKeyAndActiveAndType(String publicKey, int active, int type);

    Long countByActiveAndType(int active, int type);

    WalletDTO findFirstByEmail(String email);

    @Query(value = "select w.*" +
            " FROM elector e" +
            " left join wallet w on w.wallet_id = e.WALLET_ID" +
            " left join vote_content v on v.CONTENT_ID = e.CONTENT_ID" +
            " where START_DATE >= current_date and END_DATE <= current_date and w.type = 1", nativeQuery = true)
    List<WalletDTO> findAllElector();

    WalletDTO findFirstByWalletIdAndActive(String walletId, int active);

}
