<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                    <th>When</th>
                    <c:if test="${param.box ne 'outbox'}"><th>From</th></c:if>
                        <th>To</th>
                        <th>Text</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="message" items="${requestScope.messages}">
                    <tr>
                        <td>${message.sent}</td>
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
        <a href="?page=${requestScope.page-1}&box=${param.box}">&lt;-</a>
        <a href="?page=${requestScope.page+1}&box=${param.box}">-&gt;</a>
    </body>
</html>
