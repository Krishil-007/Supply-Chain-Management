//document.getElementById('Username').innerHTML = "Mr. Bhargav Patel";
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

window.onclick = function (event) {
    if (!event.target.matches('.dropbtn')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}
$(document).ready(function(){
    $(".navinfo .icon-button").click(function(){
        $(".notf_btn").toggleClass("active");
    })
    $(".newItemAddButton").click(function(){
        $(".newItemForm").toggleClass("activeForm");
        $(".newItemTickButton").toggleClass("activeForm");
        
        $(".newItemAddButton").toggleClass("rotate");
    })
});

var Backs = [
    "to right, #c6ffdd, #fbd786, #f7797d",
    "0deg, #FFDEE9 0%, #B5FFFC 100%",
    "45deg, #FA8BFF 0%, #2BD2FF 52%, #2BFF88 90%",
    "160deg, #0093E9 0%, #80D0C7 100%",
    "62deg, #8EC5FC 0%, #E0C3FC 100%",
    "62deg, #FBAB7E 0%, #F7CE68 100%",
    "45deg, #85FFBD 0%, #FFFB7D 100%",
    "0deg, #D9AFD9 0%, #97D9E1 100%",
    "19deg, #3EECAC 0%, #EE74E1 100%",
    "45deg, #FBDA61 0%, #FF5ACD 100%",
    "132deg, #F4D03F 0%, #16A085 100%",
    "90deg, #FEE140 0%, #FA709A 100%" ]