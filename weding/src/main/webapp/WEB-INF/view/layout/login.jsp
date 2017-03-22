<%@ taglib prefix="tiles" 	uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Pragma" content="no-cache"> 
		<meta http-equiv="Expires" content="0">    
		<title>웨이팅 관리자 로그인</title>	
		
		<!-- css -->
		<link type="text/css" rel="stylesheet" href="resources/default/css/login.css">
		<link type="text/css" rel="stylesheet" href="resources/nbpms/css/nbpms.css">

	    <!-- script -->
	    <script src="resources/default/js/jquery-1.11.2.js"></script>
	    <script src="resources/default/js/jquery-migrate-1.2.1.min.js"></script>
    		
	</head>
	
	<body>
	
    	<span id="loginMessage" style="display:none">${loginCheck}</span> 

		<form id="frmLogin">
			<input id="frmAdminId" 		type="hidden" name="ssAdminId" /> 
			<input id="frmAdminPass" 	type="hidden" name="ssAdminPass" />
			<input id="frmUserLang" 	type="hidden" name="ssLoginType" />
		</form>

		<div id="loginWrap">
<!-- 			<div class="top"> -->
<!-- 		    </div> -->
			
    		<div class="login">
				<img src="resources/weding/img/logo.png" style="width:290px;"/>
			    <form>
			    	<br/>
			    	<div class="set" style="text-align:center;">
			            <label style="font-size:16px;">[ 웨이팅 관리자 로그인 ] </label>
			        </div>
			        <div class="set" style="text-align:right;margin-top:0px;">
			        	<span style="font-size:1.2em;">ver 0.0.1</span>
			        </div>
			        <div class="set" style="margin-top:0px;">
			        	<!-- 
			            <label>UserID  <spring:message code="user.id"/></label>
			        	 -->
			            <label>Administrator ID</label>
			            <input id="inpId"  type="text" placeholder="UserID" value="">
			        </div>

			        <div class="set">
			        	<!-- 
			            <label>Password</label> <spring:message code="password"/>
			        	 -->
			            <label>Password</label> 
			            <input id="inpPass" type="password" placeholder="Password" value="">
			        </div>

			        <div class="set">
				        <label style="cursor: pointer;"><input type="radio" value="ipad" name="radLang"><img style="width:30px;" src="resources/ipad/img/ticket_history.png"> 티켓발급PAD</label>
						<label style="cursor: pointer;"><input type="radio" value="anal" name="radLang"><img style="width:25px;" src="resources/ipad/img/user_name.png"> 통계/분석</label>
        			</div>
					<br/>
					<label>인가된 사용자만 사용하실 수 있습니다.</label>
					
					
<!-- 					<label>접속에 문제가 발생할 경우 아래 연락처로 문의하여 주시기 바랍니다. 개발담당자 진의현 / 010-2231-3380</label> -->
					
			        <button id="btnLogin" type="button">LOGIN</button>
			        
			       
			        <p style="padding-top:10px;"><label style="color:blue;"><a href="http://175.125.20.238:81"><img style="width:30px;" src="resources/ipad/img/icon_app.png"> 웨이팅관리자 APP 바로가기</a></label></p>
			    </form>
		    </div>

		</div>
	
	</body>
	
</html>

<script type="text/javascript">

/**=============================================================
* 프로그램명: 로그인
*/

/**=============================================================
* etc
*/

function fnTrimProcess(x) {
    return x.replace(/^\s+|\s+$/gm,'');
}

//마지막으로 사용한 (한국어, 중국어) 로컬스토리지 저장
function fnSetStorage(){
	var chkVal = $('input[name=radLang]:checked').val(); 
	localStorage.setItem('lastLang', chkVal);
}
//마지막으로 사용한 언어 checked 처리
function fnSetLastlang(){
	var lastLang = localStorage.getItem('lastLang');
	if(lastLang == null){
		lastLang = $('input[name=radLang]').eq(0).val();
	}
	$('input[name=radLang][value='+lastLang+']').prop('checked', true);
	if($('input[name=radLang]:checked').val()==null){
		$('input[name=radLang]').eq(0).prop('checked', true);
	}
}
/**=============================================================
* Remote
*/

/**
 * login check
 */
