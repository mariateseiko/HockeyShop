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
  <span><h4>
    <button class="${type.toString().equals('player') ? 'active' : ''}">
      <a href="${pageContext.request.contextPath}/controller?command=catalog&type=player&dir=${dir}">
        <fmt:message key="label.menu.player"/>
      </a>
    </button>
    <button class="${type.toString().equalsIgnoreCase('accessory') ? 'active' : ''}">
      <a href="${pageContext.request.contextPath}/controller?command=catalog&type=accessory&dir=${dir}">
        <fmt:message key="label.menu.accessories"/>
      </a>
    </button>
    <button class="${type.toString().equals('goalie') ? 'active' : ''}">
      <a href="${pageContext.request.contextPath}/controller?command=catalog&type=goalie&dir=${dir}">
        <fmt:message key="label.menu.goalie"/>
      </a>
    </button>
  </h4></span>
  <span><h4><fmt:message key="label.sort.price"/>
    <c:if test="${'desc'.equals(dir)}">
      <button><a href="${pageContext.request.contextPath}/controller?command=catalog&type=${type}&page=${currentPage}&dir=asc">&#8593</a></button>
    </c:if>
    <c:if test="${'asc'.equals(dir)}">
      <button><a href="${pageContext.request.contextPath}/controller?command=catalog&type=${type}&page=${currentPage}&dir=desc">&#8595</a></button>
    </c:if>
  </h4></span>
  <div class="item-container">
    <c:forEach var="item" items="${items}">
    <div class="item-wrapper">
      <div class="item">
        <div class="item-name">${item.getName()}</div>
        <img src="${pageContext.request.contextPath}/${item.getImagePath()}" class="item-image"/>
        <div class="item-info">
          <div class="info"><span class="info-label"><fmt:message key="label.size"/></span> <span class="info-value">${item.getSize()}</span></div>
          <br/>
          <div class="info"><span class="info-label"><fmt:message key="label.color"/></span><span class="info-value">${item.getColor()}</span></div>
          <br/>
          <div class="info"><span class="info-label"><fmt:message key="label.price"/></span><span class="info-value">${item.getPrice()}</span></div>
          <br/>
          <div class="info"><span class="info-value"><fmt:message key="${item.status.name}"/></span></div>
        </div>
        <button class="read-more"><a href="${pageContext.request.contextPath}/controller?command=view_item&id=${item.getId()}&lastPage=${currentPage}&dir=${dir}"><fmt:message key="label.read.more"/></a></button>
      </div>
    </div>
    </c:forEach>
  </div>
  <div class="pagination">
    <c:if test="${numPages>1}">
      <c:if test="${currentPage > 1}">
        <button>
          <a href="${pageContext.request.contextPath}/controller?command=catalog&type=${type}&page=${currentPage-1}&dir=${dir}">
            <fmt:message key="button.prev"/>
          </a>
        </button>
      </c:if>
      <c:if test="${currentPage > 2}">
        <button>
          <a href="${pageContext.request.contextPath}/controller?command=catalog&type=${type}&page=1&dir=${dir}">
            <fmt:message key="button.first"/>
          </a>
        </button>
      </c:if>
      <button class="active">
        <a href="${pageContext.request.contextPath}/controller?command=catalog&type=${type}&page=${currentPage}&dir=${dir}">
          ${currentPage}
        </a>
      </button>
      <c:if test="${numPages>currentPage}">
        <button><a href="${pageContext.request.contextPath}/controller?command=catalog&type=${type}&page=${currentPage+1}&dir=${dir}"><fmt:message key="button.next"/></a></button>
        <c:if test="${numPages>currentPage+1}">
          <button>
            <a href="${pageContext.request.contextPath}/controller?command=catalog&type=${type}&page=${numPages}&dir=${dir}">
              <fmt:message key="button.last"/>
            </a>
          </button>
        </c:if>
      </c:if>
    </c:if>
  </div>
</div>
</body>
</html>
