package com.singly.client;

import java.util.Map;

/**
 * A client that handles authentication and requests to the Singly API.
 * 
 * An application must be authenticated and retrieve an access token for an 
 * account before it can make Singly API calls for that account.  The process
 * for authentication has three steps.
 * 
 * <ol>
 *   <li>The application use the {@link #getAuthenticationUrl(String, String)}
 *   method and redirect the user to the authentication web page for the service
 *   against which the user is authenticating.</li>
 *   <li>Once the user authenticates the web page is redirected back to the
 *   redirectUrl in the application.  A <strong>code</strong> parameter is 
 *   parsed from the redirectUrl.  This is the authentication code.</li>
 *   <li>The application calls the {@link #completeAuthentication(String, String)}
 *   method with the authentication code and account to store the access token
 *   for that account.</li>
 * </ol>
 * 
 * Once an access token is stored for an account, calls can be made to the API
 * using the doXApiRequest methods and passing in an account.  The access token
 * will be retrieved behind the scenes and passed to all API calls.
 * 
 * @see https://singly.com/docs/api
 */
public interface SinglyService {

  /**
   * Returns true if the application has been previously authenticated and 
   * has a Singly access token.
   * 
   * @param account The account to check for authentication.
   * 
   * @return True if the account has a Singly access token.  False otherwise.
   */
  public boolean isAuthenticated(String account);

  /**
   * The first step of the authentication process, returns the URL to which the
   * user is to be redirected for authentication with a given service.
   * 
   * @param service The service to authenticate against.
   * @param redirectUrl The URL handled by the application to which we are 
   * redirected upon successful authentication.
   * 
   * @return The URL to redirect the user to for service authentication.
   */
  public String getAuthenticationUrl(String service, String redirectUrl);

  /**
   * Completes the authentication process, getting and storing the Singly access
   * token for the account.
   * 
   * @param account The account for which to store the access token.
   * @param authCode The authentication code parsed from the redirect URL. This
   * is used to retrieve the Singly access token.
   * 
   * @return True if the access token was retrieved and stored successfully,
   * False otherwise.
   */
  public boolean completeAuthentication(String account, String authCode);

  /**
   * Makes a GET call to the Singly API.
   * 
   * @param account The account we are making the API call for.
   * @param apiEndpoint The Singly API endpoint to call.
   * @param queryParams Any query parameters for the endpoint.
   * 
   * @return The JSON returned from the API.
   * 
   * @throws SinglyApiException A RuntimeException thrown if there was an error
   * calling the SinglyAPI.
   */
  public String doGetApiRequest(String account, String apiEndpoint,
    Map<String, String> queryParams)
    throws SinglyApiException;

  /**
   * Makes a POST call to the Singly API.
   * 
   * @param account The account we are making the API call for.
   * @param apiEndpoint The Singly API endpoint to call.
   * @param queryParams Any query parameters for the endpoint.
   * 
   * @return The JSON returned from the API.
   * 
   * @throws SinglyApiException A RuntimeException thrown if there was an error
   * calling the SinglyAPI.
   */
  public String doPostApiRequest(String account, String apiEndpoint,
    Map<String, String> queryParams)
    throws SinglyApiException;

  /**
   * Makes a POST call to the Singly API passing in the body of the request. 
   * This is useful when doing sharing or proxying through the Singly API.
   * 
   * @param account The account we are making the API call for.
   * @param apiEndpoint The Singly API endpoint to call.
   * @param queryParams Any query parameters for the endpoint.  Query parameters
   * are appended to the URL and are not passed through the POST body.
   * @param body The body of the POST request.
   * @param mime The mime type set in the header of the POST request.
   * @param charst The character set set in the header of the POST request.
   * 
   * @return The JSON returned from the API.
   * 
   * @throws SinglyApiException A RuntimeException thrown if there was an error
   * calling the SinglyAPI.
   */
  public String doBodyApiRequest(String account, String apiEndpoint,
    Map<String, String> queryParams, byte[] body, String mime, String charset)
    throws SinglyApiException;
}
