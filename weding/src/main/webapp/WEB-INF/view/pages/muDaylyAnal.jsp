<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- @프로그램명: 생산량 -->

<!-- highcharts -->
<script src="resources/default/js/highstock-2/highstock.js"></script>
<script src="resources/default/js/highstock-2/highcharts-3d.js"></script>
<script src="resources/default/js/highstock-2/adapters/standalone-framework.js"></script>
<script src="resources/default/js/highstock-2/highcharts-more.js"></script>
<script src="resources/default/js/highstock-2/modules/data.js"></script>
<script src="resources/default/js/highstock-2/modules/exporting.js"></script>
<script src="resources/default/js/highstock-2/modules/solid-gauge.src.js"></script>

<div id="contents">
	<div class="pageHeader">
		<div class="titlearea">
			<h1>일별대기자통계</h1>
			<!--타이틀-->
			<div class="pageguide">
				<!--로케이션(breadcrumbs)을 타이틀 옆에 왼쪽 정렬 하시려면 'pageguide' 밖으로 빼시면 되고 오른쪽 정렬은 'pageguide' 안으로 넣으시면 됩니다.-->
				<ul class="breadcrumbs">
					<li><i class="fa fa-home"></i></li>
					<c:forEach items="${ssMenuNavi}" var="code">
					<li>${code.title}</li>
					</c:forEach>
				</ul>
			</div>
			<!--/.pageguide-->
		</div>
		<!-- /.titlearea-->
		<div class="function">
			<div class="bnarea">
				<a href="#" id="btnExcel" class="btn"><i class="fa fa-file-excel-o"></i> 엑셀다운로드</a>
			</div>
			<div class="pagemove">
				<ul class="pagetype">
<!-- 					<li id="btnList"	target-id="divTable"><a href="#" title='목록'><i class="fa fa-bars"></i></a></li> -->
					<li id="btnChart"	target-id="divChart" class="active"><a href="#" title='차트'><i class="fa fa-bar-chart"></i></a></li>
				</ul>
			</div>
		</div>
		<!--/.function-->
	</div>
	<!--/.pageHeader-->
	<div class="page" style="overflow-y: hidden">
		<!--조회영역-->
		<div class="search">
			<form>
				<div id="divSearch" class="par">
					<fieldset class="minset">
						<label class="tlabel" style="">1분이상, 60분 미만의 대기자만 유효</label>
					</fieldset>
<!-- 					<fieldset class="minset"> -->
<!-- 						<label class="tlabel dbl">2</label> -->
<!-- 					</fieldset> -->
				</div>
				<div class="par set">
					<fieldset class="minset">
						<label class="tlabel" style="width: 50px;">조회조건</label>
						<!-- select박스 시/일/월/년 -->
						<select id="selSearchType" col-data="searchDateType" style="width: 100px;">
<!-- 							<option value="H">시간별</option> -->
							<option value="D" selected>일별</option>
