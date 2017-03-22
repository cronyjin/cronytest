<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>
<!-- @프로그램명: CTP선택 good -->
<!-- 이클립스에서 수정 -->
<!-- 3번째수정 444-->
<!-- 4번째 이클립스 -->

<div id="popCtp" class="modal" style="width:900px; margin-left:-470px;">
	<div class="header">
    	<h2>CTP<spring:message code="select"/></h2>
        <a href="#" class="mclose id-pop-cancel"><i class="fa fa-close"></i></a>
    </div>
    <form>
    <div class="mbody">
    
    	<ul class="tap tabs">
            <li class="active" target-id="popCtpTab1"><a href="#" ><spring:message code="equipBy"/></a></li>
            <li target-id="popCtpTab2"><a href="#" ><spring:message code="ctqBy"/></a></li>
            <li target-id="popCtpTab3"><a href="#" ><spring:message code="tagBy"/></a></li>
        </ul>
        
        <div class="splitwrap">
        	<div class="split" style="width:80%;">
        	
            	<!--tab1 / 설비별-->
                <div id="popCtpTab1" class="splitwra">
                    <div class="split" style="width:32%;">
                        <div class="mtitle set">
                            <h3 class="tit"><i class="fa fa-caret-right "></i> <spring:message code="productionProcess"/></h3>
                            <input class="id-inpFind1" type="text" style="width:90px;">
                            <a href="#" class="btn btnicon id-btnFind1"><i class="fa fa-search"></i></a>
                        </div>
                        <!--테이블-->
                        <div class="table ctp">
                            <table class="cusor">
                            
                            <thead>
                                <tr>
                                <th><span><spring:message code="productionProcess"/></span></th>
                                </tr>
                            </thead>
                            <tbody class="id-tab1-tbody1">
                                
                            </tbody>
                            </table>
                        </div><!--/.table-->
                    </div><!--/.split-->
                    <div class="split" style="width:32%; padding-left:10px;">
                        <div class="mtitle set">
                            <h3 class="tit"><i class="fa fa-caret-right "></i> <spring:message code="equipName"/></h3>
                            <input class="id-inpFind2" type="text" style="width:100px;">
                            <a href="#" class="btn btnicon id-btnFind2"><i class="fa fa-search"></i></a>
                        </div>
                        <!--테이블-->
                        <div class="table ctp">
                            <table class="cusor">
                            
                            <thead>
                                <tr>
                                <th width="70%"><span><spring:message code="equip"/></span></th>
                                <th width="*"><span><spring:message code="tag"/></span></th>
                                </tr>
                            </thead>
                            <tbody class="id-tab1-tbody2">
                            </tbody>
                            </table>
                        </div><!--/.table-->
                    </div><!--/.split-->
                    <div class="split" style="width:31%; padding-left:10px;">
                        <div class="mtitle set">
                            <h3 class="tit"><i class="fa fa-caret-right "></i> <spring:message code="select"/></h3>
                            <select id="selTagGb" style="float:right;">
                            	<option value=""><spring:message code="all"/></option>
                            	<option value="A">Analog</option>
                            	<option value="D">Digital</option>
                            </select>
                        </div>
                        <!--테이블-->
                        <div class="table ctp">
                            <table class="cusor" style="width:400px;">
                            
                            <thead>
                                <tr>
                                <th width="10%"><span><spring:message code="select"/></span></th>
                                <th width="10%"><span><spring:message code="division"/></span></th>
                                <th width="*"><span><spring:message code="tagName"/></span></th>
                                </tr>
                            </thead>
                            <tbody class="id-tab1-tbody3">
                                
                            </tbody>
                            </table>
                        </div><!--/.table-->
                    </div><!--/.split-->
                    <div class="split choice" style="width:5%;">
                        <button type="button" class="btn btnicon btnsub id-move1-btn"><i class="fa fa-angle-double-right "></i></button>
                    </div><!--/.split-->
                </div><!--/.splitwrap-->
                <!--/설비별-->
                
                <!--tab2 / CTQ별-->
				<div id="popCtpTab2" class="splitwrap showno">
                	<div class="split" style="width:50%;">
                        <div class="mtitle set">
                            <h3 class="tit"><i class="fa fa-caret-right "></i> CTQ<spring:message code="name"/></h3>
                            <input class="id-inpFind3" type="text" style="width:90px;">
                            <a href="#" class="btn btnicon id-btnFind3"><i class="fa fa-search"></i></a>
                        </div>
                        <!--테이블-->
                        <div class="table ctp">
                            <table class="cusor">
                            
                            <thead>
                                <tr>
                                <th width="80%"><span>CTQ</span></th>
                                <th width="*"><span><spring:message code="tag"/></span></th>
                                </tr>
                            </thead>
                            <tbody class="id-tab2-tbody1">
                                
                            </tbody>
                            </table>
                        </div><!--/.table-->
                    </div><!--/.split-->
                    <div class="split" style="width:45%; padding-left:10px;">
                        <div class="mtitle set">
                            <h3 class="tit"><i class="fa fa-caret-right "></i> <spring:message code="select"/></h3>
                        </div>
                        <!--테이블-->
                        <div class="table ctp">
                            <table class="cusor">
                            
                            <thead>
                                <tr>
                                <th width="12%"><span><spring:message code="select"/></span></th>
                                <th width="12%"><span><spring:message code="division"/></span></th>
                                <th width="*"><span><spring:message code="tagName"/></span></th>
                                </tr>
                            </thead>
                            <tbody class="id-tab2-tbody2">
                            </tbody>
                            </table>
                        </div><!--/.table-->
                    </div><!--/.split-->
                    <div class="split choice" style="width:5%;">
                        <button type="button" class="btn btnicon btnsub id-move2-btn"><i class="fa fa-angle-double-right "></i></button>
                    </div><!--/.split-->
                </div><!--/.splitwrap-->
                <!--/CTQ별-->
                
                <!--tab3 / 접점별-->
                <div id="popCtpTab3" class="splitwrap showno">
                	<div class="split" style="width:95%;">
                        <div class="mtitle set">
                            <label class="tit"><i class="fa fa-caret-right "></i> <spring:message code="tagName"/></label>
                            <input type="text" style="width:90px;" class="id-inpTagName">
                            <label class="tit dbl"><i class="fa fa-caret-right "></i> <spring:message code="tagCode"/></label>
                            <input type="text" style="width:90px;" class="id-inpTagCode">
                            <a href="#" class="btn btnicon id-btnFind4"><i class="fa fa-search"></i></a>
                            <!-- 페이지 -->
                            <div class="move id-paginator" style="float: right;"></div>
                        </div>
                        <!--테이블-->
                        <div class="table ctp">
                            <table class="cusor">
                            
                            <thead>
                                <tr>
                                <th width="6%"><span><spring:message code="select"/></span></th>
                                <th width="6%"><span><spring:message code="division"/></span></th>
                                <th width="11%"><span><spring:message code="processName"/></span></th>
                                <th width="17%"><span><spring:message code="equipName"/></span></th>
                                <th width="10%"><span><spring:message code="code"/></span></th>
                                <th width="*"><span><spring:message code="tag"/></span></th>
                                </tr>
                            </thead>
                            <tbody class="id-tab3-tbody1">
                            </tbody>
                            </table>
                        </div><!--/.table-->
                    </div><!--/.split-->
                    <div class="split choice" style="width:5%;">
                        <button type="button" class="btn btnicon btnsub id-move3-btn"><i class="fa fa-angle-double-right "></i></button>
                    </div><!--/.split-->
                </div><!--/.splitwrap-->
                <!--/접점별-->
            </div><!--/.split 80%-->
            <div class="split" style="width:20%;">
            	<div class="mtitle set">
                	<h3 class="tit"><i class="fa fa-caret-right "></i> <spring:message code="selected"/></h3>
                    <button type="button" class="btn btnicon right id-clear-btn"><i class="fa fa-remove"></i></button>
                </div>
            	<!--테이블-->
                <div class="table ctp">
                    <table class="cusor id-table-result" style="width:400px;">
                    
                    <thead>
                        <tr>
                        <th width="70%"><span><spring:message code="tag"/></span></th>
                        <th width="30%"><span><spring:message code="code"/></span></th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                    </table>
                </div><!--/.table-->
            </div><!--/.split-->
        </div><!--/.splitwrap-->        
        
    </div>
    <div class="bottom">
		<button type="button" class="btn btnpoint id-pop-ok"><spring:message code="confirm"/></button>
		<button type="button" class="btn id-pop-cancel"><spring:message code="cancel"/></button>
	</div>
    </form>
