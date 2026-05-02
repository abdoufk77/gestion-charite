package com.emsi.gestioncharite.config;

import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.repository.OrganisationRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminOrgInterceptor implements HandlerInterceptor {

    private final OrganisationRepository organisationRepository;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AdminOrganisation admin
                && admin.getOrganisation() != null) {
            organisationRepository.findById(admin.getOrganisation().getId())
                    .ifPresent(admin::setOrganisation);
        }
        return true;
    }
}