<!-- 							<option value="M">월별</option> -->
<!-- 							<option value="Y">년별</option> -->
						</select>
					</fieldset>

					<!--년별-->
					<fieldset id="fidY" class="set" style="display: none">
						<legend class="tlabel dbl">년별</legend>
						<select id="selY1" col-data="selY1" style="width: 120px;"></select>
						<span class="text">&nbsp;~</span>
						<select id="selY2" col-data="selY2" style="width: 120px;"></select>
					</fieldset>

					<!--월별-->
					<fieldset id="fidM" class="set" style="display: none">
						<legend class="tlabel dbl">월별</legend>
						<select id="selYM1" col-data="selYM1" style="width: 120px;"></select>
						<select id="selYM2" col-data="selYM2" style="width: 80px;"></select>
						<span class="text">&nbsp;월</span>
						<span class="text">&nbsp;~</span>
						<select id="selYM3" col-data="selYM3" style="width: 120px;"></select>
						<select id="selYM4" col-data="selYM4" style="width: 80px;"></select>
						<span class="text">&nbsp;월</span>
					</fieldset>

					<!--일자별-->
					<fieldset id="fidD" class="set" style="display: none">
						<legend class="tlabel dbl">일</legend>
						<span class="ricon_input">
							<input id="inpYMD1" col-data="inpYMD1" type="text" class="datepicker">
							<span><i id="inpYMD1_btn" class="icon_calendar2"></i></span>
						</span>
						<span class="text">&nbsp;~</span>
						<span class="ricon_input">
							<input id="inpYMD2" col-data="inpYMD2" type="text" class="datepicker">
							<span><i id="inpYMD2_btn" class="icon_calendar2"></i></span>
						</span>
					</fieldset>

					<!--시간별-->
					<fieldset id="fidH" class="set" style="display: none">
						<legend class="tlabel dbl">시간</legend>
						<span class="ricon_input">
							<input id="inpYMDH1" col-data="inpYMDH1" type="text" class="datepicker">
							<span><i id="inpYMDH1_btn" class="icon_calendar2"></i></span>
						</span>
						<select id="selYMDH1" col-data="selYMDH1" style="width: 80px;"></select>
						<span class="text">&nbsp;시</span>
						<span class="text">&nbsp;~</span>
						<span class="ricon_input">
							<input id="inpYMDH2" col-data="inpYMDH2" type="text" class="datepicker">
							<span><i id="inpYMDH2_btn" class="icon_calendar2"></i></span>
						</span>
						<select id="selYMDH2" col-data="selYMDH2" style="width: 80px;"></select>
						<span class="text">&nbsp;시</span>
					</fieldset>

				</div>
				<div class="bnarea">
					<button id="btnSearch" type="button" class="btn searchbn">조회</button>
					<button id="btnClean" type="button" class="btn cancelbn">초기화</button>
				</div>
			</form>
		</div>
		<!--//조회영역-->
		
		<!-- 리스트 영역-->
		<div class="typecontents">
		
			<div id="divTable" class="showno">
				<div class="title id-resize-tabledSubtitle">
					<h2 id="excelTitle">
						<i class="fa fa-file-text"></i>
						<em></em><span class="id-labelSearchGoodsType"></span><span class="id-labelSearchType"></span><span class="id-labelSearchTime"></span>
					</h2>
					<div class="utilarea">
						<div id="paginator" class="move"></div>
					</div>
				</div>
				<!--/.title-->
				<!--테이블-->
				<div class="table btmbdr id-resize-tablediv">
					<table class="cusor">
						
						<thead>
							<tr>
								<th width="180px"><span>일자</span></th>
								<th width="150px"><span>방문자</span></th>
								<th width="*"><span>비고</span></th>
							</tr>
						</thead>
						<tbody id="tbody1">
						</tbody>
					</table>
				</div>
			</div>
			<div id="divChart">
				<!--소타이틀-->
				<div class="title id-parentChartHead">
					<h2>
						<i class="fa fa-file-text"></i>
						<em></em><span class="id-labelSearchGoodsType"></span><span class="id-labelSearchType"></span><span class="id-labelSearchTime"></span>
					</h2>
				</div>
				<!--/.title-->
				<!--/소타이틀-->
				<div class="box id-parentChart" >
					<div class="formgroup dotline id-chartCheck">
						<fieldset class="radioch" style="display:inline-block;">
							<legend class="tlabel">방문통계</legend>
							<label class="slabel" style="cursor:pointer;"><input type="checkbox" name="chart_ck" checked="checked" value="A">전체</label>
							<label class="slabel" style="cursor:pointer;"><input type="checkbox" name="chart_ck" checked="checked" value="B">앱이용자</label>
							<label class="slabel" style="cursor:pointer;"><input type="checkbox" name="chart_ck" checked="checked" value="C">매장방문자</label>
						</fieldset>
						<fieldset class="radioch" style="display:inline-block; margin-left:20px;">
							<legend class="tlabel">차트타입</legend>
							<label class="slabel" style="cursor:pointer;"><input type="radio" name="chart_rdo" checked="checked" value="column">막대차트</label>
							<label class="slabel" style="cursor:pointer;"><input type="radio" name="chart_rdo" value="line">라인차트</label>
						</fieldset>
					</div>
					<div class="id-isChart id-chart" id="chart0001" style="height: 200px;width: 100%">
					</div>
				</div>
			</div>
		</div>
		<!--/.table-->
		<!--메세지창-->
		<div class="messagepop" style="display: none;">메시지창입니다.</div>
		<!--/메세지창-->
	</div>
	<!--/.page-->
