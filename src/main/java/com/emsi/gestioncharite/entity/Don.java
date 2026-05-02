package com.emsi.gestioncharite.entity;

import com.emsi.gestioncharite.enums.MethodePaiement;
import com.emsi.gestioncharite.enums.StatutDon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Don {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double montant;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDon;

    @Enumerated(EnumType.STRING)
    private StatutDon statut = StatutDon.EN_ATTENTE;

    @Enumerated(EnumType.STRING)
    private MethodePaiement methodePaiement;

    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "action_charite_id")
    private ActionCharite actionCharite;

    @ManyToOne
    @JoinColumn(name = "donateur_id")
    private Donateur donateur;
}
