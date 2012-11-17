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

## Install Maven or Ant/Ivy
Build the Singly Java SDK requires either Maven 2 or Ant/Ivy to be installed.  Running the Spring MVC Example application requires Maven to be installed.  Usually these can be installed through a package manager or they can be installed manually by following these instructions.  If installing manually make sure to download Maven 2.

1. For Maven - http://maven.apache.org/download.html
2. For Ant - http://ant.apache.org/
3. For Ivy - http://ant.apache.org/ivy/history/2.2.0/install.html

## Register with Singly
You will need to register with Singly to get your client id and client secret.  These are used when making API calls.

1. Go to https://singly.com and register or login.
2. Create a new Singly application or use the default application.  This is your Singly app.
3. Get the client id and client secret for your Singly app.

## Running the Spring MVC Example
We will use maven from the command line to run the Spring MVC example app.  First make sure you have Maven 2 installed.

1. Set your client id and client secret in the spring-mvc-example/src/main/webapp/WEB-INF/conf/webapp-context.xml configuration for the SinglyService.
2. Go to the sdk root directory.
3. Run `mvn clean install`.  This builds the sdk and installs it into your local Maven repository.
4. Go to the spring-mvc-example root directory.
5. Run `mvn jetty:run`.  This starts the Spring MVC example webapp in a local Jetty server.
6. Open your browser to http://localhost:8080/ to use the web application.

## Using the Singly Java SDK with Maven
The Singly Java SDK is under the sdk folder and is arranged as a Maven project.  To use the SDK in your application you can build the SDK locally using the `mvn clean install` command from the root of the sdk directory.  This builds the sdk and installs it into your local Maven repository. Then you can use the SDK in your Maven enabled application by adding the maven dependency to your project as follows.

    <dependency>
      <groupId>com.singly</groupId>
      <artifactId>singly-sdk</artifactId>
      <version>1.0.0-SNAPSHOT</version>
    </dependency>

## Using the Singly Java SDK with Ant and Ivy
If you don't wish to use Maven, you can use Ant/Ivy to build the SinglySDK.jar and resolve dependencies.  You must have both Ant and Ivy installed.

1. Go to the sdk root directory.
2. Run the command `ant`.  This will create a build/jar directory under the sdk root, download all dependencies lib jars through Ivy, compile the sdk classes and create a SinglySDK.jar file in that build/jar directory.
3. Copy all jars from the build/jar directory into your application classpath as specified by your project.

## Calling the SinglyService
The com.singly.client.SinglyServiceImpl is the main client class you will use within your application to authenticate and make API calls.  This is not a Java main class, simply the SDK entry point.  Please see the JavaDocs for that class for a complete description of authentication flow and API usage. Here is an example of how you would use the SinglyService.

    import com.singly.client.SinglyService;
    import com.singly.client.HttpClientService;
    import com.singly.client.SinglyAccountStorage;
    import com.singly.client.InMemorySinglyAccountStorage;
    import com.singly.util.HttpClientServiceImpl;
    import com.singly.client.SinglyServiceImpl;
    ...
    HttpClientService httpClient = new HttpClientServiceImpl();
    httpClient.initialize();
    SinglyAccountStorage accountStorage = new InMemorySinglyAccountStorage();;
    SinglyService client = new SinglyServiceImpl("yourClientId", "yourClientSecret", 
      accountStorage, httpClient);
    
    String servicesJson = singlyService.doGetApiRequest(account, "/services", null);
    ... 
    use the data in your app

Usually a custom SinglyAccountStorage implementation would need to be created to save accounts and access tokens to a db or other permanent storage. 

Calling other methods in the SDK would require authenticating and getting an access token first.

    Map<String, String> qparams = new LinkedHashMap<String, String>();
    qparams.put("access_token", accountStorage.getAccessToken(account));
    String servicesJson = singlyService.doGetApiRequest(account, "/another_api_method", null);    

## Android SDK
If you are building an Android app, it is better to use the Singly SDK for Android.

https://github.com/Singly/singly-android

Support
--------------

This is a work in progress. If you have questions or comments

* Join our live chatroom at http://support.singly.com
* Email support@singly.com