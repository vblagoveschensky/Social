<!DOCTYPE html>
<html>
    <head>
        <title>Welcome!</title>
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/header.jspf" %>
        <h1>Login</h1>
        ${sessionScope.loginerror}
        <form method="POST" action="j_security_check">
            <div><label>Login:<input type="text" name="j_username" value="${sessionScope.login}"></label></div>
            <div><label>Password:<input type="password" name="j_password"></label></div>
            <input type="submit" value="Login" />
        </form>
        <a href="${pageContext.servletContext.contextPath}/registration">Register</a>
    </body>
</html>

