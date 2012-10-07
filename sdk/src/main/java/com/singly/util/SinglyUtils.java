package com.singly.util;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * Utility methods for the Singly SDK.
 */
public class SinglyUtils {

  private static final String SINGLY_SCHEME = "https";
  private static final String SINGLY_HOST = "api.singly.com";

  public static String getSinglyScheme() {
    return SINGLY_SCHEME;
  }

  public static String getSinglyHost() {
    return SINGLY_HOST;
  }

  /**
   * Creates a url using the base singly api url and the path.
   * 
   * The url is assumed to be in UTF-8 format.  The query parameters are
   * not required.
   * 
   * @param path The url path.
   * 
   * @return A formatted, UTF-8 singly url string.
   */
  public static String createSinglyURL(String path) {

    // create the formatted UTF-8 url
    try {
      return URLUtils.createURL(SINGLY_SCHEME, SINGLY_HOST, path, null);
    }
    catch (URISyntaxException e) {
      return null;
    }
  }

  /**
   * Creates a url using the base singly api url, the path, and the query
   * parameters specified.
   * 
   * The url is assumed to be in UTF-8 format.  The query parameters are
   * not required.
   * 
   * @param path The url path.
   * @param qparams The optional url query parameters.
   * 
   * @return A formatted, UTF-8 singly url string.
   */
  public static String createSinglyURL(String path,
    Map<String, String> parameters) {

    // create the formatted UTF-8 url
    try {
      return URLUtils.createURL(SINGLY_SCHEME, SINGLY_HOST, path, parameters);
    }
    catch (URISyntaxException e) {
      return null;
    }
  }
}
