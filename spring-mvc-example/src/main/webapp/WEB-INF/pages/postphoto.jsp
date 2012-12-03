<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Singly Friends Example</title>
    <link rel="stylesheet" type="text/css" href="/static/css/example.css" />
  </head>
  <body>
    <div id="content">
      <div id="header"><a href="/index.html"><img id="logo" src="/img/singly-logo.png" /></a></div>
      <h2>Post a Photo to Facebook</h2> 
      <div><a href="/index.html">Back To Index</a></div>      
      <p>This example shows how to use the /types/photos API to post a photo to 
      Facebook.  You must already be authenticated with Facebook for this example
      to work.  Select one or more photos and click Upload.</p>      
      <form:form commandName="photoUpload"  enctype="multipart/form-data">
      <form:errors htmlEscape="false" />  
      <div>        
        <label for="photos['photo1']">Choose A Photo:</label>        
        <input type="file" name="photos['photo1']" />    
        <input type="submit" name="_submit" value="Post to Facebook" />       
      </div>
      <c:if test="${uploaded}"> 
      <div>You photo has been uploaded to facebook.</div> 
      </c:if>
      </form:form>          
    </div>
  </body>
</html>