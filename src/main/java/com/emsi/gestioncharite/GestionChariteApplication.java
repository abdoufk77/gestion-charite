package com.emsi.gestioncharite;

import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.entity.Donateur;
import com.emsi.gestioncharite.entity.SuperAdmin;
import com.emsi.gestioncharite.enums.Role;
import com.emsi.gestioncharite.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class GestionChariteApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionChariteApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDemoData(UtilisateurRepository utilisateurRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            if (utilisateurRepository.count() > 0) return;

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
            donateur.setNom("Donateur");
            donateur.setPrenom("Test");
            donateur.setEmail("donateur@test.com");
            donateur.setMotDePasse(passwordEncoder.encode("test123"));
            donateur.setRole(Role.DONATEUR);

            utilisateurRepository.saveAll(List.of(superAdmin, adminOrg, donateur));

            System.out.println("=== Données de démo insérées ===");
            System.out.println("SuperAdmin    : superadmin@charite.com   / admin123");
            System.out.println("AdminOrg      : admin@organisation.com   / admin123");
            System.out.println("Donateur      : donateur@test.com        / test123");
            System.out.println("================================");
        };
    }
}
