<%@ taglib prefix="tiles" 	uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
response.setHeader("cache-control","no-cache"); 
response.setHeader("expires","0"); 
response.setHeader("pragma","no-cache"); 
%>

<!DOCTYPE html>
<html>
	<head>
		<link rel="icon" href="data:;base64,=">
		
		<meta charset="utf-8">
		<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Pragma" content="no-cache"> 
		<meta http-equiv="Expires" content="0">    
		<title>wating</title>	
		
		<!-- CSS -->
		<link rel="stylesheet" href="resources/ipad/css/normalize.css">
		<link rel="stylesheet" href="resources/ipad/css/bootstrap.min.css">
		<link rel="stylesheet" href="http://fonts.googleapis.com/earlyaccess/notosanskr.css">
		<link rel="stylesheet" href="resources/ipad/css/common.css">

		
		
		<script type="text/javascript" src="resources/default/js/jquery-1.11.2.js"></script>
		<script type="text/javascript" src="resources/default/js/jquery-ui.js"></script>
<!-- 		<script type="text/javascript" src="resources/default/js/default.js"></script> -->
		
		<!-- default -->
		<script type="text/javascript" src="resources/common/col.js"></script> 
        <script type="text/javascript" src="resources/common/common_tablet.js"></script> 

		<!-- weding -->
		<link rel="stylesheet" href="resources/ipad/css/defaultall_weding_tablet.css" />
		<link rel="stylesheet" type="text/css" href="resources/ipad/css/style.css">
	</head>
	
	<body class="bg_gray">

<!-- wrap.screenID -->
<div id="wrap" class="index">

	<!-- header -->
	<header id="hd">
		<h1><img src="resources/ipad/img/logo.png" alt=""></h1>
	</header>
	<!-- //header -->

	<!-- #container -->
	<div id="container" class="clearfix">

<!-- 페이지 시작-->

<div class="pa20 row">
	<!-- 식당 -->
	<div class="weding col-sm-6 pa10">
		<div class="inner">
			<section class="store">
				<!-- <img class="img-circle img-responsive" id="logo" src="" alt="" style="display:none;">  -->
				<h2 class="h2 font_sp" id="storeName"></h2>
				<p class="supply font_sp"><span id="text1">현재</span> <b id="currWaitCnt"></b><span id="text2">팀 대기 중<span></p>
				<p class="app" id="text3"><i class="icon_app"></i><span>웨이팅앱</span>을 설치하여 대기 예상시간을 확인해보세요.</p>
			</section>
			<section class="menu">
				<h3 class="font_sp">
					<span id="text4">오늘의 대표 메뉴</span>
					<b style="font-weight:500;" id="mainMenu">샐러드 배드 파머스</b>
				</h3>
				<div class="menu_img" id="imgArea">
				</div>
			</section>
		</div>
	</div>
	<!-- 대기표받기 -->
	<div class="weding2 col-sm-6 pa10">
		<div class="inner row">
			<h4 class="mb20" id="text5"><b>대기 인원 과 전화번호를 입력해주세요.</h4>
			
			<section class="pt pa0">
				<section class="awaiter mb5 row">
					<div class="col-sm-3 col-xs-2 text-center pa0" id="minus"><button class="btn btn_control_minus"><i class="glyphicon glyphicon-minus"></i></button></div>
					<div class="col-sm-6 col-xs-8 text-center pa0"><b><strong id="waitCnt"></strong><span id="text6">명</span></b></div>
					<div class="col-sm-3 col-xs-2 text-center pa0" id="plus"><button class="btn btn_control_add"><i class="glyphicon glyphicon-plus"></i></button></div>
				</section>
				<form class="form-inline row text-center">
					<div class="col-sm-4 col-xs-4 text-center">
						<input type="text" class="form-control" maxlength="3" id="number1" disabled style="background-color:white;"><span>-</span>
					</div>
					<div class="col-sm-4 col-xs-4 form-group text-center">
						<input type="text" class="form-control" maxlength="4" id="number2" disabled style="background-color:white;"><span>-</span>
					</div>
					<div class="col-sm-4 col-xs-4 form-group text-center">
						<input type="text" class="form-control" maxlength="4" id="number3" disabled style="background-color:white;">
					</div>
				</form>
				
				
				<ul class="row mt5">
					<li class="col-sm-4 col-xs-4"><button class="btn btn-default id-col" cu-val="1">1</button></li>
					<li class="col-sm-4 col-xs-4"><button class="btn btn-default id-col" cu-val="2">2</button></li>
					<li class="col-sm-4 col-xs-4"><button class="btn btn-default id-col" cu-val="3">3</button></li>
				</ul>
				<ul class="row">
					<li class="col-sm-4 col-xs-4"><button class="btn btn-default id-col" cu-val="4">4</button></li>
					<li class="col-sm-4 col-xs-4"><button class="btn btn-default id-col" cu-val="5">5</button></li>
					<li class="col-sm-4 col-xs-4"><button class="btn btn-default id-col" cu-val="6">6</button></li>
				</ul>
				<ul class="row">
					<li class="col-sm-4 col-xs-4"><button class="btn btn-default id-col" cu-val="7">7</button></li>
					<li class="col-sm-4 col-xs-4"><button class="btn btn-default id-col" cu-val="8">8</button></li>
					<li class="col-sm-4 col-xs-4"><button class="btn btn-default id-col" cu-val="9">9</button></li>
				</ul>
				<ul class="row">
					<li class="col-sm-4 col-xs-4"><button class="btn btn-default" id="del"><i class="icon_back"></i><span class="sr-only">지우기</span></button></li>
					<li class="col-sm-4 col-xs-4"><button class="btn btn-default id-col" cu-val="0">0</button></li>
					<li class="col-sm-4 col-xs-4"><button class="btn btn-default btn_enter" id="setEng" style="background:#a29c98  !important;">English</button></li>
				</ul>
				<button class="btn btn-default btn_enter" id="setTicket" style="width:100%;height:71px;">대기표받기</button>
			</section>
		</div>
	</div>
