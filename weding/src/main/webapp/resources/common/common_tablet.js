/**
 * ============================================================= 공통선언
 */
com = {};
com.docRoot = '';

com.date = new Object();
com.date.fullDate; // YYYYMMDD HH24:MI:SS
com.date.toDay; // YYYYMMDD
com.date.toMonth; // MM
com.date.toYear; // YYYY
com.date.toTime; // HH24
com.date.toMinute; // MI

// true: 자세한 에러정보 / false: 요약된 에러정보
com.errorInfo = true;

// config 설정
com.conf = new Object();
com.conf.pageSize = 10;
com.conf.displayPages = 3;

com.conf.dispYearRange = -10;
com.conf.dispDayRange = 30;

com.conf.waterAmtPoint = 1; // 반올림
com.conf.chartLineColor = '#d21e1f';

// url
com.url = new Object();
com.url.main = '/muMain'; // 메인화면
com.url.login = '/login'; // 로그인

// 권한
com.auth = new Object();

// templete type
com.templete = new Object();
com.templete.ADMIN = false;
com.templete.MOBILE = false;
com.templete.HOME = false;

/**
 * ============================================================= 공통내에서 사용되는 유틸
 */
com.inUtil = new Object();
com.inUtil.idcheck = function(id) {
	id = (id.substring(0, 1) == '#') ? id : '#' + id;
	return id;
};

/**
 * ============================================================= 메시지
 */
com.messageList = new Object();
com.message = function(code, params) {

	if (params == undefined || params == null || params == '') {
		return com.messageList[code];
	}
	var idx = 0;
	var str = com.messageList[code];
	if (Object.prototype.toString.call(params) === '[object Array]') {
		for (idx in params) {
			str = str.replace(/\%/, params[idx]);
		}
	} else {
		str = str.replace(/\%/, params);
	}
	return str;
};

/**
 * ============================================================= 업무공통
 */
com.CONST = new Object();

/**
 * ============================================================= 유틸
 */
com.util = new Object();

/**
 * 존재유무
 */
com.util.isExist = function(values) {
	var bool = true;
	for (idx in values) {
		if (values[idx] == undefined || values[idx] == ''
				|| values[idx] == null) {
			bool = false;
			break;
		}
	}
	return bool;
};

/**
 * locaction url 이동
 * 
 * @url : 주소
 * @obj : 값
 */
com.util.moveUrl = function(url, obj) {

	var inurl = url;
	if (!com.util.isEmptyObject(obj)) {
		inurl += '?';
		for (key in obj) {
			inurl = inurl + key + '=' + obj[key] + '&';
		}
		inurl = inurl.substring(0, (inurl.length - 1));
	}
	window.location.href = inurl;
};

/**
 * Object 값 판단
 * 
 * @obj : Object
 */
com.util.isEmptyObject = function(obj) {

	// null and undefined are "empty"
	if (obj == undefined)
		return true;
	if (obj == null)
		return true;

	// Assume if it has a length property with a non-zero value
	// that that property is correct.
	if (obj.length > 0)
		return false;
	if (obj.length === 0)
		return true;

	// Otherwise, does it have any properties of its own?
	// Note that this doesn't handle
	// toString and valueOf enumeration bugs in IE < 9
	for ( var key in obj) {
		if (hasOwnProperty.call(obj, key))
			return false;
	}

	return true;
};

/**
 * formater 형식으로 값을 변형함.
 * 
 * @value : number return 10000.00 => '10,000.00'
 */
com.util.formatThreeComma = function(value) {

	return (value.toString()).replace(

	/^([-+]?)(0?)(\d+)(.?)(\d+)$/g, function(match, sign, zeros, before,
			decimal, after) {

		var reverseString = function(string) {
			return string.split('').reverse().join('');
		};
		var insertCommas = function(string) {
			var reversed = reverseString(string);
			var reversedWithCommas = reversed.match(/.{1,3}/g).join(',');
			return reverseString(reversedWithCommas);
		};
		return sign
				+ (decimal ? insertCommas(before) + decimal + after
						: insertCommas(before + after));
	});
};

/**
 * formater 형식으로 값을 변형함.
 * 
 * @value : 값
 * @format : '####-##-##'
 * @mark : '#' (생략가능)
 */
com.util.formatMake = function(value, format, mark) {

	var mak = (mark == undefined) ? '#' : mark;
	var chk = value.split('');
	var fom = format.split('');
	var cnt = -1;
	var res = '';

	for (idx in fom) {
		if (fom[idx] == mak) {
			cnt++;
			res = res + chk[cnt];
		} else {
			res = res + fom[idx];
		}
	}
	return res;
};

/**
 * Object 의 값중 orgValue --> changeValue 변경하여 새로은 오브젝트를 리턴한다.
 */
com.util.OjectChangeValue = function(tarObj, orgValue, changeValue) {

	var obj = new Object();
	for ( var key in tarObj) {
		if (tarObj[key] == orgValue) {
			obj[key] = changeValue;
		} else {
			obj[key] = tarObj[key];
		}
	}
	return obj;
};

/**
 * Array 값중에 특정컬럼의 값을 찾아서 해당 Row를 리턴한다.
 * 
 * @tarArray : 찾을 list
 * @findColumn : 찾을 컬럼
 * @findValue : 찾을 값
 */
com.util.findValueInArray = function(tarArray, findColumn, findValue) {

	var idx = 0;
	for (idx in tarArray) {
		if (tarArray[idx][findColumn] == findValue) {
			var reObj = tarArray[idx];
			break;
		}
	}
	return reObj;
};

/**
 * 
 * 숫자를 증가시켜서 Array 값으로 리턴한다.
 * 
 * @startValue : 시작값
 * @interval : 증감값
 * @count : 반복횟수
 * @isMaxSizeFill : boolean ( true : 부족한 크기를 '0' 으로 채운다 --> '01' )
 * @objectFormat : 오브젝트를 형태를 만들어서 리턴한다. ( 예: {'value':'', 'text':''} )
 */
com.util.repeatDataToArray = function(startValue, interval, count,
		isMaxSizeFill, objectFormat) {

	var i = 0;
	var val;

	if (startValue > -1) {
		val = (interval < 0) ? startValue + 1 : startValue - 1;
	} else {
		val = startValue + 1;
	}

	var save = [];
	var maxSize = 0;
	while (i < count) {

		val += interval;
		maxSize = ((val.toString().length) > maxSize) ? val.toString().length
				: maxSize;
		save.push(val);
		i++;
	}
	;

	var fillString = function(data) {
		if (maxSize == data.toString().length)
			return data.toString();

		var tmp = '';
		var len = maxSize - data.toString().length;
		for (var i = 0; i < len; i++) {
			tmp += '0';
		}
		tmp = tmp + data;
		return tmp;
	};

	var redata = [];
	var imsi = '';
	for ( var idx in save) {

		if (isMaxSizeFill == undefined || isMaxSizeFill == false) {
			imsi = save[idx];
		} else {
			imsi = fillString(save[idx]);
		}
		if (objectFormat == undefined) {
			redata.push(imsi);
		} else {
			var obj = new Object();
			for (key in objectFormat) {
				obj[key] = imsi;
			}
			;
			redata.push(obj);
		}
	}
	;
	return redata;

};

/**
 * URL을 파싱하여 url부분과 파라미터를 Object로 담아 리턴한다.
 * 
 * @urlString : 주소
 */
com.util.parsUrl = function(urlString) {
	var split = urlString.split('?');
	var url = split[0];
	var params;
	var resultParam = {};
	if (split.length > 1) {
		params = split[1].split('&');
		for (var i = 0; i < params.length; i++) {
			var key = params[i].split('=')[0];
			var value = params[i].split('=')[1];
			resultParam[key] = value;
		}
	}

	return {
		url : url,
		param : resultParam
	};
};

/**
 * 숫자만 입력받게 한다.
 * 
 * @className : 대상 class
 */
com.util.numberInput = function(className) {

	$(className).on(
			'keydown',
			function(e) {
				// Allow: backspace, delete, tab, escape, enter and .
				if ($.inArray(e.keyCode, [ 46, 8, 9, 27, 13, 110, 190 ]) !== -1
						||
						// Allow: Ctrl+A
						(e.keyCode == 65 && e.ctrlKey === true) ||
						// Allow: home, end, left, right
						(e.keyCode >= 35 && e.keyCode <= 39)) {
					// let it happen, don't do anything
					return;
				}
				// Ensure that it is a number and stop the keypress
				if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57))
						&& (e.keyCode < 96 || e.keyCode > 105)) {
					e.preventDefault();
				}
			});
}

/**
 * 현재 포커스의 위치(index)를 return한다.
 * 
 * @ctrl : element( ex : this )
 */
com.util.doGetCaretPosition = function(ctrl) {

	var CaretPos = 0;
	// IE Support
	if (document.selection) {

		ctrl.focus();
		var Sel = document.selection.createRange();

		Sel.moveStart('character', -ctrl.value.length);

		CaretPos = Sel.text.length;
	}
	// Firefox support
	else if (ctrl.selectionStart || ctrl.selectionStart == '0')
		CaretPos = ctrl.selectionStart;

	return (CaretPos);
};

/**
 * 지정한 위치로 포커스를 이동시킨다.
 * 
 * @ctrl : element( ex : this )
 * @pos : element( ex : index )
 */
com.util.setCaretPosition = function(ctrl, pos) {
	if (ctrl.setSelectionRange) {
		ctrl.focus();
		ctrl.setSelectionRange(pos, pos);
	} else if (ctrl.createTextRange) {
		var range = ctrl.createTextRange();
		range.collapse(true);
		range.moveEnd('character', pos);
		range.moveStart('character', pos);
		range.select();
	}
};

/**
 * 엑셀다운로드
 * 
 * @url : 주소
 * @param :
 *            param
 * @option : 생략가능
 */
com.util.excelDownloadSubmit = function(url, param, option) {

	// if(com.conf.excelUse == 'N'){
	// com.alert.ok('시스템 사용자에 의하여 <br>엑셀 다운로드 기능이 제한 되었습니다.', true, function(){
	// });
	// return;
	// }

	$('#mainForm').html('');

	var key = '';
	var htm = '';

	for (key in param) {
		htm = '<input type="hidden" name="' + key + '" value="' + param[key]
				+ '">';
		$('#mainForm').append(htm);
	}
	for (key in option) {
		htm = '<input type="hidden" name="' + key + '" value="' + option[key]
				+ '">';
		$('#mainForm').append(htm);
	}
	url = com.docRoot + url;
	$('#mainForm').attr('action', url);
	$('#mainForm').attr('method', 'POST');
	$('#mainForm').attr('target', 'mainIframeTarget');
	$('#mainForm').submit();
};

