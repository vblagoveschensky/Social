<%@ include file="/WEB-INF/jspf/internationalization.jspf" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:t m="login.title" />!</title>
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/header.jspf" %>
        <h1><s:t m="login.title" />!</h1>
        <s:t m="${sessionScope.loginerror}" />
        <form method="POST" action="j_security_check">
            <div><label><s:t m="person.login" />: <input type="text" name="j_username" value="${sessionScope.login}"></label></div>
            <div><label><s:t m="person.password" />: <input type="password" name="j_password"></label></div>
            <input type="submit" value="<s:t m="login" />" />
        </form>
        <a href="${pageContext.servletContext.contextPath}/registration"><s:t m="registration" /></a>
    </body>
</html>