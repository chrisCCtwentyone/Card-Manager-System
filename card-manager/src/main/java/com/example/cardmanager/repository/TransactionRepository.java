package com.example.cardmanager.repository;

import com.example.cardmanager.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findByNumerocarta(String numerocarta);

}