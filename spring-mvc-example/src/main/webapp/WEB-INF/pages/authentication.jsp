<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Singly Example Authentication</title>
    <link rel="stylesheet" type="text/css" href="/static/css/example.css" />
  </head>
  <body>
    <div id="content">
      <div id="header"><a href="/index.html"><img id="logo" src="/img/singly-logo.png" /></a></div>
      <h2>Step 1: Authenticate with a Service</h2>
      <div><a href="/index.html">Back To Index</a></div>      
      <p>A user of your application first authenticates with a service such as
      Facebook, Twitter, or LinkedIn.  This gives you a Singly access token on
      on a per user basis.  The user can authenticate with multiple services 
      and you can pull data from each.</p>
      <c:if test="${authenticated}">
        <h2>Step 2: Authenticated! Your account is: ${account}</h2>
        <p>Now that the user has authenticated you can call Singly APIs or proxy 
        through to the service APIs. <a href="/index.html">Go To Examples</a></p>
      </c:if>      
      <table id="serviceList">
        <tr>
        <c:forEach var="service" items="${services}" varStatus="status">
          <c:set var="profile" value="${profiles[service.id]}" />
          <td <c:if test="${profile != null}">class="hasProfile"</c:if>>
            <div class="serviceCell">
              <c:if test="${profile != null}">
                <a href="/authentication.html?service=${service.id}&profile=${profile}">
                  <div class="minus">
                    <img src="/img/minus.png" alt="Remove authentication for this service" />
                  </div>
                </a>
              </c:if>
              <a href="/authentication.html?service=${service.id}">
                <img src="${service.icons['32x32']}" />
                <br>
                ${service.name}
              </a>
            </div>
          </td>
          <c:if test="${status.count % 7 == 0}"></ tr><tr></c:if>
        </c:forEach>
        </tr>
      </table>
    </div>
  </body>
</html>