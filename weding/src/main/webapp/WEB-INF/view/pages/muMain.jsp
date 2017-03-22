<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- @프로그램명: 메인화면  -->

<!-- highcharts -->
<script src="resources/default/js/highstock-2/highstock.js"></script>
<script src="resources/default/js/highstock-2/highcharts-3d.js"></script>
<script src="resources/default/js/highstock-2/adapters/standalone-framework.js"></script>
<script src="resources/default/js/highstock-2/highcharts-more.js"></script>
<script src="resources/default/js/highstock-2/modules/data.js"></script>
<script src="resources/default/js/highstock-2/modules/exporting.js"></script>
<script src="resources/default/js/highstock-2/modules/solid-gauge.src.js"></script>


<!-- 게이지차트 -->
<script type="text/javascript" src="resources/default/js/raphael.2.1.0.min.js"></script>
<script type="text/javascript" src="resources/default/js/justgage.1.0.1.js"></script>

<div id="contents">
	<div class="mainp">
	<div class="title">
        <h2><i class="fa fa-tachometer"></i> 관리자 분석통계
<!--         (<span id="pR1Date"></span>) -->
        </h2>
        <div class="utilarea">
        	<span class="circlebn real">
			       <img src="resources/nbpms/img/realtime_on.gif" alt='실시간' class="on" style="width:18px;">
			       <img src="resources/nbpms/img/realtime_off.gif" alt='' style="width:18px;display:none;" class="off">
			</span>
    	</div>
    </div><!--/.title-->
<!-- 		<div class="title"> -->
<!-- 			<h2><i class="fa fa-tachometer"></i> 설비종합효율</h2> -->
<!-- 		</div> -->
		<!--/.title-->
		<div class="splitwrap btm">
			<div class="effct e01" style="padding-top:0px;height:160px;">
				<div style="position:relative;">
					<div id="gage1" style="position:relative;left:50%;margin-left:-100px;width:200px;"></div>
					<span class="ctgrz" style="position:absolute;left:0;right:0;top:13px;font-size:17px;font-weight:900;">
						금일대기자
					</span>
				</div>
			</div>
			<div class="effct e03" style="padding-top:0px;height:160px;">
				<div style="position:relative;">
					<div id="gage3" style="position:relative;left:50%;margin-left:-100px;width:200px;"></div>
					<span class="ctgrz" style="position:absolute;left:0;right:0;top:13px;font-size:17px;font-weight:900;">
						평균대기시간
					</span>
				</div>
			</div>
			<div class="effct e04" style="padding-top:0px;height:160px;">
				<div style="position:relative;">
					<div id="gage4" style="position:relative;left:50%;margin-left:-100px;width:200px;"></div>
					<span class="ctgrz" style="position:absolute;left:0;right:0;top:13px;font-size:17px;font-weight:900;">
						매장대기자
					</span>
				</div>
			</div>
			<div class="effct e02" style="padding-top:0px;height:160px;">
				<div style="position:relative;">
					<div id="gage2" style="position:relative;left:50%;margin-left:-100px;width:200px;"></div>
					<span class="ctgrz" style="position:absolute;left:0;right:0;top:13px;font-size:17px;font-weight:900;">
						앱이용대기자
					</span>
				</div>
			</div>
		</div>
		<div class="title">
			<h2><i class="self_half_goods"></i> 통계</h2>
		</div>
		<!--/.title-->
		<div class="splitwrap btm" id="divProdArea">
		
			
			<div class="goods cap">
			</div>
		</div>
		<!--/.splitwrap-->
		<div class="title">
			<h2><i class="self_full_goods"></i> 통계</h2>
		</div>
		<!--/.title-->
		<!--테이블-->
		<!--/.table-->
		<!--/.splitwrap-->
<!-- 		<div class="title"> -->
<!--             <h2><i class="icon_mic" style="font-size:1.3em;"></i> 1</h2> -->
<!--         </div>/.title -->
<!--         <div class="notice"> -->
<!--         	<span id="noticeDate"></span> -->
<!--             <a href="muSysNotice?menuTab=A"><p id="noticeArea"></p></a> -->
<!--         </div> -->
	</div> 
	<!--/.mainp-->
</div>
<!--/#contents-->

<!-- ######################################################################## -->
<!-- ############################ view template ############################# -->
<!-- ######################################################################## -->
<!-- 리스트 -->
<script id="listView" type="text/x-dot-template">
    {{~it :value:index}}
	<tr>
				<td class="big">{{=value.item}}</td>
				<td><img src="resources/nbpms/img/icon/{{=value.boundImg}}"></td>
				<td class="big">{{=value.monthTarget}}</td>
				<td class="errc big">{{=value.monthCnt}}</td>
				<td class="big">{{=value.monthRate}}</td>
				<td class="big">{{=value.todayTarget}}</td>
				<td class="errc big">{{=value.todayCnt}}</td>
				<td class="big">{{=value.todayRate}}</td>
	</tr>
	{{~}}    
</script>


<!-- ######################################################################## -->
<!-- ######################### JavaScript Source ############################ -->
<!-- ######################################################################## -->

<script type="text/javascript">


App.vo.comPollingTime 		= parseInt( "${comPollingTime}" );

App.vo.comChartColor1 		= '#' + "${comChartColor1}";
App.vo.comChartColor2 		= '#' + "${comChartColor2}";
App.vo.comChartColor3 		= '#' + "${comChartColor3}";
App.vo.comChartColor4 		= '#' + "${comChartColor4}";
/**=============================================================
 * templete 선언
 */
App.vo.listView = $('#listView').html();

/**
 * =============================================================
 * get
 */
 
 
/**
 * =============================================================
 * Remote
 */
/**
 * 메인화면 조회
 */

function fnRemote(param){
	
// com.showLoading(true);
	
	App.ro.init();
	App.ro.dest = 'src:commonService.getMainInfo';
	App.ro.param = {};
	App.ro.execute(function(data) {
		
		//설비종합효율 set 
		fnSetForm1(data.r1);
		//완제품 SET 
		App.template(App.vo.listView, data.r2, '#tbody1');
		//필터현황 
		fnMakeFilter(data.r3);
		//부품점검현황 
		fnMakeItem(data.r4);
		//공지사항 
		fnDrawNotice( data.r5 );
		
// 		com.showLoading(false);
		
	});
	
};


/**
/**
 * =============================================================
 * set
 */


/**
 * =============================================================
 * Event Listener
 */
function fnEventListener() {

	// 폴링버튼
 	comUi.realTimeButton({
		selector:		'.real'
		, onFunction:	function(){
			if(App.vo.folTimer != undefined) clearInterval(App.vo.folTimer);
			App.vo.folInterval	= App.vo.comPollingTime * 10 * 1000;
			App.vo.folTimer 	= setInterval( fnRemoteReady, App.vo.folInterval );
			}
		, offFunction:	function(){
			if(App.vo.folTimer != undefined) clearInterval(App.vo.folTimer);
		}
	});

};
/**
 * =============================================================
 * 프로그램시작
 */
function createComp(){
	   
	
	// 화면에서 사용될 이벤트 등록
// 	fnEventListener();
	

	// 처음실행
// 	fnRemoteReady();
	
	
// 	fnSetgageChart();
	
};


$(document).ready(function() {
	// 초기실행 (필수)
	com.init(createComp);

});
	//-------------------------------------------------------------
</script>