</div>

<!-- 탭1 설비리스트 -->
<script id="popCtptab1tbody2" type="text/x-dot-template">
    {{~it :value:index}}
	<tr>
		<td col-data="equipId" style="display:none;">{{=value.equipId}}</td>
		<td col-data="procId" style="display:none;">{{=value.procId}}</td>
		<td col-data="equipName">{{=value.equipName}}</td>
		<td col-data="tagCnt">{{=value.tagCnt}}</td>
	</tr>
	{{~}}    
</script>
<!-- 탭1,2 접점리스트 -->
<script id="popCtptab1tbody3" type="text/x-dot-template">
    {{~it :value:index}}
	<tr>
		<td col-data="tagId" style="display:none;">{{=value.tagId}}</td>
		<td col-data="tagCd" style="display:none;">{{=value.tagCd}}</td>
		<td col-data="check" class="alnCenter">
			<input type="checkbox" name="tag1Chk"/>
		</td>
		<td class="alnCenter"col-data="tagGb">{{=value.tagGb}}</td>
		<td col-data="tagName">{{=value.tagName}}</td>
	</tr>
	{{~}}    
</script>
<!-- 탭3 접점리스트 -->
<script id="popCtptab3tbody1" type="text/x-dot-template">
    {{~it :value:index}}
	<tr>
		<td class="alnCenter" col-data="check">
			<input type="checkbox" name="tag1Chk"/>
		</td>
		<td col-data="tagId" style="display:none;">{{=value.tagId}}</td>
		<td class="alnCenter" col-data="tagGb">{{=value.tagGb}}</td>
		<td class="alnCenter">{{=value.procName || ''}}</td>
		<td class="alnCenter">{{=value.equipName || ''}}</td>
		<td class="alnCenter" col-data="tagCd">{{=value.tagCd}}</td>
		<td col-data="tagName">{{=value.tagName}}</td>
	</tr>
	{{~}}    
