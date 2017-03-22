/**=============================================================
 * custom ui 공통선언
 */

comUi = {};


/**=============================================================
* sort order (정렬방식) dropdown
*/
comUi.sortOrder = function(){
	
	$(".utilarea").each(function(ii, vv){
		var main = $(this);
		var tar1 = $(this).find(".setup div");
		var tar2 = $(this).find(".select-mask");
		$(this).find(".setup p, .select-mask, .setup .btn").click(function(){
			tar1.slideToggle('fast');
			tar2.toggle();
			// 다른 객체는 닫히게 한다.
			$(".utilarea").not(main).find(".select-mas, .setup div").hide();
		});
	});
};
	
/**=============================================================
* tab 클릭 이벤트
* @param {
* 	selector 		: target
* 	callbackfn 		: click 되었을때 실행되는 평션
* }  
* 
*/
comUi.tabClickRegist = function(param){
	
	var selector 		= param['selector'];
	var callbackfn		= param['callbackfn'];
	
	$(selector).click(function(){
		
		$(selector).removeClass("active");
		$(this).addClass("active");
		
		var sid = $(this).attr('target-id');
		$('#' + sid).show();
		
		$(this).siblings().each(function(){
			var nid = '#' + $(this).attr('target-id');
			$(nid).hide();
		});
		
		if(callbackfn != undefined){
			callbackfn(sid);
		}
	});
	
};


/**=============================================================
* 하이차트 설정
*/
comUi.highChartInit = function(type){
	
	if(type){
		if ( $(document).find('.id-isChart').length == 0  ) return;
	}
	
	Highcharts.setOptions({
	  global: {
			useUTC: false
	  },
	  chart: {
	    backgroundColor: null
	  },
	  lang: {
	    months: ['1월', '2월', '3월', '4월', '5월', '6월',  '7월', '8월', '9월', '10월', '11월', '12월'],
	    weekdays: ['일', '월', '화', '수', '목', '금', '토'],
	    shortMonths: ['1월', '2월', '3월', '4월', '5월', '6월',  '7월', '8월', '9월', '10월', '11월', '12월'],
	    printChart:'다운로드 차트',
	    //contextButtonTitle:'',
	    downloadPNG: 'PNG 파일로 저장',
	    downloadJPEG: 'JPEG 파일로 저장',
	    downloadPDF: 'PDF 파일로 저장',
	    downloadSVG: 'SVG 파일로 저장',
	    exportButtonTitle: '차트 내보내기',
	    loading: 'No Data',   // '로딩중...'
	    printButtonTitle: '인쇄',
	    resetZoom: '확대 초기화',
	    resetZoomTitle: '1:1',
	    thousandsSep: ',',
	    decimalPoint: '.',
	    rangeSelectorFrom: '시작 날짜',
	    rangeSelectorTo: '끝 날짜',
	    rangeSelectorZoom : ''/*'확대/축소'*/
	  },
      loading: {
          style: {
              backgroundColor: 'gray'
          },
          labelStyle: {
              'color': 'white',
          }
      },
	  title : {
	    text : '',
	    style: {
	        display: 'none'
	    }
	  },
//	  colors: ['#d21e1f', '#ff7200', '#4cc411'],
	  xAxis: {
	    title:{text:''},
        tickColor: 'gray',
        tickLength: 5,
        tickWidth: 1,
        tickPosition: 'inside'
	  },
	  yAxis: {
	    title:{text:''}
	  },
	  tooltip:{
	    dateTimeLabelFormats:{
	      millisecond:"%A, %b %e, %H:%M:%S.%L",
	      second:"%A, %b %e, %H:%M:%S",
	      minute:"%A, %b %e, %H:%M",
	      hour:"%A, %b %e, %H:%M",
	      day:"%A, %b %e, %Y",
	      week:"Week from %A, %b %e, %Y",
	      month:"%B %Y",
	      year:"%Y"
	    },
	    xDateFormat: '%Y.%m.%d'
	  }
	  
	});
	
};


/**=============================================================
 * 브라우저 Resize
 * @param {
 * 	targetClass: target class
 * 	subtractClass: 차감할 class ['', '']
 * 	margin: ex: 20 // 여유분
 * 	isEvent: 이벤트
 * }
 */
