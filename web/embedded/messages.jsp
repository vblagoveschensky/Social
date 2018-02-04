<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/jspf/internationalization.jspf" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Messages</title>
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/style.css">
    </head>
    <body>
        <table class="embedded">
            <thead>
                <tr>
                    <th><s:t m="when" />:</th>
                    <c:if test="${param.box ne 'sentMessages'}"><th><s:t m="from" />:</th></c:if>
                        <th><s:t m="to" />:</th>
                        <th><s:t m="text" />:</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="message" items="${requestScope.messages}">
                    <tr>
                        <td><fmt:formatDate value="${message.sent}" type="both"/></td>
                        <c:if test="${param.box ne 'outbox'}"><td>${message.sender.name} ${message.sender.login}</td></c:if>
                            <td>
                            <c:forEach var="recipient" items="${message.recipients}">
                                <div>${recipient.name} ${recipient.login}</div>        
                            </c:forEach>
                        </td>
                        <td>${message.text}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="?page=${requestScope.page-1}&folder=${param.folder}">&lt;-</a>
        <a href="?page=${requestScope.page+1}&folder=${param.folder}">-&gt;</a>
    </body>
</html>