</div>
<!--/#contents-->

<!-- ######################################################################## -->
<!-- ############################ view template ############################# -->
<!-- ######################################################################## -->

<!-- 코드구분리스트 -->
<script id="listView" type="text/x-dot-template">
    {{~it :value:index}}
	<tr>
		<td class="alnCenter">{{=value.name}}</td>
		<td class="alnCenter">{{=value.item}}</td>
		<td class="alnCenter">{{=value.workgroup}}</td>
		<td class="alnRight">{{=value.totalQtyFormat}}</td>	
		<td class="alnRight"><font color="#0000FF">{{=value.goodQtyFormat}}</font></td>
		<td class="alnRight"><font color="#FF0000">{{=value.badQtyFormat}}</font></td>
		<td class="alnRight"><font color="#0000FF">{{=value.goodQtyPercent}} %</font></td>
		<td class="alnRight"><font color="#FF0000">{{=value.badQtyPercent}} %</font></td>
		<td></td>
	</tr>
	{{~}}    
</script>

<!-- ######################################################################## -->
<!-- ######################### JavaScript Source ############################ -->
<!-- ######################################################################## -->


<script type="text/javascript">

/**============================================================= 
* 선언
*/

var _currStartTime;
var _currEndTime;

App.vo.nowStat	=	'C';	// L:목록 , C:차트

/**=============================================================
* templete 선언
*/
App.vo.listView	= $('#listView').html();

//menuConfig 변수선언
App.vo.comYearSearchRange 	= parseInt( "${comYearSearchRange}" )-1;
App.vo.comDaySearchRange 	= parseInt( "${comDaySearchRange}" ) * -1;
App.vo.comWorkgroupSearchYn = '${comWorkgroupSearchYn}';
App.vo.comDecimalPoint		= parseInt( "${comDecimalPoint}" );
App.vo.comPageRowCnt		= parseInt( "${comPageRowCnt}" );
App.vo.comPageShowCnt		= parseInt( "${comPageShowCnt}" );
App.vo.comChartColor1 		= '#' + "${comChartColor1}";
App.vo.comChartColor2 		= '#' + "${comChartColor2}";
App.vo.comChartColor3 		= '#' + "${comChartColor3}";

/**=============================================================
* 화면의 상태 (View State) 설정 
*/

/**
 * 기본화면
 */
App.viewState.add("default", {
	visibled: 	{ 'true':['#fidH'], 'false': ['#fidD', '#fidM', '#fidY'] },
	}
);

App.viewState.add("selectChange", {
	visibled: 	{ 'false': ['#fidH', '#fidD', '#fidM', '#fidY'] } 
	}, function(){
		var id = '#fid' + $("#selSearchType option:selected").val();
		$(id).show();
	}
);

/**
 * 조회조건 지우기
 */
App.viewState.add("dispClear", {
	clear: 	{ 'target': ['#divSearch'] }
}
);
/**
 * 메뉴권한
 */
// App.viewState.postAdd("menuAuth", {
// 	}
// );