</div>
<!-- //페이지 끝-->

	</div>
	<!-- //container -->

</div>
<!-- //wrap -->

<!-- js -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
<script src="resources/ipad/js/ipadCommon.js"></script>
</body>
	
	
	
	
	
	<!-- 알림 Modal -->
		<div id="alAlertBg" class="showno modalwrap" style="z-index:201"></div>
		<div id="alAlert" class="showno modal" style="width:300px; z-index:202">
			<div class="header" style="border-bottom:none;">
<!-- 		    	<h2>옵션을 선택해주세요.</h2> -->
		        <a id="alAlertClose"  href="#" class="mclose"><i class="fa fa-close"></i></a>
		    </div>
		    <div class="mbody">
<!-- 		    	<div class="icontext"> -->
<!-- 		            <span class="icon ok"><i class="fa fa-exclamation-triangle"></i></span> -->
<!-- 		            <p id="alAlertText" class="text"></p> -->
<!-- 		        </div> -->
		        <section class="app" style="margin:0px;">
		        <p class="txt"  id="alAlertText"></p>
		        <p class="btnwrap">
		            <span><a href="https://play.google.com/store/apps/details?id=com.weding.weding&hl=ko" class="bn roid" target="_blank"><i class="fa fa-android"></i> 안드로이드 사용자</a></span>
		            <span><a href="https://itunes.apple.com/us/app/widing/id1090305806?l=ko&ls=1&mt=8" class="bn apl" target="_blank"><i class="fa fa-apple"></i> 아이폰 사용자</a></span>
		        </p>
		    	</section>
		    </div>
<!-- 		    <div class="bottom"> -->
<!-- 		        <button id="alAlertOk" type="button" class="btn btnpoint">취소</button> -->
<!-- 		    </div> -->
		</div>
		<!--/알림 Modal-->
		
		<!-- 옵션 Modal -->
		<div id="alAlertOptionBg" class="showno modalwrap" style="z-index:201"></div>
		<div id="alAlertOption" class="showno modal" style="width:400px; z-index:202;height:370px;">
			<div class="header" >
		    	<h2 id="text7">옵션을 선택해주세요.</h2>
		        <a id="alAlertOptionClose"  href="#" class="mclose"><i class="fa fa-close"></i></a>
		    </div>
		    <div class="mbody">
		    	<div id="optionList" class="icontext" style="font-size:1.5em;">
		        </div>
		    </div>
		    <div class="bottom">
		        <button id="alAlertOptionOk" type="button" class="btn btn-default btn_enter"><span id="text8">대기표받기</span></button>
		    </div>
		</div>
		<!--/알림 Modal-->
		
		<!-- 알림 Modal -->
		<div class="showno modalwrap" style="z-index:201;"></div>
		<div class="showno modal" style="width:400px; z-index:202;padding-top:15px;max-height:200px;">
