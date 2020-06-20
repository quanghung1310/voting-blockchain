package com.voting.service.impl;

import com.voting.repository.ITransactionRepository;
import com.voting.service.ITransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService {
    private static final Logger logger = LogManager.getLogger(TransactionService.class);

    @Autowired
    public ITransactionRepository transactionRepository;
}
