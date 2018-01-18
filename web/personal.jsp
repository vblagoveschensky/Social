<%@ include file="/WEB-INF/jspf/internationalization.jspf" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:t m="personal" /></title>
        <script src="formvalidation.js"></script>
    </head>

    <body>
        <%@ include file="/WEB-INF/jspf/header.jspf" %>
        <h1><s:t m="personal" /></h1>
        <div><s:t m="person.login" />: ${requestScope.user.login}</div>
        <div><s:t m="person.name" />: ${requestScope.user.name}</div>
        <h2><s:t m="change" /> <s:t m="person.name" /></h2>
        <form method="POST">
            <div><label><s:t m="person.name" />:<input type="text" name="name" value="<c:if test="${not empty requestScope.nameerror}">${param.name}</c:if>" onkeydown="hideError(this)"></label>
                <span id="nameerror">${requestScope.nameerror}</span></div>
            <input type="submit" value="<s:t m="change" />" />
        </form>
        <h2><s:t m="change" /> <s:t m="person.password" /></h2>
        <form method="POST" onsubmit="return validate(this)">
            <div><label><s:t m="forms.password.old" />:<input type="password" name="oldpassword" onkeydown="hideError(this)"></label>
                <span id="oldpassworderror">${requestScope.oldpassworderror}</span></div>
            <div><label><s:t m="person.password" />:<input type="password" name="password" onkeydown="hideError(this)"></label>
                <span id="passworderror">${requestScope.passworderror}</span></div>
            <div><label><s:t m="person.password" /> <s:t m="forms.onemore" />: <input type="password" name="password2" onkeydown="hideError(this)"></label>
                <span id="password2error" hidden="hidden"><s:t m="forms.password.differs" /></span></div>
            <input type="submit" value="<s:t m="change" />" />
        </form>
            <a href="${pageContext.servletContext.contextPath}/personal/unregister"><s:t m="unregister" /></a>
    </body>
</html>
