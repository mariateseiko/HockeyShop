<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <title></title>
</head>
<body>
<div id="error-page-404">
  <header style="background-color: rgba(0,0,0,0.8)">
    <div class="heading" >
      <a style="text-decoration: initial" href="${pageContext.request.contextPath}/index.jsp"><h2>Hockey<span>Corner</span></h2></a>
    </div>
  </header>
  <div class="cover">
    <div class="error-container">
      <h1><span>${pageContext.errorData.statusCode}</span></h1>
      THE PAGE YOU'RE LOOKING DOESN'T EXIST :(
    </div>
  </div>
</div>
</body>
</html>
