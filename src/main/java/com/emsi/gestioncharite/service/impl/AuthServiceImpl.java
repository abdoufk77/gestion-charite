package com.emsi.gestioncharite.service.impl;

import com.emsi.gestioncharite.dto.request.RegisterRequest;
import com.emsi.gestioncharite.entity.Donateur;
import com.emsi.gestioncharite.enums.Role;
import com.emsi.gestioncharite.repository.UtilisateurRepository;
import com.emsi.gestioncharite.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Les mots de passe ne correspondent pas");
        }

        Donateur donateur = new Donateur();
        donateur.setNom(request.getUsername());
        donateur.setPrenom("");
        donateur.setEmail(request.getEmail());
        donateur.setMotDePasse(passwordEncoder.encode(request.getPassword()));
        donateur.setRole(Role.DONATEUR);

        utilisateurRepository.save(donateur);
    }
}