/**=============================================================
* Get Parameter
*/
function fnGetTime(){
	
	var obj = new Object();
	var res = new Object();
	var searchType = $("#selSearchType option:selected").val();
	//조회조건에 따라 (YMDH, YMD, YM, Y) 이분기됨
	obj = App.selector.getParam('#fid'+searchType);
	
	var startTime = '';
	var endTime = '';
	if(searchType=='H'){//시간별
		startTime 		= obj['inpYMDH1'] + obj['selYMDH1'];
		endTime 		= obj['inpYMDH2'] + obj['selYMDH2'];
		_currStartTime	= obj['inpYMDH1'] + ' ' + obj['selYMDH1'] + ':00';
		_currEndTime	= obj['inpYMDH2'] + ' ' + obj['selYMDH2'] + ':00';
	} else if(searchType=='D'){//일별
		startTime 		= obj['inpYMD1'];
		endTime 		= obj['inpYMD2'];
		_currStartTime	= obj['inpYMD1']; 
		_currEndTime	= obj['inpYMD2']; 
	} else if(searchType=='M'){//월별
		startTime 		= obj['selYM1'] + obj['selYM2'];
		endTime 		= obj['selYM3'] + obj['selYM4'];
		_currStartTime	= obj['selYM1'] + '-' + obj['selYM2'];      
		_currEndTime	= obj['selYM3'] + '-' + obj['selYM4']; 
	} else if(searchType=='Y'){//년별
		startTime 		= obj['selY1'];
		endTime 		= obj['selY2'];
		_currStartTime	= obj['selY1'] + '년';
		_currEndTime	= obj['selY2'] + '년';
	} else {//예외상황
		startTime 	= '00000000' + '00' + '00';
		endTime 	= '00000000' + '00' + '00';
	}
	// '-'를 제거
	
	res['startTime'] 	= startTime.replace(/-/gi, "");
	res['endTime'] 		= endTime.replace(/-/gi, "");
	res['searchDateType'] = searchType;
	
	return res;
}

function fnGetParam(){
	
	var obj = fnGetTime();
	var paramValue = App.selector.getParam('#divSearch');
	
	//소숫점 자리 표시	
	paramValue['point'] = App.vo.comDecimalPoint;
	
	var result = $.extend({}, obj, paramValue, App.pager1.getPageParams());
	//  ...........................................
	
	return result;
}


/**=============================================================
* Remote
*/

/**
 * 리스트조회
 */

function fnRemotePaging(){
	
	com.showLoading(true);
	
	App.ro.init();
	App.ro.dest 	= 'map:bpGoodsProd.selectGoodsProd:rp'; 
	App.ro.param 	= fnGetParam();
	App.ro.execute( function(data) {
	
		App.templatePaging(App.vo.listView, data, '#tbody1', App.pager1);
		setSubTitle();
		
		fnDisplayResize(false);
		com.showLoading(false);
		
		if (data.isExist) {
			com.trClickRegist('#tbody1', '', true, 0);
		} else {
		}
		
	});
}

function fnRemoteChartList(){
	
	com.showLoading(true);
	
	App.ro.init();
	App.ro.dest 	= 'map:weAdmin.selectDaylyAnal:rl';
	App.ro.param 	= fnGetParam();
	App.ro.execute( function(data) {
		setSubTitle();
		
		
		if($('input[name=chart_rdo]:checked').val() == 'line'){
			fnDrawChart(data.result);	
		} else {
			fnDrawChartBar(data.result);
		}
		
		if (data.isExist) {
		} else {
		}
		com.showLoading(false);
	});
}

/**=============================================================
 * 차트그리기
 */
