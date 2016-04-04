<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setBundle basename="pagecontent"/>

<div style="margin-bottom: 15px"><span><h4 style="border-bottom: 1px solid white;">
  <c:choose>
    <c:when test="${user.getRole().toString().equalsIgnoreCase('admin')}">
      <c:set var="command" value="view_all_orders" scope="page"/>
    </c:when>
    <c:when test="${user.getRole().toString().equalsIgnoreCase('client')}">
      <c:set var="command" value="view_user_orders" scope="page"/>
    </c:when>
  </c:choose>
  <button class="${empty param.type.toString() && param.command.toString().equals("view_all_orders") ? 'active' : ''}">
    <a href="${pageContext.request.contextPath}/controller?command=${command}">
      <fmt:message key="label.orders.all"/>
    </a>
  </button>
  <button class="${param.type.toString().equals('paid') && param.command.toString().equals("view_all_orders") ? 'active' : ''}">
    <a href="${pageContext.request.contextPath}/controller?command=${command}&type=paid">
      <fmt:message key="label.orders.paid"/>
    </a>
  </button>
  <button class="${param.type.toString().equals('unpaid') && param.command.toString().equals("view_all_orders") ? 'active' : ''}">
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
</h4></span>
  <c:if test="${param.command.toString().equalsIgnoreCase('view_user_orders') && user.role.toString().equalsIgnoreCase('admin')}">
    <span>
      <h4 >
        <button class="sub-menu ${empty param.type? 'active-bottom' : ''}">
          <a href="controller?command=view_user_orders&id=${param.id}&login=${param.login}">
              ${param.login}:<fmt:message key="label.orders.all"/>
          </a>
        </button>
        <button class="sub-menu ${param.type.toString().equals('paid') ? 'active-bottom' : ''}">
          <a href="controller?command=view_user_orders&type=paid&id=${param.id}&login=${param.login}">
              ${param.login}:<fmt:message key="label.orders.paid"/>
          </a>
        </button>
        <button class="sub-menu ${param.type.toString().equals('unpaid') ? 'active-bottom' : ''}">
          <a href="controller?command=view_user_orders&type=unpaid&id=${param.id}&login=${param.login}">
              ${param.login}:<fmt:message key="label.orders.unpaid"/>
          </a>
        </button>
      </h4>
    </span>
  </c:if></div >