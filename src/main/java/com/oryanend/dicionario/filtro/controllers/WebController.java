package com.oryanend.dicionario.filtro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

  @GetMapping("/")
  public String showHomePage(Model model) {
    return "index";
  }
}
