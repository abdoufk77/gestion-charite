package com.emsi.gestioncharite;

import com.emsi.gestioncharite.entity.ActionCharite;
import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.entity.Donateur;
import com.emsi.gestioncharite.entity.Organisation;
import com.emsi.gestioncharite.entity.SuperAdmin;
import com.emsi.gestioncharite.enums.Categorie;
import com.emsi.gestioncharite.enums.Role;
import com.emsi.gestioncharite.enums.StatutOrg;
import com.emsi.gestioncharite.repository.ActionChariteRepository;
import com.emsi.gestioncharite.repository.OrganisationRepository;
import com.emsi.gestioncharite.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class GestionChariteApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionChariteApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDemoData(UtilisateurRepository utilisateurRepository,
                                          OrganisationRepository organisationRepository,
                                          ActionChariteRepository actionChariteRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            if (utilisateurRepository.count() > 0) return;

            // Utilisateurs
            SuperAdmin superAdmin = new SuperAdmin();
            superAdmin.setNom("Super Admin");
            superAdmin.setPrenom("Système");
            superAdmin.setEmail("superadmin@charite.com");
            superAdmin.setMotDePasse(passwordEncoder.encode("admin123"));
            superAdmin.setRole(Role.SUPER_ADMIN);

            AdminOrganisation adminOrg = new AdminOrganisation();
            adminOrg.setNom("Admin Org");
            adminOrg.setPrenom("Test");
            adminOrg.setEmail("admin@organisation.com");
            adminOrg.setMotDePasse(passwordEncoder.encode("admin123"));
            adminOrg.setRole(Role.ADMIN_ORGANISATION);

            Donateur donateur = new Donateur();
            donateur.setNom("Foukahy");
            donateur.setPrenom("Abderrahmane");
            donateur.setEmail("donateur@test.com");
            donateur.setMotDePasse(passwordEncoder.encode("test123"));
            donateur.setRole(Role.DONATEUR);

            utilisateurRepository.saveAll(List.of(superAdmin, adminOrg, donateur));

            // Organisation de démo
            Organisation org = new Organisation();
            org.setNom("Fondation Espoir Maroc");
            org.setAdresseLegale("123 Avenue Hassan II, Casablanca");
            org.setNumIdentFiscale("IF-2024-001");
            org.setContactPrincipal("contact@espoir-maroc.ma");
            org.setDescription("Organisation dédiée au soutien des communautés vulnérables.");
            org.setStatut(StatutOrg.ACTIVE);
            organisationRepository.save(org);

            // 3 Actions de charité
            ActionCharite action1 = new ActionCharite();
            action1.setTitre("Fournitures scolaires pour enfants défavorisés");
            action1.setDescription("Distribution de cartables, cahiers et fournitures aux élèves des zones rurales au début de l'année scolaire.");
            action1.setCategorie(Categorie.EDUCATION);
            action1.setDateDebut(new Date());
            action1.setDateFin(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
            action1.setOrganisation(org);

            ActionCharite action2 = new ActionCharite();
            action2.setTitre("Campagne de vaccination gratuite");
            action2.setDescription("Organisation de journées de vaccination pour les familles sans couverture médicale dans les quartiers défavorisés.");
            action2.setCategorie(Categorie.SANTE);
            action2.setDateDebut(new Date());
            action2.setDateFin(new Date(System.currentTimeMillis() + 15L * 24 * 60 * 60 * 1000));
            action2.setOrganisation(org);

            ActionCharite action3 = new ActionCharite();
            action3.setTitre("Plantation d'arbres — Forêt urbaine");
            action3.setDescription("Reboisement de 500 arbres dans les périphéries urbaines pour lutter contre la désertification et améliorer la qualité de l'air.");
            action3.setCategorie(Categorie.ENVIRONNEMENT);
            action3.setDateDebut(new Date());
            action3.setDateFin(new Date(System.currentTimeMillis() + 60L * 24 * 60 * 60 * 1000));
            action3.setOrganisation(org);

            actionChariteRepository.saveAll(List.of(action1, action2, action3));

            System.out.println("=== Données de démo insérées ===");
            System.out.println("SuperAdmin    : superadmin@charite.com   / admin123");
            System.out.println("AdminOrg      : admin@organisation.com   / admin123");
            System.out.println("Donateur      : donateur@test.com        / test123");
            System.out.println("Actions       : 3 actions créées (Education, Santé, Environnement)");
            System.out.println("================================");
        };
    }
}
