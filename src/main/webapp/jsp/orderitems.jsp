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
    <div class="error_message">${successMessage}${errorMessage}${successRemove}</div>
    <div style="border-bottom: 1px solid white; margin-bottom: 10px"><span ><h4>
    <c:choose>
      <c:when test="${user.getRole().toString().equalsIgnoreCase('admin')}">
        <c:set var="command" value="view_all_orders" scope="page"/>
      </c:when>
      <c:when test="${user.getRole().toString().equalsIgnoreCase('client')}">
        <c:set var="command" value="view_user_orders" scope="page"/>
      </c:when>
    </c:choose>
      <button>
        <a href="${pageContext.request.contextPath}/controller?command=${command}">
          <fmt:message key="label.orders.all"/>
        </a>
      </button>
      <button>
        <a href="${pageContext.request.contextPath}/controller?command=${command}&type=paid">
          <fmt:message key="label.orders.paid"/>
        </a>
      </button>
      <button>
        <a href="${pageContext.request.contextPath}/controller?command=${command}&type=unpaid">
          <fmt:message key="label.orders.unpaid"/>
        </a>
      </button>
      <c:if test="${user.getRole().toString().equalsIgnoreCase('client')}">
        <button>
          <a href="${pageContext.request.contextPath}/controller?command=view_order_items">
            <fmt:message key="label.order.current"/>
          </a>
        </button>
      </c:if>
    </h4></span></div>
    <div class="item-container">
      <span><h4 style="color:white"><fmt:message key="label.total.price"/> ${sum}</h4></span>
      <span><h4 style="color:white"> <fmt:message key="label.total.quantity"/> ${quantity}</h4></span>
      <c:forEach var="item" items="${items}">
        <div class="item-wrapper-big">
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
              <c:if test="${empty creationDate}">
                <form method="post" action="${pageContext.request.contextPath}/controller">
                  <div class="number">
                    <input type="hidden" name="itemId" value="${item.key.getId()}"/>
                    <button class="bottom" type="submit" name="command" value="remove_from_order">
                      <fmt:message key="button.order.item.remove"/>
                    </button>
                  </div>
                </form>
              </c:if>
            </div>
          </div>
        </div>
      </c:forEach>
      <div style="margin-top: 5px">
        <c:choose>
          <c:when test="${items.size()>0 && empty creationDate}">
          <form method="post" action="${pageContext.request.contextPath}/controller">
          <div>${id}</div>
            <input type="hidden" name="id" value="${id}"/>
            <button type="submit" name="command" value="submit_order"><fmt:message key="button.order.submit"/></button>
          </form>
          </c:when>
          <c:when test="${not empty creationDate && empty paymentDate}">
            <button ng-click="paymentClick()"><fmt:message key="button.order.pay"/></button>
            <button>
              <a href="${pageContext.request.contextPath}/controller?command=delete_order&id=${id}">
              <fmt:message key="button.order.cancel"/>
                </a>
            </button>
          </c:when>
        </c:choose>
        <div class="payment input-form" ng-show="showPayment">
          <form method="post" action="${pageContext.request.contextPath}/controller">
          <label><fmt:message key="label.cart"/>*</label>
          <input name="card" type="text" required/>
          <input type="hidden" name="id" value="${id}"/>
          <br/>
          <button name="command" value="pay_order"><fmt:message key="label.pay"/></button>
          </form>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
