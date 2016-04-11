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
<body  ng-app="showApp">
<ctg:userheader/>
<div ng-controller="mainController" class="about" id="catalog">
  <c:import url="/jsp/admin/ordersMenu.jsp"/>
  <span><h4 style="margin-top: -15px">
    <button class="sub-menu">
      <a href="controller?command=view_user_orders&id=${appUser.id}&login=${appUser.login}">
        ${appUser.getLogin()}:<fmt:message key="label.orders.all"/>
      </a>
    </button>
    <button class="sub-menu">
      <a href="controller?command=view_user_orders&type=paid&id=${appUser.id}&login=${appUser.login}">
        ${appUser.getLogin()}:<fmt:message key="label.orders.paid"/>
      </a>
    </button>
    <button class="sub-menu">
      <a href="controller?command=view_user_orders&type=unpaid&id=${appUser.id}&login=${appUser.login}">
        ${appUser.getLogin()}:<fmt:message key="label.orders.unpaid"/>
      </a>
    </button>
  </h4></span>

  <div class="error_message">${successMessage}${errorMessage}</div>
  <div class="item-container">
    <div class="item-wrapper-big">
      <div class="user">
        <div class="item-name">${appUser.login} </div>
        <div class="user-info">
          <div class="info">
            <span class="info-label"><fmt:message key="label.email"/></span>
            <span class="info-value">${appUser.email}</span></div>
          <br/>
          <div class="info">
            <span class="info-label"><fmt:message key="label.submitted"/></span>
            <span class="info-value">${appUser.countOfSubmittedOrders}</span>
          </div>
          <br/>
          <div class="info">
            <span class="info-label"><fmt:message key="label.paid"/></span>
            <span class="info-value">${appUser.countOfPaidOrders}</span>
          </div>
          <br/>
          <div class="info">
            <span class="info-label"><fmt:message key="label.unpaid"/></span>
            <span class="info-value">${appUser.countOfUnpaidOrders}</span>
          </div>
          <br/>
          <div class="info">
            <span class="info-label"><fmt:message key="label.late"/></span>
            <span class="info-value">${appUser.countOfLateOrders}</span>
          </div>
          <br/>
        </div>
        <c:if test="${appUser.countOfLateOrders >0 && !appUser.isBanned()}">
          <div class="number">
            <c:if test="${appUser.getCountOfLateOrders() > 0 && !appUser.isBanned()}">
              <form method="post" action="${pageContext.request.contextPath}/controller" >
                <input type="hidden" name="id" value="${appUser.id}"/>
                <input type="hidden" name="ban" value="true"/>
                <button type="submit" name="command" value="ban_user">
                  <fmt:message key="label.ban.user"/>
                </button>
              </form>
            </c:if>
          </div>
        </c:if>
        <c:if test="${appUser.isBanned()}">
          <div class="number">
            <form method="post" action="${pageContext.request.contextPath}/controller" >
              <input type="hidden" name="id" value="${appUser.id}"/>
              <input type="hidden" name="ban" value="false"/>
              <button type="submit" name="command" value="ban_user">
                <fmt:message key="button.unban"/>
              </button>
            </form>
          </div>
        </c:if>
      </div>
    </div>
  </div>
</div>
</body>
</html>
