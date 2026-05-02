package com.emsi.gestioncharite.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProfilDonateurRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;

    // Rempli uniquement si le donateur veut changer son mot de passe
    private String motDePasseActuel;
    private String nouveauMotDePasse;
    private String confirmMotDePasse;
}
