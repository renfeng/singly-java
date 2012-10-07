package com.singly.client;

import java.util.Map;

public interface SinglyService {

  public boolean isAuthenticated(String account);

  public String getAuthenticationUrl(String service, String redirectUrl);

  public boolean completeAuthentication(String account, String authCode);

  public String doGetApiRequest(String account, String apiEndpoint,
    Map<String, String> queryParams)
    throws SinglyApiException;

  public String doPostApiRequest(String account, String apiEndpoint,
    Map<String, String> queryParams)
    throws SinglyApiException;

  public String doBodyApiRequest(String account, String apiEndpoint,
    Map<String, String> queryParams, byte[] body, String mime, String charset)
    throws SinglyApiException;
}
