<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib  uri="customtags" prefix="ctg"%>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="pagecontent"/>
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
  <div class="about">
    <span><h3><fmt:message key="label.about.us"/></h3></span>
    <p class="para"><fmt:message key="para.about.us"/></p>
    <div class="cat-nav" id="skater-nav"><a href="controller?command=catalog&type=player"><img src="images/p2.jpg"> <button class="nav-button"><fmt:message key="label.menu.player"/></button></a></div>
    <div class="cat-nav" id="acc-nav"><a href="controller?command=catalog&type=accessory"><img src="images/acc4.jpg"> <button class="nav-button"><fmt:message key="label.menu.accessories"/></button></a></div>
    <div class="cat-nav" id="goalie-nav"><a href="controller?command=catalog&type=goalie"><img src="images/g23.jpg"> <button class="nav-button"><fmt:message key="label.menu.goalie"/></button></a></div>
  </div>
</body>
</html>
