package com.example.cardmanager.dto;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto
{
    private Long id_transazione;
    private String numerocarta;
    @NotEmpty
    private float importo; 
    @CreationTimestamp
    private Date data_transazione;
    @NotEmpty
    private String tipo;
    @NotEmpty
    private String emailNegoziante;

}