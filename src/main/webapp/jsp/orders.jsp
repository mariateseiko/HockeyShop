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
<div class="about" id="catalog" style="background-color: #212121">
  <div class="error_message">${successMessage}${errorMessage}${successRemove}</div>
  <div style="border-bottom: 1px solid white; margin-bottom: 10px; "><span ><h4>
    <c:choose>
      <c:when test="${user.getRole().toString().equalsIgnoreCase('admin')}">
        <c:set var="command" value="view_all_orders" scope="page"/>
      </c:when>
      <c:when test="${user.getRole().toString().equalsIgnoreCase('client')}">
        <c:set var="command" value="view_user_orders" scope="page"/>
      </c:when>
    </c:choose>
    <button class="${empty param.type.toString() && param.command.toString().equals(command) ? 'active' : ''}">
      <a href="${pageContext.request.contextPath}/controller?command=${command}">
        <fmt:message key="label.orders.all"/>
      </a>
    </button>
    <button class="${param.type.toString().equals('paid') ? 'active' : ''}">
      <a href="${pageContext.request.contextPath}/controller?command=${command}&type=paid">
        <fmt:message key="label.orders.paid"/>
      </a>
    </button>
    <button class="${param.type.toString().equals('unpaid') ? 'active' : ''}">
      <a href="${pageContext.request.contextPath}/controller?command=${command}&type=unpaid">
        <fmt:message key="label.orders.unpaid"/>
      </a>
    </button>
    <c:if test="${user.getRole().toString().equalsIgnoreCase('client')}">
      <button class="${param.command.toString().equals('view_order_items') ? 'active' : ''}">
        <a href="${pageContext.request.contextPath}/controller?command=view_order_items">
          <fmt:message key="label.order.current"/>
        </a>
      </button>
    </c:if>
  </h4></span></div>
  <div class="item-container" style="background-color: white">
    <table class="orders">
      <tr class="headings">
        <td><fmt:message key="label.order.id"/></td>
        <c:if test="${user.getRole().toString().equalsIgnoreCase('admin')}">
          <td><fmt:message key="label.user"/></td>
        </c:if>
        <td><fmt:message key="label.creation.date"/></td>
        <td><fmt:message key="label.payment.date"/></td>
        <td><fmt:message key="label.quantity"/></td>
        <td><fmt:message key="label.total.price"/></td>
        <td></td>
        <c:if test="${user.getRole().toString().equalsIgnoreCase('admin')}">
          <td></td>
        </c:if>
      </tr>
      <c:forEach var="order" items="${orders}">
        <tr>
          <td>${order.getId()}</td>
          <c:if test="${user.getRole().toString().equalsIgnoreCase('admin')}">
            <td>${order.getUser().getLogin()}</td>
          </c:if>
          <td><ctg:orderdate calendar="${order.creationDateTime}"/></td>
          <td>
            <c:if test="${empty order.getPaymentDateTime()}">
              <fmt:message key="label.order.not.payed"/>
            </c:if>
            <ctg:orderdate calendar="${order.getPaymentDateTime()}"/>
          </td>
          <td>${order.getCountOfItems()}</td>
          <td>${order.getPaymentSum()}</td>
          <c:if test="${user.getRole().toString().equalsIgnoreCase('admin')}">
            <td><button><a href="${pageContext.request.contextPath}/controller?command=view_user&id=${order.getUser().getId()}"><fmt:message key="button.view.user"/></a></button></td>
          </c:if>
          <td>
            <button>
              <a href="${pageContext.request.contextPath}/controller?command=view_order_items&id=${order.getId()}">
                <fmt:message key="label.view.order"/>
              </a>
            </button>
          </td>
        </tr>
      </c:forEach>
    </table>
  </div>
</div>
</body>
</html>
