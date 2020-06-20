package com.voting.repository;

import com.voting.dto.VoteContentDTO;
import com.voting.dto.WalletDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface IWalletRepository extends CrudRepository<WalletDTO, Long> {
    public WalletDTO getAllByWalletId(String walletId);

    @Query(value = "select w.*" +
            " FROM voting.elector e" +
            " left join voting.wallet w on w.wallet_id = e.WALLET_ID" +
            " left join voting.vote_content v on v.CONTENT_ID = e.CONTENT_ID" +
            " where e.CONTENT_ID = :contentId and START_DATE <= sysdate() and END_DATE >= sysdate() and w.type = 1", nativeQuery = true)
    public List<WalletDTO> findAllByContentId(@Param("contentId") String contentId);

    public WalletDTO findAllByPublicKeyAndActive(String publicKey, int active);

    public WalletDTO findAllByPublicKeyAndActiveAndType(String publicKey, int active, int type);
}
