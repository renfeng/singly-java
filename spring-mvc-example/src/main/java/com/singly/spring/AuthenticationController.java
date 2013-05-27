package com.singly.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.singly.client.SinglyAccountStorage;
import com.singly.client.SinglyService;
import com.singly.util.JSON;

/**
 * Controller that demonstrates the Singly authentication process.
 */
@Controller
@RequestMapping("/authentication.html")
public class AuthenticationController {

  @Autowired
  private SinglyService singlyService;

  @Autowired
  private SinglyAccountStorage accountStorage;

  public class AuthService {

    public String id;
    public String userIdentity;
    public String name;
    public Map<String, String> icons;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getUserIdentity() {
      return userIdentity;
    }

    public void setUserIdentity(String userIdentity) {
      this.userIdentity = userIdentity;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Map<String, String> getIcons() {
      return icons;
    }

    public void setIcons(Map<String, String> icons) {
      this.icons = icons;
    }

  }

  private List<AuthService> getAuthServices(String account) {

    // new list of services
    List<AuthService> curServices = new ArrayList<AuthService>();

    // make an API call to get profiles data and add the JSON to the model
    String servicesJson = singlyService.doGetApiRequest("/services", null);

    JsonNode rootNode = JSON.parse(servicesJson);
    Map<String, JsonNode> serviceNodes = JSON.getFields(rootNode);

    // loop through the service name to objects
    for (Map.Entry<String, JsonNode> entry : serviceNodes.entrySet()) {

      // parse and add the service to the services list
      JsonNode serviceNode = entry.getValue();
      AuthService authService = new AuthService();
      authService.id = entry.getKey();
      authService.name = StringUtils.capitalize(JSON.getString(serviceNode,
        "name"));

      // create a map of the icons and their sizes
      Map<String, String> icons = new HashMap<String, String>();
      List<JsonNode> iconNodes = JSON.getJsonNodes(serviceNode, "icons");
      for (JsonNode iconNode : iconNodes) {
        int height = JSON.getInt(iconNode, "height");
        int width = JSON.getInt(iconNode, "width");
        String source = JSON.getString(iconNode, "source");
        String key = height + "x" + width;
        icons.put(key, source);
      }
      authService.icons = icons;

      curServices.add(authService);
    }

    // sort the services by name
    Collections.sort(curServices, new Comparator<AuthService>() {

      @Override
      public int compare(AuthService lhs, AuthService rhs) {
        return lhs.name.compareTo(rhs.name);
      }
    });

    return curServices;
  }

  private Map<String, String> getProfiles(String account) {

    Map<String, String> profiles = new HashMap<String, String>();

    // query parameters for the api call, add in access token
    Map<String, String> qparams = new LinkedHashMap<String, String>();
    qparams.put("access_token", accountStorage.getAccessToken(account));

    // make an API call to get profiles data and add the JSON to the model
    String profilesJson = singlyService.doGetApiRequest("/profiles", qparams);

    // parse the response, extract authenticated profiles
    JsonNode root = JSON.parse(profilesJson);
    List<String> profileNames = JSON.getFieldnames(root);
    for (String profileName : profileNames) {
      if (!profileName.equals("id")) {
        List<String> profileIds = JSON.getStrings(root, profileName);
        profiles.put(profileName, profileIds.get(0));
      }
    }

    return profiles;
  }

  @RequestMapping(method = RequestMethod.GET)
  public String getView(Model model, @RequestParam(value = "code",
      required = false) String authCode, @RequestParam(value = "profile",
      required = false) String profile, @RequestParam(value = "service",
      required = false) String service, HttpServletRequest request,
    HttpServletResponse response) {

    // store the account in the session
    HttpSession session = request.getSession();
    String account = (String)session.getAttribute("account");

    // check if we are authenticating against a service, or completing an
    // authentication by handling the redirectURL
    if (StringUtils.isNotBlank(service)) {

      if (StringUtils.isNotBlank(profile)) {

        Map<String, String> postParams = new HashMap<String, String>();
        postParams.put("delete", profile + "@" + service);
        postParams.put("access_token", accountStorage.getAccessToken(account));

        // delete the profile service
        singlyService.doPostApiRequest("/profiles", null, postParams);

        // then redirect to authentication URL
        return "redirect:/authentication.html";
      }
      else {

        // if so then redirect to the service authentication URL
        return "redirect:"
          + singlyService.getAuthenticationUrl(account, service,
        		  request.getRequestURL().substring(0,
        				  request.getRequestURL().indexOf(request.getContextPath()))
      					+ request.getContextPath() + "/authentication.html", null);
      }
    }
    else if (StringUtils.isNotBlank(authCode)) {

      // parse the authentication code and pass to complete the authentication
      account = singlyService.completeAuthentication(authCode);
      session.setAttribute("account", account);

      // if so then redirect to authentication URL
      return "redirect:/authentication.html";
    }

    // get authentication services through singly api
    List<AuthService> authServices = getAuthServices(account);
    model.addAttribute("services", authServices);

    // get if the user is previously authenticated
    boolean authenticated = singlyService.isAuthenticated(account);
    model.addAttribute("authenticated", authenticated);

    // if the user is authenticated get their authenticated profiles
    if (authenticated) {
      Map<String, String> profiles = getProfiles(account);
      model.addAttribute("profiles", profiles);
    }

    return "/authentication";
  }
}