function fnDrawChart(param){
	
	var seriesData 	= [];
	var chkVal 		= '';
	
	$('input[name=chart_ck]:checked').each(function(){
		
		chkVal = $(this).val();

		var obj 	= new Object();
		obj['type'] = 'line';
		obj['name'] = $(this).closest('label').text() ;
		obj['turboThreshold'] = 0;
		
		var datas = [];
		for (var idx = 0; idx < param.length; idx++) {
			var objin = new Object();
			objin['name'] 	= param[idx]['name'];
			objin['x'] 		= param[idx]['x'];
			if(chkVal == 'A'){
				objin['y'] 	= param[idx]['y'];
			} else if(chkVal == 'B'){
				objin['y'] 	= param[idx]['q'];
			} else {
				objin['y'] 	= param[idx]['y'];
			}
			datas.push(objin);
		}
		
		obj['data'] = datas;
		seriesData.push( obj );
	});

	$('#chart0001').highcharts({
    	title: {
            text: ''
        },
    	chart: {
    		marginTop: 50,
    		alignTicks: false,
    		events: {
    			load: function(event) {
    				fnChartReSize(false);
    			}
    		},
    	},
		xAxis: {
			type: 'datetime',
            labels: {
                formatter: function() {
                	var stype = $("#selSearchType option:selected").val();
                	if(stype=='H') {
                 		return Highcharts.dateFormat('%m-%d<br/>%H:%M', this.value);
                	} else if(stype=='D'){
                		return Highcharts.dateFormat('%m-%d', this.value);
                	} else if(stype=='M'){
                		return Highcharts.dateFormat('%Y-%m', this.value);
                	} else {
                		return Highcharts.dateFormat('%Y', this.value);
                	}
                }
            }
		},
		yAxis: {        
	    	startOnTick: false,
            title: {
                text: '(EA)'
            },
	    	labels: {
			   x: 0,
			   format: '{value}'
		    }
		},
		tooltip: {
			crosshairs: true,
			shared: true
		},
//         colors: [ App.vo.comChartColor1, App.vo.comChartColor2, App.vo.comChartColor3  ],
        legend: {
            enabled: true
        },
    	plotOptions:{
            series: {
                marker: {
                    enabled: false
                },
            },
    	},
		series : seriesData 

	});
};
/**
 * 막대 차트그리기
 */
function fnDrawChartBar(param){
	
	var seriesData 	= [];
	var chkVal 		= '';
	
	$('input[name=chart_ck]:checked').each(function(){
		
		chkVal = $(this).val();

		var obj 	= new Object();
		obj['type'] = 'column';
		obj['name'] = $(this).closest('label').text() ;
		obj['turboThreshold'] = 0;
		
		var datas = [];
		for (var idx = 0; idx < param.length; idx++) {
			var objin = new Object();
			objin['name'] 	= param[idx]['name'];
			objin['x'] 		= param[idx]['x'];
			if(chkVal == 'A'){
				objin['y'] 	= param[idx]['y'];
			} else if(chkVal == 'B'){
				objin['y'] 	= param[idx]['Q'];
			} else {
				objin['y'] 	= param[idx]['y'];
			}
			datas.push(objin);
		}
		
		obj['data'] = datas;
		seriesData.push( obj );
	});

	$('#chart0001').highcharts({
    	
    	title: {
            text: ''
        },
    	chart: {
    		marginTop: 40,
    		alignTicks: false,
    		events: {
    			load: function(event) {
    			}
    		},
    	},
		xAxis: {
			type: 'datetime',
            labels: {
                formatter: function() {
                	var stype = $("#selSearchType option:selected").val();
                	if(stype=='H') {
                 		return Highcharts.dateFormat('%m-%d<br/>%H:%M', this.value);
                	} else if(stype=='D'){
                		return Highcharts.dateFormat('%m-%d', this.value);
                	} else if(stype=='M'){
                		return Highcharts.dateFormat('%Y-%m', this.value);
                	} else {
                		return Highcharts.dateFormat('%Y', this.value);
                	}
                }
            }
		},
		yAxis: {        
	    	startOnTick: false,
            title: {
                text: '(EA)'
            },
	    	labels: {
			   x: 0,
			   format: '{value}'
		    }
		},
		tooltip: {
			crosshairs: true,
			shared: true
		},
//         colors: [ App.vo.comChartColor1, App.vo.comChartColor2, App.vo.comChartColor3  ],
    	plotOptions:{
    		line:{
    			dataGrouping:{
    				enabled: false
    			}
    		}
    	},
    	series : seriesData,
       legend : {
         	enabled : false
       }
       
    });
};
	
