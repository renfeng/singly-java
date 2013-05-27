<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Singly Friends Example</title>
    <link rel="stylesheet" type="text/css" href="/static/css/example.css" />
  </head>
  <body>
    <div id="content">
      <div id="header"><a href="index.html"><img id="logo" src="img/singly-logo.png" /></a></div>
      <h2>All Photos from All Services</h2> 
      <div><a href="index.html">Back To Index</a></div>      
      <p>This example uses the /photos API to get all photos from all services.</p>      
      <table id="friends">
        <tr>      
        <c:forEach var="photo" items="${photos}" varStatus="photoStatus">
          <td>
            <a href="${photo.imageUrl}"><img src="${photo.thumbnailUrl}" /></a>
          </td>
          <c:if test="${photoStatus.count % 6 == 0}"></ tr><tr></c:if>
        </c:forEach>
        </tr>
      </table>
    </div>
  </body>
</html>