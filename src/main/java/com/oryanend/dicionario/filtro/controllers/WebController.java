package com.oryanend.dicionario.filtro.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
  @GetMapping("/")
  public String showHomePage(HttpServletRequest request, Model model) {

    String scheme = request.getScheme();
    String serverName = request.getServerName();
    int serverPort = request.getServerPort();

    String baseUrl =
        scheme
            + "://"
            + serverName
            + ((serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort);

    String displayBaseUrl = baseUrl.replace("http://", "").replace("https://", "");

    model.addAttribute("baseUrl", baseUrl);
    model.addAttribute("displayBaseUrl", displayBaseUrl);

    model.addAttribute("baseUrl", baseUrl);

    return "index";
  }
}
