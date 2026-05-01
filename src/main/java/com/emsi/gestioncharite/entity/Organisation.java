package com.emsi.gestioncharite.entity;

import com.emsi.gestioncharite.enums.StatutOrg;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;
    private String adresseLegale;
    private String numIdentFiscale;
    private String contactPrincipal;
    private String logoUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String mission;

    private String telephone;
    private String siteWeb;

    @Enumerated(EnumType.STRING)
    private StatutOrg statut;

    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL)
    private List<ActionCharite> actions;
}
