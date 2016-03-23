<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="pagecontent"/>
<div class="header-image">
  <header>
    <div class="cd-dropdown-wrapper" id="left-menu" ng-controller="menuController">
      <button class="dropdown-button"  id="left-menu-button" ng-click="leftClick()"><fmt:message key="label.menu"/></button>
      <nav class="cd-dropdown dropdown-is-active" id="left-dropdown" ng-show="showLeft">
        <ul class="cd-dropdown-content">
          <li>
            <form class="cd-search">
              <input type="search" placeholder=<fmt:message key="label.search"/>>
            </form>
          </li>
          <li class="has-children">
            <a href="http://codyhouse.co/?p=748">Clothing</a>
          </li>
          <li class="has-children">
            <a href="http://codyhouse.co/?p=748">Gallery</a>
          </li>
          <li class="has-children">
            <a href="http://codyhouse.co/?p=748">Services</a>
          </li>
          <li class="cd-divider">Divider</li>
          <li><a href="http://codyhouse.co/?p=748">Page 1</a></li>
          <li><a href="http://codyhouse.co/?p=748">Page 2</a></li>
          <li><a href="http://codyhouse.co/?p=748">Page 3</a></li>
        </ul>
      </nav>
    </div>
    <div style="position: absolute; right: 0">
      <button class="dropdown-button locale"  >EN</button>
      <button class="dropdown-button active locale" >RU</button>
      <button class="dropdown-button guest-button" ><a href="jsp/register.jsp"><fmt:message key="label.menu.signup"/></a></button>
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
      <a href="jsp/login.jsp"><button style="margin-top: 4px"><fmt:message key="guest.button.center"/></button></a>
    </div>
  </div>
</div>

