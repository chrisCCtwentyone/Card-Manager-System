package com.example.cardmanager.service;

import com.example.cardmanager.dto.TransactionDto;
import com.example.cardmanager.entity.Transaction;

import java.util.List;

public interface TransactionService {
    void saveTransaction(String cnumber, float amount, String tipo, String emailNegoziante);

    Transaction findTransactionByNumeroCarta(String numerocarta);


    List<TransactionDto> findAllTransactions();
}