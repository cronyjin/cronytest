// Scroll Move
function scrollMove(t,h,m){
	"use strict";
	if(h==undefined) h=0;
	if(m!=undefined && jQuery(window).width()<993) h=m;
		var o = jQuery('body');
	if(navigator.userAgent.toLowerCase().match(/trident/i)){
		o = jQuery('html');
	}
	o.animate({
		scrollTop:jQuery(t).offset().top-h
	},500);
}

// Menu Open
function menuOpen(o){
	"use strict";
	$('#wrap').after('<button type="button" id="sidebar_tg" onclick="menuClose();"><b class="sr-only">Close</b></button>');
	var a = -$(window).scrollTop();
	$('#'+o).show(0,function(){
		$('#sidebar_tg').addClass('in');
		$('body').addClass('nav_open '+o+'_open');
		$('#wrap').addClass('if_m').css('top',a);
	});
}

// Menu Close
function menuClose(o){
	"use strict";
	$('#sidebar_tg').removeClass('in');
	$('body').removeClass('snb_open');
	var originScroll = -$('#wrap').position().top;
	setTimeout(function(){
		$('div.side_nav').hide();
		$('body').removeClass('nav_open');
		$(window).scrollTop(originScroll);
		$('#wrap').removeClass('if_m').removeAttr('style');
		$('#sidebar_tg').remove();
	},500);
}

jQuery(function($){
	"use strict";
	var w = $(window);
	var $body = $('body');
});