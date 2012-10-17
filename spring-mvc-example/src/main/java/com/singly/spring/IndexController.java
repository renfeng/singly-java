package com.singly.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.singly.client.SinglyAccountStorage;
import com.singly.client.SinglyService;

/**
 * Controller that handle index redirects to authentication and samples.
 */
@Controller
@RequestMapping({
  "/", "/index.html"
})
public class IndexController {

  @Autowired
  private SinglyService singlyService;

  @Autowired
  private SinglyAccountStorage accountStorage;

  @RequestMapping(method = RequestMethod.GET)
  public String getView(Model model, HttpServletRequest request,
    HttpServletResponse response) {

    // using a singly test account, usually this is the user account of the
    // web application, you need a way to distinguish access token per user
    String account = "test_account";

    // get if the user is previously authenticated
    boolean authenticated = singlyService.isAuthenticated(account);
    if (!authenticated) {
      return "redirect:/authentication.html";
    }
    
    model.addAttribute("accessToken", accountStorage.getAccessToken(account));

    return "/index";
  }
}
