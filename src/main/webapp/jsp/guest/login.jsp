<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="pagecontent"/>
<html>  
<head>
  <title><fmt:message key="label.title"/></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" />
</head>
<body >
<div id="login_back">
  <header style="background-color: rgba(0,0,0,0.8)">
    <div class="heading" >
      <a style="text-decoration: initial" href="${pageContext.request.contextPath}/index.jsp"><h2>Hockey<span>Corner</span></h2></a>
    </div>
  </header>
    <div class="cover">
<div class="popupContainer" id="login_container">
  <header class="popupHeader">
    <span><fmt:message key="label.login"/></span>
  </header>

  <section class="popupBody">
    <div class="user_login">
      <div class="error_message">${successMessage}</div>
      <form method="post" action="${pageContext.request.contextPath}/controller">
        <label><fmt:message key="label.userlogin"/></label>
        <input type="text" name="login" required/>
        <br />
        <label><fmt:message key="label.password"/></label>
        <input type="password" name="password" required/>
        <br />
        <div class="center">
          <button type="submit" name="command" value="login"><fmt:message key="button.login"/></button>
        </div>
        <div class="error_message">${errorMessage}</div>

      </form>
    </div>
  </section>
    </div>
    </div>
</div>
</body>
</html>