/**
 * 엑셀다운로드(자동컨트롤러처리)
 * 
 * @url : 주소
 * @param :
 *            param
 * @option : 생략가능
 */
com.util.excelDownloadSubmitDest = function(url, param) {

	// if(com.conf.excelUse == 'N'){
	// com.alert.ok('시스템 사용자에 의하여 <br>엑셀 다운로드 기능이 제한 되었습니다.', true, function(){
	// });
	// return;
	// }

	$('#mainForm').html('');

	var key = '';
	var htm = '';

	for (key in param) {
		htm = '<input type="hidden" name="' + key + '" value="' + param[key]
				+ '">';
		$('#mainForm').append(htm);
		if (param[key] == 'param') {
			for ( var idx in param[key]) {
				htm = '<input type="hidden" name="' + idx + '" value="'
						+ param[key][idx] + '">';
				$('#mainForm').append(htm);
			}
		}
	}
	url = com.docRoot + url;
	$('#mainForm').attr('action', url);
	$('#mainForm').attr('method', 'POST');
	$('#mainForm').attr('target', 'mainIframeTarget');
	$('#mainForm').submit();
};

/**
 * 파일다운로드
 * 
 * @url : 주소
 * @param :
 *            param
 */
com.util.fileDownloadSubmit = function(url, param) {

	$('#mainForm').html('');

	var key = '';
	var htm = '';

	for (key in param) {
		htm = '<input type="hidden" name="' + key + '" value="' + param[key]
				+ '">';
		$('#mainForm').append(htm);
	}
	url = com.docRoot + url;
	$('#mainForm').attr('action', url);
	$('#mainForm').attr('method', 'POST');
	$('#mainForm').attr('target', 'mainIframeTarget');
	$('#mainForm').submit();
};

/**
 * 파일업로드(Ajax)
 * 
 * @fileInfo : File객체
 * @orgParam : 기존(추가) 파라미터
 * 
 */
com.util.fileUploadAjax = function(fileInfo, orgParam) {

	var obj = new Object();
	var formData = new FormData();

	obj = $.extend({}, orgParam, fileInfo);

	for ( var property in obj) {
		if (obj.hasOwnProperty(property)) {
			formData.append(property, obj[property]);
		}
	}
	return formData;
};

/**
 * 쿠키 생성 및 삭제 생성: 'test', 'cookie test, 쿠키 테스트', 1 삭제: 'test', '', -1
 */
com.util.cookieSet = function(cName, cValue, cDay) {

	var expire = new Date();
	expire.setDate(expire.getDate() + cDay);
	cookies = cName + '=' + escape(cValue) + '; path=/ ';
	if (typeof cDay != 'undefined') {
		cookies += ';expires=' + expire.toGMTString() + ';';
	}
	document.cookie = cookies;
};

/**
 * 쿠키 가져오기
 * 
 * @cName : id
 */
com.util.cookieGet = function(cName) {

	cName = cName + '=';
	var cookieData = document.cookie;
	var start = cookieData.indexOf(cName);
	var cValue = '';
	if (start != -1) {
		start += cName.length;
		var end = cookieData.indexOf(';', start);
		if (end == -1)
			end = cookieData.length;
		cValue = cookieData.substring(start, end);
	}
	return unescape(cValue);
};

/**
 * 쿠기 전체를 Array로 추출한다.
 * 
 * @param keyName :
 *            key로 사용될 이름 (없는경우: 'key')
 * @param valueName :
 *            value로 사용될 이름. (없는경우: 'value') return : Array[]
 */
com.util.cookieGetArray = function(keyName, valueName) {
	var reArr = [];
	if (document.cookie.length > 0) {
		var inArr = document.cookie.split('; ');
		for ( var idx in inArr) {
			var tmp = inArr[idx].split('=');
			var obj = new Object();
			if (keyName != undefined) {
				var patt1 = new RegExp(keyName);
				if (patt1.test(tmp[0])) {
					obj[keyName] = tmp[0].replace(keyName, '');
				}
			} else {
				obj['key'] = tmp[0];
			}
			if (valueName != undefined) {
				var patt2 = new RegExp(valueName);
				if (patt2.test(tmp[1])) {
					obj[valueName] = tmp[1].replace(valueName, '');
				}
			} else {
				obj['value'] = tmp[1];
			}
			reArr.push(obj);
		}
	}
	return reArr;
};

/**
 * 쿠기 전체를 삭제한다.
 * 
 * @param keyName :
 *            key로 사용된 이름
 */
com.util.cookieDeleteAll = function(keyName) {
	var inArr = [];
	if (document.cookie.length > 0) {
		inArr = document.cookie.split('; ');
		for ( var idx in inArr) {
			var tmp = inArr[idx].split('=');
			if (keyName != undefined) {
				var patt1 = new RegExp(keyName);
				if (patt1.test(tmp[0])) {
					com.util.cookieSet(tmp[0], '', -1);
				}
			} else {
				com.util.cookieSet(tmp[0], '', -1);
			}
		}
	}
};

/**
 * 테이블의 선택된 tr의 위치를 찾아서 스크롤을 위치시킨다.
 * 
 * @param {
 *            container : container(div) table : table selectIndex : selectIndex //
 *            tr 이 선택된 index }
 */
com.util.scrollFindTable = function(param) {

	// param
	var container = param['container'];
	var table = param['table'];
	var selectIndex = param['selectIndex'];

	selectIndex = (selectIndex == 0) ? 0 : selectIndex - 2;

	var w = $(container);
	var row = $(table).find('tr').eq(selectIndex);
	if (row.length) {
		w.scrollTop(0);
		w.scrollTop(row.offset().top - (w.height() / 2));
	}
};

/**
 * 16진수 칼라값을 10진수로 변경하여 리턴한다.
 * 
 * @hex : '#4b4b4b'
 * @return: [255,255,0]
 * 
 */
