package com.singly.spring;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.singly.client.SinglyAccountStorage;
import com.singly.client.SinglyService;

@Controller
@RequestMapping({
  "/postphoto.html"
})
public class PostPhotoController {

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

    model.addAttribute("photoUpload", new PhotoUpload());

    return "/postphoto";
  }

  @RequestMapping(method = RequestMethod.POST)
  public String submitForm(Model model,
    @ModelAttribute("photoUpload") PhotoUpload photoUpload,
    BindingResult bindingResult, HttpServletRequest request,
    HttpServletResponse response) {

    HttpSession session = request.getSession();
    String account = (String)session.getAttribute("account");
    Map<String, String> postParams = new LinkedHashMap<String, String>();
    postParams.put("access_token", accountStorage.getAccessToken(account));
    postParams.put("to", "facebook");

    MultipartFile[] files = photoUpload.toArray();
    if (files != null && files.length > 0) {

      try {
        Map<String, Object> postFiles = new LinkedHashMap<String, Object>();
        postFiles.put("photo", files[0].getBytes());
        singlyService.doPostMultipartApiRequest("/types/photos", null,
          postParams, postFiles, null);
        model.addAttribute("uploaded", true);
      }
      catch (Exception e) {
        bindingResult.reject("bad", "Error posting images to Facebook");
      }

    }

    return "/postphoto";
  }
}
