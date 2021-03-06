<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="pagecontent"/>
<html>
<head>
  <title><fmt:message key="label.title"/></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body >
<div id="login_back">
  <header style="background-color: rgba(0,0,0,0.8)">
    <div class="heading" >
      <a style="text-decoration: initial" href="${pageContext.request.contextPath}/index.jsp"><h2>Hockey<span>Corner</span></h2></a>
    </div>
  </header>
  <div class="cover">
    <div class="popupContainer" id="register_container">
      <header class="popupHeader">
        <span><fmt:message key="label.registration"/></span>
      </header>
      <div class="popupBody">
        <div class="user_register">
          <form method="post" action="${pageContext.request.contextPath}/controller">
            <label><fmt:message key="label.login"/>*</label>
            <fmt:message key="message.invalid.login" var="invalidLogin"/>
            <input type="text" name="login" required pattern="^[A-z]\w{3,14}$"
                   oninvalid="this.setCustomValidity('${invalidLogin}')"
                   oninput="setCustomValidity('')"/>
            <br/>
            <label><fmt:message key="label.email"/>*</label>
            <input type="email" name="email" required/>
            <br/>
            <label><fmt:message key="label.password"/>*</label>
            <fmt:message key="message.invalid.password" var="invalidPassword"/>
            <input type="password" name="password" required pattern="^[A-z0-9]\w{5,19}$"
                    oninvalid="this.setCustomValidity('${invalidPassword}')"
                    oninput="setCustomValidity('')"/>
            <br/>
            <div class="center">
              <button type="submit" name="command" value="register"><fmt:message key="button.register"/></button>
            </div>
            <div class="error_message">${errorMessage}</div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>

