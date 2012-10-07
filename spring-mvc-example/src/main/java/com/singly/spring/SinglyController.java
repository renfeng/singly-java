package com.singly.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.singly.client.SinglyService;

@Controller
@RequestMapping("/singly.html")
public class SinglyController {

  @Autowired
  private SinglyService singlyService;

  @RequestMapping(method = RequestMethod.GET)
  public String getView(Model model, @RequestParam(value = "code",
      required = false) String authCode, @RequestParam(value = "service",
      required = false) String service, HttpServletRequest request,
    HttpServletResponse response) {

    String account = "test_account";

    boolean authenticated = singlyService.isAuthenticated(account);
    if (StringUtils.isNotBlank(service)) {
      return "redirect:"
        + singlyService.getAuthenticationUrl(service,
          "http://localhost:8080/singly.html");
    }
    else if (StringUtils.isNotBlank(authCode)) {
      authenticated = singlyService.completeAuthentication(account, authCode);
      model.addAttribute("completedAuth", authenticated);
    }

    if (authenticated) {
      String json = singlyService.doGetApiRequest(account, "/profiles", null);
      model.addAttribute("profilesJSON", json);
    }

    return "/singly";
  }
}
