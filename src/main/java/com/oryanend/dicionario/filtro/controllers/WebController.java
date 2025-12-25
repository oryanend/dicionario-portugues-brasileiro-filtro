package com.oryanend.dicionario.filtro.controllers;

import com.oryanend.dicionario.filtro.services.StatusService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
  @Autowired private StatusService statusService;

  @GetMapping("/")
  public String showHomePage(HttpServletRequest request, Model model) {
    return urlConfig(request, model, "index");
  }

  @GetMapping("/status")
  public String showStatusPage(HttpServletRequest request, Model model) {
    // Database Info
    Map<String, Object> status = statusService.getStatus();
    Map<String, Object> db =
        (Map<String, Object>) ((Map<String, Object>) status.get("dependencies")).get("database");

    Map<String, Object> latency = (Map<String, Object>) db.get("latency");

    model.addAttribute("dbStatus", db.get("status"));
    model.addAttribute("dbVersion", db.get("version"));
    model.addAttribute("dbMaxConnection", db.get("max_connections"));
    model.addAttribute("dbOpenConnection", db.get("open_connections"));
    model.addAttribute("dbFirstQuery", latency.get("first_query"));
    model.addAttribute("dbSecondQuery", latency.get("second_query"));
    model.addAttribute("dbThirdQuery", latency.get("third_query"));

    // Webserver Info
    Map<String, Object> webserver =
        (Map<String, Object>) ((Map<String, Object>) status.get("dependencies")).get("webserver");

    model.addAttribute("webStatus", webserver.get("status"));
    model.addAttribute("webProvider", webserver.get("provider"));
    model.addAttribute("webEnvironment", webserver.get("environment"));
    model.addAttribute("webVersion", webserver.get("version"));

    return urlConfig(request, model, "status");
  }

  private String urlConfig(HttpServletRequest request, Model model, String page) {
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

    return page;
  }
}
