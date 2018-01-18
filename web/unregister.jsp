<%@ include file="/WEB-INF/jspf/internationalization.jspf" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:t m="unregister" /></title>
        <script src="${pageContext.servletContext.contextPath}/formvalidation.js"></script>
    </head>
    <body>
        <h1><s:t m="unregister" /></h1>
        <form method="POST">
            <div><label><s:t m="person.password" />:<input type="password" name="password" onkeydown="hideError(this)"></label>
                <span id="passworderror"><s:t m="${requestScope.passworderror}" /></span></div>
            <input type="submit" value="<s:t m="unregister" />" />
        </form>
    </body>
</html>