<!-- 			<div class="header"> -->
<!-- 		    	<h2>정보</h2> -->
<!-- 		    </div> -->
		    <div class="mbody">
		    	<div class="icontext">
		            <span class="icon ok"><i class="fa fa-exclamation-triangle"></i></span>
		            <p class="text" style="font-size:17px;width:320px;"></p>
		        </div>
		    </div>
		</div>
		<!--/알림 Modal-->
		
		
		<div id="alAlertAutoBg" class="showno modalwrap" style="z-index:201;"></div>
		<div id="alAlertAuto" class="showno alert qst" style="z-index:202;width:400px;min-height:150px;">
			<p class="txt" id="alAlertAutoText" style="font-size:23px;"></p>
		    <p class="btnwrap">
<!-- 		    	<span><a href="#" class="btn cancel">아니오</a></span> -->
<!-- 		        <span><a href="#" class="btn">예</a></span> -->
		    </p>
		</div>
		
</html>

<script type="text/javascript">


/**=============================================================
* Remote
*/
/**
 *  
 */
 var i = '${i}';
 var _waitCnt = 2;
 var _number = '010';
 var _selOpt = '';
 var _nowLen = 'kr';
 
function fnRemote(){
	
//  	com.showLoading(true);
	
	App.ro.init();
	App.ro.url = 'data/getTabletInfo';
	App.ro.param = {};
	App.ro.execute(function(data) {
		
		$('#storeName').text(data.storeName);
		$('#currWaitCnt').text(data.waitCnt);
// 		$("#imgArea").append("<img src=\"/img/"+data.img+"\" alt=\"\">")
		$("#imgArea").css("background-image", "url(http://wdg.kr/img/"+data.img+")");
		$("#imgArea").css("background-position", "50% 50%");
		$("#mainMenu").text(data.menuName);
		
		if(data.logo != null && data.logo != ''){
			$("#logo").attr('src', '/img/' + data.logo);
			$("#logo").show();
		} else {
			$("#logo").attr('src', '');
			$("#logo").hide();
		}
		
// 		reset();
//  		com.showLoading(false);
	});
	
	
};

//옵션 유무 체크 
function fnCheckOption(){
	
	App.ro.init();
	App.ro.url = 'data/getIsExistOptionByTablet';
	App.ro.param = {'name':_number.substring(7,11)+"님", 'phonNumber':_number, 'wait':_waitCnt};
	App.ro.execute(function(data) {
		
		
		var isResult = data.result;
		
		if(isResult == 'true'){
			//옵션사용 , 옵션선택처리 
			console.log(data);
			$("#optionList").html('');
			_selOpt = "";
			//옵션의 갯수만큼 옵션 처리 
			for(var i in data.optionList){
				var optStr ="";
				if(i==0){
					optStr = '<label><input type="radio" value="'+data['optionList'][i]['seqId']+'" name="option" checked="checked"> '+data['optionList'][i]['optionName']+'</label> <br/>';
				} else {
					optStr = '<label><input type="radio" value="'+data['optionList'][i]['seqId']+'" name="option"> '+data['optionList'][i]['optionName']+'</label> <br/>';
				}
				
				$("#optionList").append(optStr);
			}
			
			com.alert.option("", true, function(){
			});
			$("#alAlertOptionOk").unbind('click touch');
			$('#alAlertOptionOk').on('click touch', function(){
				_selOpt = $('input:radio[name=option]:checked').val();
				fnSetTicket();
				$("#alAlertOption").hide();
				$("#alAlertOptionBg").hide();
			});
		} else { 
			//옵션미사용 바로 발급처리 
			fnSetTicket();
		}
	});
	
}

//대기표 발급처리 
function fnSetTicket(){
	if(_nowLen=='eng'){
		engYn = 'Y';
	} else {
		engYn = 'N';
	}
	App.ro.init();
	App.ro.url = 'data/setTicketByTablet';
	App.ro.param = {'name':_number.substring(7,11)+"님", 'phonNumber':_number, 'wait':_waitCnt, 'option':_selOpt, 'engYn':engYn};
	App.ro.execute(function(data) {
		
		
		var msg = "";
		var isResult = data.result;
		
		if(isResult == 'ture'){
			if(_nowLen == 'kr'){
				msg = '<b>' + data.reason + '</b> <br><br> <p style="font-size:16px;">대기표를 문자로 전송 하였습니다! <br> '+data.callCnt+'팀 남았을 때 다시 문자를 발송 하니 확인해 주세요</p>';
			} else {
				msg = '<b>' + data.reason + '</b> <br><br> <p style="font-size:16px;">Your ticket was issued. <br> A SMS will be sent if you are '+data.callCnt+' order, so please check your mobile</p>';
			}
		} else {
			msg = data.reason;
		}
		com.alert.auto2(msg, true, function(){
			reset();
		});
	});
	
}