// 조회 조건 : ~~~  헤드라인입력.
function setSubTitle() {
	$('.id-labelSearchGoodsType').text( $('#selWorkgroup option:selected').text()  );
	$('.id-labelSearchType').text($('#selSearchType option:selected').text() + ' : ');
	$('.id-labelSearchTime').text(_currStartTime + ' ~ ' + _currEndTime);
}; 

/**
 * 엑셀다운로드 처리
 */
function fnExcelProcess(){
	
	return;
	//조회조건 문자열 처리 
	var searchTypeString = 	$('#excelTitle').text();


 	//엑셀다운로드 실행 
 	com.util.excelDownloadSubmit('/excel/common', fnGetParam(),{
		title		: '제목',
		sub1		: '일자',
		sub2		: '방문자',
		file		: 'produceReport.xls',
		dest		: 'map:bpGoodsProd.selectGoodsProd',
		searchType 	: searchTypeString
	}); 
}

function fnPluezero(val){
	if(parseInt(val) < 10){
		return '0'+''+val;
	} else {
		return val;
	}
}
/**
 * select tag 초기셋팅
 */
function setSelectTag(){
	var d = new Date();
	App.vo.comDaySearchRange 	= parseInt( "60" ) * -1;
	com.date.toDay = d.getFullYear()+''+fnPluezero((d.getMonth() + 1))+''+fnPluezero(d.getDate());
	console.log(com.date.toDay);
	var hlist = com.util.repeatDataToArray(0, 1, 24, true, {'value':'', 'text':''} );
	com.selectCreateAndClickRegist('#selYMDH1',	hlist, 0);
	com.selectCreateAndClickRegist('#selYMDH2', hlist, Number(com.date.toTime));
	
	var mlist = com.util.repeatDataToArray(1, 1, 12, true, {'value':'', 'text':''} );
	com.selectCreateAndClickRegist('#selYM2', 	mlist, 0);
	com.selectCreateAndClickRegist('#selYM4', 	mlist, Number(com.date.toMonth)-1);
	
	var  count = 0;
	var  range = 0;
	if( com.conf.dispYearRange < 0 ){
		count = -1;
		range = com.conf.dispYearRange * -1;
	} else {
		count = 1;
		range = com.conf.dispYearRange;
	}
	
 	var toyear = parseInt(com.date.toYear);
	var ylist = com.util.repeatDataToArray(toyear, count, range, true, {'value':'', 'text':''} );
	com.selectCreateAndClickRegist('#selYM1', 	ylist, 0);
	com.selectCreateAndClickRegist('#selYM3', 	ylist, 0);
	com.selectCreateAndClickRegist('#selY1', 	ylist, App.vo.comYearSearchRange);
	com.selectCreateAndClickRegist('#selY2', 	ylist, 0);
	
	
	//현재날짜
	var toDay = com.date.toDay;
	//시작 범위일자설정
	var prevDay = com.dateUtil.stringToCount(toDay, App.vo.comDaySearchRange);

 	com.dateCreateAndEventRegist('inpYMDH1', 'inpYMDH1_btn');
	 $('#inpYMDH1').datepicker('setDate', prevDay);
	com.dateCreateAndEventRegist('inpYMDH2', 'inpYMDH2_btn');
	 $('#inpYMDH2').datepicker('setDate', com.util.formatMake(toDay, '####-##-##'));
	com.dateCreateAndEventRegist('inpYMD1', 'inpYMD1_btn');
	 $('#inpYMD1').datepicker('setDate', prevDay);
	com.dateCreateAndEventRegist('inpYMD2', 'inpYMD2_btn');
	 $('#inpYMD2').datepicker('setDate', com.util.formatMake(toDay, '####-##-##'));
	
	$('#selSearchType').val('D');

};


