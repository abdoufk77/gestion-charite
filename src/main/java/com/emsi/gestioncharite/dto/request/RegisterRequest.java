package com.emsi.gestioncharite.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {
    // Champs communs
    private String typeCompte;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String confirmPassword;

    // Champs organisation
    private String nomOrganisation;
    private String adresseLegale;
    private String numIdentFiscale;
    private String descriptionOrg;
}
