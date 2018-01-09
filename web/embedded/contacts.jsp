<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Contacts</title>
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/style.css">
        <script>
            function updateRecipients(id, add, login, name) {
                if (add) {
                    parent.addRecipient(id, login, name);
                } else {
                    parent.removeRecipient(id);
                }
            }

            function restore() {
                var userCheckboxes = document.getElementsByClassName("usercheckbox");
                for (var i = 0; i < userCheckboxes.length; i++) {
                    userCheckboxes[i].checked = parent.inRecipients(userCheckboxes[i].value);
                }
           }
           
           function undo(id) {
               document.getElementById(id).checked = false;
           }

            document.addEventListener("DOMContentLoaded", restore);
        </script>
    </head>
    <body>
        <form><input type="text" name="search" /><input type="submit" value="Search"/></form>
        <table class="embedded">
            <thead>
                <tr>
                    <th></th>
                    <th>Login</th>
                    <th>Name</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${requestScope.users}">
                    <tr>
                        <td><input type="checkbox" name="recipients" id="${user.id}" class="usercheckbox" value="${user.id}"
                                   onchange="updateRecipients(this.value, this.checked, parentElement.parentElement.children[1].innerHTML, parentElement.parentElement.children[2].innerHTML)" />
                        </td>
                        <td>${user.login}</td>
                        <td>${user.name}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="?page=${requestScope.page-1}&search=${param.search}">&lt;-</a>
        <a href="?page=${requestScope.page+1}&search=${param.search}">-&gt;</a>
    </body>
</html>