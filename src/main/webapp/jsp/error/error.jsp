<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title></title>
</head>
<body>
<div id="error-page">
    <header style="background-color: rgba(0,0,0,0.9)">
        <div class="heading" >
            <a style="text-decoration: initial" href="${pageContext.request.contextPath}/index.jsp"><h2>Hockey<span>Corner</span></h2></a>
        </div>
    </header>
    <div class="cover">
        <div class="error-container">
            <h1><span>${pageContext.errorData.statusCode}</span></h1>
                SOMETHING WENT WRONG. TRY AGAIN OR CONTACT US IF THE PROBLEM PERSISTS.

        </div>
        </div>
    </div>
</body>
</html>
