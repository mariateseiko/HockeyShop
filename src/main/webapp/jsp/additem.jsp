<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="pagecontent"/>
<%@ taglib  uri="customtags" prefix="ctg"%>
<html>
<head>
  <title><fmt:message key="label.title"/></title>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/angular.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/lasthope.js"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
</head>
<body ng-app="showApp">
<ctg:userheader/>
<div class="about">
  <span><h3><fmt:message key="label.add.new.item"/></h3></span>
  <div class="form_container">
    <div class="error_message">${successMessage}${errorMessage}</div>
    <form method="post" action="${pageContext.request.contextPath}/controller" enctype="multipart/form-data" class="input-form center">
      <label><fmt:message key="label.name"/>*</label>
      <input type="text" name="name" required max="45"/>
      <br/>
      <label><fmt:message key="label.size"/>*</label>
      <input type="text" name="size" required max="45"/>
      <br/>
      <label><fmt:message key="label.color"/>*</label>
      <input type="text" name="color" required max="45"/>
      <br/>
      <label><fmt:message key="label.price"/>*</label>
      <input type="number" min="0" name="price" required/>
      <br/>
      <label><fmt:message key="label.status"/>*</label>
      <select multiple size="3" name="status">
        <option value="in_stock" selected><fmt:message key="label.instock"/></option>
        <option value="out_of_stock"><fmt:message key="label.outofstock"/></option>
        <option value="out_of_production"><fmt:message key="label.outofproduction"/></option>
      </select>
      <br/>
      <label><fmt:message key="label.type"/></label>
      <select multiple size="3" name="type">
        <option value="goalie" selected><fmt:message key="label.menu.goalie"/></option>
        <option value="player"><fmt:message key="label.menu.player"/></option>
        <option value="accessory"><fmt:message key="label.menu.accessories"/></option>
      </select>
      <br/>
      <label><fmt:message key="label.additional"/></label>
      <input type="text" name="additional" max="255"/>
      <br/>
      <label><fmt:message key="label.description"/></label>
      <input type="text" name="description" max="255"/>
      <br/>
      <label><fmt:message key="label.image"/></label>
      <input type="file" name="image_file" accept="image/jpeg"/>
      <br />
      <div class="center">
        <button type="submit" name="command" value="new_item"><fmt:message key="button.add"/></button>
      </div>
    </form>
  </div>
</div>
</body>
</html>
