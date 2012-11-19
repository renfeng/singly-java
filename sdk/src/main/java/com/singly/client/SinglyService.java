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
   * @param account The Singly account to check for authentication.
   * 
   * @return True if the account has a Singly access token.  False otherwise.
   */
  public boolean isAuthenticated(String account);

  /**
   * The first step of the authentication process, returns the URL to which the
   * user is to be redirected for authentication with a given service.
   * 
   * The first time a user authenticates through Singly for a given application
   * a unique Singly account token is generated and stored during the 
   * {@link #completeAuthentication(String)} method.  As a user authenticates 
   * with more services, those services are linked to the same Singly account.
   * 
   * If this is the first time your user is authenticating with Singly for your
   * application you won't have an account, set it to null.  Otherwise pass in
   * the Singly account token that was previously generated so the new services
   * can be properly linked to the same account.
   *  
   * Some services require extra parameters such as scope and flag to be passed
   * in.  Use the authExtra input to pass in the parameters by name.
   * 
   * @param account The Singly account for which to authenticate.
   * @param service The service to authenticate against.
   * @param redirectUrl The URL handled by the application to which we are 
   * redirected upon successful authentication.
   * @param scope Optional scope passed to the service.  Used by some services
   * to allow for extra permissions.
   * @param authExtra Any optional extra parameters used in oauth of services.
   * This includes scope and flag parameters.
   * 
   * @return The URL to redirect the user to for service authentication.
   */
  public String getAuthenticationUrl(String account, String service,
    String redirectUrl, Map<String, String> authExtra);

  /**
   * Completes the authentication process, getting and storing the Singly access
   * token and account.
   *
   * The Singly account token is returned from this method and is also stored
   * by the SinglyAccountStorage implementation.
   * 
   * @param authCode The authentication code parsed from the redirect URL. This
   * is used to retrieve the Singly access token.
   * 
   * @return The Singly account or null if the authentication process did not
   * complete successfully.
   */
  public String completeAuthentication(String authCode);

  /**
   * Makes a GET call to the Singly API.
   * 
   * If an API call requires an access token it must be added to the queryParams
   * passed into the method.
   * 
   * @param apiEndpoint The Singly API endpoint to call.
   * @param params Any query parameters for the endpoint.
   * 
   * @return The JSON returned from the API.
   * 
   * @throws SinglyApiException A RuntimeException thrown if there was an error
   * calling the SinglyAPI.
   */
  public String doGetApiRequest(String apiEndpoint, Map<String, String> params)
    throws SinglyApiException;

  /**
   * Makes a POST call to the Singly API.
   * 
   * If an API call requires an access token it must be added to the queryParams
   * passed into the method.
   * 
   * @param apiEndpoint The Singly API endpoint to call.
   * @param params Any query parameters for the endpoint.
   * 
   * @return The JSON returned from the API.
   * 
   * @throws SinglyApiException A RuntimeException thrown if there was an error
   * calling the SinglyAPI.
   */
  public String doPostApiRequest(String apiEndpoint, Map<String, String> params)
    throws SinglyApiException;

  /**
   * Makes a POST call to the Singly API passing in the body of the request. 
   * This is useful when doing sharing or proxying through the Singly API.
   * 
   * If an API call requires an access token it must be added to the queryParams
   * passed into the method.
   * 
   * @param apiEndpoint The Singly API endpoint to call.
   * @param params Any query parameters for the endpoint.  Query parameters
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
  public String doBodyApiRequest(String apiEndpoint,
    Map<String, String> params, byte[] body, String mime, String charset)
    throws SinglyApiException;
}
