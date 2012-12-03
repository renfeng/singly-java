package com.singly.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

public class HttpClientServiceImpl
  implements HttpClientService {

  private int connectionsTotal = 1000;
  private int connectionsPerHost = 50;
  private int connectionTimeoutInSeconds = 20;
  private int socketTimeoutInSeconds = 10;
  private int idleConnectionTimeoutInSeconds = 10;
  private boolean tcpNoDelay = true;
  private int soLingerInSeconds = 0;
  private boolean retry = true;
  private int maxRetries = 3;
  private PoolingClientConnectionManager connMgr;
  private DefaultHttpClient httpClient;
  private int cullInterval = 2500;
  private ConnectionCuller cullerThread = null;
  private String userAgent = "Singly-Spring-SDK-HTTPClient-1.0";

  private class ConnectionCuller
    extends Thread {

    @Override
    public void run() {

      while (true) {
        try {
          Thread.sleep(cullInterval);
          if (connMgr != null) {
            connMgr.closeExpiredConnections();
            connMgr.closeIdleConnections(idleConnectionTimeoutInSeconds,
              TimeUnit.SECONDS);
          }
        }
        catch (InterruptedException e) {
          // do nothing and continue
        }
      }
    }
  }

  public HttpClientServiceImpl() {

  }

  public void initialize() {

    HttpParams params = new BasicHttpParams();

    params.setParameter(HttpProtocolParams.USER_AGENT, userAgent);
    params.setParameter(HttpProtocolParams.PROTOCOL_VERSION,
      HttpVersion.HTTP_1_1);
    params.setParameter(HttpProtocolParams.HTTP_CONTENT_CHARSET, "UTF-8");
    params.setParameter("http.protocol.cookie-policy",
      CookiePolicy.BROWSER_COMPATIBILITY);
    params.setBooleanParameter("http.protocol.single-cookie-header", true);
    params.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
      connectionTimeoutInSeconds * 1000);
    params.setIntParameter(HttpConnectionParams.SO_LINGER, soLingerInSeconds);
    params.setBooleanParameter(HttpConnectionParams.TCP_NODELAY, tcpNoDelay);
    params.setIntParameter(HttpConnectionParams.SO_TIMEOUT,
      socketTimeoutInSeconds * 1000);

    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", 80, PlainSocketFactory
      .getSocketFactory()));
    registry.register(new Scheme("https", 443, SSLSocketFactory
      .getSocketFactory()));

    connMgr = new PoolingClientConnectionManager(registry);
    connMgr.setDefaultMaxPerRoute(connectionsPerHost);
    connMgr.setMaxTotal(connectionsTotal);

    httpClient = new DefaultHttpClient(connMgr);

    // start up the idle connection culling thread
    cullerThread = new ConnectionCuller();
    cullerThread.setDaemon(true);
    cullerThread.start();
  }

  public void shutdown() {
    httpClient.getConnectionManager().shutdown();
  }

  public byte[] get(String url, Map<String, String> params)
    throws HttpException {

    int numRetries = retry ? maxRetries : 1;
    HttpGet httpget = null;

    String getURL = null;
    try {
      URIBuilder builder = new URIBuilder(url);
      if (params != null && !params.isEmpty()) {
        for (Map.Entry<String, String> param : params.entrySet()) {
          builder.setParameter(param.getKey(), param.getValue());
        }
      }
      getURL = builder.toString();
    }
    catch (URISyntaxException e1) {
      throw new HttpException("Invalid URL: " + url);
    }

    for (int i = 0; i < numRetries; i++) {

      try {

        httpget = new HttpGet(getURL);
        HttpResponse response = httpClient.execute(httpget);
        StatusLine status = response.getStatusLine();
        int statusCode = status.getStatusCode();
        if (statusCode >= 300) {
          throw new HttpException(status.getReasonPhrase());
        }

        HttpEntity entity = response.getEntity();
        if (entity != null) {
          return EntityUtils.toByteArray(entity);
        }
        return null;
      }
      catch (HttpException he) {
        httpget.abort();
        throw he;
      }
      catch (Exception e) {
        httpget.abort();
      }
    }

    // tried max times and errored, throw exception
    throw new HttpException("GET error: " + getURL + ", attempts:" + numRetries);
  }

  public byte[] post(String url, Map<String, String> postParams)
    throws HttpException {

    int numRetries = retry ? maxRetries : 1;
    HttpPost httppost = null;

    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    if (postParams != null && !postParams.isEmpty()) {
      for (Map.Entry<String, String> param : postParams.entrySet()) {
        nameValuePairs.add(new BasicNameValuePair(param.getKey(), param
          .getValue()));
      }
    }

    for (int i = 0; i < numRetries; i++) {

      try {

        httppost = new HttpPost(url);
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpClient.execute(httppost);
        StatusLine status = response.getStatusLine();
        int statusCode = status.getStatusCode();
        if (statusCode >= 300) {
          throw new HttpException(status.getReasonPhrase());
        }

        HttpEntity entity = response.getEntity();
        if (entity != null) {
          return EntityUtils.toByteArray(entity);
        }
        return null;
      }
      catch (HttpException he) {
        httppost.abort();
        throw he;
      }
      catch (Exception e) {
        httppost.abort();
      }
    }

    // tried max times and errored, throw exception
    throw new HttpException("POST error: " + url + ", attempts:" + numRetries);
  }

  public byte[] postMultipart(String url, Map<String, String> postParams,
    Map<String, Object> files, Map<String, String> filenames)
    throws HttpException {

    int numRetries = retry ? maxRetries : 1;
    HttpPost httppost = null;
    MultipartEntity multipart = new MultipartEntity();

    if (postParams != null && !postParams.isEmpty()) {

      for (Map.Entry<String, String> postParam : postParams.entrySet()) {
        try {
          String key = postParam.getKey();
          String val = postParam.getValue();
          FormBodyPart formPart = new FormBodyPart(key, new StringBody(val));
          multipart.addPart(formPart);
        }
        catch (UnsupportedEncodingException e) {
          // continue
        }
      }
    }

    if (files != null && !files.isEmpty()) {

      int fileIndex = 0;
      for (Map.Entry<String, Object> file : files.entrySet()) {

        String key = file.getKey();
        Object val = file.getValue();
        String filename = filenames != null ? filenames.get(key) : null;
        if (StringUtils.isBlank(filename)) {
          filename = "file" + fileIndex++;
        }

        if (val instanceof InputStream) {
          InputStreamBody isBody = new InputStreamBody((InputStream)val,
            filename);
          multipart.addPart(key, isBody);
        }
        else if (val instanceof byte[]) {
          ByteArrayBody byteBody = new ByteArrayBody((byte[])val, filename);
          multipart.addPart(key, byteBody);
        }
        else if (val instanceof File) {
          FileBody fileBody = new FileBody((File)val, filename);
          multipart.addPart(key, fileBody);
        }
      }
    }

    for (int i = 0; i < numRetries; i++) {

      try {

        httppost = new HttpPost(url);
        httppost.setEntity(multipart);

        HttpResponse response = httpClient.execute(httppost);
        StatusLine status = response.getStatusLine();
        int statusCode = status.getStatusCode();
        if (statusCode >= 300) {
          throw new HttpException(status.getReasonPhrase());
        }

        HttpEntity entity = response.getEntity();
        if (entity != null) {
          return EntityUtils.toByteArray(entity);
        }
        return null;
      }
      catch (HttpException he) {
        httppost.abort();
        throw he;
      }
      catch (Exception e) {
        httppost.abort();
      }
    }

    // tried max times and errored, throw exception
    throw new HttpException("POST error: " + url + ", attempts:" + numRetries);
  }

  public byte[] postAsBody(String url, byte[] body, String mime, String charset)
    throws HttpException {

    int numRetries = retry ? maxRetries : 1;
    HttpPost httppost = null;

    for (int i = 0; i < numRetries; i++) {
      try {

        httppost = new HttpPost(url);
        httppost.setHeader("Content-Type", mime + ";" + "charset=" + charset);
        httppost.setEntity(new ByteArrayEntity(body));

        HttpResponse response = httpClient.execute(httppost);
        StatusLine status = response.getStatusLine();
        int statusCode = status.getStatusCode();
        if (statusCode >= 300) {
          throw new HttpException(status.getReasonPhrase());
        }

        HttpEntity entity = response.getEntity();
        if (entity != null) {
          return EntityUtils.toByteArray(entity);
        }

        return null;
      }
      catch (HttpException he) {
        httppost.abort();
        throw he;
      }
      catch (Exception e) {
        httppost.abort();
      }
    }

    // tried max times and errored, throw exception
    throw new HttpException("POST error: " + url + ", attempts:" + numRetries);
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public void setConnectionsTotal(int connectionsTotal) {
    this.connectionsTotal = connectionsTotal;
  }

  public void setConnectionsPerHost(int connectionsPerHost) {
    this.connectionsPerHost = connectionsPerHost;
  }

  public void setConnectionTimeoutInSeconds(int connectionTimeoutInSeconds) {
    this.connectionTimeoutInSeconds = connectionTimeoutInSeconds;
  }

  public void setSocketTimeoutInSeconds(int socketTimeoutInSeconds) {
    this.socketTimeoutInSeconds = socketTimeoutInSeconds;
  }

  public void setTcpNoDelay(boolean tcpNoDelay) {
    this.tcpNoDelay = tcpNoDelay;
  }

  public void setSoLingerInSeconds(int soLingerInSeconds) {
    this.soLingerInSeconds = soLingerInSeconds;
  }

  public void setIdleConnectionTimeoutInSeconds(
    int idleConnectionTimeoutInSeconds) {
    this.idleConnectionTimeoutInSeconds = idleConnectionTimeoutInSeconds;
  }

  public void setCullInterval(int cullInterval) {
    this.cullInterval = cullInterval;
  }

  public void setRetry(boolean retry) {
    this.retry = retry;
  }

  public void setMaxRetries(int maxRetries) {
    this.maxRetries = maxRetries;
  }

}
