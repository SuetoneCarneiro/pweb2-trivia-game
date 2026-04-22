package br.edu.ifpb.pweb2.psp.trivia.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Resultado")
@Getter @Setter
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pontuacao")
    private BigDecimal pontuacao;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "id_participante", referencedColumnName = "id")
    private Participante idParticipante;

    @ManyToOne
    @JoinColumn(name = "id_corrida", referencedColumnName = "id")
    private Corrida idCorrida;

}
