package com.singly.spring;

import java.util.ArrayList;
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

import com.singly.client.SinglyAccountStorage;
import com.singly.client.SinglyService;
import com.singly.util.JSON;

@Controller
@RequestMapping("/photos.html")
public class PhotosController {

  @Autowired
  private SinglyService singlyService;

  @Autowired
  private SinglyAccountStorage accountStorage;

  public class Photo {

    public String thumbnailUrl;
    public String imageUrl;

    public String getThumbnailUrl() {
      return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
      this.thumbnailUrl = thumbnailUrl;
    }

    public String getImageUrl() {
      return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
    }

  }

  private int getPhotosCount(String account) {

    // query parameters for the api call, add in access token
    Map<String, String> qparams = new LinkedHashMap<String, String>();
    qparams.put("access_token", accountStorage.getAccessToken(account));

    // make an API call to get types data
    String typesJson = singlyService.doGetApiRequest("/types", qparams);
    JsonNode root = JSON.parse(typesJson);
    return JSON.getInt(root, "photos");
  }

  private List<Photo> getPhotos(String account) {

    List<Photo> photos = new ArrayList<Photo>();
    int numPhotos = getPhotosCount(account);

    Map<String, String> qparams = new LinkedHashMap<String, String>();
    qparams.put("access_token", accountStorage.getAccessToken(account));
    qparams.put("limit", String.valueOf(numPhotos));
    String photosJson = singlyService.doGetApiRequest("/types/photos", qparams);

    JsonNode root = JSON.parse(photosJson);

    for (JsonNode node : root) {

      JsonNode data = JSON.getJsonNode(node, "data");

      // parse the photo and add it to the block
      Photo photo = new Photo();
      photo.thumbnailUrl = JSON.getString(data, "picture");
      photo.imageUrl = JSON.getString(data, "source");

      photos.add(photo);
    }

    return photos;
  }

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

    List<Photo> photos = getPhotos(account);
    model.addAttribute("photos", photos);

    return "/photos";
  }
}
