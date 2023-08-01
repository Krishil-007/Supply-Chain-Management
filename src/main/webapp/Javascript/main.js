$(window).scroll(function(e){ 
  var $el = $('.categ'); 
  var isPositionFixed = ($el.css('position') == 'fixed');
  if ($(this).scrollTop() > 660){ 
    $el.css({'position': 'fixed', 'top': '0px'}); 
  }
  if ($(this).scrollTop() < 660){
    $el.css({ 'position': 'absolute', 'bottom': '0px', 'top': '' }); 
  }
});

function scrollPage(O)
{
var element = document.getElementById(O.name);
    var headerOffset = 120;
    var elementPosition = element.getBoundingClientRect().top;
    var offsetPosition = elementPosition + window.pageYOffset - headerOffset;
    window.scrollTo({
         top: offsetPosition,
         behavior: "smooth"
    });
}
