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
  <c:import url="/jsp/ordersMenu.jsp"/>
  <div class="item-container" style="background-color: white">
    <table class="orders">
      <tr class="headings">
        <td><fmt:message key="label.order.id"/></td>
        <c:if test="${user.getRole().toString().equalsIgnoreCase('admin') && command.equalsIgnoreCase('view_all_orders')}">
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
          <c:if test="${user.getRole().toString().equalsIgnoreCase('admin') && command.equalsIgnoreCase('view_all_orders')}">
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
    <div class="pagination">
      <c:if test="${numPages>1}">
        <c:if test="${currentPage > 1}">
          <button><a href="${pageContext.request.contextPath}/controller?command=catalog&type=${type}&page=${currentPage-1}&dir=${dir}"><fmt:message key="button.prev"/></a></button>
        </c:if>
        <c:if test="${currentPage > 2}">
          <button><a href="${pageContext.request.contextPath}/controller?command=catalog&type=${type}&page=1&dir=${dir}"><fmt:message key="button.first"/></a></button>
        </c:if>
        <button class="active"><a href="${pageContext.request.contextPath}/controller?command=catalog&type=${type}&page=${currentPage}&dir=${dir}">${currentPage}</a></button>
        <c:if test="${numPages>currentPage}">
          <button><a href="${pageContext.request.contextPath}/controller?command=catalog&type=${type}&page=${numPages}&dir=${dir}"><fmt:message key="button.next"/></a></button>
          <c:if test="${numPages>currentPage+1}">
            <button><a href="${pageContext.request.contextPath}/controller?command=catalog&type=${type}&page=${currentPage+1}&dir=${dir}"><fmt:message key="button.last"/></a></button>
          </c:if>
        </c:if>
      </c:if>
    </div>
  </div>
</div>
</body>
</html>
