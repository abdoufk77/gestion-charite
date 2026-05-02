package com.emsi.gestioncharite.entity;

import com.emsi.gestioncharite.enums.StatutParticipation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private StatutParticipation statut = StatutParticipation.EN_ATTENTE;

    @ManyToOne
    @JoinColumn(name = "action_charite_id")
    private ActionCharite actionCharite;

    @ManyToOne
    @JoinColumn(name = "donateur_id")
    private Donateur donateur;
}
