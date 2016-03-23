<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="pagecontent"/>
<%@ taglib  uri="customtags" prefix="ctg"%>
<html>
<head>
  <title><fmt:message key="label.title"/></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/angular.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/lasthope.js"></script>
</head>
<body ng-app="showApp">
<ctg:userheader/>
<div class="about" id="catalog">
  <span><h4 style="border-bottom: 1px solid white"><button><fmt:message key="label.all.users"/></button></h4></span>
  <div class="error_message">${successMessage}${errorMessage}${successRemove}</div>
  <div class="item-container">
    <table class="orders">
      <tr class="headings">
        <td>ID</td>
        <td><fmt:message key="label.login"/></td>
        <td><fmt:message key="label.submitted"/></td>
        <td><fmt:message key="label.paid"/></td>
        <td><fmt:message key="label.unpaid"/></td>
        <td><fmt:message key="label.late"/></td>
        <td></td>
        <td></td>
      </tr>
      <c:forEach var="user" items="${users}">
        <tr>
          <td>${user.getId()}</td>
          <td>${user.getLogin()}</td>
          <td>${user.getCountOfSubmittedOrders()}</td>
          <td>${user.getCountOfPaidOrders()}</td>
          <td>${user.getCountOfUnpaidOrders()}</td>
          <td>${user.getCountOfLateOrders()}</td>
          <td>
            <button>
              <a href="${pageContext.request.contextPath}/controller?command=view_user&id=${user.getId()}">
                <fmt:message key="button.view.user"/>
              </a>
            </button>
          </td>
          <td>
            <c:if test="${user.getCountOfLateOrders() > 0 && !user.isBanned()}">
            <button>
              <a href="${pageContext.request.contextPath}/controller?command=ban_user&id=${user.getId()}">
                <fmt:message key="label.ban.user"/>
              </a>
            </button>
            </c:if>
            <c:if test="${user.isBanned()}">
              <button>
                <a href="${pageContext.request.contextPath}/controller?command=unban_user&id=${user.getId()}">
                  <fmt:message key="button.unban"/>
                </a>
              </button>
            </c:if>
          </td>
        </tr>
      </c:forEach>
    </table>
  </div>
</div>
</body>
</html>
