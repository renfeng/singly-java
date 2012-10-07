<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head></head>
<body>
  <h2>Processing</h2>
  <c:if test="${completedAuth}">You Just Completed An Authentication</c:if>
  <p>${profilesJSON}</p>
  
  <h2>Services</h2>
  <ol>
    <li><a href="http://localhost:8080/singly.html?service=facebook">Facebook</a></li>
    <li><a href="http://localhost:8080/singly.html?service=github">Github</a></li>
    <li><a href="http://localhost:8080/singly.html?service=twitter">Twitter</a></li>
    <li><a href="http://localhost:8080/singly.html?service=linkedin">LinkedIn</a></li>
  </ol>
</body>
</html>
  