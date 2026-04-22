package br.edu.ifpb.pweb2.psp.trivia.entities;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
@Table(name = "Pergunta")
@Getter @Setter
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enunciado")
    private String enunciado;

    @Column(name = "resposta")
    private String resposta;

    @ManyToOne
    @JoinColumn(name = "id_corrida", referencedColumnName = "id")
    private Corrida idCorrida;

    @ElementCollection
    @CollectionTable(name = "Alternativa", joinColumns = @JoinColumn(name = "id_pergunta"))
    @Column(name = "alternativa")
    private List<String> alternativas;    


}
