package com.emsi.gestioncharite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "Gestion Charite");
        return "index";
    }
}