/**=============================================================
 * etc
 */
 
 /**
  * Display Resize
  */
 function fnDisplayResize(isEvent){
 	
 	
 	comUi.resizeDisplay({
 		'targetClass':		'id-resize-tablediv',
 		'subtractClass':	['pageHeader', 'search', 'id-resize-tabledSubtitle'],
 		'margin':			2,
 		'isEvent':			isEvent,
 	});
 	
 	comUi.resizeDisplay({
 		'targetClass':		'id-parentChart',
 		'subtractClass':	['pageHeader', 'search', 'id-parentChartHead'],
 		'margin':			25,
 		'isEvent':			isEvent,
 	});
 	
 }

/**
 * 차트 Resize®
 */
function fnChartReSize(isEvent){

	var id = '#chart0001';
	
	if(isEvent){
		$(window).resize(function(){
			h = $(id).parent('div').height();
			h = h - $('.id-chartCheck').height() - 30;
			$(id).height(h);
		});
	}
    
    setTimeout(function(){
        $(window).trigger('resize');
    }, 500);
	
};

 
/**
 * =============================================================
 * Event Listener
 */
function fnEventListener(){
	
	//클릭 & 엔터 이벤트
	com.event.clickAndEnter([
		{	selector:'#btnSearch', //조회버튼
			rightKeyTarget: '#btnClean',
			executeFn:function(){
				if(App.vo.nowStat=='L'){
					fnRemotePaging();
				} else {
					fnDisplayResize(false);
					fnRemoteChartList();
				}
				}	
		},
		{	selector:'#btnClean',    //초기화버튼
			leftKeyTarget: '#btnSearch',
			executeFn:function(){
				App.viewState.change('dispClear');
				App.viewState.change('default');
				setSelectTag();
			}
		}
	]);
	
	//목록
	$("#btnList").click(function() {
		comUi.changeDivButton( $(this) );
		App.vo.nowStat = 'L';		
		fnRemotePaging();
	});

	//차트
	$("#btnChart").click(function() {
		comUi.changeDivButton( $(this) );
		App.vo.nowStat = 'C';		
		fnDisplayResize(false);
		fnRemoteChartList();
	});
	
	// 엑셀다운로드 
	$( "#btnExcel" ).click(function() { 
		fnExcelProcess();
	}); 
	
	//기간선택 selectBox 변경시 해당 달력으로 전환
	$("#selSearchType").change(function() {
		App.viewState.change('selectChange');
	});

	// 차트 checkbox 이벤트
	$('input[name=chart_ck]').click(function() {
		fnDisplayResize(false);
		fnRemoteChartList();
	});
	
	// 차트 radio 이벤트
	$('input[name=chart_rdo]').click(function() {
		fnDisplayResize(false);
		fnRemoteChartList();
	});
	
	fnDisplayResize(true);
	fnChartReSize(true);
	
};


/**
 * =============================================================
 * 프로그램시작
 */
function createComp(){
	
	// 화면에서 사용될 이벤트 등록
	fnEventListener();
	
	//[Page] 초기설정시 페이징처리시 사용할 RemoteFunction을 지정하여야 한다.
	App.pager1 		= Col.Paginator(fnRemotePaging);
	App.pager1.init({
		paginator:		'#paginator',
		pageSize: 		App.vo.comPageRowCnt,
		displayPages: 	App.vo.comPageShowCnt
	});
	
	// 기본상태 
	App.viewState.change('default');
	
	// select tag 초기셋팅
	setSelectTag();
	
	fnRemoteChartList();
	
};

$(document).ready(function() {
	// 초기실행 (필수)
	com.init(createComp);
	
});
//-------------------------------------------------------------


</script>


