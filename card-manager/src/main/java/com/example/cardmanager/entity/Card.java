/*Classe carta che verrà mappata nella tabella cards del mio database */
package com.example.cardmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carta;

    /*Nome comprende sia il nome che il cognome del proprietario */

    @Column(nullable = false)
    private String email_proprietario;

    @Column(nullable = false, unique = true)
    private String cnumber;

    @Column(nullable = false)
    private float saldo;

    /* Stato della carta, può essere "Attivata" o "Bloccata" */
    @Column(nullable = true)
    private String stato;

}