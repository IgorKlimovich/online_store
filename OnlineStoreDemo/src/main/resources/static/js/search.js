const input = document.getElementById('input')
const browsers = document.getElementById("category")
for (let option of browsers.options) {
    option.onfocus = function () {
        input.value = option.value;
        browsers.style.display = 'none';
        input.style.borderRadius = "5px";
    }
}
