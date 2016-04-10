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
  <div  ng-controller="paymentController" class="about" id="catalog">
    <div class="error_message">${successMessage}${errorMessage}</div>
    <c:import url="/jsp/admin/ordersMenu.jsp"/>
    <div class="item-container">
      <c:if test="${empty items}"><span><h4 style="color:#04b5d4">
        <fmt:message key="message.order.no.items"/>
      </h4></span></c:if>
      <c:if test="${not empty items}">
        <c:if test="${not empty creationDate}">
          <span><h4 style="color:white">ID ${id}</h4></span>
        </c:if>
        <span><h4 style="color:white"><fmt:message key="label.total.price"/> ${sum}</h4></span>
        <span><h4 style="color:white"> <fmt:message key="label.total.quantity"/> ${quantity}</h4></span>
        <c:if test="${not empty creationDate}">
          <span><h4 style="color:white"> <fmt:message key="label.creation.date"/>
            <ctg:orderdate calendar="${creationDate}"/>
          </h4></span>
        </c:if>
        <c:if test="${not empty paymentDate}">
          <span><h4 style="color:white"> <fmt:message key="label.payment.date"/>
            <ctg:orderdate calendar="${paymentDate}"/>
          </h4></span>
        </c:if>
      </c:if>
      <c:forEach var="item" items="${items}">
        <div class="item-wrapper-big" style="margin-bottom: 5px">
          <div class="item" style="height: 210px">
            <div class="item-name">${item.key.getName()}</div>
            <img src="${pageContext.request.contextPath}/${item.key.imagePath}" class="item-image-big" style="width:150px"/>
            <div class="item-info">
              <div class="info">
                <span class="info-label"><fmt:message key="label.size"/></span>
                <span class="info-value">${item.key.getSize()}</span></div>
                <br/>
                <div class="info">
                  <span class="info-label"><fmt:message key="label.color"/></span>
                  <span class="info-value">${item.key.getColor()}</span>
                </div>
                <br/>
                <div class="info">
                  <span class="info-label"><fmt:message key="label.price"/></span>
                  <span class="info-value">${item.key.getPrice()}</span>
                </div>
                <br/>
                <c:if test="${not empty item.key.getAdditionalInfo()}">
                  <div class="info"><span class="info-label"><fmt:message key="label.additional"/></span><span class="info-value">${item.getAdditionalInfo()}</span></div>
                  <br/>
                </c:if>
                <div class="info">
                  <span class="info-label"><fmt:message key="label.quantity"/></span>
                  <span class="info-value">${item.value}</span>
                </div>
                <br/>
                <c:if test="${empty creationDate && user.role.toString().equals('CLIENT')}">
                  <form method="post" action="${pageContext.request.contextPath}/controller">
                    <div class="number">
                      <input type="hidden" name="itemId" value="${item.key.getId()}"/>
                      <button class="bottom" type="submit" name="command" value="remove_from_order">
                        <fmt:message key="button.order.item.remove"/>
                      </button>
                    </div>
                  </form>
                </c:if>
                <c:if test="${user.role.toString().equals('ADMIN')}">
                  <div class="number">
                    <button class="bottom">
                      <a href="${pageContext.request.contextPath}/controller?command=view_item&id=${item.key.id}">
                        <fmt:message key="label.read.more"/>
                      </a>
                    </button>
                  </div>
                </c:if>
              </div>
            </div>
          </div>
        </c:forEach>
      <c:choose>
        <c:when test="${items.size()>0 && empty creationDate && user.role.toString().equalsIgnoreCase('client')}">
        <form method="post" action="${pageContext.request.contextPath}/controller">
        <div>${id}</div>
          <input type="hidden" name="id" value="${id}"/>
          <button type="submit" name="command" value="submit_order"><fmt:message key="button.order.submit"/></button>
        </form>
        </c:when>
        <c:when test="${not empty creationDate && empty paymentDate}">
          <c:if test="${user.role.toString().equalsIgnoreCase('client')}">
          <button ng-click="paymentClick()"><fmt:message key="button.order.pay"/></button>
          </c:if>
          ${late}
          <c:if test="${user.role.toString().equals('ADMIN') && late || user.role.toString().equals('CLIENT')}">
            <form method="post" action="${pageContext.request.contextPath}/controller" style="display: inline">
            <input type="hidden" name="id" value="${id}"/>
            <button name="command" value="delete_order" type="submit">
              <fmt:message key="button.order.cancel"/>
            </button>
          </form>
          </c:if>
        </c:when>
      </c:choose>
      <c:if test="${user.role.toString().equalsIgnoreCase('client')}">
        <div class="payment input-form" ng-show="showPayment">
          <form method="post" name="forms" action="${pageContext.request.contextPath}/controller">
          <label><fmt:message key="label.cart"/>*</label>
          <input name="card" ng-model="card" ng-pattern="/[0-9]{16}/" type="text" minlength="16" maxlength="16" required/>
            <br/>
            <span class="invalid-message" ng-show="forms.card.$touched && forms.card.$error.required">
              <fmt:message key="message.card.error.required"/>
            </span>
            <span class="invalid-message" ng-show="forms.card.$touched && forms.card.$error.pattern">
              <fmt:message key="message.card.error.pattern"/>
            </span>
          <input type="hidden" name="id" value="${id}"/>
          <br/>
          <button name="command" value="pay_order"><fmt:message key="label.pay"/></button>
          </form>
        </div>
      </c:if>
    </div>
  </div>
</body>
</html>
