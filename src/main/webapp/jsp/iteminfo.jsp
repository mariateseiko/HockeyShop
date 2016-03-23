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
<div ng-controller="mainController" class="about" id="catalog">
  <span><h4>
    <c:if test="${not empty lastPage}">
      <button>
        <a href="${pageContext.request.contextPath}/controller?command=catalog&type=${item.getType()}&page=${lastPage}&dir=${dir}">
          <fmt:message key="button.back"/>
        </a>
      </button>
    </c:if>|
    <button>
      <a href="${pageContext.request.contextPath}/controller?command=catalog&type=goalie&dir=${dir}">
        <fmt:message key="label.menu.goalie"/>
      </a>
    </button>
    <button>
      <a href="${pageContext.request.contextPath}/controller?command=catalog&type=player&dir=${dir}">
        <fmt:message key="label.menu.player"/>
      </a>
    </button>
    <button>
      <a href="${pageContext.request.contextPath}/controller?command=catalog&type=accessory&dir=${dir}">
        <fmt:message key="label.menu.accessories"/>
      </a>
    </button>
  </h4></span>
  <div class="error_message">${successMessage}${errorMessage}</div>
  <div class="item-container">
    <div class="item-wrapper-big">
      <div class="item">
        <div class="item-name">${item.getName()} </div>
        <img src="${pageContext.request.contextPath}/${item.imagePath}" class="item-image-big" />
        <div class="item-info">
          <div class="info">
            <span class="info-label"><fmt:message key="label.size"/></span>
            <span class="info-value">${item.getSize()}</span></div>
          <br/>
          <div class="info">
            <span class="info-label"><fmt:message key="label.color"/></span>
            <span class="info-value">${item.getColor()}</span></div>
          <br/>
          <div class="info"><span class="info-label"><fmt:message key="label.price"/></span><span class="info-value">${item.getPrice()}</span></div>
          <br/>
          <c:if test="${user.getRole().toString().equalsIgnoreCase('admin')}">
            <div class="info"><span class="info-label"><fmt:message key="label.status"/></span><span class="info-value">${item.getStatus().getName()}</span></div>
            <br/>
          </c:if>
          <c:if test="${not empty item.getAdditionalInfo()}">
          <div class="info"><span class="info-label"><fmt:message key="label.additional"/></span><span class="info-value">${item.getAdditionalInfo()}</span></div>
          <br/>
          </c:if>
          <c:if test="${not empty item.getDescription()}">
          <div class="info"><span class="info-label-desc"><fmt:message key="label.description"/></span>
            <br/><span class="info-value" id="desc">${item.getDescription()}</span></div>
          <br/>
          </c:if>
        </div>

          <c:if test="${user.getRole().toString().equalsIgnoreCase('client')}">
        <form method="post" action="${pageContext.request.contextPath}/controller">

        <div class="number">
              <span class="quant"><fmt:message key="label.quantity"/></span>
              <button type="button" class="minus" ng-click="dec()">-</button>
              <input class="q-input" name="count" type="text" value="{{count}}" readonly/>
              <button type="button" class="plus" ng-click="inc()">+</button>
          <input type="hidden" name="id" value="${item.getId()}"/>
              <button id="add" type="submit" name="command" value="add_to_order">
                <fmt:message key="label.add.to.order"/>
              </button>
            </div>
        </form>
          </c:if>
          <c:if test="${user.getRole().toString().equalsIgnoreCase('admin')}">
            <div class="number">
              <button class="bottom" ng-click="showUpdatePrice=!showUpdatePrice; showUpdateStatus=false">
                <fmt:message key="label.action.update.price"/>
              </button>
              <button class="bottom" ng-click="showUpdateStatus=!showUpdateStatus; showUpdatePrice=false">
                <fmt:message key="label.action.update.status"/>
              </button>
            </div>
          </c:if>

      </div>
    </div>

    <div class="form_container" ng-show="showUpdatePrice">
      <form method="post" action="${pageContext.request.contextPath}/controller" enctype="multipart/form-data" class="input-form center">
        <label><fmt:message key="label.new.price"/></label>
        <input type="number" min="0" name="price" id="update-form" required value="${item.getPrice()}"/>
        <br />
        <input type="hidden" name="id" value="${item.getId()}"/>
        <div class="center">
          <button type="submit" name="command" value="update_item_price"><fmt:message key="button.update"/></button>
        </div>
      </form>
    </div>

    <div class="form_container" ng-show="showUpdateStatus">
      <form method="post" action="${pageContext.request.contextPath}/controller" enctype="multipart/form-data" class="input-form center">
        <label id="new-status"><fmt:message key="label.new.status"/></label>
        <select multiple size="3" name="status" id="update-form">
          <option value="in_stock" selected><fmt:message key="label.instock"/></option>
          <option value="out_of_stock"><fmt:message key="label.outofstock"/></option>
          <option value="out_of_production"><fmt:message key="label.outofproduction"/></option>
        </select>
        <input type="hidden" name="id" value="${item.getId()}"/>
        <br/>
        <div class="center">
          <button type="submit" name="command" value="update_item_status"><fmt:message key="button.update"/></button>
        </div>
      </form>
    </div>
  </div>

</div>

</body>
</html>
