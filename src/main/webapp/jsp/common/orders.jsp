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
  <div class="error_message">${successMessage}${errorMessage}</div>
  <c:import url="/jsp/admin/ordersMenu.jsp"/>
  <div class="item-container" style="background-color: white">
    <table class="orders">
      <c:if test="${not empty orders}">
      <tr class="headings">
        <td><fmt:message key="label.order.id"/></td>
        <c:if test="${user.role.toString().equalsIgnoreCase('admin') && param.command.equalsIgnoreCase('view_all_orders')}">
          <td><fmt:message key="label.user"/></td>
        </c:if>
        <td><fmt:message key="label.creation.date"/></td>
        <td><fmt:message key="label.payment.date"/></td>
        <td><fmt:message key="label.quantity"/></td>
        <td><fmt:message key="label.total.price"/></td>
        <td></td>
        <c:if test="${user.role.toString().equalsIgnoreCase('admin')}">
          <td></td>
        </c:if>
      </tr>
      </c:if>
      <c:if test="${empty orders}">
        <span><h4 style="color:#04b5d4; background-color: #212121"><fmt:message key="message.no.orders"/></h4></span>
      </c:if>
      <c:forEach var="order" items="${orders}">
        <tr>
          <td>${order.getId()}</td>
          <c:if test="${user.role.toString().equalsIgnoreCase('admin') && param.command.equalsIgnoreCase('view_all_orders')}">
            <td>${order.user.login}</td>
          </c:if>
          <td><ctg:orderdate calendar="${order.creationDateTime}"/></td>
          <td>
            <c:if test="${empty order.paymentDateTime}">
              <fmt:message key="label.order.not.payed"/>
            </c:if>
            <ctg:orderdate calendar="${order.paymentDateTime}"/>
          </td>
          <td>${order.countOfItems}</td>
          <td>${order.paymentSum}</td>
          <c:if test="${user.role.toString().equalsIgnoreCase('admin')}">
            <td><button><a href="${pageContext.request.contextPath}/controller?command=view_user&id=${order.user.id}">
              <fmt:message key="button.view.user"/>
            </a></button></td>
          </c:if>
          <td>
            <button>
              <a href="${pageContext.request.contextPath}/controller?command=view_order_items&id=${order.id}&login=${order.user.login}&userId=${order.user.id}">
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
