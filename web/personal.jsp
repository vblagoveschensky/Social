<!DOCTYPE html>
<html>
    <head>
        <title>Personal page</title>
        <script src="formvalidation.js"></script>
    </head>

    <body>
        <%@ include file="/WEB-INF/jspf/header.jspf" %>
        <h1>My page</h1>
        <div>Login: ${requestScope.user.login}</div>
        <div>Name: ${requestScope.user.name}</div>
        <h2>Change name</h2>
        <form method="POST">
            <div><label>New name:<input type="text" name="name" value="${param.name}" onkeydown="hideError(this)"></label>
                <span id="nameerror">${requestScope.nameerror}</span></div>
            <input type="submit" value="Change" />
        </form>
        <h2>Change password</h2>
        <form method="POST" onsubmit="return validate(this)">
            <div><label>Old password:<input type="password" name="oldpassword" onkeydown="hideError(this)"></label>
                <span id="oldpassworderror">${requestScope.oldpassworderror}</span></div>
            <div><label>Password:<input type="password" name="password" onkeydown="hideError(this)"></label>
                <span id="passworderror">${requestScope.passworderror}</span></div>
            <div><label>Password one more time: <input type="password" name="password2" onkeydown="hideError(this)"></label>
                <span id="password2error" hidden="hidden">Passwords must be the same.</span></div>
            <input type="submit" value="Change" />
        </form>
    </body>
</html>
