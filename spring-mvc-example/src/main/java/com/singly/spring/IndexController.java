package com.singly.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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

    // store the account in the session
    HttpSession session = request.getSession();
    String account = (String)session.getAttribute("account");

    // redirect is not authenticated
    if (StringUtils.isBlank(account) || !singlyService.isAuthenticated(account)) {
      return "redirect:/authentication.html";
    }

    model.addAttribute("accessToken", accountStorage.getAccessToken(account));

    return "/index";
  }
}
