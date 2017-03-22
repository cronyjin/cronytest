<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>
<% 
response.setHeader("cache-control","no-cache"); 
response.setHeader("expires","0"); 
response.setHeader("pragma","no-cache"); 
%>

<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
		<meta http-equiv="Cache-Control" content="no-cache"/>
		<meta http-equiv="Expires" content="0"/>
		<meta http-equiv="Pragma" content="no-cache"/>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>웨이팅-관리자</title>
        
		<!-- CSS -->
		<link rel="stylesheet" href="resources/default/css/defaultall.css" />
		<link rel="stylesheet" href="resources/default/css/ui.easytree.css" />
		<link rel="stylesheet" href="resources/default/css/gantt.css" />
		<link rel="stylesheet" href="resources/default/css/colpick.css" />
		<link rel="stylesheet" href="resources/nbpms/css/nbpms.css" />

		<script type="text/javascript" src="resources/default/js/jquery-1.11.2.js"></script>
		<script type="text/javascript" src="resources/default/js/jquery-ui.js"></script>
		<script type="text/javascript" src="resources/default/js/jquery.number.min.js"></script>
		<script type="text/javascript" src="resources/default/js/default.js"></script>
		
		<!-- 
		<script type="text/javascript" src="resources/default/js/Chart.js"></script>
		<script type="text/javascript" src="resources/default/js/jquery.tablesorter.js"></script>
		 -->
		
		<script type="text/javascript" src="resources/default/js/jquery.treetable.js"></script>
		<script type="text/javascript" src="resources/default/js/jquery.easytree.min.js"></script>
		
		<!-- 대한I&S -->
		<script type="text/javascript" src="resources/common/col.js"></script> 
        <script type="text/javascript" src="resources/common/common_wating.js"></script> 
        
    </head>
    
	<body class="black" data-lang="BLACK">
		<!-- 메뉴탭과 즐겨찾기탭 중에 어떤탭을 선택했는지 알기 위함  -->
    	<span id="mainSelMenuTab" style="display:none"><%=request.getParameter("menuTab")%></span> 
    	
    	<!-- 선택한 메뉴  -->
    	<span id="mainSelMenuUrl" style="display:none">${ssMenuUrl}</span> 
		
		<iframe id="mainIframe" class="blind" name="mainIframeTarget"  ></iframe>
		<form id="mainForm" ></form>
		
		
		<tiles:insertAttribute name="top"/>

		<div id="container">
	    
		    <tiles:insertAttribute name="left" />

		    <tiles:insertAttribute name="body" />

		</div>

	    <!-- 업무 팝업 -->
		<div id="alBizBg" class="showno modalwrap" ></div>
	    <div id="alBiz" class="showno" >
	    </div>

    	<!-- 알림 Modal -->
		<div id="alAlertBg" class="showno modalwrap" style="z-index:201"></div>
		<div id="alAlert" class="showno modal" style="width:300px; z-index:202">
			<div class="header">
		    	<h2>정보</h2>
		        <a id="alAlertClose"  href="#" class="mclose"><i class="fa fa-close"></i></a>
		    </div>
		    <div class="mbody">
		    	<div class="icontext">
		            <span class="icon ok"><i class="fa fa-exclamation-triangle"></i></span>
		            <p id="alAlertText" class="text"></p>
		        </div>
		    </div>
		    <div class="bottom">
		        <button id="alAlertOk" type="button" class="btn btnpoint">확인</button>
		    </div>
		</div>
		<!--/알림 Modal-->
		
    	<!-- 컨펌 Modal -->
		<div id="alConfirmBg" class="showno modalwrap" style="z-index:201"></div>
		<div id="alConfirm" class="showno modal" style="width:300px; z-index:202">
			<div class="header">
		    	<h2>확인</h2>
		        <a id="alConfirmClose" href="#" class="mclose"><i class="fa fa-close"></i></a>
		    </div>
		    <div class="mbody">
		    	<div class="icontext">
		            <span class="icon confirm"><i class="fa fa-exclamation-triangle"></i></span>
		            <p id="alConfirmText" class="text"></p>
		        </div>
		    </div>
		    <div class="bottom">
		        <button id="alConfirmOk" 		type="button" class="btn btnpoint">확인</button>
		        <button id="alConfirmCancel" 	type="button" class="btn">취소</button>
		    </div>
		</div>		
		<!--/컨펌 Modal-->

    	<!-- 알림 Modal -->
		<div id="alAlertAutoBg" class="showno modalwrap" style="z-index:201"></div>
		<div id="alAlertAuto" class="showno modal" style="width:300px; z-index:202">
			<div class="header">
		    	<h2>정보</h2>
		    </div>
		    <div class="mbody">
		    	<div class="icontext">
		            <span class="icon ok"><i class="fa fa-exclamation-triangle"></i></span>
		            <p id="alAlertAutoText" class="text"></p>
		        </div>
		    </div>
		</div>
		<!--/알림 Modal-->

		<!-- 로딩 -->
		<div id="comLoadingBg" class="showno modalwrap" style="z-index:301"></div>
		<div id="comLoading" class="showno modal loding" style="width:270px; margin-left:-170px; z-index:302">
			<div class="header">
		    	<h2>DATA LODING...</h2>
		        <a href="#" class="mclose"><i class="fa fa-close"></i></a>
		    </div>
		    <div>
		    	<img src="resources/default/img/loadingBar.gif" alt="데이터로딩">
		    </div>
		</div>
		<!--/로딩 -->

		<script id="pageTmpl" type="text/x-dot-template">
               	<span>( <strong>{{=it.currStart}}-{{=it.currEnd}}</strong> / {{=it.totalRecords}} )</span>
				<ul>
					{{? it.buttonPn === 'Y'}}
					<li><a href="#none" class="prev pager" page='p'><i class="fa fa-caret-left"></i></a></li>
					{{?}}
					{{~it.pages:value:index}}
					<li {{? it.selected=== value}}class="active"{{?}}><a href="#none" class="pager" page='{{=value}}'>{{=value}}</a></li> 
					{{~}}
					{{? it.buttonPn === 'Y'}}
					<li><a href="#none" class="next pager" page='n'><i class="fa fa-caret-right"></i></a></li>
					{{?}}
				</ul>
		</script>
		
		<script id="fileInputTmpl" type="text/x-dot-template">
			<a href="#" name="fileDeleteMark" class="iconbn" style="display:none" ><i class="fa fa-remove"></i></a>
			<input type="file" class="blind" name="fileData" style="display:none">
			<input type="text" name="fileName" title="파일명" readonly style="width:30%;">
			<button  class="btn btnicon" name="fileSelectBtn" style="display:none"><i class="fa fa-upload"></i></button>
			<button  class="btn btnicon" name="fileDeleteBtn" style="display:none"><i class="fa fa-remove"></i></button>
			<button  class="btn btnicon" name="fileCancelBtn" style="display:none"><i class="fa fa-undo"></i></button>
			<button  class="btn btnicon" name="fileDownloadBtn" style="display:none"><i class="fa fa-download"></i></button>
		</script>
		
	</body>
	
</html>


