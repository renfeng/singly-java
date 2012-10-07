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

Support
--------------

This is a work in progress. If you have questions or comments

* Join our live chatroom at http://support.singly.com
* Email support@singly.com
