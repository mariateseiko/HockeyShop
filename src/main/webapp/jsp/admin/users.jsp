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

  <div class="error_message">${successMessage}${errorMessage}${successRemove}</div>
  <c:import url="/jsp/admin/ordersMenu.jsp"/>
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
      <c:forEach var="appUser" items="${users}">
        <tr>
          <td>${appUser.getId()}</td>
          <td>${appUser.getLogin()}</td>
          <td>${appUser.getCountOfSubmittedOrders()}</td>
          <td>${appUser.getCountOfPaidOrders()}</td>
          <td>${appUser.getCountOfUnpaidOrders()}</td>
          <td>${appUser.getCountOfLateOrders()}</td>
          <td>
            <button>
              <a href="${pageContext.request.contextPath}/controller?command=view_user&id=${appUser.getId()}">
                <fmt:message key="button.view.user"/>
              </a>
            </button>
          </td>
          <td>
            <c:if test="${appUser.getCountOfLateOrders() > 0 && !appUser.isBanned()}">
              <form method="post" action="${pageContext.request.contextPath}/controller" >
                <input type="hidden" name="id" value="${appUser.id}"/>
                <input type="hidden" name="ban" value="true"/>
                <button type="submit" name="command" value="ban_user">
                  <fmt:message key="label.ban.user"/>
                </button>
              </form>
            </c:if>
            <c:if test="${appUser.isBanned()}">
              <form method="post" action="${pageContext.request.contextPath}/controller" >
                <input type="hidden" name="id" value="${appUser.id}"/>
                <input type="hidden" name="ban" value="false"/>
                <button type="submit" name="command" value="ban_user">
                  <fmt:message key="button.unban"/>
                </button>
              </form>
            </c:if>
          </td>
        </tr>
      </c:forEach>
    </table>
  </div>
</div>
</body>
</html>
