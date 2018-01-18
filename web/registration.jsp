<%@ include file="/WEB-INF/jspf/internationalization.jspf" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:t m="registration" /></title>
        <script src="formvalidation.js"></script>
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/header.jspf" %>
        <h1><s:t m="registration" /></h1>
        <form method="POST" onsubmit="return validate(this)">
            <div><label><s:t m="person.login" />: <input type="text" name="login" value="${param.login}" onkeydown="hideError(this)"></label>
                <span id="loginerror"><s:t m="${requestScope.loginerror}" /></span></div>
            <div><label><s:t m="person.name" />: <input type="text" name="name" value="${param.name}" onkeydown="hideError(this)"></label>
                <span id="nameerror"><s:t m="${requestScope.nameerror}" /></span></div>
            <div><label><s:t m="person.password" />: <input type="password" name="password" onkeydown="hideError(this)"></label>
                <span id="passworderror"><s:t m="${requestScope.passworderror}" /></span></div>
            <div><label><s:t m="person.password" /> <s:t m="forms.onemore" />: <input type="password" name="password2" onkeydown="hideError(this)"></label>
                <span id="password2error" hidden><s:t m="forms.password.differs" /></span></div>
            <input type="submit" value="<s:t m="registration" />" />
        </form>
        <a href="${pageContext.servletContext.contextPath}"><s:t m="login" /></a>
    </body>
</html>
