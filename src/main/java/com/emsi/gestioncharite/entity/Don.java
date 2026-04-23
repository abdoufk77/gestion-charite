package com.emsi.gestioncharite.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Don {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double montant;

    @ManyToOne
    @JoinColumn(name = "action_charite_id")
    private ActionCharite actionCharite;

    @ManyToOne
    @JoinColumn(name = "donateur_id")
    private Donateur donateur;
}