function fnRemoteGetLogin() {
	//세션스토리지에 선택한 언어저장
	fnSetStorage();
	
	$('#loginMessage').text('');
	
	$('#frmAdminId').val( $('#inpId').val() );
	$('#frmAdminPass').val( $('#inpPass').val() );

	var lang = $('input[name=radLang]:checked').val();
	$('#frmUserLang').val( lang );
	
	$('#frmLogin').attr('method', 'POST');
	$('#frmLogin').attr('action', 'login');
	
	$('#frmLogin').submit();
};

/**=============================================================
 * Validate
 */
function fnValidate(){

	var result = true;
	var id = $('#inpId').val();
	var ps = $('#inpPass').val();
	id = fnTrimProcess(id);
	ps = fnTrimProcess(ps);
	$('#inpId').val(id);
	$('#inpPass').val(ps);
	
	if(id == null || id == undefined || id == ''){
		alert('ID 를 입력하세요');
		$('#inpId').focus();
		result = false;
		return result;
	} else {
		if( id.length > 20 ){
			alert('ID는 20자를 초과할 수 없습니다.');
			$('#inpId').focus();
			result = false;
			return result;
		}
	}
	
	if(ps == null || ps == undefined || ps == ''){
		alert('Password 를 입력하세요');
		  $('#inpPass').focus();
		result = false;
		return result;
	} else {
		if( ps.length > 20 ){
			alert('Password는 20자를 초과할 수 없습니다.');
			  $('#inpPass').focus();
			result = false;
			return result;
		}
	}
	
	return result;
}

/**=============================================================
* Event Listener
*/
var fnEventListener = function(){
	
	$( "#btnLogin" ).click(function() {
		if(fnValidate()) {
			fnRemoteGetLogin();
		}
	});
	
	$( "#inpId" ).keypress(function( event ) {
		  if ( event.which == 13 ) {
			  $('#inpPass').focus();
			  event.preventDefault();
		  }
	});	
	
	$( "#inpPass" ).keypress(function( event ) {
		if ( event.which == 13 ) {
			if(fnValidate()) {
				fnRemoteGetLogin();
			}
		}
	});	
	
};

/**=============================================================
* 브라우저 check
*/

var fnBrowserCheck = function(){
	
	var bro = $.browser;
	var ver = parseInt($.browser.version);
	var chk = false;
	
	if(bro['safari'] != undefined && bro['safari'] == true){
		chk = true;
	}else if(bro['mozilla'] != undefined && bro['mozilla'] == true){
		chk = true;
	}else if(bro['chrome'] != undefined && bro['chrome'] == true){
		chk = true;
	}else if(bro['Opera'] != undefined && bro['Opera'] == true){
		chk = true;
	}else if(bro['msie'] != undefined && bro['msie'] == true){
		chk = (ver < 10) ? false : true;
	};
	if(!chk){
		var msg = '\n';
		msg 	= '지원하지 않는 브라우저입니다. !!' + '\n\n';
		msg 	+= '[사용가능 브라우저] ' + '\n';
		msg 	+= 'Internet Explorer: 10.0 이상' + '\n';
		msg 	+= 'Chrome' + '\n';
		msg 	+= 'Firefox' + '\n';
		msg 	+= 'Safari' + '\n';
		msg 	+= 'Mozilla' + '\n';
		msg 	+= 'Opera' + '\n';
		alert(msg);
		
		$('#inpId').prop("disabled", true);
		$('#inpPass').prop("disabled", true);
		$('#btnLogin').prop("disabled", true);
	};
	
};

/**=============================================================
 * 프로그램시작
 */
function createComp(){
	
	fnBrowserCheck();
	
	//마지막으로 사용한 언어 checked 처리
	fnSetLastlang();
	
	// 화면에서 사용될 이벤트 등록
	fnEventListener();
	
	// 로그인 결과 메시지
	var meg = $('#loginMessage').text();
	if(meg === 'err'){
		alert('등록하신 ID 혹은 비밀번호가 틀립니다.');
		return;
	};
	
	
	var pz = '<%=request.getParameter("pz")%>';
	var px = '<%=request.getParameter("px")%>';
	var lang = '<%=request.getParameter("lang")%>';
	
	console.log(pz, px);
	
	if(pz!='null'&&px!='null'&&lang!='null'){
		$("#inpId").val(pz);
		$("#inpPass").val(px);
		
		if(lang=='k'){
			$('input[name=radLang]').eq(0).prop('checked', true);
		} else {
			$('input[name=radLang]').eq(1).prop('checked', true);
		}
		
		fnRemoteGetLogin();
	}
	
	
	
};

$(document).ready(function() {
	createComp();
});
//-------------------------------------------------------------


</script>


  