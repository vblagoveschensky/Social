<!DOCTYPE html>
<html>
    <head>
        <title>Unregister</title>
        <script src="${pageContext.servletContext.contextPath}/formvalidation.js"></script>
    </head>
    <body>
        <h1>Unregister</h1>
        <form method="POST">
            <div><label>Password:<input type="password" name="password" onkeydown="hideError(this)"></label>
                <span id="passworderror">${requestScope.passworderror}</span></div>
            <input type="submit" value="Unregister" />
        </form>
    </body>
</html>
