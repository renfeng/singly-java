# Singly Java SDK

## Alpha
Be aware that the Singly Java Client is in an alpha state.  Package names, class names, and method names are subject to change.  As we work to make the sdk better we are not concerned with breaking backwards compatibility for now.  This will change in the future as the sdk becomes more stable.

## Overview
This repository contains two different projects.  The first is the Singly Java client library.  This is a library project you can include into your Java apps that makes it easy to use the Singly API.  The second in an example Spring project that show useage of the Singly client in a web app.

The Singly Java client is a library supporting the [Singly](https://singly.com) social API that will:

  - Allow users to easily authenticate with any service supported by Singly; for example Facebook, Twitter, Github, Foursquare and others
  - Make requests to the Singly API to retrieve your users' social data for use in your app


The library code is contained in the SinglySDK project in the sdk folder.  The com.singly.client.SinglyServiceImpl class is the entry point to using the Singly API in your Java project.

Sample implementations are contained in the SinglySpringExample project in the spring-mvc-example folder.  This is a spring webapp that demonstrates the usage of authentication with the Singly api and retrieving social data.

## Runing the Spring MVC Example application

1. Go to https://singly.com and register or login.
2. Create a new Singly application or use the default application.  This is your Singly oauth application.
3. Get the client id and client secret for your application.
4. For the Spring MVC example, set your client id and client secret in the /WEB-INF/conf/webapp-context.xml configuration for the SinglyService.
5. Startup the Spring MVC example using the mvn jetty:run command in the root of the application.  You will need to have Maven 2 installed.  This starts a local web server running the example application.
6. Point your browser to http://localhost:8080/singly.html to use the application.

## Using the Singly Java SDK

The Singly Java SDK is under the sdk folder and is arranged as a Maven project.  To use the SDK in your application you can add th dependency to your Maven repository, then add the maven dependency to your project as follows, specifying the version.

    <dependency>
      <groupId>com.singly</groupId>
      <artifactId>singly-sdk</artifactId>
      <version>x.x.x</version>
    </dependency>

If you don't wish to use Maven for your application, you can use Ant/Ivy to build the SinglySDK.jar and resolve dependencies.  In the root of the sdk directory there are build.xml and ivy.xml files. Run ant in the root of the sdk directory.  It will create a build directory, download all dependencies through Ivy, compile the sdk classes and create a SinglySDK.jar file in the jar directory.  All jars in the jar directory must then be included in your application.

The com.singly.client.SinglyServiceImpl is the main client class you will use within your application to authenticate and make API calls.  This is not a Java main class, simply the SDK entry point.  Please see the JavaDocs for that class for a complete description of authentication flow and API usage. Here is an example of how you would use the SinglyService.

    import com.singly.client.InMemorySinglyAccountStorage;
    import com.singly.util.HttpClientServiceImpl;
    ...
    HttpClientService httpClient = new HttpClientServiceImpl();
    httpClient.initialize();
    SinglyAccountStorage accountStorage = new InMemorySinglyAccountStorage();;
    SinglyService client = new SinglyServiceImpl("yourClientId", "yourClientSecret", 
      accountStorage, httpClient);
    
    Map<String, String> qparams = new LinkedHashMap<String, String>();
    qparams.put("access_token", accountStorage.getAccessToken(account));
    String servicesJson = singlyService.doGetApiRequest(account, "/services", qparams);
    ... 
    use the data in your app

Usually a custom SinglyAccountStorage implementation would need to be created to save accounts and access tokens to a db or other permanent storage. 

## Android SDK

If you are building an Android app, it is better to use the Singly SDK for Android.

https://github.com/Singly/singly-android

Support
--------------

This is a work in progress. If you have questions or comments

* Join our live chatroom at http://support.singly.com
* Email support@singly.com
