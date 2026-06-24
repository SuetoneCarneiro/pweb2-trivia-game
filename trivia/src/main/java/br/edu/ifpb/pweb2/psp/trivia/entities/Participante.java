package br.edu.ifpb.pweb2.psp.trivia.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Participante")
@Getter @Setter
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 80)
    private String nome;

    @Column(name = "email", length = 80, unique = true)
    private String email;

    // Senha criptografada
    @Column(name = "senha", length = 100)
    private String senha;

    // Perfis do participante, persistidos em tabela separada (participante_perfis).
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "participante_perfis",
        joinColumns = @JoinColumn(name = "id_participante")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", length = 20)
    private Set<Perfil> perfis = new HashSet<>();

    public boolean isAdmin() {
        return perfis != null && perfis.contains(Perfil.ADMIN);
    }
}
