package com.emsi.gestioncharite.service.impl;

import com.emsi.gestioncharite.entity.Utilisateur;
import com.emsi.gestioncharite.enums.Role;
import com.emsi.gestioncharite.repository.AdminOrganisationRepository;
import com.emsi.gestioncharite.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final AdminOrganisationRepository adminOrganisationRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));

        if (utilisateur.getRole() == Role.ADMIN_ORGANISATION) {
            return adminOrganisationRepository.findByEmailWithOrganisation(email)
                    .orElse((com.emsi.gestioncharite.entity.AdminOrganisation) utilisateur);
        }

        return utilisateur;
    }
}
