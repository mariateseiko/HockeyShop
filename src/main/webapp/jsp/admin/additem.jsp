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
  <div class="about" style="background-color: #212121">
    <span><h3 style="color:white"><fmt:message key="label.add.new.item"/></h3></span>
    <div class="form_container">
      <div class="error_message">${successMessage}${errorMessage}</div>
      <form name="forms" method="post" action="${pageContext.request.contextPath}/controller" enctype="multipart/form-data" class="input-form center">
        <label><fmt:message key="label.name"/>*</label>
        <input type="text" name="itemname" ng-model="itemname" required minlength="3" maxlength="40" pattern="^[А-я\w-/ ]+$"/>
        <br/>
        <span class="invalid-message" ng-show="forms.itemname.$touched && forms.itemname.$error.required">
          <fmt:message key="message.name.error.required"/>
        </span>
        <span class="invalid-message" ng-show="forms.itemname.$touched && forms.itemname.$error.minlength">
          <fmt:message key="message.name.error.minlength"/>
        </span>
        <span class="invalid-message" ng-show="forms.itemname.$touched && forms.itemname.$error.pattern">
          <fmt:message key="message.name.error.pattern"/>
        </span>
        <br/>
        <label><fmt:message key="label.size"/>*</label>
        <input type="text" name="size" ng-model="size" required minlength=1 maxlength="12" pattern="^[А-я\w-/ ]+$"/>
        <br/>
        <span class="invalid-message" ng-show="forms.size.$touched && forms.size.$error.required">
          <fmt:message key="message.size.error.required"/>
        </span>
        <span class="invalid-message" ng-show="forms.size.$touched && forms.size.$error.pattern">
          <fmt:message key="message.name.error.pattern"/>
        </span>
        <br/>
        <label><fmt:message key="label.color"/>*</label>
        <input type="text" name="color" ng-model="color" required minlength=1 maxlength="12" pattern="^[А-я\w-/ ]+$"/>
        <br/>
        <span class="invalid-message" ng-show="forms.color.$touched && forms.color.$error.required">
          <fmt:message key="message.color.error.required"/>
        </span>
        <span class="invalid-message" ng-show="forms.color.$touched && forms.color.$error.pattern">
          <fmt:message key="message.name.error.pattern"/>
        </span>
        <label><fmt:message key="label.price"/>*</label>
        <input type="number" min="0" max="10000" name="price" value="1" required/>
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
        <textarea style="resize: none" name="additional" maxlength="255" rows="5"></textarea>
        <br/>
        <label><fmt:message key="label.description"/></label>
        <textarea style="resize: none" name="description" maxlength="255" rows="5"></textarea>
        <br/>
        <label><fmt:message key="label.image"/></label>
        <input style="color: white" type="file" name="image_file" accept="image/*"/>
        <br />
        <span class="invalid-message" style="display: block; width: 50%; margin: 5px auto">
          <fmt:message key="message.image.note"/>
        </span>
        <div class="center">
          <button type="submit" name="command" value="new_item"><fmt:message key="button.add"/></button>
        </div>
      </form>
    </div>
  </div>
</body>
</html>