com.util.Hex2RGB = function(hex) {
	if (hex.lastIndexOf('#') > -1) {
		hex = hex.replace(/#/, '0x');
	} else {
		hex = '0x' + hex;
	}
	var r = hex >> 16;
	var g = (hex & 0x00FF00) >> 8;
	var b = hex & 0x0000FF;
	return [ r, g, b ];
};

/**
 * ============================================================= 유틸 - Date
 */

com.dateUtil = new Object();

/**
 * 날짜를 문자열로 변환 한다.
 * 
 * @param{ cdate : date type : 24 , 12 (default: 24) : 시간제 mask : 'YYYY-MM-DD',
 *         'YYYY-MM-DD HH:MI:SS' , 'YYYYMMDDHHMI' (default: 'YYYY-MM-DD') }
 * @return : 문자열 날짜 ( '2014-05-01' or '2014-05-01 14:25:35' or '201405011425' )
 */
com.dateUtil.getStringToDate = function(param) {

	var cdate = param['cdate'];
	var type = param['type'];
	var mask = param['mask'];

	type = (type == undefined) ? 24 : type;
	mask = (mask == undefined) ? 'YYYY-MM-DD' : mask;

	var addZero = function(val) {
		if (val < 10)
			val = "0" + val;
		return val;
	};

	// 추출
	var ry = cdate.getFullYear();
	var rm = addZero(cdate.getMonth() + 1);
	var rd = addZero(cdate.getDate());
	var hours = addZero(cdate.getHours());
	if (type == undefined || type == '12' || type == 12) {
		hours = (hours < 13) ? 'AM' + hours : 'PM' + hours;
	}
	var minutes = addZero(cdate.getMinutes());
	var seconds = addZero(cdate.getSeconds());

	mask = mask.replace('YYYY', ry);
	mask = mask.replace('MM', rm);
	mask = mask.replace('DD', rd);
	mask = mask.replace('HH', hours);
	mask = mask.replace('MI', minutes);
	mask = mask.replace('SS', seconds);

	return mask;
};

/**
 * 날짜계산: 기준일부터 몇일까지
 * 
 * @baseDate : 기준일자 ( '2014-05-01' or '20140501' )
 * @cDay : 증감일 ( -30 혹은 30 )
 * @returnMask : default '-'
 * @return : 문자열 날짜 ( '2014-05-01' or '20140501' )
 */
com.dateUtil.stringToCount = function(baseDate, cDay, returnMask) {

	var reg1 = /[1-9]{1}[0-9]{3}[0-9]{2}[0-9]{2}/;
	var rval = baseDate.replace(/[^0-9]/g, '');
	if (!reg1.test(rval))
		return '';

	returnMask = (returnMask == undefined) ? '-' : returnMask;
	var reg2 = /[^0-9]/g;

	var ymask = reg2.exec(baseDate);
	ymask = (ymask === null) ? returnMask : ymask;
	rval = rval.substring(0, 4) + '-' + rval.substring(4, 6) + '-'
			+ rval.substring(6, 8);

	var mdate = new Date(rval);
	mdate.setDate(mdate.getDate() + cDay);

	var ry = mdate.getFullYear();
	var rm = ((mdate.getMonth() + 1) < 10) ? '0' + (mdate.getMonth() + 1)
			: (mdate.getMonth() + 1);
	var rd = (mdate.getDate() < 10) ? '0' + mdate.getDate() : mdate.getDate();

	return ry + ymask + rm + ymask + rd;
};

/**
 * 날짜차이 계산
 * 
 * @sTarget : 시작일자 혹은 selector ( '2014-05-01' or '20140501' or '#sdate' )
 * @eTarget : 종료일자 혹은 selector ( '2014-04-10' or '20140410' or '#edate' )
 * @return : 일수
 */
com.dateUtil.getDifferentDate = function(sTarget, eTarget) {

	var inDateCheck = function(param) {

		var reg1 = /[1-9]{1}[0-9]{3}[0-9]{2}[0-9]{2}/;
		var tmp = param.replace(/[^0-9]/g, '');
		if (!reg1.test(tmp)) {
			if ($(param).is('input')) {
				tmp = $(param).val();
			} else {
				tmp = $(param).text();
			}
			tmp = tmp.replace(/[^0-9]/g, '');
			if (!reg1.test(tmp)) {
				return '';
			}
		}
		return tmp;
	};

	var schk = inDateCheck(sTarget);
	var echk = inDateCheck(eTarget);

	schk = schk.substring(0, 4) + '-' + schk.substring(4, 6) + '-'
			+ schk.substring(6, 8);
	schk = new Date(schk);
	echk = echk.substring(0, 4) + '-' + echk.substring(4, 6) + '-'
			+ echk.substring(6, 8);
	echk = new Date(echk);

	return (schk.getTime() - echk.getTime()) / 1000 / 60 / 60 / 24;
};

/**
 * 기준날짜부터 일자를 차감(가감)한 날짜를 계산한다.
 * 
 * @param{ startDate : new Date(year, month, day, hours, minutes, seconds,
 *         milliseconds) ( default: new Date() ) addDay : 숫자 (+-) type : 24 , 12
 *         (default: 24) : 시간제 mask : 'YYYY-MM-DD', 'YYYY-MM-DD HH:MI:SS' ,
 *         'YYYYMMDDHHMI' (default: 'YYYY-MM-DD') }
 * @return : object {sDate:'2014-05-02 14:25:35', eDate:'2014-05-01 14:25:35'} :
 *         -1 일을 한 경우
 */
com.dateUtil.getStringAddDate = function(param) {

	var addDay = param['addDay'];
	var startDate = param['startDate'];
	var type = param['type'];
	var mask = param['mask'];

	addDay = (addDay == undefined || isNaN(addDay)) ? 1 : addDay;
	addDay = addDay * -1;

	var now1 = '';
	if (startDate == undefined) {
		now1 = new Date();
	} else {
		now1 = startDate;
	}

	var now2 = new Date(now1.getFullYear(), now1.getMonth(), now1.getDay(),
			now1.getHours(), now1.getMinutes(), now1.getSeconds(), now1
					.getMilliseconds());
	now2.setDate(now1.getDate() - addDay);

	var str1 = com.dateUtil.getStringToDate({
		cdate : now1,
		type : type,
		mask : mask
	});
	var str2 = com.dateUtil.getStringToDate({
		cdate : now2,
		type : type,
		mask : mask
	});

	return {
		sDate : str1,
		eDate : str2
	};
};

/**
 * 실시간 시계표현
 * 
 * @param {
 *            selector : target selector type : 12, 24 (default 12) interval :
 *            폴링간격 seconds (default 1) }
 */
com.dateUtil.autoClock = function(param) {

	var selector = param['selector'];
	var type = param['type'];
	var interval = param['interval'];

	var returnClock = function() {
		var mdate = new Date();
		var ry = mdate.getFullYear();
		var rm = ((mdate.getMonth() + 1) < 10) ? '0' + (mdate.getMonth() + 1)
				: (mdate.getMonth() + 1);
		var rd = (mdate.getDate() < 10) ? '0' + mdate.getDate() : mdate
				.getDate();

		var revalue = '';
		revalue = ry + '-' + rm + '-' + rd;

		var hours = mdate.getHours();
		if (type == undefined || type == '12' || type == 12) {
			hours = (hours < 13) ? ' AM ' + hours : ' PM ' + hours;
		} else {
			hours = ' ' + hours;
		}
		hours += ':' + mdate.getMinutes() + ':';
		hours += (mdate.getSeconds() < 10) ? '0' + mdate.getSeconds() : mdate
				.getSeconds();
		revalue += hours;
		return revalue;
	};

	interval = (interval == undefined || interval == 0) ? 1 : interval;
	interval = interval * 1000;

	// timer
	setInterval(function() {
		selector = $(selector);
		if (selector.prop('value')) {
			selector.val(returnClock());
		} else {
			selector.text(returnClock());
		}
	}, interval);

};

/**
 * ============================================================= 이벤트 - Event
 */

com.event = new Object();

/**
 * click & enter event 를 동시에 부여한다.
 * 
 * @param(array)[ {selector: '', leftKeyTarget:'', rightKeyTarget:'', executeFn:
 *                ''}, // leftKeyTarget, rightKeyTarget, executeFn : 생략가능
 *                {selector: '', leftKeyTarget:'', rightKeyTarget:'', executeFn:
 *                ''} ]
 */
com.event.clickAndEnter = function(param) {

	var sel = '';
	for ( var idx in param) {
		sel = param[idx]['selector'];

		$(sel).on('click', function(obj) {
			return function(ev) {
				if (obj['executeFn'] != undefined) {
					obj['executeFn']();
				}
			};
		}(param[idx]));

		$(sel).on('keydown', function(obj) {
			return function(ev) {
				if (ev.which == 13) {
					if (obj['executeFn'] != undefined) {
						obj['executeFn']();
					}
				}
				if (ev.which == 37) {
					if (obj['leftKeyTarget'] != undefined) {
						$(obj['leftKeyTarget']).focus();
					}
				}
				if (ev.which == 39) {
					if (obj['rightKeyTarget'] != undefined) {
						$(obj['rightKeyTarget']).focus();
					}
				}
			};
		}(param[idx]));

	}
	;
};

/**
 * enter를 치면 포커스가 이동된다.
 * 
 * @param(array)[ {selector: '', target: '', executeFn: ''}, // executeFn (생략가능)
 *                {selector: '', target: '', executeFn: ''} ]
 */
com.event.enterGoFocus = function(param) {

	var sel = '';
	for ( var idx in param) {
		sel = param[idx]['selector'];

		$(sel).on('keydown', function(obj) {
			return function(ev) {
				if (ev.which == 13) {
					$(obj['target']).focus();
					if (obj['executeFn'] != undefined) {
						obj['executeFn']();
					}
				}
			};
		}(param[idx]));
	}
	;
};

/**
 * enter를 치면 trigger가 실행된다.
 * 
 * @param(array)[ {selector: '', target: '', eventType: ''}, {selector: '',
 *                target: '', eventType: ''} ]
 */
com.event.enterGoTrigger = function(param) {

	var sel = '';
	for ( var idx in param) {
		sel = param[idx]['selector'];

		$(sel).on('keydown', function(obj) {
			return function(ev) {
				if (ev.which == 13) {
					$(obj['target']).trigger(obj['eventType']);
				}
			};
		}(param[idx]));
	}
	;
};

/**
 * enter를 치면 펑션이 실행된다.
 * 
 * @param(array)[ {selector: '', executeFn: ''}, {selector: '', executeFn: ''} ]
 */
com.event.enterGoFunction = function(param) {

	var sel = '';
	for ( var idx in param) {
		sel = param[idx]['selector'];

		$(sel).on('keydown', function(obj) {
			return function(ev) {
				if (ev.which == 13) {
					obj['executeFn']();
				}
			};
		}(param[idx]));
	}
	;
};

/**
 * ============================================================= list data를
 * Hierarchical 형식으로 변환
 * 
 * @keyName : code Name
 * @upperKeyName : 부모code Name
 * @rootName : 'root'
 * @openFolder : 처음에 폴더를 오픈할지를 결정 true, false
 * @data : array list data
 */
com.listToHierarchical = function(keyName, upperKeyName, rootName, openFolder,
		data) {

	var len = data.length;
	var count = 0;
	var output = [];
	var i = 0;
	var j = 0;

	while (count < len) {

		for (i = 0; i < len; i++) {
			var rowi = data[i];

			if (rowi[upperKeyName] == rootName && rowi['inner_reg'] != 'Y') {
				rowi['inner_reg'] = 'Y';
				output.push(rowi);
				count++;
			}

			for (j = 0; j < len; j++) {
				var rowj = data[j];
				if (rowj['inner_reg'] != 'Y'
						&& rowj[upperKeyName] == rowi[keyName]) {

					rowi['expanded'] = openFolder;
					rowi['folder'] = true;
					if (rowi['children'] == undefined)
						rowi['children'] = [];

					rowj['inner_reg'] = 'Y';
					rowi['children'].push(rowj);
					count++;
				}
			}
		}
	}
	;
	return output;
};

/**
 * ============================================================= Alert
 */
com.alert = new Object();

com.alert.okcallbackfn = '';
com.alert.cfokcallbackfn = '';
com.alert.cfcancelcallbackfn = '';

/**
 * 팝업 size 계산
 */
com.alert.size = function(id) {

	var cn = parseInt($(id).css('width'));
	cn = cn / 2;
	cn = cn + 20;
	cn = cn * -1;
	return cn + 'px';
};

/**
 * 알림창
 */
com.alert.ok = function(msg, modalYn, okCallbackFn) {

	var ml = com.alert.size('#alAlert');
	$('#alAlert').css('margin-left', ml);

	$('#alAlertText').text(msg);

	com.alert.okcallbackfn = okCallbackFn;

	$('#alAlert').show();
	$('#alAlertBg').show();

	$('#alAlertOk').focus();
};

/**
 * 옵션 선택창
 */
com.alert.option = function(msg, modalYn, okCallbackFn) {
	
	var ml = com.alert.size('#alAlert');
	$('#alAlertOption').css('margin-left', ml);
	
	$('#alAlertOptionText').text(msg);
	
	com.alert.okcallbackfn = okCallbackFn;
	
	$('#alAlertOption').show();
	$('#alAlertOptionBg').show();
	
	$('#alAlertOptionOk').focus();
	
};

/**
 * 컨펌창
 */
com.alert.confirm = function(msg, modalYn, okCallbackFn, cancelCallbackFn) {

	var ml = com.alert.size('#alConfirm');
	$('#alConfirm').css('margin-left', ml);

	$('#alConfirmText').text(msg);

	com.alert.cfokcallbackfn = okCallbackFn;
	com.alert.cfcancelcallbackfn = cancelCallbackFn;

	$('#alConfirm').show();
	$('#alConfirmBg').show();

	$('#alConfirmCancel').focus();
};

/**
 * 오토알림창
 */
com.alert.auto = function(msg, modalYn, okCallbackFn) {

	var ml = com.alert.size('#alAlertAuto');
	$('#alAlertAuto').css('margin-left', ml);

	$('#alAlertAutoText').text(msg);
	$('#alAlertAuto').show();
	$('#alAlertAutoBg').show();

	setTimeout(function() {
		$('#alAlertAutoBg').hide();
		$('#alAlertAuto').hide();
		okCallbackFn();
	}, 1000);
};

/**
 * 오토알림창<2초>
 */
com.alert.auto2 = function(msg, modalYn, okCallbackFn) {

	var ml = com.alert.size('#alAlertAuto');
	$('#alAlertAuto').css('margin-left', ml);

	$('#alAlertAutoText').html(msg);
	$('#alAlertAuto').show();
	$('#alAlertAutoBg').show();

	setTimeout(function() {
		$('#alAlertAutoBg').hide();
		$('#alAlertAuto').hide();
		okCallbackFn();
	}, 3500);
};

/**
 * 오토알림창<4초>
 */
com.alert.auto4 = function(msg, modalYn, okCallbackFn) {

	var ml = com.alert.size('#alAlertAuto');
	$('#alAlertAuto').css('margin-left', ml);

	$('#alAlertAutoText').html(msg);
	$('#alAlertAuto').show();
	$('#alAlertAutoBg').show();

	setTimeout(function() {
		$('#alAlertAutoBg').hide();
		$('#alAlertAuto').hide();
		okCallbackFn();
	}, 4000);
};

/**
 * 시스템에러 알림창
 */
com.alert.error = function(msg, modal, okCallbackFn) {
	if (modal)
		$(com.alert.sysAlertId).attr("data-backdrop", "static");
	$(com.alert.sysAlertId).modal();
	com.alert.core(msg, 'sysErr', com.alert.sysAlertId, okCallbackFn);
};

/**
 * ============================================================= Popup init
 */
com.popupInit = function(url, afterFn) {

	url = com.docRoot + url;
	$.post(url, function(data) {

		$('#alBiz').html(data);

		var id = '#' + url;
		var ml = com.alert.size(id);
		$(id).css('margin-left', ml);
		$('#alBiz').show();
		$('#alBizBg').show();

		if (afterFn != undefined)
			afterFn();
	});
};

com.popupShow = function(modalYn, popupId, popupExeFnName, param, okCallbackFn,
		cancelCallbackFn) {
	popupExeFnName(param, okCallbackFn, cancelCallbackFn);
};

/**
 * ============================================================= TR리스트의 클릭시
 * selected처리 & 이벤트핸들러 실행
 * 
 * @tbodyId : tbody id
 * @callbackFn : callback function
 * @isHeadSort : Head sort
 * @findValue : selected index or {'key':'data'}
 */
com.trClickRegist = function(tbodyId, callbackFn, isHeadSort, findValue) {

	tbodyId = com.inUtil.idcheck(tbodyId);

	// $(tbodyId).find("a").on("click", function(event) {
	// event.preventDefault();
	// $(this).parents('tr').siblings().removeClass('active').end().addClass('active');
	// $(this).closest('tr').siblings().removeClass('active').end().addClass('active');
	// var po = App.selector.getParam( $(this).closest('tr') );
	// if(callbackFn !='' && callbackFn != null){
	// callbackFn(po);
	// }
	// });

	$(tbodyId).find("td").on(
			"click",
			function(event) {
				// event.preventDefault();
				$(this).parents('tr').siblings().removeClass('active').end()
						.addClass('active');
				$(this).closest('tr').siblings().removeClass('active').end()
						.addClass('active');
				var po = App.selector.getParam($(this).closest('tr'));
				if (callbackFn != '' && callbackFn != null) {
					callbackFn(po);
				}
			});

	// table head sort
	if (isHeadSort) {
		// var tableObj = $(tbodyId).closest('table');
		// _nfui.fnTableHeadSort(tableObj);
	}

	var pkval = '';
	var inKey = '';
	var isOk = false;
	for ( var key in findValue) {
		inKey = key;
	}
	if (isNaN(findValue)) {
		var str = 'td[col-data="' + inKey + '"]';

		if (findValue == undefined || findValue[inKey] == null
				|| findValue[inKey] == '') {
			$(tbodyId).find('tr:eq(0)').find("td:eq(0)").trigger('click');
			return false;
		}
		$(tbodyId).find(str).each(function(ii, vv) {
			pkval = $(this).text();
			if (pkval == findValue[inKey]) {
				isOk = true;
				$(this).closest('tr').find("td:eq(0)").trigger('click');
				return false;
			}
		});
		if (!isOk) {
			$(tbodyId).find('tr:eq(0)').find("td:eq(0)").trigger('click');
		}
	} else {
		$(tbodyId).find('tr:eq(' + findValue + ')').find("td:eq(0)").trigger(
				'click');
	}
};

/**
 * ============================================================= TR리스트의 값을 찾아서
 * 클릭해 준다.
 * 
 * @tbodyId : tbody id
 * @findValue : selected index or {'key':'data'}
 */
com.trFindValue = function(tbodyId, findValue) {

	tbodyId = com.inUtil.idcheck(tbodyId);

	var pkval = '';
	var inKey = '';
	var isOk = false;
	for ( var key in findValue) {
		inKey = key;
	}
	if (isNaN(findValue)) {
		var str = 'td[col-data="' + inKey + '"]';

		if (findValue == undefined || findValue[inKey] == null
				|| findValue[inKey] == '') {
			$(tbodyId).find('tr:eq(0)').find("td:eq(0)").trigger('click');
			return false;
		}
		$(tbodyId).find(str).each(function(ii, vv) {
			pkval = $(this).text();
			if (pkval == findValue[inKey]) {
				isOk = true;
				$(this).closest('tr').find("td:eq(0)").trigger('click');
				return false;
			}
		});
		if (!isOk) {
			$(tbodyId).find('tr:eq(0)').find("td:eq(0)").trigger('click');
		}
	} else {
		$(tbodyId).find('tr:eq(' + findValue + ')').find("td:eq(0)").trigger(
				'click');
	}
};

/**
 * ============================================================= TR리스트의 더블클릭시
 * selected처리 & 이벤트핸들러 실행
 * 
 * @tbodyId : tbody id
 * @callbackFn : callback function
 * @isHeadSort : Head sort
 * @findValue : selected index or {'key':'data'}
 */
com.trDbClickRegist = function(tbodyId, callbackFn, isHeadSort, findValue) {

	tbodyId = com.inUtil.idcheck(tbodyId);

	$(tbodyId).find("td").dblclick(
			function(e) {
				e.stopPropagation();
				$(this).parents('tr').siblings().removeClass('active').end()
						.addClass('active');
				$(this).closest('tr').siblings().removeClass('active').end()
						.addClass('active');
				var po = App.selector.getParam($(this).closest('tr'));
				if (callbackFn != '' && callbackFn != null) {
					callbackFn(po);
				}
			});

	var pkval = '';
	var inKey = '';
	var isOk = false;
	for ( var key in findValue) {
		inKey = key;
	}
	if (isNaN(findValue)) {
		var str = 'td[col-data="' + inKey + '"]';

		if (findValue == undefined || findValue[inKey] == null
				|| findValue[inKey] == '') {
			$(tbodyId).find('tr:eq(0)').find("td:eq(0)").trigger('dblclick');
			return false;
		}
		$(tbodyId).find(str).each(function(ii, vv) {
			pkval = $(this).text();
			if (pkval == findValue[inKey]) {
				isOk = true;
				$(this).closest('tr').find("td:eq(0)").trigger('dblclick');
				return false;
			}
		});
		if (!isOk) {
			$(tbodyId).find('tr:eq(0)').find("td:eq(0)").trigger('dblclick');
		}
	} else {
		$(tbodyId).find('tr:eq(' + findValue + ')').find("td:eq(0)").trigger(
				'dblclick');
	}
};

/**
 * ============================================================= UL리스트의 li클릭시
 * selected처리 & 이벤트핸들러 실행
 * 
 * @tbodyId : tbody id
 * @callbackFn : callback function
 * @isHeadSort : Head sort
 * @findValue : selected index or {'key':'data'}
 */
com.liClickRegist = function(ulId, callbackFn, isHeadSort, findValue) {

	ulId = com.inUtil.idcheck(ulId);

	$(ulId).find("li").on("click", function(event) {
		$(this).siblings().removeClass('active').end().addClass('active');
		var po = App.selector.getParam($(this));
		if (callbackFn != '' && callbackFn != null) {
			callbackFn(po);
		}
	});

	// $(ulId).find('li:eq(0)').trigger('click');

	var pkval = '';
	var inKey = '';
	for ( var key in findValue) {
		inKey = key;
	}
	if (isNaN(findValue)) {
		var str = 'input[col-data="' + inKey + '"]';

		if (findValue == undefined || findValue[inKey] == null
				|| findValue[inKey] == '') {
			$(ulId).find('li:eq(0)').trigger('click');
			return false;
		}
		$(ulId).find(str).each(function(ii, vv) {
			console.log($(this));
			pkval = $(this).val();
			if (pkval == findValue[inKey]) {
				$(this).closest('li').trigger('click');
				return false;
			}
		});
	} else {
		$(ulId).find('li:eq(' + findValue + ')').trigger('click');
	}
};

/**
 * ============================================================= TR리스트안에 select
 * tag를 만들고 이벤트를 실행한다.
 * 
 * @tbodyId : tbody id
 * @columnName : td안에 설정된 col-data
 * @data : select tag 에 만들어질 option 값 -> {'value':'1', text:'내용'}
 */
com.trCreateSelectTagChangeRegist = function(tbodyId, columnName, data) {

	tbodyId = com.inUtil.idcheck(tbodyId);
	var id = tbodyId + ' tr';
	var key = '';
	var kval = '';
	var idx = 0;
	var htm = '';
	var sel;
	$(id)
			.each(
					function() {
						$(this)
								.children('td')
								.each(
										function(ii, vv) {

											key = $(this).attr('col-data');
											if (key == columnName) {
												kval = $(this).text();
												sel = $(this).find('select');
												$(this).text('');
												for (idx in data) {
													if (kval == data[idx]['value']) {
														htm = '<option selected="selected" value="'
																+ data[idx]['value']
																+ '">'
																+ data[idx]['text']
																+ '</option>';
													} else {
														htm = '<option value="'
																+ data[idx]['value']
																+ '">'
																+ data[idx]['text']
																+ '</option>';
													}
													sel.append(htm);
												}
												$(this).append(sel);
											}

										});
					});
};

/**
 * ============================================================= TR리스트의 1 row의
 * data를 가져오고, 클릭한다.
 * 
 * @tbodyId : tbody id
 * @idx : index
 * @selected : 선택여부 (trigger click)
 */
com.trGetRowData = function(tbodyId, idx, selected) {

	tbodyId = com.inUtil.idcheck(tbodyId);
	if (idx == undefined)
		idx = 0;
	var id = tbodyId + ' tr';
	var data = {};
	var key = '';
	$(id).eq(idx).each(function() {
		$(this).children('td').each(function(ii, vv) {
			key = $(this).attr('col-data');
			data[key] = $(this).text();
		});
	});
	if (selected) {
		$(tbodyId).find('tr:eq(' + idx + ')').find("a:eq(0)").trigger('click');
	}
	return data;
};

/**
 * ============================================================= treeTable을 만들고
 * 특정 DATA를 찾아서 1 ROW 를 리턴하고 클릭한다.
 * 
 * @param {
 *            tbodyId : tbody id setColumn : ex {'cdKey':'code',
 *            'upperCdKey':'upperCode', 'leafKey':'leaf', 'rootValue':'root' }
 *            data : data dataColumn : 컬럼 ex ['name', 'url', 'position', '',
 *            'detail'] ''은 빈컬럼 hiddenColumn : 컬럼 숨기기 ex ['detail'] compColumn :
 *            콤포넌트 컬럼 ex { 'position':{'select':'<select><option value="Y">Yes</option></selct>'} }
 *            ex { 'position':{'select':'<select><option value="Y">Yes</option></selct>',
 *            'showColumn':'showYn'} } 서버에서 showYn 컬럼에 'Y' , 'N' 를 가지고 온다.
 *            compColumnNodeHideAll: [{'column':'position', 'findCdKey':'12'}], //
 *            findCdKey를 포함한 자식노드 모두가 compColumn 에서 지정한 테그를 숨긴다. expandAll :
 *            expandAll All true or false callbackFunction : click callback
 *            function(param) findValue : selected index or {'key':'data'}
 *            specialColumn : { 'column':'nodeTypeCd', 'condition':{'D':'fa
 *            fta', 'V':'fa ftaend', 'O':'fa ftaor', 'A':'fa ftaand' } } ex 'D' :
 *            일반노드 / 'fa fta' : css class ex 'V' : 종단노드 / 'fa ftaend' : css
 *            class ex 'O' : OR / 'fa ftaor' : css class ex 'A' : AND / 'fa
 *            ftaand' : css class nvl : {'is':false, value:''} / default }
 */
com.treeTableClickRegist = function(param) {

	// param
	var tbodyId = param['tbodyId'];
	var cdkey = param['setColumn']['cdKey'];
	var ucdkey = param['setColumn']['upperCdKey'];
	var leafkey = param['setColumn']['leafKey'];
	var rootval = param['setColumn']['rootValue'];
	var data = param['data'];
	var dataColumn = param['dataColumn'];
	var hiddenColumn = param['hiddenColumn'];
	var compColumn = param['compColumn'];
	var compColumnNodeHideAll = param['compColumnNodeHideAll']; // [{'column':'positon',
																// 'findCdKey':'15'}]
	var expandAll = param['expandAll'];
	var callbackFunction = param['callbackFunction'];
	var findValue = param['findValue'];
	var specialColumn = param['specialColumn'];
	var nvl = param['nvl'];

	nvl = (nvl == undefined) ? {
		'is' : false,
		value : ''
	} : nvl;
	if (!nvl.hasOwnProperty('value')) {
		nvl['value'] = '';
	}

	// 현재 위치로 스크롤 이동
	var setPosition = function(index) {

		container = $(tbodyId).closest('div');
		table = $(tbodyId).closest('table');

		selectIndex = index;
		selectIndex = (selectIndex < 0) ? 0 : selectIndex;

		var w = container;
		var row = table.find('tr').eq(selectIndex);
		var top = 0;
		if (row.length) {
			w.scrollTop(0);
			top = (row.offset().top - (w.height() / 2));
			top = (top < 60) ? 0 : (top - 60); // 마진을 조정할것 80
			w.scrollTop(top);
		}
	};

	// local val
	var htm = '';

	// clear
	$(tbodyId).html('');
	$(tbodyId).closest('table').treetable('destroy');

	// hiddenColumn key make
	var dataColumnH = [];
	var isShow = false;
	if (hiddenColumn != undefined && hiddenColumn.length > 0) {
		for (var j = 0; j < dataColumn.length; j++) {
			isShow = false;
			for (var k = 0; k < hiddenColumn.length; k++) {
				if (dataColumn[j] == hiddenColumn[k]) {
					isShow = true;
					break;
				}
			}
			dataColumnH.push(isShow);
		}
	}

	// compColumnNodeHideAll key make
	if (compColumnNodeHideAll != undefined) {

		var colna = '';
		for (var idx = 0; idx < compColumnNodeHideAll.length; idx++) {

			colna = compColumnNodeHideAll[idx]['column'];
			colna = 'in-' + colna + '-showYn';
			var fval = compColumnNodeHideAll[idx]['findCdKey'];
			var imsiArr = [];
			var findArr = [];

			findArr.push(fval);

			var objs = {};
			while (findArr.length > 0) {

				for ( var idx in findArr) {
					findArr[idx];

					for (var i = 0; i < data.length; i++) {
						objs = data[i];
						if (fval == data[i][cdkey]) {
							objs[colna] = 'N';
						} else if (findArr[idx] == data[i][ucdkey]) {
							imsiArr.push(objs[cdkey]);
							objs[colna] = 'N';
						} else {
							if (!objs.hasOwnProperty(colna)
									|| objs[colna] == 'Y') {
								objs[colna] = 'Y';
							}
						}
					}
					;
				}
				findArr = [];
				findArr = imsiArr.slice();
				imsiArr = [];
			}
		}
		;
	}

	// start
	var obj = {};
	var key = '';
	var keyA = '';
	var comCk = '';
	var strA = '';
	var strB = '';
	var strC = '';
	var tmpA = '';

	for (var idx = 0; idx < data.length; idx++) {

		htm = '';
		obj = data[idx];
		if (obj[ucdkey] == rootval) {
			htm += '<tr data-tt-id="' + obj[cdkey] + '">';
		} else {
			htm += '<tr data-tt-id="' + obj[cdkey] + '" data-tt-parent-id="'
					+ obj[ucdkey] + '">';
		}

		// null 처리
		if (nvl['is']) {
			for ( var inkey in obj) {
				if (obj[inkey] == null) {
					obj[inkey] = nvl['value'];
				}
			}
			;
		}

		for (var i = 0; i < dataColumn.length; i++) {
			key = dataColumn[i];
			if (i == 0) {

				if (specialColumn == undefined) {
					// 일반 트리시..
					if (obj[leafkey] == 'Y') {
						htm += '<td col-data="'
								+ key
								+ '"><i class="icon_file-text2" style="margin-right:5px"></i>'
								+ obj[key] + '</td>';
					} else {
						htm += '<td col-data="'
								+ key
								+ '"><i class="icon_folder" style="margin-right:5px"></i>'
								+ obj[key] + '</td>';
					}
				} else {
					// fta 형식의 트리시..
					strC = '';
					for ( var fkey in specialColumn['condition']) {
						if (obj[specialColumn['column']] == fkey) {
							strC = specialColumn['condition'][fkey]
							break;
						}
					}
					htm += '<td col-data="' + key + '"><i class="' + strC
							+ '" style="margin-right:5px"></i>' + obj[key]
							+ '</td>';
				}

			} else {
				strA = (dataColumnH[i]) ? 'style="display:none"' : '';
				if (key != '') {
					comCk = '';
					strB = '';
					// .....................................
					for ( var cpKey in compColumn) {
						if (key == cpKey) {
							if (compColumn[cpKey].hasOwnProperty('select')) {
								// select tag
								comCk = 'select';
								tmpA = 'value="' + obj[key] + '"';
								strB = compColumn[cpKey]['select'];
								strB = strB.replace(tmpA, tmpA + " selected");

								if (compColumn[cpKey]
										.hasOwnProperty('showColumn')) {
									keyA = compColumn[cpKey]['showColumn'];
									strB = (obj[keyA] == 'Y') ? strB : '';
									strA += ' col-data="' + key + '"';
								}
								colna = 'in-' + cpKey + '-showYn';
								if (obj.hasOwnProperty(colna)) {
									strB = (obj[colna] == 'Y') ? strB : '';
									var patt = new RegExp("col-data");
									if (!patt.test(strA)) {
										strA += ' col-data="' + key + '"';
									}
								}
							} else if (compColumn[cpKey]
									.hasOwnProperty('checkbox')) {
								// input checkbox tag
								comCk = 'checkbox';
								if (obj[cpKey] == 'Y' || obj[cpKey] == 'y') {
									tmpA = 'value="' + obj[key] + '" checked';
								} else {
									tmpA = 'value="' + obj[key] + '"';
								}
								strB = compColumn[cpKey]['checkbox'];
								strB = strB.replace("input", "input " + tmpA);

								if (compColumn[cpKey]
										.hasOwnProperty('showColumn')) {
									keyA = compColumn[cpKey]['showColumn'];
									strB = (obj[keyA] == 'Y') ? strB : '';
									strA += ' col-data="' + key + '"';
								}
								colna = 'in-' + cpKey + '-showYn';
								if (obj.hasOwnProperty(colna)) {
									strB = (obj[colna] == 'Y') ? strB : '';
									var patt = new RegExp("col-data");
									if (!patt.test(strA)) {
										strA += ' col-data="' + key + '"';
									}
								}
							}
						}
					}
					// .....................................
					if (comCk == '') {
						if (strA == '') {
							htm += '<td col-data="' + key + '">' + obj[key]
									+ '</td>';
						} else {
							htm += '<td ' + strA + ' col-data="' + key + '">'
									+ obj[key] + '</td>';
						}
					} else if (comCk == 'select' || comCk == 'checkbox') {
						if (strA == '') {
							htm += (strB == '') ? '<td></td>' : '<td>' + strB
									+ '</td>';
						} else {
							htm += (strB == '') ? '<td ' + strA + '></td>'
									: '<td ' + strA + '>' + strB + '</td>';
						}
					}
				} else {
					if (strA == '') {
						htm += '<td></td>';
					} else {
						htm += '<td ' + strA + ' ></td>';
					}
				}
			}
		}

		// hidde column
		htm += '<td style="display:none" col-data="' + cdkey + '">'
				+ obj[cdkey] + '</td>';
		htm += '<td style="display:none" col-data="' + ucdkey + '">'
				+ obj[ucdkey] + '</td>';

		// make tr
		htm += '</tr>';
		$(tbodyId).append(htm);

	}

	// td click event
	$(tbodyId).find('td').each(
			function() {
				$(this).on(
						"click",
						function(event) {
							event.preventDefault();
							$(this).parents('tr').siblings().removeClass(
									'active').end().addClass('active');
							$(this).closest('tr').siblings().removeClass(
									'active').end().addClass('active');
							var po = App.selector.getParam($(this)
									.closest('tr'));
							po['eventType'] = 'td';
							po['trIndex'] = $(this).closest('tr').index();
							if (callbackFunction != ''
									&& callbackFunction != null) {
								callbackFunction(po);
							}
						});
			});

	// comp event
	if (!com.util.isEmptyObject(compColumn)) {

		for ( var cpKey in compColumn) {

			if (compColumn[cpKey].hasOwnProperty('select')) {

				// select
				$(tbodyId).find('td select').each(
						function() {

							$(this).change(
									function(event) {
										event.preventDefault();
										var po = App.selector.getParam($(this)
												.closest('tr'));
										if (callbackFunction != ''
												&& callbackFunction != null) {
											po['eventType'] = 'select';
											callbackFunction(po);
										}
									});
						});
			} else if (compColumn[cpKey].hasOwnProperty('checkbox')) {

				// input
				$(tbodyId).find('td input:checkbox').each(
						function() {

							$(this).click(
									function(event) {

										event.stopPropagation();
										var po = App.selector.getParam($(this)
												.closest('tr'));
										if ($(this).is(':checked')) {
											$(this).val('Y');
										} else {
											$(this).val('N');
										}
										if (callbackFunction != ''
												&& callbackFunction != null) {
											po['eventType'] = 'checkbox';
											callbackFunction(po);
										}
									});
						});
			}
			// --------------
		}
	}

	// make treeTable
	$(tbodyId).closest('table').treetable({
		expandable : true
	});
	if (expandAll) {
		$(tbodyId).closest('table').treetable('expandAll');
	}
	
	var hideColumn = param['hideColumn'];
	if(hideColumn != null && hideColumn != undefined &&hideColumn.length > 0){
		for(var idx in hideColumn){
			$(tbodyId).closest('table').treetable('collapseNode', hideColumn[idx]);
		}
	}

	// findValue
	var pkval = '';
	var inKey = '';
	var findex = 0;
	for ( var fkey in findValue) {
		inKey = fkey;
	}
	if (isNaN(findValue)) {
		var strX = 'td[col-data="' + inKey + '"]';

		if (findValue == undefined || findValue[inKey] == null
				|| findValue[inKey] == '') {
			// scroll position
			setPosition(0);
			// ..
			$(tbodyId).find('tr:eq(0)').find("td:eq(0)").trigger('click');
			return false;
		}
		$(tbodyId).find(strX).each(function(ii, vv) {
			pkval = $(this).text();
			if (pkval == findValue[inKey]) {
				// scroll position
				findex = $(this).closest('tr').index();
				setPosition(findex);
				// ..
				$(this).closest('tr').find("td:eq(0)").trigger('click');
				return false;
			}
		});
	} else {
		// scroll position
		findex = $(tbodyId).find('tr:eq(' + findValue + ')').index();
		setPosition(findex);
		// ..
		$(tbodyId).find('tr:eq(' + findValue + ')').find("td:eq(0)").trigger(
				'click');
	}

};

/**
 * ============================================================= treeTable 안의 특정
 * node를 찾는다.
 * 
 * @param {
 *            tbodyId : tbodyId findValue : selected index or {'key':'data'}
 *            callbackFunction : 찾는 문자열이 없을때 '' 를 리턴한다. }
 */
com.treeTableFindValue = function(param) {

	// param
	var tbodyId = param['tbodyId'];
	var findValue = param['findValue'];
	var callbackFunction = param['callbackFunction'];

	var inKey = '';
	var revealCd = '';
	var findex = 0;

	// 트리테이블의 특정노드를 open한다.
	var inFunTreeReveal = function(code) {
		$(tbodyId).closest('table').treetable("reveal", code);
	};

	// 현재 위치로 스크롤 이동
	var setPosition = function(index) {

		container = $(tbodyId).closest('div');
		table = $(tbodyId).closest('table');

		selectIndex = index;
		selectIndex = (selectIndex < 0) ? 0 : selectIndex;

		var w = container;
		var row = table.find('tr').eq(selectIndex);
		var top = 0;
		if (row.length) {
			w.scrollTop(0);
			top = (row.offset().top - (w.height() / 2));
			top = (top < 60) ? 0 : (top - 60); // 마진을 조정할것 80
			w.scrollTop(top);
		}
	};

	for ( var fkey in findValue) {
		inKey = fkey;
	}

	var check = false;
	if (isNaN(findValue)) {

		if (findValue == undefined || findValue[inKey] == null
				|| findValue[inKey] == '') {
			findex = 0;
			setPosition(findex);
			$(tbodyId).find('tr:eq(0)').find("td:eq(0)").trigger('click');
			revealCd = $(tbodyId).find('tr:eq(0)').attr('data-tt-id');
			check = true;
		}

		var strId = tbodyId + ' td[col-data="' + inKey + '"]';

		$(strId).filter(function() {
			return $(this).is(':contains("' + findValue[inKey] + '")');
		}).each(function() {
			findex = $(this).closest('tr').index();
			setPosition(findex);
			$(this).closest('tr').find("td:eq(0)").trigger('click');
			revealCd = $(this).closest('tr').attr('data-tt-id');
			check = true;
			return false;
		});

	} else {
		findex = $(tbodyId).find('tr:eq(' + findValue + ')').index();
		setPosition(findex);
		$(tbodyId).find('tr:eq(' + findValue + ')').find("td:eq(0)").trigger(
				'click');
		revealCd = $(tbodyId).find('tr:eq(' + findValue + ')').attr(
				'data-tt-id');
		check = true;
	}

	var reParam = {};
	if (check) {
		inFunTreeReveal(revealCd);
		reParam = {
			'find' : 'Y',
			'trIndex' : findex
		};
	} else {
		// like 검색으로 찾지 못했을때 선택을 지운다.
		$(tbodyId).find('tr').removeClass('active');
		reParam = {
			'find' : 'N',
			'trIndex' : findex
		};
	}
	if (callbackFunction != undefined) {
		callbackFunction(reParam);
	}
};

/**
 * ============================================================= select tag 생성 및
 * 이벤트 등록
 * 
 * @selectId: select ID
 * @data: list data ( {value:'', text:''} )
 * @selectedIndex: selected Index position
 * @firstValue: ex: {value:'-1', text:'선택'}
 * @callbackFn: 콜벡펑션
 * @columnName: ex: {value:'code', text:'codeName'}
 */
com.selectCreateAndClickRegist = function(selectId, data, selectedIndex,
		firstValue, callbackFn) {

	var id = '';
	var str = '';
	var idx = 0;
	var newlist = [];

	if (firstValue != undefined && firstValue != '') {
		newlist.push(firstValue);
	}

	if (data != undefined) {
		for (idx in data) {
			newlist.push(data[idx]);
		}
	}

	id = com.inUtil.idcheck(selectId);
	$(id).html('');

	var len = newlist.length;
	var reobj = new Object();

	selectedIndex = parseInt(selectedIndex);
	for (idx = 0; idx < len; idx++) {
		if (selectedIndex != undefined) {
			if (idx == selectedIndex) {
				str += '<option value="' + newlist[idx]['value']
						+ '" selected="selected" >' + newlist[idx]['text']
						+ '</option>';
				reobj = {
					'value' : newlist[idx]['value'],
					'text' : newlist[idx]['text']
				};
			} else {
				str += '<option value="' + newlist[idx]['value'] + '">'
						+ newlist[idx]['text'] + '</option>';
			}
		} else {
			str += '<option value="' + newlist[idx]['value'] + '">'
					+ newlist[idx]['text'] + '</option>';
		}
	}
	id = com.inUtil.idcheck(selectId);
	$(id).html(str);

	if (callbackFn != undefined && callbackFn != '') {
		$(id).on("change", function(event) {
			event.preventDefault();
			var pa = {};
			pa['value'] = $(this).find('option:selected').val();
			pa['text'] = $(this).find('option:selected').text();
			callbackFn(pa);
		});
	}

	return reobj;
};

/**
 * ============================================================= select tag 생성 및
 * 이벤트 등록
 * 
 * @selectId: select ID
 * @data: list data ( {value:'', text:''} )
 * @selectedIndex: selected Index position
 * @firstValue: ex: {value:'-1', text:'선택'}
 * @columnName: ex: {value:'code', text:'codeName'}
 * @callbackFn: 콜벡펑션
 */
com.selectCreateAndClickRegist2 = function(selectId, data, selectedIndex,
		firstValue, columnName, callbackFn) {

	var id = '';
	var str = '';
	var idx = 0;
	var newlist = [];

	if (firstValue != undefined && firstValue != '') {
		newlist.push(firstValue);
	}

	if (data != undefined) {
		for (idx in data) {
			if (columnName == null || columnName == '') {
				newlist.push(data[idx]);
			} else {
				var obj = data[idx];
				obj['value'] = obj[columnName['value']];
				obj['text'] = obj[columnName['text']];
				newlist.push(obj);
			}
		}
	}

	id = com.inUtil.idcheck(selectId);
	$(id).html('');

	var len = newlist.length;
	var reobj = new Object();

	selectedIndex = parseInt(selectedIndex);
	for (idx = 0; idx < len; idx++) {
		if (selectedIndex != undefined) {
			if (idx == selectedIndex) {
				str += '<option value="' + newlist[idx]['value']
						+ '" selected="selected" >' + newlist[idx]['text']
						+ '</option>';
				reobj = {
					'value' : newlist[idx]['value'],
					'text' : newlist[idx]['text']
				};
			} else {
				str += '<option value="' + newlist[idx]['value'] + '">'
						+ newlist[idx]['text'] + '</option>';
			}
		} else {
			str += '<option value="' + newlist[idx]['value'] + '">'
					+ newlist[idx]['text'] + '</option>';
		}
	}
	id = com.inUtil.idcheck(selectId);
	$(id).html(str);

	if (callbackFn != undefined && callbackFn != '') {
		$(id).on("change", function(event) {
			event.preventDefault();
			var pa = {};
			pa['value'] = $(this).find('option:selected').val();
			pa['text'] = $(this).find('option:selected').text();
			callbackFn(pa);
		});
	}

	return reobj;
};

/**
 * ============================================================= select tag 생성 및
 * Find Value 및 이벤트 등록
 * 
 * @selectId: select ID
 * @data: list data ( {value:'', text:''} )
 * @findValue: find value
 * @firstValue: ex: {value:'-1', text:'선택'}
 * @callbackFn: 콜벡펑션
 */
com.selectCreateFindValueAndClickRegist = function(selectId, data, findValue,
		firstValue, callbackFn) {

	var id = '';
	var str = '';
	var idx = 0;
	var newlist = [];

	if (firstValue != undefined && firstValue != '') {
		newlist.push(firstValue);
	}
	if (data != undefined) {
		for (idx in data) {
			newlist.push(data[idx]);
		}
	}

	id = com.inUtil.idcheck(selectId);
	$(id).html('');

	var len = newlist.length;
	var reobj = new Object();

	for (idx = 0; idx < len; idx++) {
		if (findValue != undefined) {
			if (newlist[idx]['value'] == findValue) {
				str += '<option value="' + newlist[idx]['value']
						+ '" selected="selected" >' + newlist[idx]['text']
						+ '</option>';
				reobj = {
					'value' : newlist[idx]['value'],
					'text' : newlist[idx]['text']
				};
			} else {
				str += '<option value="' + newlist[idx]['value'] + '">'
						+ newlist[idx]['text'] + '</option>';
			}
		} else {
			str += '<option value="' + newlist[idx]['value'] + '">'
					+ newlist[idx]['text'] + '</option>';
		}
	}
	id = com.inUtil.idcheck(selectId);
	$(id).html(str);

	if (callbackFn != undefined && callbackFn != '') {
		$(id).on("change", function(event) {
			event.preventDefault();
			var pa = {};
			pa['value'] = $(this).find('option:selected').val();
			pa['text'] = $(this).find('option:selected').text();
			callbackFn(pa);
		});
	}

	return reobj;
};

/**
 * ============================================================= select set
 * 
 * @selectId: select ID
 * @dataField: 'value', 'text'
 * @findValue: 찾고자하는 값
 * @selectedIndex: index (생략가능)
 */
com.selectSet = function(selectId, dataField, findValue, selectedIndex) {

	var id = com.inUtil.idcheck(selectId);
	if (selectedIndex != undefined) {
		id = id + ' option:eq(' + selectedIndex + ')';
		$(id).prop("selected", true);
	} else {
		if (dataField.toLowerCase() == 'value') {
			// value로 selected 하기
			$(id).val(findValue);
		} else {
			// text로 selected 하기
			$(id + ' option').each(function() {
				if (findValue == $(this).text()) {
					$(this).prop("selected", true);
				}
			});
		}
	}
};

/**
 * ============================================================= table 의 모든
 * data를 가지고 온다.
 * 
 * @tbodyId : tbody id
 * @selectType : select tag attr 선택 ( default: 생략가능 )
 */
com.tableGetAllData = function(tbodyId, selectType) {

	var id = com.inUtil.idcheck(tbodyId) + ' tr';
	var data = [];
	var key = '';
	$(id)
			.each(
					function() {
						var rowData = {};
						$(this)
								.children('td')
								.each(
										function() {

											key = $(this).attr('col-data');

											if ($(this).children('select').is(
													'select')) {

												// td 안에 select tag가 있는경우
												if (selectType == undefined
														|| selectType == 'text') {
													rowData[key] = $(this)
															.find('select')
															.find(
																	'option:selected')
															.text();
												} else {
													rowData[key] = $(this)
															.find('select')
															.find(
																	'option:selected')
															.val();
												}

											} else if ($(this).find('input')
													.is('input[type=checkbox]')) {

												// td 안에 checkbox tag가 있는경우
												rowData[key] = ($(this).find(
														'input[type=checkbox]')
														.is(":checked")) ? 'Y'
														: 'N';

											} else if ($(this).find('input')
													.is('input[type=radio]')) {

												// td 안에 radio tag가 있는경우
												$(this)
														.find('input')
														.each(
																function() {
																	if ($(this)
																			.prop(
																					"checked")) {
																		rowData[key] = $(
																				this)
																				.val();
																	}
																});

											} else if ($(this).find('input')
													.is('input[type=text]')) {
												// td 안에 input tag가 있는경우
												rowData[key] = ($(this).find(
														'input').val());
											} else {
												rowData[key] = $(this).text();
											}

										});
						data.push(rowData);
					});
	return data;
};

/**
 * ============================================================= Simple tree를
 * 만들고 특정 DATA를 찾아서 1 ROW 를 리턴하고 클릭한다.
 * 
 * @param {
 *            treeId : tree id setColumn : ex {'cdKey':'code',
 *            'upperCdKey':'upperCode', 'leafKey':'leaf', 'rootValue':'root',
 *            'text': 'title', 'href':'url', 'urlParam' } data : data open :
 *            true or false (default: true) }
 * @return : easyTree 객체
 */
com.treeSimpleTableClickRegist = function(param) {

	// param
	var p_treeId = param['treeId'];
	var p_sc_cdkey = param['setColumn']['cdKey'];
	var p_dc_ucdkey = param['setColumn']['upperCdKey'];
	var p_sc_leafkey = param['setColumn']['leafKey'];
	var p_sc_rootval = param['setColumn']['rootValue'];
	var p_sc_text = param['setColumn']['text'];
	var p_sc_href = param['setColumn']['href'];
	var p_sc_urlParam = param['setColumn']['urlParam'];
	var p_data = param['data'];
	var p_open = param['open'];

	p_treeId = com.inUtil.idcheck(p_treeId);

	p_sc_urlParam = (p_sc_urlParam == undefined) ? '' : p_sc_urlParam;

	function getNestedChildren(arr, parent) {
		var out = []
		for ( var i in arr) {
			if (arr[i][p_dc_ucdkey] == parent) {
				var children = getNestedChildren(arr, arr[i][p_sc_cdkey])

				if (children.length) {
					arr[i].children = children;
				}
				// 추가 input
				var obj = arr[i];
				obj['text'] = obj[p_sc_text];

				if (obj['url'] == null || obj['url'] == '') {
					obj['href'] = '#none';
				} else {
					obj['href'] = obj[p_sc_href] + p_sc_urlParam;
				}
				// ...
				out.push(arr[i]);
			}
		}
		return out;
	}
	;

	var redata = getNestedChildren(p_data, p_sc_rootval);

	var easytree = $(p_treeId).easytree({
		data : redata
	});

	p_open = (p_open == undefined) ? true : p_open;
	com.treeSimpleOpen({
		target : easytree,
		open : p_open
	});

	return easytree;

};

/**
 * ============================================================= Simple tree를
 * 특정노드를 Active 한다.
 * 
 * @param{ target : easyTree object column : findColumn findValue : findValue }
 */
com.treeSimpleActive = function(param) {

	// param
	var p_target = param['target'];
	var p_column = param['column'];
	var p_findValue = param['findValue'];

	// 현재 위치로 스크롤 이동
	var setPosition = function(target) {
		var w = $('#nav');
		w.scrollTop(0);
		w.scrollTop(target.offset().top - (w.height() / 2));
	};

	var allNode = p_target.getAllNodes();

	function nodeFindValue(nodes, col, findVal) {
		for (var idx = 0; idx < nodes.length; idx++) {
			if (nodes[idx][col] == findVal) {
				p_target.activateNode(nodes[idx].id);
				setPosition($('#' + nodes[idx]['id']));
				break;
			}

			if (nodes[idx].children && nodes[idx].children.length > 0) {
				nodeFindValue(nodes[idx].children, col, findVal);
			}
		}
	}
	nodeFindValue(allNode, p_column, p_findValue);
};

/**
 * ============================================================= Simple tree를
 * Open or Close 한다.
 * 
 * @param{ target : easyTree object open : true or false }
 */
com.treeSimpleOpen = function(param) {

	// param
	var p_target = param['target'];
	var p_open = (param['open'] == undefined) ? true : param['open'];

	function toggleNodes(nodes, openOrClose) {
		var i = 0;
		for (i = 0; i < nodes.length; i++) {
			nodes[i].isExpanded = openOrClose == "open"; // either expand
															// node or don't

			// if has children open/close those as well
			if (nodes[i].children && nodes[i].children.length > 0) {
				toggleNodes(nodes[i].children, openOrClose);
			}
		}
	}

	var nodes = p_target.getAllNodes();
	p_open = (p_open) ? 'open' : 'close';
	toggleNodes(nodes, p_open);
	p_target.rebuildTree(nodes);

};

/**
 * ============================================================= date Tag create
 * 
 * @inputId: input tag id
 * @buttonId: button tag id
 * @changeEventCallbackFn: date change event callbackFunction
 */
com.dateCreateAndEventRegist = function(inputId, buttonId,
		changeEventCallbackFn) {

	var inpId = com.inUtil.idcheck(inputId);
	$(inpId).attr('size', '12');

	$(inpId).datepicker().on('changeDate', function(e) {
		$(this).datepicker('hide');
		if (changeEventCallbackFn != undefined) {
			changeEventCallbackFn($(this).val());
		}
	});

	var btnId = com.inUtil.idcheck(buttonId);
	$(btnId).on('click', function(ev) {
		ev.preventDefault();
		$(inpId).trigger('focus');
	});
};

/**
 * ============================================================= time Tag create
 * (시간 콤포넌트 초기화)
 * 
 * @inputId: input tag id
 * @buttonId: button tag id
 * @changeEventCallbackFn: date change event callbackFunction
 */
com.timeCreateAndEventRegist = function(inputId, buttonId,
		changeEventCallbackFn) {

	var inpId = com.inUtil.idcheck(inputId);
	$(inpId).timepicker({
		minuteStep : 1,
		defaultTime : '12:00 PM' // 'current', false
	}).on('change', function(e) {
		if (changeEventCallbackFn != undefined) {
			changeEventCallbackFn($(this).val());
		}
	});
	var btnId = com.inUtil.idcheck(buttonId);
	$(btnId).on('click', function(e) {
		e.preventDefault();
		$(inpId).trigger('focus');
	});
};

/**
 * ============================================================= 시간 콤포넌트 값 셋팅하기
 * 
 * @timeStr: '1145' or '1845' return: '11:45 AM' or '6:45 PM'
 */
com.timeSetValue = function(timeStr) {
	var t = parseInt(timeStr.substr(0, 2));
	var m = parseInt(timeStr.substr(2, 4));
	var c = '';
	if (t == 00) {
		t = 12;
		c = t + ':' + m + ' AM';
	} else if (t == 12) {
		// t = t - 12;
		c = t + ':' + m + ' PM';
	} else if (t < 12) {
		c = t + ':' + m + ' AM';
	} else {
		t = t - 12;
		if (t == 12) {
			c = t + ':' + m + ' AM';
		} else {
			c = t + ':' + m + ' PM';
		}

	}
	return c;
};

/**
 * ============================================================= 시간 콤포넌트 값 셋팅하기
 * 
 * @timeStr: '11:45 AM' or '6:45 PM' return: '1145' or '1845'
 */
com.timeGetValue = function(timeStr) {

	var p = timeStr.indexOf(" ");
	var c = timeStr.substring(p, timeStr.length);
	c = c.trim(c);

	var t = (timeStr.substr(0, p)).replace(/\:/g, '');
	if (t.length == 2) {
		t = '0' + t.substr(0, 1) + '0' + t.substr(1, 1);
	} else if (t.length == 3) {
		t = '0' + t;
	} else {
		t = t;
	}
	if (c == 'PM' && parseInt(t.substr(0, 2)) < 12) {
		var a = parseInt(t.substr(0, 2)) + 12;
		t = a + '' + t.substr(2, 2);
	}
	if (c == 'AM' && parseInt(t.substr(0, 2)) == 12) {
		var a = parseInt(t.substr(0, 2)) + 12;
		if (a == 24) {
			a = '00';
		}
		t = a + '' + t.substr(2, 2);
	}
	return t;
};

/**
 * ============================================================= 로딩바
 */
com.showLoading = function(bool) {
	if (bool) {
		$('#comLoadingBg').show();
		$('#comLoading').show();
	} else {
		$('#comLoadingBg').hide();
		$('#comLoading').hide();
	}
};

/**
 * ============================================================= action(button,
 * a 등) group Execute & Event regist
 * 
 * @pClasss : parentClass
 * @ftag : child tag
 * @sClass : select 된 Class
 * @dClass : defalut 된 Class / 없는경우 ''
 * @fidx: 처음 select 할 index
 * @ratu: child 에 기록된 value 의 attribute(속성)
 * @clickFn: click function
 */
com.actionGroupExecuteRegist = function(pClass, ftag, sClass, dClass, fidx,
		ratu, clickFn) {

	pClass = '.' + pClass;
	var param;
	$(pClass).find(ftag).on(
			"click",
			function(event) {
				event.preventDefault();
				if (!$(this).hasClass(sClass)) {
					if (dClass != '') {
						$(pClass).find('.' + sClass).removeClass(sClass)
								.addClass(dClass);
					} else {
						$(pClass).find('.' + sClass).removeClass(sClass);
					}
					$(this).removeClass(dClass).addClass(sClass);
				}
				// event
				param = $(this).attr(ratu);
				clickFn(param);
			});

	// first index
	var ftarget = $(pClass).find(ftag + ':eq(' + fidx + ')');
	if (!ftarget.hasClass(sClass)) {
		if (dClass != '') {
			$(pClass).find('.' + sClass).removeClass(sClass).addClass(dClass);
		} else {
			$(pClass).find('.' + sClass).removeClass(sClass);
		}
		ftarget.removeClass(dClass).addClass(sClass);
	}

	// first one event
	// ftarget = $(pClass).find('.' + sClass);
	// param = { 'value':ftarget.attr(ratu), 'text':ftarget.text() };
	// clickFn(param);

};

/**
 * ============================================================= ViewState의
 * enabled 처리를 시스템별 특징에 맞게 보완하여 적용하는 함수
 */
com.viewEnableFunc = function(elements, action) {
	var divExists = 0;
	$.each(elements, function(index, selector) {
		// overLay된 div의 존재여부 확인 0 = 없음, 나머지 있음
		divExists = $(selector + ' .disablewrap').length;
		if (action) {
			$(selector).css('position', 'relative');
			if (divExists == 0) {
				$(selector).append('<div class="disablewrap"></div>');
			}
			$(selector + ' .disablewrap').show();
		} else {
			$(selector).css('position', '');
			if (divExists) {
				$(selector + ' .disablewrap').hide();
			}
		}
	});
};

/**
 * ============================================================= 초기작업
 */
com.init = function(callbackFn) {

	// top로그에 url 등록
	// $('#aTopMainUrl').attr('href', com.url.main);

	// enter key disabled
	$(document).keypress(function(event) {
		if (event.which == '13') {
			if ($(event.target).is('textarea'))
				return;
			event.preventDefault();
		}
	});

	// domain root
	// com.docRoot = $('#inpContextPath').val();

	var param = {};
	// menu url
	// var url = $('#inpMenuUrl').val();
	// param['menuUrl'] = ( url == undefined || url == '' ) ? '' : url ;

	App.ro.init();
	App.ro.url = '/common/startMenu';
	App.ro.docRoot = com.docRoot;
	App.ro.param = param;
	App.ro.execute(function(data) {

		// logo 변경
		 if(data['userLang']=='ko'){
		 $('#mainTopLogoKo').show();
		 $('#mainTopLogoCn').hide();
		 } else {
		$('#mainTopLogoKo').hide();
		$('#mainTopLogoCn').show();
		 }

		// 메시지
		com.messageList = {};
		var objA = data['msg'];
		for (var idx = 0; idx < objA.length; idx++) {
			com.messageList[objA[idx]['msgCd']] = objA[idx]['message'];
		}

		// 날짜
		com.date.fullDate = data['date']['fullDate'];
		com.date.toDay = data['date']['toDay'];
		com.date.toYear = data['date']['toYear'];
		com.date.toMonth = data['date']['toMonth'];
		com.date.toTime = data['date']['toTime'];
		com.date.toMinute = data['date']['toMinute'];

		// // 메뉴권한설정
		// com.auth.menu = data['selMenuAuth'];
		//		
		// // 메뉴 네비게이션 만들기
		// com.menuNaviCreate( data['selMenuNavi'] );
		//		
		// // 메인메뉴 URL를 화면 상단 회사로고에 적용하기..
		// if( data['mainCheck']['existYn'] == 'Y' ){
		// if($('#topLogoLink').length > 0) {
		// var urltmp = com.docRoot + data['mainCheck']['url'] + '?menuTab=A';
		// $('#topLogoLink').attr('href', urltmp);
		// }
		// }
		//		
		com.auth.userId = data['userId'];
		$('#mainTopUser').text(com.auth.userId);

		// 메뉴 만들기
		var selMenuTab = $('#mainSelMenuTab').text();
		var selMenuUrl = $('#mainSelMenuUrl').text();

		// 메뉴A
		var easytreeObjA = com.treeSimpleTableClickRegist({
			treeId : 'mainNaviMenuA',
			setColumn : {
				'cdKey' : 'programCd',
				'upperCdKey' : 'upperProgramCd',
				'leafKey' : 'leaf',
				'rootValue' : 'root',
				'text' : 'title',
				'href' : 'url',
				'urlParam' : '?menuTab=A'
			},
			data : data['menuA'],
			open : true
		});
		if (selMenuTab == 'null' || selMenuTab == 'A') {
			if (selMenuUrl != 'null') {
				$(".navtab button").eq(0).trigger('click');
				com.treeSimpleActive({
					target : easytreeObjA,
					column : 'url',
					findValue : selMenuUrl
				});
			}
		}

		// 메뉴B
		var easytreeObjB = com.treeSimpleTableClickRegist({
			treeId : 'mainNaviMenuB',
			setColumn : {
				'cdKey' : 'programCd',
				'upperCdKey' : 'upperProgramCd',
				'leafKey' : 'leaf',
				'rootValue' : 'root',
				'text' : 'title',
				'href' : 'url',
				'urlParam' : '?menuTab=B'
			},
			data : data['menuB'],
			open : true
		});
		if (selMenuTab == 'B') {
			$(".navtab button").eq(1).trigger('click');
			if (selMenuUrl != 'null') {
				com.treeSimpleActive({
					target : easytreeObjB,
					column : 'url',
					findValue : selMenuUrl
				});
			}
		}

		// sys-config
		com.conf.logUse = data['logUse'];

		if (callbackFn != undefined)
			callbackFn();

	});

};

com.ageInit = function(callbackFn) {

	// enter key disabled
	$(document).keypress(function(event) {
		if (event.which == '13') {
			if ($(event.target).is('textarea'))
				return;
			event.preventDefault();
		}
	});

	var param = {};

	App.ro.init();
	App.ro.url = '/common/startAgeMenu';
	App.ro.docRoot = com.docRoot;
	App.ro.param = param;
	App.ro.execute(function(data) {

		// logo 변경
//		if (data['userLang'] == 'ko') {
//			$('#mainTopLogoKo').show();
//			$('#mainTopLogoCn').hide();
//		} else {
			$('#mainTopLogoKo').hide();
			$('#mainTopLogoCn').show();
//		}

		// 메시지
		com.messageList = {};
		var objA = data['msg'];
		for (var idx = 0; idx < objA.length; idx++) {
			com.messageList[objA[idx]['msgCd']] = objA[idx]['message'];
		}

		// 날짜
		com.date.fullDate = data['date']['fullDate'];
		com.date.toDay = data['date']['toDay'];
		com.date.toYear = data['date']['toYear'];
		com.date.toMonth = data['date']['toMonth'];
		com.date.toTime = data['date']['toTime'];
		com.date.toMinute = data['date']['toMinute'];

		com.auth.userId = data['userId'];
		$('#mainTopUser').text(com.auth.userId);

		// sys-config
		com.conf.logUse = data['logUse'];

		if (callbackFn != undefined)
			callbackFn();

	});

};
/**
 * ============================================================= 종합화면 이벤트 등록
 */
com.eventListener = function() {

	// //개인정보수정
	// $( "#btnPopPersonSetting").click(function() {
	// com.popupInit('popConfPerson.do', function(){
	//			
	// var param = new Object();
	// // 팝업오픈
	// com.popupShow(true, 'popConfPerson', _popConfPerson.exeFirstFn, param,
	// function(){});
	// });
	// });
	//	
	// 정보수정
	$("#mainInfoEdit").on('click', function() {
		com.util.moveUrl('/mobile/brmMemEdit');
		return;
	});

	// 로그아웃
	$("#mainTopLogout").on('click', function() {

		com.alert.confirm(com.message('V105'), true, function() {
			var url = '/logout';
			window.location.replace(url);
			return;
		}, function() {
			// 취소 ...
		});

	});

	// 알림창
	com.event.clickAndEnter([ {
		selector : '#alAlertOk, #alAlertClose',
		executeFn : function() {
			$('#alAlertBg').hide();
			$('#alAlert').hide();
			if (com.alert.okcallbackfn != undefined) {
				com.alert.okcallbackfn();
			}
		}
	} ]);

	// 컨펌창
	com.event.clickAndEnter([ {
		selector : '#alConfirmOk',
		rightKeyTarget : '#alConfirmCancel',
		executeFn : function() {
			$('#alConfirmBg').hide();
			$('#alConfirm').hide();
			com.alert.cfokcallbackfn();
		}
	}, {
		selector : '#alConfirmCancel, #alConfirmClose',
		leftKeyTarget : '#alConfirmOk',
		executeFn : function() {
			$('#alConfirmBg').hide();
			$('#alConfirm').hide();
			if (com.alert.cfcancelcallbackfn != undefined) {
				com.alert.cfcancelcallbackfn();
			}
		}
	} ]);

};

/**
 * ===== Application 공통 설정 ==========
 */
var App = Col.Create({
	docRoot : com.docRoot,
	logLevel : "debug" // debug 사용안할시 주석처리
});

/**
 * ======= Col 객체 생성 및 설정 =========
 */
App.store = Col.DataStore();
App.selector = Col.DataSelector();
App.validator = Col.Validator();
App.viewState = Col.ViewState();
App.viewState.enableFunc(com.viewEnableFunc);

App.ro = Col.Remote();
App.ro.preLoading = com.loadingStart;
App.ro.postLoading = com.loadingEnd;
App.ro.errorLoading = com.loadingError;
App.template = Col.template;
App.templatePaging = Col.templatePaging;

App.vo = {};

/**
 * ============================================================= document ready
 */
$(document).ready(function() {

	com.eventListener();

	console.log('[common.js] ready.......');

});
