package com.example.cardmanager.service.impl;

import com.example.cardmanager.service.TransactionService;
import com.example.cardmanager.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import com.example.cardmanager.dto.TransactionDto;
import com.example.cardmanager.entity.Transaction;
import java.util.Date;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void saveTransaction(String cnumber, float amount, String tipo, String emailNegoziante) {
        Transaction transaction = new Transaction();
        transaction.setImporto(amount);
        transaction.setTipo(tipo);  
        transaction.setEmailNegoziante(emailNegoziante);
        transaction.setNumerocarta(cnumber);
        transaction.setData_transazione(new Date());

       
        

        transactionRepository.save(transaction);
    }

    @Override
    public Transaction findTransactionByNumeroCarta(String numerocarta) {
        return transactionRepository.findByNumerocarta(numerocarta);
    }

    @Override
    public List<TransactionDto> findAllTransactions(){

        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map((transaction) -> mapToTransactionDto(transaction))
                .collect(Collectors.toList());
    }

    private TransactionDto mapToTransactionDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setImporto(transaction.getImporto());
        transactionDto.setData_transazione(transaction.getData_transazione());
        transactionDto.setTipo(transaction.getTipo());
        transactionDto.setEmailNegoziante(transaction.getEmailNegoziante());
        transactionDto.setNumerocarta(transaction.getNumerocarta());
        return transactionDto;
    }
}