<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="pagecontent"/>
<div class="header-image">
  <header>
    <div class="dropdown-wrapper" id="left-menu" ng-controller="menuController">
      <button class="dropdown-button"  id="left-menu-button" ng-click="leftClick()"><fmt:message key="label.menu"/></button>
      <nav class="dropdown dropdown-is-active" id="left-dropdown" ng-show="showLeft">
        <ul class="dropdown-content">
          <li class="cd-divider"><fmt:message key="label.catalog"/></li>
          <li>
            <a href="${pageContext.request.contextPath}/controller?command=catalog&type=goalie">
              <fmt:message key="label.menu.goalie"/>
            </a>
          </li>
          <li>
            <a href="${pageContext.request.contextPath}/controller?command=catalog&type=player">
              <fmt:message key="label.menu.player"/>
            </a>
          </li>
          <li>
            <a href="${pageContext.request.contextPath}/controller?command=catalog&type=accessory">
              <fmt:message key="label.menu.accessories"/>
            </a>
          </li>
        </ul>
      </nav>
    </div>
    <div style="position: absolute; right: 0">
      <button class="dropdown-button ${sessionScope.locale.toString().equals('en_US') ? 'active' : ''} locale">
        <a href="${pageContext.request.contextPath}/controller?command=change_locale&locale=en">EN</a>
      </button>
      <button class="dropdown-button ${sessionScope.locale.toString().equals('ru_RU') ? 'active' : ''} locale">
        <a href="${pageContext.request.contextPath}/controller?command=change_locale&locale=ru">RU</a>
      </button>
      <button class="dropdown-button guest-button"><a href="${pageContext.request.contextPath}/jsp/guest/register.jsp"><fmt:message key="label.menu.signup"/></a></button>
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
        <a href="${pageContext.request.contextPath}/jsp/guest/login.jsp">
          <button style="margin-top: 4px">
            <fmt:message key="guest.button.center"/>
          </button>
        </a>
    </div>
  </div>
</div>

