function addRecipient(id, login, name) {
    var item = document.createElement("li");
    item.id = id;
    item.innerHTML = name + " " + login;
    var input = document.createElement("input");
    input.name = "recipients";
    input.type = "hidden";
    input.value = id;
    var removehref = document.createElement("a");
    removehref.href = "#remove";
    removehref.onclick = function () {
        removeRecipient(this.parentElement.id);
        return false;
    };
    removehref.innerHTML = "[x]";
    document.getElementById("recipients").appendChild(item);
    item.appendChild(input);
    item.appendChild(removehref);
}

function removeRecipient(id) {
    document.getElementById("recipients").removeChild(document.getElementById(id));
    window.frames["contacts"].undo(id);
}

function inRecipients(id) {
    return document.getElementById(id) !== null;
}