<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib  uri="customtags" prefix="ctg"%>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="pagecontent"/>
<html>
<head>
  <title><fmt:message key="label.title"/></title>
  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="css/menu.css">
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/angular.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/lasthope.js"></script>
</head>
<body ng-app="showApp">
<ctg:userheader/>

<div class="about">
  <span><h3>What do we offer</h3></span>
  <p class="para">Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.</p>
  <div class="cat-nav" id="skater-nav"><a href="controller?command=catalog&type=player"><img src="images/p2.jpg"> <button class="nav-button"><fmt:message key="label.menu.player"/></button></a></div>
  <div class="cat-nav" id="acc-nav"><a href="controller?command=catalog&type=accessory"><img src="images/acc4.jpg"> <button class="nav-button"><fmt:message key="label.menu.accessories"/></button></a></div>
  <div class="cat-nav" id="goalie-nav"><a href="controller?command=catalog&type=goalie"><img src="images/g23.jpg"> <button class="nav-button"><fmt:message key="label.menu.goalie"/></button></a></div>
</div>

<div id="modal" class="popupContainer" style="display:none;">
  <header class="popupHeader">
    <span>Login</span>
    <span class="modal_close"><i class="fa fa-times"></i></span>
  </header>

  <section class="popupBody">
    <div class="user_login">
      <form action="controller" method="post">
        <label>Email / Username</label>
        <input type="text" name="login"/>
        <br />

        <label>Password</label>
        <input type="password" name="password" />
        <br />
        <div class="center">
          <button href="#" class="form-button" name="command" value="login">Login</button>
        </div>
      </form>
    </div>

    <div class="user_register">
      <form>
        <label>Full Name</label>
        <input type="text" />
        <br />
        <label>Email Address</label>
        <input type="email" />
        <br />
        <label>Password</label>
        <input type="password" />
        <br />
        <div class="checkbox">
          <input id="send_updates" type="checkbox" />
          <label for="send_updates">Send me occasional email updates</label>
        </div>
        <div class="action_btns">
          <div class="one_half"><a href="#" class="btn back_btn"><i class="fa fa-angle-double-left"></i> Back</a></div>
          <div class="one_half last"><a href="#" class="btn btn_red">Register</a></div>
        </div>
      </form>
    </div>
  </section>
</div>

<userinfo></userinfo>
</body>
</html>