comUi.resizeDisplay = function(param){
	
	var targetClass 	= param['targetClass'];
	var subtractClass 	= param['subtractClass'];
	var margin		 	= param['margin'];
	var isEvent 		= param['isEvent'];
	
	var inresize = function(it, is){
        var h = $(window).height() - $('#header').height();
        var s = 0;
        for (var idx = 0; idx < is.length; idx++) {
        	s = $('.' + is[idx]).height();
        	if(s == undefined || isNaN(s)){
        		s = 0;
        	} else {
        		s = s + parseInt( $('.' + is[idx]).css('padding-top') );
        		s = s + parseInt( $('.' + is[idx]).css('padding-bottom') );
        		s = s + margin; // 여유분
        	}
			h = h - s;
		}
    	$('.' + it).css('height', h);
	};
	
	inresize(targetClass, subtractClass);
	
	if(!isEvent) return;
	
	$(window).resize(function(){
		inresize(targetClass, subtractClass);
    });	
	
};


/**=============================================================
 * 상단 목록, 입력, 차트 선택버튼 
 * selector : jquery 객체 or 'id'
 */
comUi.changeDivButton = function(selector){
	
	var target  = ( selector instanceof String ) ? $(selector) : selector;
	target.siblings().removeClass("active");
	target.addClass("active");
	
	var id 	= '#' + target.attr('target-id');
	$(id).show();
	
	var tid = '';
	target.siblings().each(function(){
		tid = '#' + $(this).attr('target-id');
		$(tid).hide();
	});

};


/**=============================================================
* 실시간 폴링 버튼
* @param {
* 	selector 		: target
* 	onFunction 		: on 되었을때 실행되는 평션
* 	offFunction 	: off 되었을때 실행되는 평션 
* }  
*/
comUi.realTimeButton = function(param){
	
	var selector 		= param['selector'];
	var onFunction		= param['onFunction'];
	var offFunction 	= param['offFunction'];
	
	// event regist
	selector  = ( selector instanceof String ) ? $(selector) : selector;
	$(selector).click(function(){
		if( $(this).find('.on').is(':visible') ){
			$(this).find('.on').hide();
			$(this).find('.off').show('slow');
			if(offFunction != undefined){
				offFunction();
			}
		} else {
			$(this).find('.on').show('slow');
			$(this).find('.off').hide();
			if(onFunction != undefined){
				onFunction();
			}
		}
	});
	
	// 현재 상태가 on 이면 처음 한번은 실행한다.
	if( $(selector).find('.on').is(':visible') ){
		if(onFunction != undefined){
			onFunction();
		}
	}
	
};

/**=============================================================
 * colorPicker 생성
 * @param: input selector  
 */
comUi.colorPickerSet = function(selector){       
	
 	$(selector).colpick({
		layout:'hex',
		submit:0,
		colorScheme:'dark',
		onChange:function(hsb,hex,rgb,el,bySetColor) {
			$(el).css('border-color','#'+hex);
			// Fill the text box just if the color was set using the picker, and not the colpickSetColor function.
			if(!bySetColor) $(el).val(hex);
		}
	}).keyup(function(){
		$(this).colpickSetColor(this.value);
	});	 
	
 	$(selector).css('border-right-width', '20px');
};


/**=============================================================
 * leftMenu 
 * @event: 	이벤트등록
 * @open:	메뉴를 수동으로 open , close 할때 사용  (이경우 event 는 false)
 */
comUi.leftMenu = function(event, open){ 
	
	var isOpen = true;
	
	var menuOff = function(){
		
		$(".nav_bn i").removeClass("self_nav_close");
		$(".nav_bn i").addClass("self_nav_open");
		if($("#header").width() <= 1005){
			$("#contents").css("marginLeft","0");
			$(".mainp").css("marginLeft","0");	// pms main화면
			$(".mainf").css("marginLeft","0");	// fems main화면
		}else{
			$("#contents").css("left","0");
			$(".mainp").css("left","0");		// pms main화면
			$(".mainf").css("left","0");		// fems main화면
			$(".page").css("left","0");
		}
		isOpen = false;
	};
	
	var menuOn = function(){
		
		$(".nav_bn i").removeClass("self_nav_open");
		$(".nav_bn i").addClass("self_nav_close");
		$("#contents").removeAttr("style");
		$(".mainp").removeAttr("style");		// pms main화면
		$(".mainf").removeAttr("style");		// fems main화면
		$(".page").removeAttr("style");
		
		isOpen = true;
	};
	
	if(event){
		
		$(".nav_bn").click(function(){
			
			$("#nav").toggle();
			if($(".nav_bn i").hasClass("self_nav_close")){
				console.log( 'menuOff' );			
				menuOff();
			}else{
				console.log( 'menuOn' );	
				menuOn();
			}
		});
		
		$(window).resize(function(){
	        var w = $(window).width();
	        if(!isOpen){
	        	if(w <= 1005){
	        		$("#contents").css("marginLeft","0");
	        	} else {
	        		$("#contents").css("left","0");
	    			$(".page").css("left","0");
	        	}
	        }
	    });	
		
	} else {
		if(open==undefined) return;
		if(open){
			menuOn();
		} else {
			menuOff();
		}
	}
};



