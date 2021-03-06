<%@ include file="/WEB-INF/jspf/internationalization.jspf" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/style.css">
        <title><s:t m="messenger" /></title>
        <script src="${pageContext.servletContext.contextPath}/contacts.js"></script>
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/header.jspf" %>
        <h1><s:t m="messenger" /></h1>
        <h2><s:t m="messenger.inbox" /></h2>
        <iframe class="box" src="${pageContext.servletContext.contextPath}/personal/embedded/messages"></iframe>
        <h2><s:t m="messenger.outbox" /></h2>
        <iframe class="box" src="${pageContext.servletContext.contextPath}/personal/embedded/messages?folder=sentMessages"></iframe>
        <h2><s:t m="messenger.newmessage" /></h2>
        <iframe id="contacts" src="${pageContext.servletContext.contextPath}/personal/embedded/contacts"></iframe>
        <form method="POST">
            <textarea name="text" class="floatblock"><c:if test="${not empty requestScope.senderror}">${param.text}</c:if></textarea>
                <ul id="recipients" class="floatblock">
                <c:forEach var="recipient" items="${requestScope.recipients}">
                    <li id="${recipient.id}">${recipient.name} ${recipient.login}
                        <input name="recipients" type="hidden" value="${recipient.id}" />
                        <a href="#remove" onclick="removeRecipient(this.parentElement.id); return false;">[x]</a>
                    </li>
                </c:forEach>
            </ul>
            <div class="clearfix"></div>
            <input type="submit" value="Send"/>
            <s:t m="${requestScope.senderror}" />
        </form>
    </body>
</html>
