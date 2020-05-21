function showWhatsThisModal() {
    showModal("whatshismodal");
}

function showSettings() {
    showModal("settings");
}

function saveSettings() {
    hideModal("settings");
}

function hideModal(modalId) {
    var modal = document.getElementById(modalId);
    modal.style.display = "none";
}

function select(item) {
    console.log("selected: " + item.text);
    hideModal("settings");
    changeDropDownTextTo(item.text);
    drawMap(item.text);
}

function changeDropDownTextTo(text) {
    var element = document.getElementById('dropbtn');
    element.innerHTML = text;
}

function showModal(modalId) {
    // Get the modal
    var modal = document.getElementById(modalId);
    modal.style.display = "block";
    // Get the <span> element that closes the modal
    var span = modal.getElementsByClassName("close")[0];

    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
      modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
      if (event.target == modal) {
        modal.style.display = "none";
      }
    }
}
