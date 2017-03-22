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
		<title>Weding</title>	
		
		<!-- CSS -->
		<link rel="stylesheet" href="resources/default/css/defaultall_weding.css" />

		<script type="text/javascript" src="resources/default/js/jquery-1.11.2.js"></script>
		<script type="text/javascript" src="resources/default/js/jquery-ui.js"></script>
<!-- 		<script type="text/javascript" src="resources/default/js/default.js"></script> -->
		
		<!-- default -->
		<script type="text/javascript" src="resources/common/col.js"></script> 
        <script type="text/javascript" src="resources/common/common_tablet.js"></script> 

		<!-- weding -->
		<link rel="stylesheet" type="text/css" href="resources/weding/css/style.css">
	</head>
	
	<body>
		
		<main style="padding:0;">
			
			<section class="wr_ticket">
		    	
		    	<div class="wbox top">
		        	<span class="circle"></span>
		        	<div class="stroke">
		            	<span class="circle"></span>
		                <h2 id="txtStoreName"></h2>
		                <p>오늘 총 <span id="txtTotalWaitCnt"></span>팀 기다림</p>
		                <div class="bnwrap">
		                    <a href="#" class="sbn" id="btnStoreInfo"><i class="sinfo"></i>맛집정보</a>
		                    <a href="#" class="sbn" id="btnStoreCall"><i class="stel"></i>통화하기</a>
		                </div>
		            </div>
		        </div>
		        
		        <div class="wbox mywait">
		        	<div class="stroke">
		            	<hr>
		            	<p class="userinfo">
		                	<strong class="name" id="txtUserName"></strong>
		                    <span id="txtWait"></span>
		                    <a href="#" class="refresh" id="btnRefresh"><i class="fa fa-refresh"></i></a>
		                </p>
		                <div class="wait_info">
		                	<p class="group"><strong id="txtWaitCnt"></strong>팀<span>대기중</span></p>
		                    <p class="time"><strong>-</strong>분<span>소요예정</span></p>
		                </div>
		            	<ul class="note">
		                    <li>중복예약은 불가하오니 취소 후 예약해주세요.</li>
		                    <li>미루기는 1회만 가능하오니 신중히 해주세요.</li>
		                    <li>오지 않을 때는 다른 이를 배려하는 마음으로 대기표를 취소하는 아름다운 위딩 유저가 되어주세요.</li>
		                </ul>
		            </div>
		        </div>
		        
		    </section>
		    
		    <p class="btnwrap">
		    	<span><a id="btnNext" href="#" class="btn">미루기</a></span>
		        <span><a id="btnCancel" href="#" class="btn cancel">취소하기</a></span>
		    </p>
		    
		    <section class="app">
		    	<p class="txt">위딩앱을 설치 하시면<br>예상 대기 시간을 확인 하실 수 있습니다.</p>
		        <p class="btnwrap">
		            <span><a href="https://play.google.com/store/apps/details?id=com.weding.weding&hl=ko" class="bn roid" target="_blank"><i class="fa fa-android"></i> 안드로이드 사용자</a></span>
		            <span><a href="https://itunes.apple.com/us/app/widing/id1090305806?l=ko&ls=1&mt=8" class="bn apl" target="_blank"><i class="fa fa-apple"></i> 아이폰 사용자</a></span>
		        </p>
		    </section>
			
		      
		</main>
				
	
	</body>
	
	
	
	
	
	<!-- 알림 Modal -->
		<div id="alAlertBg" class="showno modalwrap" style="z-index:201"></div>
		<div id="alAlert" class="showno modal" style="width:300px; z-index:202">
			<div class="header" style="border-bottom:none;">
<!-- 		    	<h2>정보</h2> -->
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
		
</html>

<script type="text/javascript">


/**=============================================================
* Remote
*/
/**
 *  
 */
 var i = '${i}';

 
function fnRemote(){
	
// 	com.showLoading(true);
	
	App.ro.init();
	App.ro.url = 'data/getTicketInfo';
	App.ro.param = {'i':i};
	App.ro.execute(function(data) {
		
		if(data.result){
			$('#txtStoreName').text(data.storeName);
			$('#txtTotalWaitCnt').text(data.totalWaitCnt);
			$('#txtUserName').text(data.userName);
			$('#txtWait').text(data.wait + "명 예약");
			$('#txtWaitCnt').text(data.waitCnt);
		} else {
			location.replace("/tnone");
		}
		
		
// 		com.showLoading(false);
	});
	
	
};



/**=============================================================
* Event Listener
*/
var fnEventListener = function(){
	//어플유도 팝업창
	$("#btnNext,#btnCancel,#btnStoreInfo,#btnStoreCall").click(function(){
		com.alert.ok('앱을 통해서 제공되는 기능 입니다.', true, function(){
		});
	});
	
	//새로고침 클릭
	$("#btnRefresh").click(function(){
		fnRemote();
	});

	
};

/**=============================================================
 * 프로그램시작
 */
function createComp(){
	
	fnEventListener();
	
	if(i == null || i == ''){
		alert('잘못된 접근입니다. 로그인 페이지로 이동합니다.');
		location.replace("/index");  
	} else {
		fnRemote();
	}
	
	
	
};

$(document).ready(function() {
	createComp();
});




//-------------------------------------------------------------


</script>


  