<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <title></title>
</head>
<body>

<div id="banned">
  <header style="background-color: rgba(0,0,0,0.9)">
    <div class="heading" >
      <a style="text-decoration: initial" href="${pageContext.request.contextPath}/index.jsp"><h2>Hockey<span>Corner</span></h2></a>
    </div>
  </header>
  <div class="cover">
    <div class="error-container-banned">
      <h1><span>Ooops, you're banned</span></h1>
      You had some late unpaid orders and got banned. Please contact the administrator at admin@admin.com if you feel
      that was unnecessary
    </div>
  </div>
</div>
</body>
</html>
