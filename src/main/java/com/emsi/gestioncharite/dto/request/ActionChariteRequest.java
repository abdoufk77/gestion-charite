package com.emsi.gestioncharite.dto.request;

import com.emsi.gestioncharite.enums.Categorie;
import com.emsi.gestioncharite.enums.TypeAction;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter @Setter
public class ActionChariteRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "La date de début est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFin;

    @NotNull(message = "La catégorie est obligatoire")
    private Categorie categorie;

    @NotNull(message = "Le type d'action est obligatoire")
    private TypeAction typeAction;

    @Positive(message = "L'objectif doit être un montant positif")
    private Double objectifMontant;

    @Min(value = 1, message = "Le nombre de places doit être au moins 1")
    private Integer nombrePlacesMax;
}