function reset(){
	_waitCnt = 2;
	_number = '010';
	_selOpt = '';
	_nowLen = 'kr';
	setNum();
	$("#waitCnt").text(_waitCnt);
	fnChangeLen();
}

function waitCntProc(job){
	if(job == '+'){
		if(_waitCnt != 15){
			_waitCnt = _waitCnt + 1;
		}
	} else {
		if(_waitCnt != 1){
			_waitCnt = _waitCnt - 1;
		}
	}
	
	$("#waitCnt").text(_waitCnt);
}

function numberPorc(num){
	var len = _number.length;
	if(len != 11){
		_number = _number +''+ num;
	}
	
	setNum();
}

function setNum(){
	if(_number != null && _number != ''){
		$("#number1").val(_number.substring(0,3));
		$("#number2").val(_number.substring(3,7));
		$("#number3").val(_number.substring(7,11));
	}
}

//언어변경
function fnChangeLen(){
	if(_nowLen=='eng'){
		$("#text1").text('');
		$("#text2").text('Teams waiting');
		$("#text3").html('<i class="icon_app"></i>Check your estimated waiting time by installing <span>Waiting</span> App in Store');
		$("#text4").text("Today's Menu");
		$("#text5").text('Enter your mobile and number of your team');
		$("#text6").text('people');
		$("#text7").text('Select option');
		$("#setTicket").text('Get ticket');
		$("#text8").text('Get ticket');
		$("#setEng").text('Korean');
	} else {
		$("#text1").text('현재');
		$("#text2").text('팀 대기 중');
		$("#text3").html('<i class="icon_app"></i><span>웨이팅앱</span>을 설치하여 대기 예상시간을 확인해보세요.');
		$("#text4").text('오늘의 대표 메뉴');
		$("#text5").text('대기 인원 과 전화번호를 입력해주세요.');
		$("#text6").text('명');
		$("#text7").text('옵션을 선택해주세요.');
		$("#setTicket").text('대기표 받기');
		$("#text8").text('대기표 받기');
		$("#setEng").text('English');
	}
}


/**=============================================================
* Event Listener
*/
var fnEventListener = function(){
	
	$("#plus").on('click touch', function(){
		waitCntProc('+');
	});
	
	$("#minus").on('click touch', function(){
		waitCntProc('-');
	});
	
	$(".id-col").on('click touch', function(){
		numberPorc($(this).attr('cu-val'));
	});
	
	$("#del").on('click touch', function(){
		
		var len = _number.length;
		
		if(len == 1){
			_number = '';
			$("#number1").val('');
		} else {
			_number = _number.substring(0, (len-1)) ;
		}
		
		setNum();
	});
	
	$("#setTicket").on('click touch', function(){
		
		if(_number.length < 10){
			var msg = '휴대폰 번호가 잘못 되었습니다.';
			if(_nowLen=='eng'){
				msg = 'Wrong mobile number';
			} 
			
			com.alert.auto(msg, true, function(){
			});
			return;
		}
		
		fnCheckOption();
		
	});
	
	$("#setEng").on('click touch', function(){
		
		if(_nowLen == 'kr'){
			_nowLen = 'eng';
			$("#setEng").text('Korean');
		} else {
			_nowLen = 'kr';
			$("#setEng").text('English');
		}
		
		fnChangeLen();
	});
	
	//어플유도 팝업창
// 	$("#btnNext,#btnCancel,#btnStoreInfo,#btnStoreCall").click(function(){
// 		com.alert.ok('앱을 통해서 제공되는 기능 입니다.', true, function(){
// 		});
// 	});
	
	//새로고침 클릭
// 	$("#btnRefresh").click(function(){
// 		fnRemote();
// 	});

	
};

/**=============================================================
 * 프로그램시작
 */
function createComp(){
	
	fnEventListener();
	reset();
	setInterval(function(){ fnRemote(); }, 3000);
};

$(document).ready(function() {
	createComp();
});




//-------------------------------------------------------------


</script>


  