package com.example.cardmanager.repository;

import com.example.cardmanager.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByCnumber(String numero_carta);

}