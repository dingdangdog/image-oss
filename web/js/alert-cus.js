function alertSuccess(msg, duration) {
    alertCust(msg, "msg success-msg", duration);
}


function alertError(msg, duration) {
    alertCust(msg, "msg error-msg", duration);
}

function alertCust(msg, className, duration) {
    console.log(msg, className);
    const msgContainer = document.getElementById("msg-container");

    let msgDiv = document.createElement("div");
    msgDiv.innerText = msg;
    msgDiv.className = className;
    msgContainer.appendChild(msgDiv);
    msgContainer.style.display = "block";

    window.setTimeout(function () {
        msgContainer.removeChild(msgDiv);
        if (msgContainer.innerHTML == null || msgContainer.innerHTML.length === 0){
            msgContainer.style.display = "none";
        }
    }, duration)
}
