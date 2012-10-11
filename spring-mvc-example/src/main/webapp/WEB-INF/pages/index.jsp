<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head></head>
<body>
  <h2>Processing</h2>
  <c:if test="${completedAuth}">You Just Completed An Authentication</c:if>
  <p>${profilesJSON}</p>
  
  <h2>Services</h2>
  <ol>
    <li>
      <a href="http://localhost:8080/index.html?service=facebook">Facebook</a>
      <c:if test="${facebookAuthenticated}">&nbsp;<img src="/img/green_checkmark.jpg" /></c:if>
    </li>
    <li>
      <a href="http://localhost:8080/index.html?service=github">Github</a>
      <c:if test="${githubAuthenticated}">&nbsp;<img src="/img/green_checkmark.jpg" /></c:if>
    </li>
    <li>
      <a href="http://localhost:8080/index.html?service=twitter">Twitter</a>
      <c:if test="${twitterAuthenticated}">&nbsp;<img src="/img/green_checkmark.jpg" /></c:if>
    </li>
    <li>
      <a href="http://localhost:8080/index.html?service=linkedin">LinkedIn</a>
      <c:if test="${linkedinAuthenticated}">&nbsp;<img src="/img/green_checkmark.jpg" /></c:if>
    </li>
  </ol>
</body>
</html>
  