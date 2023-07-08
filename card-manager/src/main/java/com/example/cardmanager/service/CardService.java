package com.example.cardmanager.service;

import com.example.cardmanager.dto.CardDto;
import com.example.cardmanager.entity.Card;
import com.example.cardmanager.entity.User;

import java.util.List;

public interface CardService {
    void saveCard(CardDto cardDto, User user);

    Card findCardByCnumber(String cnumber);

    List<CardDto> findAllCards();

    void blockCard(String cnumber);

    void activeCard(String cnumber);

    void rechargeCard(String cnumber, float amount);

    void payCard(String cnumber, float amount);
}