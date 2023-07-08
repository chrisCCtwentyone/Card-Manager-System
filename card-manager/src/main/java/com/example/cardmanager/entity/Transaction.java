/*Classe transazione che verrà mappata nella tabella transactions del mio database */
package com.example.cardmanager.entity;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_transazione;

    @Column()
    private String numerocarta;

    @Column()
    private float importo;

    @Column(nullable = false)
    @CreationTimestamp
    private Date data_transazione;

    @Column()
    private String tipo;

    @Column()
    private String emailNegoziante;

}