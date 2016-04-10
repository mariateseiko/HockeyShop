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
  <span><h4 style="border-bottom: 1px solid white">
    <button class="sub-menu ${empty param.type.toString() && param.command.toString().equals(command) ? 'active' : ''}">
      <a href="${pageContext.request.contextPath}/controller?command=${command}">
        <fmt:message key="label.orders.all"/>
      </a>
    </button>
    <button class="sub-menu ${param.type.toString().equals('paid') ? 'active' : ''}">
      <a href="${pageContext.request.contextPath}/controller?command=${command}&type=paid">
        <fmt:message key="label.orders.paid"/>
      </a>
    </button>
    <button class="sub-menu ${param.type.toString().equals('unpaid') ? 'active' : ''}">
      <a href="${pageContext.request.contextPath}/controller?command=${command}&type=unpaid">
        <fmt:message key="label.orders.unpaid"/>
      </a>
    </button>
    <button>
      <a href="${pageContext.request.contextPath}/controller?command=view_users_list">
        <fmt:message key="label.all.users"/>
      </a>
    </button>
</h4></span>
  <span><h4>
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
            <button class="bottom" ng-click="showUpdateStatus=!showUpdateStatus; showUpdatePrice=false">
              <a href="${pageContext.request.contextPath}/controller?command=ban_user&id=${appUser.id}&ban=true">
                <fmt:message key="label.ban.user"/>
              </a>
            </button>
          </div>
        </c:if>
        <c:if test="${appUser.isBanned()}">
          <div class="number">
            <button class="bottom" ng-click="showUpdateStatus=!showUpdateStatus; showUpdatePrice=false">
              <a href="${pageContext.request.contextPath}/controller?command=ban_user&id=${appUser.id}&ban=false">
                <fmt:message key="button.unban"/>
              </a>
            </button>
          </div>
        </c:if>
      </div>
    </div>
  </div>
</div>
</body>
</html>
