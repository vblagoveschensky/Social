function validate(form) {
    if (form.password.value === form.password2.value) {
        return true;
    } else {
        document.getElementById("password2error").hidden = false;
        return false;
    }
}

function hideError(input) {
    document.getElementById(input.name + "error").hidden = true;
}