<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>

<!--#header-->
<div id="header">
	<a href="#" class="nav_bn"><i class="self_nav_close"></i></a>
    <a href="#" class="logo" style="margin-top:5px;">
<!--     	<img id="mainTopLogoKo" src="resources/weding/img/logo.png" style="width:290px;"		class="" alt='웨이팅로고'> -->
<%--     	<img id="mainTopLogoCn" src="resources/nbpms/img/logo_ch.png"	class="showno" alt='<spring:message code="nongshim"/>'> --%>
    </a>
    <div class="setarea">
    	<span class="username"><strong id="mainTopUser"></strong></span>
    	<a id="mainTopLogout" href="#" class="log" title='로그아웃'><i class="fa fa-power-off"></i></a>
        <span class="select-mask"></span>
        <div class="setup" title='설정'>
            <p><i class="fa fa-gear"></i> <span><i class="fa fa-chevron-down"></i></span></p>
            <ul style="display:none" >
<!-- 	            <li><a href="muMainPerInfo" >개인정보수정</a></li> -->
	            <li class="selectcolor"><p>색상선택</p>
	                <span class="color_grey"></span>
	                <span class="color_black"></span>
	                <span class="color_brown"></span>
	                <span class="color_blue"></span>
	                <span class="color_green"></span>
	            </li>
			</ul>
        </div>
    </div>
</div>
<!--/#header-->
