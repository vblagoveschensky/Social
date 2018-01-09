<!DOCTYPE html>
<html>
    <head>
        <title>Registration</title>
        <script src="formvalidation.js"></script>
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/header.jspf" %>
        <h1>Registration</h1>
        <form method="POST" onsubmit="return validate(this)">
            <div><label>Login:<input type="text" name="login" value="${param.login}" onkeydown="hideError(this)"></label>
                <span id="loginerror">${requestScope.loginerror}</span></div>
            <div><label>Name:<input type="text" name="name" value="${param.name}" onkeydown="hideError(this)"></label>
                <span id="nameerror">${requestScope.nameerror}</span></div>
            <div><label>Password:<input type="password" name="password" onkeydown="hideError(this)"></label>
                <span id="passworderror">${requestScope.passworderror}</span></div>
            <div><label>Password one more time: <input type="password" name="password2" onkeydown="hideError(this)"></label>
                <span id="password2error" hidden>Passwords must be the same.</span></div>
            <input type="submit" value="Register" />
        </form>
        <a href="${pageContext.servletContext.contextPath}">Login</a>
    </body>
</html>
