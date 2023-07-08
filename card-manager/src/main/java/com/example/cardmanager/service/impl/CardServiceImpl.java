package com.example.cardmanager.service.impl;

import com.example.cardmanager.service.CardService;
import com.example.cardmanager.repository.CardRepository;
import org.springframework.stereotype.Service;
import com.example.cardmanager.dto.CardDto;
import com.example.cardmanager.entity.Card;
import com.example.cardmanager.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public void saveCard(CardDto cardDto, User user) {

        Card card = new Card();
        card.setEmail_proprietario(cardDto.getEmail_intestatario());
        card.setCnumber(cardDto.getCnumber());
        card.setSaldo(cardDto.getSaldo());
        card.setStato("Attiva");

        cardRepository.save(card);
    }

    @Override
    public Card findCardByCnumber(String cnumber) {
        return cardRepository.findByCnumber(cnumber);
    }

    @Override
    public List<CardDto> findAllCards() {

        List<Card> cards = cardRepository.findAll();
        return cards.stream()
                .map((card) -> mapToCardDto(card))
                .collect(Collectors.toList());
    }

    private CardDto mapToCardDto(Card card) {
        CardDto cardDto = new CardDto();
        cardDto.setEmail_intestatario(card.getEmail_proprietario());
        cardDto.setCnumber(card.getCnumber());
        cardDto.setSaldo(card.getSaldo());
        return cardDto;
    }

    public void blockCard(String cnumber) {
        Card card = cardRepository.findByCnumber(cnumber);
        card.setStato("Bloccata");
        cardRepository.save(card);
    }

    public void activeCard(String cnumber) {
        Card card = cardRepository.findByCnumber(cnumber);
        card.setStato("Attiva");
        cardRepository.save(card);
    }

    public void rechargeCard(String cnumber, float amount) {
        Card card = cardRepository.findByCnumber(cnumber);
        card.setSaldo(card.getSaldo() + amount);
        cardRepository.save(card);

    }

    public void payCard(String cnumber, float amount) {
        Card card = cardRepository.findByCnumber(cnumber);
        card.setSaldo(card.getSaldo() - amount);
        cardRepository.save(card);
    }

}