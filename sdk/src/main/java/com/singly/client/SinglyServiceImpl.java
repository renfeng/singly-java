package com.singly.client;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpException;
import org.codehaus.jackson.JsonNode;

import com.singly.util.HttpClientService;
import com.singly.util.JsonUtils;
import com.singly.util.SinglyUtils;

public class SinglyServiceImpl
  implements SinglyService {

  private String clientId;
  private String clientSecret;
  private SinglyAccountStorage accountStorage = new InMemorySinglyAccountStorage();
  private HttpClientService httpClientService;

  public SinglyServiceImpl() {

  }

  public SinglyServiceImpl(String clientId, String clientSecret,
    SinglyAccountStorage accountStorage, HttpClientService httpClientService) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.accountStorage = accountStorage;
    this.httpClientService = httpClientService;
  }

  @Override
  public boolean isAuthenticated(String account) {
    return accountStorage.hasAccessToken(account);
  }

  @Override
  public String getAuthenticationUrl(String service, String redirectUrl) {

    Map<String, String> qparams = new LinkedHashMap<String, String>();
    qparams.put("client_id", clientId);
    qparams.put("client_secret", clientSecret);
    qparams.put("redirect_uri", redirectUrl);
    qparams.put("service", service);

    // create the authentication url
    return SinglyUtils.createSinglyURL("/oauth/authorize", qparams);
  }

  @Override
  public boolean completeAuthentication(String account, String authCode) {

    // create the post parameters
    Map<String, String> qparams = new LinkedHashMap<String, String>();
    qparams.put("client_id", clientId);
    qparams.put("client_secret", clientSecret);
    qparams.put("code", authCode);

    // create the access token url
    String accessTokenUrl = SinglyUtils.createSinglyURL("/oauth/access_token");

    try {

      // perform the http request to get the access token from the auth code
      byte[] response = httpClientService.post(accessTokenUrl, qparams);
      if (response != null) {

        // parse the json, get the access token
        JsonNode root = JsonUtils.parseJson(new String(response));
        String accessToken = JsonUtils.getStringValue(root, "access_token");

        // save off the access token for the account to storage.
        accountStorage.saveAccessToken(account, accessToken);

        // completed the authentication successfully, move along
        return true;
      }
    }
    catch (HttpException e) {
      // do nothing falls through to false, didn't complete
    }

    return false;
  }

  @Override
  public String doGetApiRequest(String account, String apiEndpoint,
    Map<String, String> queryParams)
    throws SinglyApiException {

    // fail if no access token
    String accessToken = accountStorage.getAccessToken(account);
    if (accessToken == null) {
      throw new SinglyApiException("No access token found");
    }

    Map<String, String> params = new LinkedHashMap<String, String>();
    params.put("access_token", accessToken);
    if (queryParams != null) {
      params.putAll(queryParams);
    }
    String postApiCallUrl = SinglyUtils.createSinglyURL(apiEndpoint);

    try {
      byte[] response = httpClientService.get(postApiCallUrl, params);
      return new String(response);
    }
    catch (HttpException e) {
      throw new SinglyApiException(e);
    }
  }

  @Override
  public String doPostApiRequest(String account, String apiEndpoint,
    Map<String, String> queryParams)
    throws SinglyApiException {

    // fail if no access token
    String accessToken = accountStorage.getAccessToken(account);
    if (accessToken == null) {
      throw new SinglyApiException("No access token found");
    }

    Map<String, String> params = new LinkedHashMap<String, String>();
    params.put("access_token", accessToken);
    if (queryParams != null) {
      params.putAll(queryParams);
    }
    String postApiCallUrl = SinglyUtils.createSinglyURL(apiEndpoint);

    try {
      byte[] response = httpClientService.post(postApiCallUrl, params);
      return new String(response);
    }
    catch (HttpException e) {
      throw new SinglyApiException(e);
    }
  }

  @Override
  public String doBodyApiRequest(String account, String apiEndpoint,
    Map<String, String> queryParams, byte[] body, String mime, String charset)
    throws SinglyApiException {

    // fail if no access token
    String accessToken = accountStorage.getAccessToken(account);
    if (accessToken == null) {
      throw new SinglyApiException("No access token found");
    }

    Map<String, String> params = new LinkedHashMap<String, String>();
    params.put("access_token", accessToken);
    if (queryParams != null) {
      params.putAll(queryParams);
    }
    String postApiCallUrl = SinglyUtils.createSinglyURL(apiEndpoint);

    try {
      byte[] response = httpClientService.postAsBody(postApiCallUrl, body,
        mime, charset);
      return new String(response);
    }
    catch (HttpException e) {
      throw new SinglyApiException(e);
    }
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public SinglyAccountStorage getAccountStorage() {
    return accountStorage;
  }

  public void setAccountStorage(SinglyAccountStorage accountStorage) {
    this.accountStorage = accountStorage;
  }

  public HttpClientService getHttpClientService() {
    return httpClientService;
  }

  public void setHttpClientService(HttpClientService httpClientService) {
    this.httpClientService = httpClientService;
  }

}
