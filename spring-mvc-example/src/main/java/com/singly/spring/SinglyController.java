package com.singly.spring;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.singly.client.SinglyAccountStorage;
import com.singly.client.SinglyService;

/**
 * Example Controller class that shows usage of the SinglyService to do service
 * authentication and retrieve social data.
 */
@Controller
@RequestMapping({
  "/", "/index.html"
})
public class SinglyController {

  @Autowired
  private SinglyService singlyService;

  @Autowired
  private SinglyAccountStorage accountStorage;

  @RequestMapping(method = RequestMethod.GET)
  public String getView(Model model, @RequestParam(value = "code",
      required = false) String authCode, @RequestParam(value = "service",
      required = false) String service, HttpServletRequest request,
    HttpServletResponse response) {

    // using a singly test account, usually it is one account per user of the
    // web application
    String account = "test_account";

    // get if the user is previously authenticated
    boolean authenticated = singlyService.isAuthenticated(account);

    // check if we are authenticating against a service, or completing an
    // authentication by handling the redirectURL
    if (StringUtils.isNotBlank(service)) {

      // if so then redirect to the service authentication URL
      return "redirect:"
        + singlyService.getAuthenticationUrl(service,
          "http://localhost:8080/index.html", null);
    }
    else if (StringUtils.isNotBlank(authCode)) {

      // parse the authentication code and pass to complete the authentication
      authenticated = singlyService.completeAuthentication(account, authCode);
      model.addAttribute("completedAuth", authenticated);
    }

    // if the user is authenticated
    if (authenticated) {

      // query parameters for the api call, add in access token
      Map<String, String> queryParams = new LinkedHashMap<String, String>();
      queryParams.put("access_token", accountStorage.getAccessToken(account));

      // make an API call to get profiles data and add the JSON to the model
      String json = singlyService.doGetApiRequest(account, "/profiles",
        queryParams);
      model.addAttribute("profilesJSON", json);
    }

    return "/singly";
  }
}
