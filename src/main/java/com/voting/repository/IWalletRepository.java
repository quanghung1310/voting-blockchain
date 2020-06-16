package com.voting.repository;

import com.voting.dto.WalletDTO;
import org.springframework.data.repository.CrudRepository;

public interface IWalletRepository extends CrudRepository<WalletDTO, Long> {
    public WalletDTO getAllByWalletId(String walletId);
}
