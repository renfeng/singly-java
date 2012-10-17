<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Singly Friends Example</title>
    <link rel="stylesheet" type="text/css" href="/static/css/example.css" />
  </head>
  <body>
    <div id="content">
      <div id="header"><a href="/index.html"><img id="logo" src="/img/singly-logo.png" /></a></div>
      <h2>All Friends from All Services</h2> 
      <div><a href="/index.html">Back To Index</a></div>      
      <p>This example uses the /friends API to get all friends from all services.</p>      
      <table id="friends">
        <tr>      
        <c:forEach var="friend" items="${friends}" varStatus="friendStatus">
          <td>
            <div class="friendServices">
              <c:forEach var="service" items="${friend.serviceIds}" varStatus="serviceStatus">
                <span class="serviceImage"><img src="/img/${service}.png" /></span>                
              </c:forEach>
            </div>
            <div class="friendInfo">
              <a href="${friend.profileUrl}">${friend.name}</a>
            </div>
          </td>
          <c:if test="${friendStatus.count % 5 == 0}"></ tr><tr></c:if>
        </c:forEach>
        </tr>
      </table>
    </div>
  </body>
</html>