/**=============================================================
* ready ....
*/

$(document).ready(function() {	

	//즐겨찾기, 메뉴 선택
	$(".navtab button").click(function(){
		$(".navtab button").removeClass("active");
		$(this).addClass("active");
		
		var sid = $(this).attr('target-id');
		$('#' + sid).show();
		
		$(this).siblings().each(function(){
			var nid = '#' + $(this).attr('target-id');
			$(nid).hide();
		});
	});	
	
	// 네비게이션
	$(".nav2dth li").click(function(){
		
		var num = $(this).parent().attr("class").substr(3,1);
		num = parseInt(num, 10);
		//console.log(num);
		
		$('.nav' + (num+1) + 'dth').hide();
		$('.nav' + (num+1) + 'dth', this).show();
	});
	
	// 상단 header 설정 dropdown
	$("#header .setup ul").hide();
	$("#header .select-mask").hide();
	$("#header .setup p, #header .select-mask").click(function(){
		$("#header .setup ul").slideToggle('fast');
		$("#header .select-mask").toggle();
	});
	$("#header .setup li").click(function(){
		//$(".dropdown p").html($(this).text() + '<span>▼</span>');
		$("#header .setup ul").slideToggle('fast');
		$("#header .select-mask").toggle();
	});
	
	// ------------------------------------------------------------------------------
	// 화면색상 변경 
	var fnChangeColor = function(color){
		$("body").removeAttr("class");
		$("body").addClass(color);
		
		if($("body").data("lang") == "ch") {
			$("body").addClass("ch");
		}
		if(color == "black"){
			if($("body").data("lang") == "ch") {
				$(".logo").html("<img src='resources/weding/img/logo2.png' style=\"width:150px;\" alt='웨이팅'><img src='resources/weding/img/logo2_2.png' style=\"width:50px;\" alt='웨이팅'>");
			} else {
				$(".logo").html("<img src='resources/weding/img/logo2.png' style=\"width:150px;\" alt='웨이팅'><img src='resources/weding/img/logo2_2.png' style=\"width:50px;\" alt='웨이팅'>");
			}
		}else{
			if($("body").data("lang") == "ch") {
				$(".logo").html("<img src='resources/weding/img/logo2.png' style=\"width:150px;\" alt='웨이팅'><img src='resources/weding/img/logo2_2.png' style=\"width:50px;\" alt='웨이팅'>");
			} else {
				$(".logo").html("<img src='resources/weding/img/logo2.png' style=\"width:150px;\" alt='웨이팅'><img src='resources/weding/img/logo2_2.png' style=\"width:50px;\" alt='웨이팅'>");
			}
		}
	};
	
	// 화면색상 가져와서 적용하기
	var lastColor = localStorage.getItem('lastColor');
	if(lastColor != null){
		fnChangeColor(lastColor);
	};
	
	// 화면색상 선택
	$("[class^='color_']").click(function(){
		
		var color = $(this).attr("class").substr(6);
		// 화면색상 저장
		localStorage.setItem('lastColor', color);
		fnChangeColor(color);
	});
	// ------------------------------------------------------------------------------
	
	// etc
	$(".self_message").click(function(){
		$(".messagepop").toggle();
	});
	$(".self_help").click(function(){
		//$(".modalwrap").toggle();
		$(".helpop").toggle();
	});
	
	// datepicker
	$( ".datepicker" ).datepicker({
		monthNames:['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		dayNamesMin: ["日","月","火","水","木","金","土"]
	});
	
	
	// function 으로 지정해서 맨 아래에서 실행할 부분
	comUi.leftMenu(true);
	comUi.sortOrder();
	comUi.highChartInit(true);
});