package br.edu.ifpb.pweb2.psp.trivia.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Corrida")
@Getter @Setter
public class Corrida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", length = 80)
    private String titulo; 

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "tempo")
    private Integer tempo;

    @Column(name = "ativo")
    private Boolean ativo;

    @OneToMany(mappedBy = "idCorrida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pergunta> perguntas = new ArrayList<>();
}
