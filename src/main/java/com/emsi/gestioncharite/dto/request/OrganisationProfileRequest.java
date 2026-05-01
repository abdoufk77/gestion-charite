package com.emsi.gestioncharite.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class OrganisationProfileRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "L'adresse légale est obligatoire")
    private String adresseLegale;

    private String contactPrincipal;
    private String telephone;
    private String siteWeb;
    private String description;
    private String mission;

    private MultipartFile logo;
}
