package com.emsi.gestioncharite.service;

import com.emsi.gestioncharite.dto.request.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
}
