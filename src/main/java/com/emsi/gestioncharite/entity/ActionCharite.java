package com.emsi.gestioncharite.entity;

import com.emsi.gestioncharite.enums.Categorie;
import com.emsi.gestioncharite.enums.TypeAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ActionCharite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titre;
    private String description;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateFin;

    @Enumerated(EnumType.STRING)
    private Categorie categorie;

    @Enumerated(EnumType.STRING)
    private TypeAction typeAction;

    // Renseigné uniquement si typeAction == FINANCIER
    private Double objectifMontant;

    // Renseigné uniquement si typeAction == PHYSIQUE
    private Integer nombrePlacesMax;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @OneToMany(mappedBy = "actionCharite", cascade = CascadeType.ALL)
    private List<Don> dons;

    @OneToMany(mappedBy = "actionCharite", cascade = CascadeType.ALL)
    private List<Participation> participations;
}
