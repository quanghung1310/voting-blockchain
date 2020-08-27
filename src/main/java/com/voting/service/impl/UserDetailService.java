package com.voting.service.impl;


import com.voting.dto.WalletDTO;
import com.voting.repository.IWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("USER_DETAIL")
public class UserDetailService implements UserDetailsService {

    private IWalletRepository walletRepository;

    @Autowired
    public UserDetailService(IWalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        WalletDTO user = walletRepository.findFirstByEmail(username);
        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