</script>
<!-- ######################################################################## -->
<!-- ######################### JavaScript Source ############################ -->
<!-- ######################################################################## -->


<script type="text/javascript">

var _popCtp = (function(){


	/**=============================================================
	* 내부 전역변수
	*/
		
	// id
	var _rootId			= '#popCtp';
	
	var _parentOkCallbackReturn 		= '';
	var _parentCancelCallbackReturn 	= '';
	
	var _currItem = {};
	var _getParam = {};
	
	var _tab1FirstCheck = false;
	var _tab2FirstCheck = false;
	var _tab3FirstCheck = false;
	
	// 선택한 접점
	var _tableRid		= _rootId + ' .id-table-result';
	var _tableRbodyId	= _rootId + ' .id-table-result > tbody';
	
	
	_tab1tbody2	= $('#popCtptab1tbody2').html();
	_tab1tbody3	= $('#popCtptab1tbody3').html();
	_tab3tbody1	= $('#popCtptab3tbody1').html();
	/**=============================================================
	* Get, Set Parameter
	*/
	var fnGetParam = function(){
		
	};
	
	
	/**=============================================================
	* Remote
	*/
	
	/**
	 * list 가져오기
	 */
	var fnRemoteTab1 = function(){
		
		App.ro.init();
		App.ro.dest 	= 'map:bpProc.selectProcList:rl'; 
		App.ro.param 	= {};
		App.ro.execute( function(data) {
	 		
			com.treeTableClickRegist({
				tbodyId:			_rootId + ' .id-tab1-tbody1',
				setColumn:			{'cdKey':'procId', 'upperCdKey':'upperProcId', 'leafKey':'leaf', 'rootValue':'root' },
				data:				data.result,
				dataColumn:			['procName'],
				expandAll:			true,
				callbackFunction:	fnRemoteOneTab1
// 				findValue:			paramPk
			}); 
			
		});
	};
	//remoteTab1의 한건선택
	var fnRemoteOneTab1 = function(param){
		App.ro.init();
		App.ro.dest 	= 'map:bpProcEquip.selectEquipByProc:rl'; 
		App.ro.param 	= param;
		App.ro.execute( function(data) {
			
			App.template(_tab1tbody2, data.result, _rootId + ' .id-tab1-tbody2');
			
			if (data.isExist) {
				com.trClickRegist(_rootId + ' .id-tab1-tbody2', fnRemoteOne2Tab1, true, 0);
			} else {
				$(_rootId + ' .id-tab1-tbody3').html('');
			}
			
		});
		
	};
	var _popTab1Param = {};
	//tab1의 한건선택 2번쨰 
	var fnRemoteOne2Tab1 = function(param){
		_popTab1Param = param;
		//현제선택한 접점유형 (Analog, Digital)
		var tagGb = $("#selTagGb option:selected").val();
		param['tagGb'] = tagGb;
		
		App.ro.init();
		App.ro.dest 	= 'map:bpTag.selectTagByEquip:rl'; 
		App.ro.param 	= param;
		App.ro.execute( function(data) {
			
			App.template(_tab1tbody3, data.result, _rootId + ' .id-tab1-tbody3');
			
			if (data.isExist) {
				_tab1FirstCheck = true;
				com.trDbClickRegist(_rootId + ' .id-tab1-tbody3', fnTab1TrClick, true);
			} else {
				
			}
			
		});
		
	};
	
	$("#selTagGb").change(function(){
		fnRemoteOne2Tab1(_popTab1Param);
	});
	
	var fnTab1TrClick = function(param){
		if(!_tab1FirstCheck){
			fnSetTagOne(param, _tableRid);
		} else {
			_tab1FirstCheck = false;
		}
	};
	

	//CTQ별 
	var fnRemoteTab2 = function(){
		//id-tab2-tbody1
		App.ro.init();
		App.ro.dest 	= 'map:bpCtq.selectCtqByTagCntPop:rl'; 
		App.ro.param 	= {};
		App.ro.execute( function(data) {
	 		
			com.treeTableClickRegist({
				tbodyId:			_rootId + ' .id-tab2-tbody1',
				setColumn:			{'cdKey':'ctqCd', 'upperCdKey':'upperCtqCd', 'leafKey':'leaf', 'rootValue':'root' },
				data:				data.result,
				dataColumn:			['ctqName', 'tagCnt'],
				expandAll:			true,
				callbackFunction:	fnRemoteOneTab2,
				//findValue:			{'ctqCd':  }
			}); 
			
		});
		
	};
	//remoteTab2의 한건선택 
	function fnRemoteOneTab2(param){
		
		App.ro.init();
		App.ro.dest 	= 'map:bpCtq.selectCtpByCtq:rl'; 
		App.ro.param 	= param;
		App.ro.execute( function(data) {
			
			App.template(_tab1tbody3, data.result, _rootId + ' .id-tab2-tbody2');
			
			if (data.isExist) {
				_tab2FirstCheck = true;
				com.trDbClickRegist(_rootId + ' .id-tab2-tbody2', fnTab2TrClick, true);
			} else {
				$(_rootId + ' .id-tab2-tbody2').html('');
			}
			
		});
	};

	var fnTab2TrClick = function(param){
		if(!_tab2FirstCheck){
			fnSetTagOne(param, _tableRid);
		} else {
			_tab2FirstCheck = false;
		}
	};
	
	
	//접점별 
	var fnRemoteTab3 = function(){
		
		var obj = {'tagName' : $(_rootId + ' .id-inpTagName').val(), 'tagCd' : $(_rootId + ' .id-inpTagCode').val() };
		
		App.ro.init();
		App.ro.dest 	= 'map:bpTag.selectTagByPop:rp'; 
		App.ro.param 	= $.extend(obj, App.pagerPopCtp.getPageParams());
		App.ro.execute( function(data) {
			
			$(_rootId + ' .id-tab3-tbody1').html('');
			
			App.templatePaging(_tab3tbody1, data, _rootId + ' .id-tab3-tbody1', App.pagerPopCtp);
			
			if (data.isExist) {
				_tab3FirstCheck = true;
				com.trDbClickRegist(_rootId + ' .id-tab3-tbody1', fnTab3TrClick, true);
				com.trClickRegist(_rootId + ' .id-tab3-tbody1', function(){}, true, 0);
			} else {
				$(_rootId + ' .id-tab3-tbody1').html('');
			}
			
		});
	};
	
	var fnTab3TrClick = function(param){
		if(!_tab3FirstCheck){
			fnSetTagOne(param, _tableRid);
		} else {
			_tab3FirstCheck = false;
		}
	};

	
	/**================================
	 * 접점 Get
	 */ 
	var fnGetTag = function(id){
		
		fnSetTagList( com.tableGetAllData(id), _tableRid );
	};
	
	/**================================
	 * 접점 n개를 Set
	 */ 
	var fnSetTagList = function(list, targetId){
		
		if(list.length==0) return;
		var idx 	= 0;
		var key		= '';
		var val 	= '';
		var htm 	= '';
		var isExist = false;
		
		for(idx in list){
			
			if(list[idx]['check'] == 'N') continue;
			
			isExist = false;
			$(targetId).find('tr').each(function(){
				$(this).find('td').each(function(ii, vv){
					key = $(this).attr('col-data');	        
					val = $(this).text();
					if(key=='tagId' && list[idx]['tagId']== val) isExist = true;
				}); 		
			});
			
			console.log(list);
			
			if(!isExist){
				htm = '<tr><td class="text-center" col-data="tagId" style="display:none">' + list[idx]['tagId'] + '</td><td class="text-center" col-data="tagName">' + list[idx]['tagName']  + '</td><td class="text-center" col-data="tagCd">' + list[idx]['tagCd'] + '</td><td class="text-center" col-data="tagGb" style="display:none">' + list[idx]['tagGb'] + '</td></tr>';
				$(targetId).append(htm);
			};
			
		};
	};
	
	/**================================
	 * 접점 1개를 Set
	 */ 
	var fnSetTagOne = function(param, targetId){
		
		var isExist = false;

		$(targetId).find('tr').each(function(){
			$(this).find('td').each(function(ii, vv){
				key = $(this).attr('col-data');	        
				val = $(this).text();
				if(key=='tagId' && param['tagId']== val) {
					isExist = true;
				}
			}); 		
		});
		
		if(!isExist){
			htm = '<tr><td class="text-center" col-data="tagId" style="display:none">' + param['tagId'] + '</td><td class="text-center" col-data="tagName">' + param['tagName']  + '</td><td class="text-center" col-data="tagCd">' + param['tagCd'] + '</td><td class="text-center" col-data="tagGb" style="display:none">' + param['tagGb'] + '</td></tr>';
			$(targetId).append(htm);
		};
		
	};
	
	
	/**=============================================================
	* Event Listener
	*/
	//조회
	com.event.clickAndEnter([
		{	selector: _rootId + " .id-pop-ok", //조회버튼
			rightKeyTarget: _rootId + " .id-pop-cancel",
			executeFn:function(){
				var list = com.tableGetAllData( _tableRbodyId );
				
				if(list.length > 0){
					fnClose('ok', list);
				} else {
					com.alert.ok(com.message('V027'), true, function(){
					});
				}
			}	
		},
		{	selector:_rootId + " .id-pop-cancel",    //초기화버튼
			leftKeyTarget: _rootId + " .id-pop-ok",
			executeFn:function(){
				var param = new Object();
				param['cancle'] = 'cancel';
				fnClose('cancle', param);
			}
		}
	]);
	
 	//엔터를 치면 trigger가 실행된다. 
 	com.event.enterGoTrigger([
 		{selector: _rootId + " .id-inpFind1", 	target: _rootId + " .id-btnFind1", eventType:'click'},
 		{selector: _rootId + " .id-inpFind2", 	target: _rootId + " .id-btnFind2", eventType:'click'},
 		{selector: _rootId + " .id-inpFind3", 	target: _rootId + " .id-btnFind3", eventType:'click'},
 		{selector: _rootId + " .id-inpTagName", target: _rootId + " .id-btnFind4", eventType:'click'}, 		
 		{selector: _rootId + " .id-inpTagCode", target: _rootId + " .id-btnFind4", eventType:'click'} 		
 	]);
	
	$( _rootId + " .id-move1-btn" ).click(function() {
		fnGetTag( _rootId + " .id-tab1-tbody3" );
	});
	$( _rootId + " .id-move2-btn" ).click(function() {
		fnGetTag( _rootId + " .id-tab2-tbody2" );
	});
	$( _rootId + " .id-move3-btn" ).click(function() {
		fnGetTag( _rootId + " .id-tab3-tbody1" );
	});
	
	
	$( _rootId + " .id-clear-btn" ).click(function() {
		$( _tableRbodyId ).html('');
	});
	
	//찾기이벤트 [설비별-생산공정]
	$( _rootId + " .id-btnFind1" ).click(function() {
		var find = $(_rootId + " .id-inpFind1").val()
		com.treeTableFindValue({
			'tbodyId'	:	_rootId + ' .id-tab1-tbody1',
			'findValue'	:	{ 'procName' : find },
			'callbackFunction': function(param){
				if(param['find']=='N'){
					fnRemoteTab1();
				}
			}
		});
	});
	
	//찾기이벤트 [설비별-설비명]
	$( _rootId + " .id-btnFind2" ).click(function() {
		var find = $(_rootId + " .id-inpFind2").val()
		com.trFindValue(_rootId + ' .id-tab1-tbody2', {'equipName':find});
	});
	
	//찾기이벤트 [CTQ별 - CTQ]
	$( _rootId + " .id-btnFind3" ).click(function() {
		var find = $(_rootId + " .id-inpFind3").val()
		com.treeTableFindValue({
			'tbodyId'	:	_rootId + ' .id-tab2-tbody1',
			'findValue'	:	{ 'ctqName' : find },
			'callbackFunction': function(param){
				if(param['find']=='N'){
					fnRemoteTab2();
				}
			}
		});
	});
	
	//찾기이벤트 [접점별]
	$( _rootId + " .id-btnFind4" ).click(function() {
		fnRemoteTab3();
	});
	
	// tab 이벤트
	comUi.tabClickRegist({
		selector: 	_rootId + ' .tap li',
		callbackfn: function(param){
			if(param == 'popCtpTab1') fnRemoteTab1();
			if(param == 'popCtpTab2') fnRemoteTab2();
 			if(param == 'popCtpTab3') fnRemoteTab3();
		}
	});
	
		
	/**================================
	 * Close
	 */ 
	var fnClose = function(type, param){
		
		$(_rootId).hide();
		$('#alBiz').hide();
		$('#alBizBg').hide();
		
		if(type == 'ok'){
			_parentOkCallbackReturn(param);
		} else {
			_parentCancelCallbackReturn(param);
		}
	};
	
	/**================================
	* End
	*/ 
	return {
		exeFirstFn: function(param, okCallbackFn, cancelCallbackFn){
			_parentOkCallbackReturn 	= okCallbackFn;
			_parentCancelCallbackReturn = cancelCallbackFn;
			//전달받은 값 저장
			_getParam = param;
			
			//[Page] 초기설정시 페이징처리시 사용할 RemoteFunction을 지정하여야 한다.
			App.pagerPopCtp 		= Col.Paginator(fnRemoteTab3);
			App.pagerPopCtp.init({
				paginator:		_rootId + ' .id-paginator',
				pageSize: 		9,
				displayPages: 	5
			});
			
			//기본셋팅 
			fnRemoteTab1();
			
			
		}
	};

}());

</script>

