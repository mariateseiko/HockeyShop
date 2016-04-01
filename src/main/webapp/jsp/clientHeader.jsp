<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="pagecontent"/>
<div class="header-image">
  <header>
    <div class="cd-dropdown-wrapper" id="left-menu" ng-controller="menuController">
      <button class="dropdown-button"  id="left-menu-button" ng-click="leftClick()"><fmt:message key="label.menu"/></button>
      <nav class="cd-dropdown dropdown-is-active" id="left-dropdown" ng-show="showLeft">
        <ul class="cd-dropdown-content">
          <li class="cd-divider"><fmt:message key="label.catalog"/></li>
          <li>
            <a href="${pageContext.request.contextPath}/controller?command=catalog&type=goalie"><fmt:message key="label.menu.goalie"/></a>
          </li>
          <li>
            <a href="${pageContext.request.contextPath}/controller?command=catalog&type=player"><fmt:message key="label.menu.player"/></a>
          </li>
          <li>
            <a href="${pageContext.request.contextPath}/controller?command=catalog&type=accessory"><fmt:message key="label.menu.accessories"/></a>
          </li>
        </ul>
      </nav>
    </div>
      <div class="cd-dropdown-wrapper" id="right-menu" ng-controller="menuController">
          <button class="dropdown-button " id="right-menu-button" ng-click="rightClick()"><img src="${pageContext.request.contextPath}/images/user.png" id="logo_client">${user.getLogin()}</button>
        <nav class="cd-dropdown dropdown-is-active" id="right-dropdown" ng-show="showRight">
          <ul class="cd-dropdown-content">
            <li class="cd-divider"><fmt:message key="label.orders"/></li>
            <li>
              <a href="${pageContext.request.contextPath}/controller?command=view_user_orders"><fmt:message key="label.orders.all"/></a>
            </li>
            <li>
              <a href="${pageContext.request.contextPath}/controller?command=view_user_orders&type=paid"><fmt:message key="label.orders.paid"/></a>
            </li>
            <li>
              <a href="${pageContext.request.contextPath}/controller?command=view_user_orders&type=unpaid"><fmt:message key="label.orders.unpaid"/></a>
            </li>
            <li class="cd-divider"></li>
            <li><a href="${pageContext.request.contextPath}/controller?command=logout"><fmt:message key="label.logout"/></a></li>
          </ul>
        </nav>
      </div>
    <div style="position: absolute; right: 0">
      <button class="dropdown-button locale" ><a href="${pageContext.request.contextPath}/controller?command=change_locale&locale=en">EN</a></button>
      <button class="dropdown-button active locale" ><a href="${pageContext.request.contextPath}/controller?command=change_locale&locale=ru">RU</a></button>
    </div>
    <div class="heading">
      <a href="${pageContext.request.contextPath}/index.jsp"><h2>Hockey<span>Corner</span></h2></a>
    </div>
  </header>

  <div id="wrapper">
    <div class="test">
        <span><fmt:message key="label.header.first"/></span>
        <br>
        <span><fmt:message key="label.header.second"/></span><br>
        <button type="submit" style="margin-top: 4px">
          <a href="${pageContext.request.contextPath}/controller?command=view_order_items">
            <fmt:message key="client.button.center"/>
          </a>
        </button>
    </div>
  </div>
</div>