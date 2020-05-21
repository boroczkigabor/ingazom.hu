
function select(item) {
    console.log("selected: " + item.text);
    hideModal("settings");
    changeDropDownTextTo(item.text);
    drawMap(item.text);
}

