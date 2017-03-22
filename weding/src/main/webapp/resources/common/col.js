/* Col.js 0.7.0, @license MIT, (c) 2014 DaehanIns Inc + contributors */
/*
 * change log
 * 0.8
 * currStart, currEnd 추가
 *
 * 0.7
 * - Paginator 기능 추가
 * 0.6
 * - viewState에 post 기능 추가
 * 0.5
 * - Publish & Subscribe 추가
 * - doT.js 추가
 * 0.4
 * - ViewState, Validator 추가
 * 0.3
 * - DataConfig를 DataSelector로 대체
 * - DataSet을 DataStore로 변경
 */

(function(Col) { "use strict";

Col.config = {};
Col.Create = function (config) {
  if (config !== undefined)
    Col.config = config;
  return config;
};

function log() {
  try {
    console.log.apply(console, arguments);
  }
  catch(e) {
    try {
      opera.postError.apply(opera, arguments);
    }
    catch(e) {
      alert(Array.prototype.join.call(arguments, " "));
    }
  }
}

function logInfo() {
  if (Col.config.logLevel !== undefined && Col.config.logLevel === "info") {
    log.apply(this, arguments);
  }
}

function logDebug() {
  if (Col.config.logLevel !== undefined && Col.config.logLevel == "debug" || Col.config.logLevel == "info" ) {
    log.apply(this, arguments);
  }
}

Col.Logger = function() {
  return logDebug;
};

// 데이터와 뷰의 상호 연동 처리
// 사용 attr : ex) col-data="engCd" col-name="에너지코드" col-check="" col-conv="3comma"
Col.DataSelector = function() {

  var self = Col.observable({});

  self.getParam = function(selector, callback) {
    // TODO:: col-conv 적용
    var po = {};
    // selector가 jQuery or string 에 따른 처리
    var colQuery = selector instanceof jQuery ? selector : $(selector);
    colQuery.find("[col-data]").each(function(index, elem) {
      var key = $(elem).attr("col-data");
      // TODO:: input, select, checkbox, radio 등 form elements 정상작동 확인 필요
      if ($(elem).is('select')) {
        po[key] = $(elem).find('option:selected').val();
      } else if ($(elem).is('input[type=checkbox]')) {
        po[key] = $(elem).is(':checked') ? 'Y' : 'N';
      } else if ($(elem).children('input').is('input[type=checkbox]') ) {
    	  po[key] = $(elem).children('input').is(':checked') ? 'Y' : 'N';
      } else if ($(elem).is('input')) {
        po[key] = $(elem).val();
      } else if ($(elem).is('textarea')) {
    	po[key] = $(elem).val();
      } else {
        po[key] = $(elem).text();
      }
    });
    // callback 이 있는 경우 처리
    if (callback !== undefined && typeof callback == 'function') {
      callback();
    }
    return po;
  };

  self.setValue = function(selector, value, callback) {
    // TODO:: col-conv 적용
    // selector가 jQuery or string 에 따른 처리
    var colQuery = selector instanceof jQuery ? selector : $(selector);
    colQuery.find("[col-data]").each(function(index, elem) {
      var key = $(elem).attr("col-data");
      if (value.hasOwnProperty(key)) {
        // TODO:: input, select, checkbox, radio 등 form elements 정상작동 확인 필요
        if ($(elem).is('input')) {
          $(elem).val(value[key]);
        } else if( $(elem).is('textarea') ){
            $(elem).val(value[key]);
        } else if( $(elem).is('select') ){
          $(elem).val(value[key]);
        } else {
          $(elem).text(value[key]);
        }
      }
    });
    // callback 이 있는 경우 처리
    if (callback !== undefined && typeof callback == 'function') {
      callback();
    }
    return self;
  };

  self.validate = function(selector, validator) {
  return validator.validate(selector);
  };

  // number를 천단위 3자리마다 ',' 표시
  function numberWithCommas(num) {
      var parts = num.toString().split(".");
      parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
      return parts.join(".");
  }

  return self;
};

// 데이터셋 캐싱처리
Col.DataStore = function() {

  var self = Col.observable({});

  self.datas = {};

  self.add = function(name, value) {
    if (value === undefined) {
      return self.datas[name];
    }
    // Array에 포함된 Object에 rid 프로퍼티 추가(unique)
    $.each(value, function(index, item) {
      item.rid = index;
    });

    self.datas[name] = value;
    return self;
  };

  self.append = function(name, value) {
    if (self.datas[name]) {
      self.datas[name].push(value);
    } else {
      self.datas[name] = [].push(value);
    }

    return self;
  };

  self.remove = function(name) {
    delete self.datas[name];
    return self;
  };

  self.getData = function(name, index) {
    if (index === undefined) {
      return self.datas[name];
    }

    return self.datas[name][index];
  };

  return self;

};

// 뷰 상태처리
Col.ViewState = function() {

  var self = Col.observable({});

  self.currState = "";  // 현재 작업하는 상태
  self.lastState = "";  // 최종 반영한 상태
  self.states = {};     // 상태s
  self.callbackFns = {};   // 첵크함수

//  self.postState = {};
//  self.postFn = {};  // 상태반영후 공통반영 처리

  self.add = function(name, config, callbackFunc) {
    self.states[name] = config;
    if (callbackFunc !== undefined && typeof callbackFunc === "function") {
      self.callbackFns[name] = callbackFunc;
    } else {
      self.callbackFns[name] = (function() { return true; });
    }
    return self;
  };

  self.postAdd = function(name, config, checkFunc) {
    self.postState = config;
    if (checkFunc !== undefined && typeof checkFunc === "function") {
      self.postFn = checkFunc;
    } else {
      self.postFn = (function() { return true; });
    }
    return self;
  };

  self.remove = function(name) {
    delete self.states[name];
    return self;
  };

  self.enableFunc = function(enableFunc) {
    self.actionEnabled = enableFunc;
  };

  var CHANGE = {
    ENABLED  : "enabled",
    DISABLED : "disabled",
    VISIBLED : "visibled",
    CLEAR    : "clear"
  };

  self.change = function(name, cacheable, isPost) {

    // cachaeble이 true 인 경우, 이전 상태와 동일하면 수행하지 않음, 기본은 false
    if (self.lastState === name && (cacheable !== undefined && cacheable === true)) {
      return self;
    }

    if (self.currState === "") {
      self.currState = name;
    }

    var state = self.states[name];
    if (state.dependOn !== undefined && self.states[state.dependOn]) {
      self.change(state.dependOn, cacheable);
    }

    // 상태변경처리
    changeAction(state);
    // 콜백함수 실행
    if (self.callbackFns[name] !== undefined) {
      self.callbackFns[name]();
    }

    // ViewState change 완료 이벤트
    if (self.currState === name) {
      self.currState = "";
      self.lastState = name;
      // 공통 반영할 상태 (Post) 반영
      changePost();
      self.trigger("changed", name);
    }

    return self;
  };

  function changePost() {
    if (self.postFn !== undefined && self.postState !== undefined && self.postFn() === true ) {
      changeAction(self.postState);
    }
  }

  self.clear = function(elements, except) {
    procClear(elements, except);
  };

  function changeAction(state) {

    $.each(state, function(key, value) {
      switch(key) {
        case CHANGE.ENABLED :
            if (value["false"]) {
            	procDisabled(value["false"], false);
            	procEnabled(value["false"], false);
            }
            if (value["true"])  {
            	procDisabled(value["true"], true);
            	procEnabled(value["true"], true);
            }
            break;
        case CHANGE.DISABLED :
            if (value["false"]) procDisabled(value["false"], false);
            if (value["true"])  procDisabled(value["true"], true);
             break;
        case CHANGE.VISIBLED :
            if (value["false"]) procVisibled(value["false"], false);
            if (value["true"])  procVisibled(value["true"], true);
            break;
        case CHANGE.CLEAR :
            if (value["target"]) {
              if (value["except"]) {
                procClear(value["target"], value["except"]);
              } else {
                procClear(value["target"]);
              }
            }
            break;
        default :
      }
    });

  };


  function procEnabled(elements, action) {
    self.actionEnabled(elements, action);
  };

  function procDisabled(elements, action) {
    $.each(elements, function(index, selector) {
      if (action === true) {
      // 이하 모든것
        $(selector).find('*').prop('disabled', action);
        // 자신
        $(selector).prop('disabled', action);
      } else {
        $(selector).find('*').prop('disabled', action);
        $(selector).prop('disabled', action);
      }
    });
  };

  function procVisibled(elements, action) {
    $.each(elements, function(index, selector) {
      if (action === true) {
//        $(selector).css('display', 'block');
        $(selector).show();
      } else {
//        $(selector).css('display', 'none');
        $(selector).hide();
      }
    });
  };

  function procClear(elements, except) {
    var notElem = "";
    if (except !== undefined) {
      notElem = except.join(', ');
    }
    $.each(elements, function(index, selector) {
      $(selector + " :input").not(':button, :submit, :reset, :checkbox, :radio').not(notElem).val('');
      $(selector + " :checkbox").not(notElem).prop('checked', false);
      $(selector + " :radio").not(notElem).prop('checked', false);
//      $(selector + " option").not(notElem).prop('selected', false);
      $(selector + " select").each(function(){
    	  $(this).find('option').first().prop('selected', true);
      });

    });

  };

  return self;
};


// 서버데이터 조회처리
Col.Remote = function(el) {
  if (el === undefined)
    el = {};

  var self = Col.observable(el);

  var DEFAULT_URL = "/col/remote";

  var paramObject = {};

  self.init = function(value) {
	if (value === undefined) {
		value = {};
	}
    self.type        = 'POST';
    self.dataType    = value.dataType || 'json';
    self.loadingBar  = value.loadingBar  || '#spinner';
    self.preLoading  = value.preLoading;
    self.postLoading = value.postLoading;
    self.useLoading  = value.useLoading || true;
    self.errorLoading= value.errorLoading;
    self.url         = value.url || DEFAULT_URL;
    self.param       = value.param || {};
    self.dest        = value.dest || '';
    self.docRoot     = value.docRoot || Col.config.docRoot || '';
    self.getParamTag = value.getParamTag || '';
    self.setValueTag = value.setValueTag || '';
    self.storeKey    = value.storeKey || '';
    return self;
  };

  self.init(el);

  self.dataStore = function(store, keyName) {
    self.store = store;
    if (keyName !== undefined) {
      self.storeKey = keyName;
    }
  };

  self.execute = function(fn) {
    // 로딩바 보이기
    // $(self.loadingBar).show();
	  if (self.preLoading !== undefined && self.useLoading === true) {
	    if (typeof self.preLoading == "function" ) {
	      self.preLoading();
	    } else {
	      throw new Error("Col.Remote.execute(): preLoading은 function이어야 합니다.");
	    }
	  }

    // 기본 URL 처리
    if (self.url === undefined || self.url === "") {
      self.url = DEFAULT_URL;
    }
    // SqlMap 처리
    if (self.dest !== undefined && self.param !== undefined && self.param instanceof Object) {
        self.param["colDest"] = self.dest;
    }
    // getParamTag가 정의되어 있으면 파라미터에 추가
    if (self.getParamTag !== '') {
      paramObject = Col.DataSelector().getParam(self.getParamTag);
    }

    logDebug("##################################################################");
    logDebug("# request: ", self.docRoot + self.url, "("+ self.type + ")");
    logDebug("# params: ");
    logDebug(self.param);
//    console.log(self.param);

    $.ajax({
      type: self.type,
      url:  self.docRoot + self.url,
      data: $.extend({}, paramObject, self.param),
      dataType: self.dataType,
      success: function(data) {

        logDebug("#--------------------------------------------------------------");
        logDebug("# reponse: ", self.docRoot + self.url, "("+ self.type + ")");
        logDebug("# length: ", (data === null || typeof data !== "Array" ? 0 : data.length));
        logDebug("# result data: ");
        logDebug(data);
//        console.log(data);

        // 로딩바 감추기
        // $(self.loadingBar).hide();
      if (self.postLoading !== undefined && self.useLoading === true) {
        if (typeof self.postLoading == "function") {
          self.postLoading();
        } else {
          throw new Error("Col.Remote.execute(): postLoading은 function이어야 합니다.");
        }
      }

        if (data !== null && data.hasOwnProperty('result')) {
          self.result = data.result;
        }
        // 데이터 캐싱처리
        if (self.store !== undefined && self.storeKey !== undefined) {
          self.store.add(self.storeKey, self.result);
        }

        // TODO:: setValueTag 처리 추가 필요

        // callback 함수 실행
        if (fn !== undefined && typeof fn === "function") {
        	fn(data);
        	/*
          if (self.dest === '') {
        	  fn(data);
          } else {
        	  fn(data.result);
          }
          */
        }
        // 실행완료 이벤트
        self.trigger("success", data, self.dest);
      },
      error: function(xhr, status, error) {

        // 로딩바 감추기 ..
    	com.showLoading(false);
		com.alert.auto(
				'Network Connection Error !  Please contact to System Administrator',
				true,
				function(){
				}
		);

        if (self.errorLoading !== undefined) {
          if (typeof self.errorLoading == "function") {
            self.errorLoading(xhr, status, error);
          } else {
            throw new Error("Col.Remote.execute(): errorLoading은 function(xhr, status, error) 이어야 합니다.");
          }
        }

        // 실행완료 이벤트
        self.trigger("error", error);
      }
    });

    return self;
  };

  self.multipartExecute = function(fn) {
    // 로딩바 보이기
    // $(self.loadingBar).show();
    if (self.preLoading !== undefined && self.useLoading === true) {
      if (typeof self.preLoading == "function" ) {
        self.preLoading();
      } else {
        throw new Error("Col.Remote.multipartExecute(): preLoading은 function이어야 합니다.");
      }
    }

    // getParamTag가 정의되어 있으면 파라미터에 추가
    if (self.getParamTag !== '') {
      paramObject = Col.DataSelector().getParam(self.getParamTag);
    }

    logDebug("##################################################################");
    logDebug("# request: ", self.docRoot + self.url, "("+ self.type + ")");
    logDebug("# params: ");
    logDebug(self.param);
//    console.log(self.param);

    // formData = self.param   // self.param 을 formData 로 변환  ...

    $.ajax({
      type: 'POST',
      url:  self.docRoot + self.url,
      data: self.param,
      cache : false,
      dataType: self.dataType,
      processData : false,
      contentType : false,
      success: function(data) {

        logDebug("#--------------------------------------------------------------");
        logDebug("# reponse: ", self.docRoot + self.url, "("+ self.type + ")");
        logDebug("# length: ", (data === null || typeof data !== "Array" ? 0 : data.length));
        logDebug("# result data: ");
        logDebug(data);
//        console.log(data);

        // 로딩바 감추기
        // $(self.loadingBar).hide();
      if (self.postLoading !== undefined && self.useLoading === true) {
        if (typeof self.postLoading == "function") {
          self.postLoading();
        } else {
          throw new Error("Col.Remote.multipartExecute(): postLoading은 function이어야 합니다.");
        }
      }

        if (data !== null && data.hasOwnProperty('result')) {
          self.result = data.result;
        }
        // 데이터 캐싱처리
        if (self.store !== undefined && self.storeKey !== undefined) {
          self.store.add(self.storeKey, self.result);
        }

        // TODO:: setValueTag 처리 추가 필요

        // callback 함수 실행
        if (fn !== undefined && typeof fn === "function") {
        	fn(data);
        	/*
          if (self.dest === '') {
        	  fn(data);
          } else {
        	  fn(data.result);
          }
          */

        }
        // 실행완료 이벤트
        self.trigger("success", data, self.dest);
      },
      error: function(xhr, status, error) {
        // 로딩바 감추기
        // $(self.loadingBar).hide();
        if (self.errorLoading !== undefined) {
          if (typeof self.errorLoading == "function") {
            self.errorLoading(xhr, status, error);
          } else {
            throw new Error("Col.Remote.multipartExecute(): errorLoading은 function(xhr, status, error) 이어야 합니다.");
          }
        }

        // 실행완료 이벤트
        self.trigger("error", error);
      }
    });

    return self;
  };

  return self;
};

Col.Validator = function() {
  var self = Col.observable({});

  var checkConfigs = {};

  function findCheckCondition(selector) {
    // 첵크대상 항목의 check 설정내용을 구함
    var checks = {};
    $(selector + " [col-check]").each(function(index, elem) {
      var colData = $(elem).attr("col-data");
      var colName = $(elem).attr("col-name") || $(elem).attr("title") || colData;
      var colCheck = $(elem).attr("col-check");
      checks[colData] = [parseColCheck(colCheck), colName];
    });

    checkConfigs[selector] = checks;
  };

  // Validation 타입 상수
  var CHECK = {
    REQUIRED  : "required",
    DATE      : "date",
    NUMBER    : "number",
    INTEGER   : "integer",
    ALPHA     : "alpha",
    ALPHANUM  : "alphanum",
    HANGUL    : "hangul",
    URL       : "url",
    EMAIL     : "email",
    MINBYTES  : "minbytes",
    MAXBYTES  : "maxbytes",
    MINLENGTH : "minlength",
    MAXLENGTH : "maxlength",
    LENGTH    : "length",
    MIN       : "min",
    MAX       : "max",
    RANGE     : "range",
    MINCHECK  : "mincheck",
    CHECK     : "check",
    PATTERN   : "pattern",
    EQUALTO   : "equalto"
  };

  var MESSAGE = {
    REQUIRED  : "{0}은(는) 필수입력 항목입니다.",
    DATE      : "{0}은(는) 날짜만 입력 가능합니다.",
    NUMBER    : "{0}은(는) 숫자만 입력 가능합니다.",
    INTEGER   : "{0}은(는) 정수만 입력 가능합니다.",
    ALPHA     : "{0}은(는) 영문만 입력 가능합니다.",
    ALPHANUM  : "{0}은(는) 영숫자만 입력 가능합니다.",
    HANGUL    : "{0}은(는) 한글만 입력 가능합니다.",
    URL       : "{0}은(는) URL형식만 입력 가능합니다.",
    EMAIL     : "{0}은(는) 잘못된 이메일 형식입니다.",
    MINBYTES  : "{0}은(는) 최소 {1}Byte 이상 입력하셔야 합니다.",
    MAXBYTES  : "{0}은(는) 최대 {1}Byte 이하로 입력하셔야 합니다.",
    MINLENGTH : "{0}은(는) 최소 {1}글자 이상 입력하셔야 합니다.",
    MAXLENGTH : "{0}은(는) 최대 {1}글자 이하로 입력하셔야 합니다.",
    LENGTH    : "{0}은(는) {1}~{2}글자 내에서 입력하셔야 합니다.",
    MIN       : "{0}은(는) {1}이상 이어야 합니다.",
    MAX       : "{0}은(는) {1}이하 이어야 합니다.",
    RANGE     : "{0}은(는) {1}~{2} 사이의 값이어야 합니다.",
    MINCHECK  : "{0}은(는) 최소 {1}개 이상 첵크하셔야 합니다.",
    CHECK     : "{0}은(는) {1}~{2}개 를 체크하셔야 합니다.",
    PATTERN   : "{0}은(는) 입력형식이 올바르지 않습니다.",
    EQUALTO   : "{0}은(는) {1}과 같은 값이어야 합니다."
  };
  // TODO::
  // 상대비교 확장할 것 - GT, GE, LT, LE, EQUALTO

  function parseColCheck(colCheck) {
    /*
      col-check="required number digits email url minlength:6 maxlength:6 length:6,10"
                "min:6 max:10 range:6,10 pattern:\d+ check:1,3 equalto:#anotherid"
     */
    // 문자열 trim 후 공백으로 분리
    var checkArray = colCheck.replace(/^\s+|\s+$/g, "").replace(/\s{2,}/g, ' ').split(' ');
    // var checkArray = colCheck.replace(/^\s+|\s+$/g, "").split(/\s*/);

    var checkConfig = { "required": false};
    var cKey = "";
    var cValue = "";
    $.each(checkArray, function(index, item){
      if (item.indexOf(":") === -1) {
        // 독립형 첵크 - required, number, email, url...
        checkConfig[item] = true;
        return true; // continue
      } else {
        var checktem = item.split(":");
        cKey   = checktem[0];
        cValue = checktem[1];

        if (cValue.indexOf(",") === -1) {
          // 단일조건값 첵크 - minlength, maxlength, pattern, equalto...
          if (cKey === CHECK.PATTERN || cKey === CHECK.EQUALTO) {
            // 문자열 값
            // pattern, equalto
            checkConfig[cKey] = cValue;
          } else if (cKey === CHECK.MIN || cKey === CHECK.MAX) {
            // 실수 값
            // min, max
            checkConfig[cKey] = parseFloat(cValue);
          } else {
            // 정수 값
            // minlength, maxlength
            checkConfig[cKey] = parseInt(cValue);
          }
        } else {
          // 구간 조건값   TODO:: 선택 조건값 추가 고려할 것
          var cValueArr = cValue.split(",");
          if (cKey === CHECK.RANGE) {
            // 실수 값 range
            checkConfig[cKey] = [parseFloat(cValueArr[0]), parseFloat(cValueArr[1])];
          } else {
            // 정수 값 length, check
            checkConfig[cKey] = [parseInt(cValueArr[0]), parseInt(cValueArr[1])];
          }
        }
      }

    }); // end each

    return checkConfig;
  };

  // Validation 오류시 오류내용을 보관
  self.errors = [];

  self.validate = function(selector) {

    if (checkConfigs[selector] === undefined) {
      findCheckCondition(selector);
    }

    // 첵크대상 항목의 값을 구함
    var values = {};
    $(selector + " [col-check]").each(function(index, elem) {
      var colData = $(elem).attr("col-data");

      // values[colData] = $(elem).val();
      // 아래와 같이 각 element의 타입에 따라 적절한 값을 구한다.  by jsj 2014.06.24
      if ($(elem).is('select')) {
    	values[colData] = $(elem).find('option:selected').val();
	  } else if ($(elem).is('input[type=checkbox]')) {
		values[colData] = $(elem).is(':checked') ? 'Y' : 'N';
	  } else if ($(elem).is('input')) {
		values[colData] = $(elem).val();
	  } else if ($(elem).is('textarea')) {
		values[colData] = $(elem).val();
	  } else {
		values[colData] = $(elem).text();
	  }

    });

    // errors 초기화
    self.errors.length = 0;
    // checks = [ colCheckObject, colName]
    var checkConfig = checkConfigs[selector];
    $.each(values, function(key, value) {
      runValidation(value, checkConfig[key], key);
    });

    return self.errors.length > 0 ? false : true ;
  };

  function runValidation(value, aCheckConfig, key) {
    var checkObj = aCheckConfig[0];
    var checkName = aCheckConfig[1];

    var required = checkObj[CHECK.REQUIRED];
    // 필수여부 검증
    if (required === true && self.isEmpty(value)) {
      self.errors.push(getErrorInfo(key, checkName, CHECK.REQUIRED));
      return;
    }

    // 값이 empty인 경우는 validation 불필요
    if (self.isEmpty(value)) {
      return;
    }

    // validation 수행
    $.each(checkObj, function(cKey, cValue) {
      switch (cKey) {
        case CHECK.DATE :
            if (!self.isDate(value))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.NUMBER :
            if (!self.isNumber(value))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.INTEGER :
            if (!self.isInteger(value))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.DIGITS :
            if (!self.isDigits(value))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.ALPHA :
            if (!self.isAlpha(value))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.ALPHANUM :
            if (!self.isAlphanum(value))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.HANGUL :
            if (!self.isHangul(value))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.URL :
            if (self.isUrl(value))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.EMAIL :
            if (!self.isEmail(value))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.MINBYTES :
            if (!self.checkMinBytes(value, cValue))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.MAXBYTES :
            if (!self.checkMaxBytes(value, cValue))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.MINLENGTH :
            if (!self.checkMinLength(value, cValue))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.MAXLENGTH :
            if (!self.checkMaxLength(value, cValue))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.LENGTH :
            if (!self.checkLength(value, cValue[0], cValue[1]))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue[0], cValue[1]));
            break;
        case CHECK.MIN :
            if (!self.checkMin(value, cValue))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.MAX :
            if (!self.checkMax(value, cValue))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.RANGE :
            if (!self.checkRange(value, cValue[0], cValue[1]))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue[0], cValue[1]));
            break;
        case CHECK.MINCHECK :
            if (!self.checkMinCheck(value, cValue))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.CHECK :
            if (!self.checkCheck(value, cValue[0], cValue[1]))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue[0], cValue[1]));
            break;
        case CHECK.PATTERN :
            if (!self.checkPattern(value, cValue))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
        case CHECK.EQUALTO :
             if (!self.checkEqualto(value, cValue))
              self.errors.push(getErrorInfo(key, checkName, cKey, cValue));
            break;
       default:
      }

    });

  };

  function getErrorInfo(key, checkName, cKey, cValue1, cValue2) {
    var errMsg = MESSAGE[cKey.toUpperCase()];
    errMsg = errMsg.replace("{0}", checkName);
    if (cValue1 !== undefined) errMsg = errMsg.replace("{1}", cValue1);
    if (cValue2 !== undefined) errMsg = errMsg.replace("{2}", cValue2);

    return {id:key, name:checkName, msg:errMsg};
  };


  var regexIsEmpty = /^\s+$/;
  self.isEmpty = function(value) {
    return (value === null || value === undefined || value === '' || regexIsEmpty.test(value));
  };
  var regexIsDate = /^(19|20)\d\d[\-\/.]?(0[1-9]|1[012])[\-\/.]?(0[1-9]|[12][0-9]|3[01])$/;
  self.isDate = function(value) {
    return regexIsDate.test(value);
  };
  var regexIsNumber = /^(\+|-)?(\d+\.?\d*|\d*\.?\d+)$/;
  self.isNumber = function(value) {
    // return /^\d{1,3}(,\d{3})*(\.\d+)?$/.test(value);    // 99,999,999.99
    return regexIsNumber.test(value);
  };
  // var regexIsInteger = new RegExp("^\\d+$");
  var regexIsInteger = /^\d+$/;
  self.isInteger = function(value) {
    return regexIsInteger.test(value);
  };
  var regexIsDigits = /^[0-9]+$/;
  self.isDigits = function(value) {
    return regexIsDigits.test(value);
  };
  var regexIsAlpha = /^[a-zA-Z]*$/;
  self.isAlpha = function(value) {
    return regexIsAlpha.test(value);
  };
  var regexAlphanum = /^[a-zA-Z][a-zA-Z0-9]*$/;
  self.isAlphanum = function(value) {
    return regexAlphanum.test(value);
  };
  var regexIsHangul = /^([가-힣ㄱ-ㅎㅏ-ㅣ]*)$/;
  self.isHangul = function(value) {
    return regexIsHangul.test(value);
  };
  var regexIsUrl = /[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/;
  self.isUrl = function(value) {
    return regexIsUrl.test(value);
  };
  var regexIsEmail = /^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/;
  self.isEmail = function(value) {
    return regexIsEmail.test(value);
  };
  // 문자열 byte길이 계산
  function stringByteLength(s, b, i, c) {
    for(b=i=0;c=s.charCodeAt(i++); b+=c>>11?3:c>>7?2:1);
    return b;
  }
  self.checkMinBytes = function(value, minlength) {
    return (stringByteLength(value) >= minlength);
  };
  self.checkMaxBytes = function(value, maxlength) {
    return (stringByteLength(value) <= maxlength);
  };
  self.checkMinLength = function(value, minlength) {
    return (value.length >= minlength);
  };
  self.checkMaxLength = function(value, maxlength) {
    return (value.length <= maxlength);
  };
  self.checkLength = function(value, minlength, maxlength) {
    return (value.length >= minlength && value.length <= maxlength);
  };
  self.checkMin = function(value, min) {
    return (parseFloat(value.replace(',','')) >= min);
  };
  self.checkMax = function(value, max) {
    return (parseFloat(value.replace(',','')) <= max);
  };
  self.checkRange = function(value, start, end) {
    var checkNum = parseFloat(value.replace(',',''));
    return (checkNum >= start && checkNum <= end);
  };
  self.checkMinCheck = function(value, min) {
    return true;
  };
  self.checkCheck = function(value, min, max) {
    return true;
  };
  self.checkPattern = function(value, pattern) {
    var regex = new RegExp(pattern);
    return regex.test(value);
  };
  self.checkEqualto = function(value, selector) {
    return true;
  };

  return self;
};

// Publish & Subscribe
var channels = {};

// publish('some/channel', [....]);
Col.publish = function (channel, data) {
  var subscribes = channels[channel] || [];
  $.each(subscribes, function( index, handler ) {
    handler.apply(Col, data || []);
  });
};

// var handle = subscribe('some/channel', function (args...) {...});
Col.subscribe = function (channel, handler) {
  (channels[channel] = channels[channel] || []).push(handler);
  return [channel, handler];
};

// unsubscribe(handle);
Col.unsubscribe = function (handle) {
  var subscribes = channels[handle[0]],
    l = subscribes.length;

  while (l--) {
    if (subscribes[l] === handle[1]) {
      subscribes.splice(l, 1);
    }
  }
};

// -------------- common ------------------
// Observer 적용
Col.observable = function(el) {
  var callbacks = {}, slice = [].slice;

  el.on = function(events, fn) {
    if (typeof fn === "function") {
      events.replace(/[^\s]+/g, function(name, pos) {
        (callbacks[name] = callbacks[name] || []).push(fn);
        fn.typed = pos > 0;
      });
    }
    return el;
  };

  el.off = function(events) {
    events.replace(/[^\s]+/g, function(name) {
      callbacks[name] = [];
    });
    if (events == "*") callbacks = {};
    return el;
  };

  // only single event supported
  el.one = function(name, fn) {
    if (fn) fn.one = true;
    return el.on(name, fn);
  };

  el.trigger = function(name) {
    var args = slice.call(arguments, 1),
      fns = callbacks[name] || [];

    for (var i = 0, fn; (fn = fns[i]); ++i) {
      if (!fn.busy) {
        fn.busy = true;
        fn.apply(el, fn.typed ? [name].concat(args) : args);
        if (fn.one) { fns.splice(i, 1); i--; }
        fn.busy = false;
      }
    }

    return el;
  };

  return el;

};


// ROUTE 처리
/* Cross browser popstate */

// for browsers only
if (typeof top != "object") return;

var currentHash = "",
  pops = Col.observable({}),
  listen = window.addEventListener,
  doc = document;

function pop(hash) {
  hash = hash.type ? location.hash : hash;
  if (hash != currentHash) pops.trigger("pop", hash);
  currentHash = hash;
}

/* Always fire pop event upon page load (normalize behaviour across browsers) */

// standard browsers
if (listen) {
  listen("popstate", pop, false);
  doc.addEventListener("DOMContentLoaded", pop, false);

// IE
} else {
  doc.attachEvent("onreadystatechange", function() {
    if (doc.readyState === "complete") pop("");
  });
}

/* Change the browser URL or listen to changes on the URL */
Col.route = function(to) {
  // listen
  if (typeof to === "function") return pops.on("pop", to);

  // fire
  if (history.pushState) history.pushState(0, 0, to);
  pop(to);

};

//템플릿 생성 및 DOM에 결과 입력
Col.template = function (tmpl, data, target) {
  var result = doT.template(tmpl)(data);
  if (target !== undefined) {
    if (typeof target === 'string') {
      $(target).html(result);
    } else if (target instanceof jQuery) {
      target.html(result);
    }
  }
  return result;
};

Col.templatePaging = function (tmpl, data, target, pager) {
	Col.template(tmpl, data.result, target);
	pager.totalCount(data.totalCount);
	pager.changePage(data.pageNo);
}

//====== 필수 UI 컴포넌트 ============

// Paginator
Col.Paginator = function(pagingFunc) {

  var self = Col.observable({});

  var defaults = {
    pageTmpl  : '#pageTmpl',
    paginator : '#paginator',
    pageSize  : 10,
    displayPages: 10 ,
    buttonFl    : 'Y',
    buttonPn    : 'Y',
    useStartClick : true
  };

  var settings = {};

  var pageTmpl  = "";
  var maxPage     = 0;
  var totalRecords = 0;
  var currentPan  = 0;

  var pagingExcuted = false;	// pagingFunc이

  self.pagingFunc = pagingFunc;

  self.currentPage = 1;

  self.pagePk		= ''; 	// 페이지 pk

  self.reset = function() {
	  self.currentPage 	= 1;
	  self.pagePk 		= '';
  };

  self.init = function(options) {
	  settings = $.extend({}, defaults, options);
	  self.pageSize = settings.pageSize;
	  pageTmpl = $(settings.pageTmpl).html();
  };

  self.totalCount = function(cnt) {
	  totalRecords = cnt;
	  var lastPage = Math.ceil(cnt / self.pageSize);
	  if (maxPage === lastPage) return;
	  maxPage = lastPage;
	  redraw(self.currentPage);
  };

  self.totalPages = function(page) {
  if (maxPage === page) return;
    maxPage = page;
    redraw(self.currentPage);
  };

  self.getPageParams = function() {
	  return { pageNo : self.currentPage, pageSize : self.pageSize, pagePk : self.pagePk };
  }

  var isCreated = false;

  self.changePage = function(selectedPage, totalPage) {

//  if (self.currentPage === selectedPage) return ;
  if (totalPage !== undefined)  maxPage = totalPage;

    // 페이지 범위내로 설정
    selectedPage = selectedPage > maxPage ? maxPage : selectedPage;
    selectedPage = selectedPage < 1 ? 1 : selectedPage;

  self.currentPage = selectedPage;

  var pagePans = Math.ceil(selectedPage/ settings.displayPages);
  redraw(selectedPage);
  if (currentPan !== pagePans) {
    currentPan = pagePans;

    redraw(selectedPage);
  }

  if (!isCreated && settings.useStartClick) {
    isCreated = true;
    self.trigger("pageChanged", 1);
  }
  };

  function redraw(selectedPage) {

  var startPage = settings.displayPages * (currentPan - 1) + 1;
  var endPage   = settings.displayPages * currentPan;
  endPage = endPage > maxPage ? maxPage : endPage;

  var panList = [];
  for (var i = startPage; i <= endPage; i++) {
    panList.push(i);
  }
//  console.log("panList", panList);
  logDebug("panList", panList);

  var pageParam = {
      'pages': panList,
      'selected': selectedPage ,
      'buttonFl': settings.buttonFl,
      'buttonPn': settings.buttonPn,
      'totalRecords' : totalRecords,
      'maxPage' : maxPage,
      'pageSize' : settings.pageSize,
      'currStart' :  (totalRecords == 0)?0:settings.pageSize * (selectedPage-1) +1 ,
      'currEnd'	: (totalRecords == 0)?0:(selectedPage == maxPage)?totalRecords:settings.pageSize * (selectedPage-1) + settings.pageSize
    };


//  ( settings.pageSize * (selectedPage-1) +1 ) - (settings.pageSize * (selectedPage-1) + settings.pageSize)

  Col.template(pageTmpl, pageParam, settings.paginator);
  $(settings.paginator + " .pager").click(function() {

    var clickedPage = $(this).attr('page');

//      if (self.self.currentPage == clickedPage)  return;

    var goPage = 1;
    if (clickedPage === 'f') {
      goPage = 1;
    } else if (clickedPage == 'p') {
      goPage = (currentPan -1) * settings.displayPages;				// 1판 이전
      goPage = goPage < 1 ? 1 : goPage;
    } else if (clickedPage == 'n') {
        goPage = currentPan * settings.displayPages + 1;			// 1판 다음
        goPage = goPage < maxPage ? goPage : maxPage;
    } else if (clickedPage == 'l') {
      goPage = maxPage;
    } else {
      goPage = parseInt(clickedPage);
      var index = $(".pager").index(this);
      $(".pager").removeClass("selected");
      $(this).addClass("selected");
    }

    self.changePage(goPage);

    self.trigger("pageChanged", goPage);
  });
  }

  self.on("pageChanged", function(pageNo) {
		self.pagePk 		= '';
		self.currentPage  = pageNo;
		(pagingExcuted)? pagingFunc() : pagingExcuted = true;
	});

  return self;
};


//FileInput
Col.FileInput = function(options) {

	var self = Col.observable({});

	var defaults = {
			template	: '#fileInputTmpl',
			emSelector	: '#fileInput',
			maxFileSize	: 10485760,
			fileExt		: 'txt,jpg,jpeg,pdf,xls,xlsx,doc,docx,ppt,pptx,hwp,zip',
			validateFunc: function(){}
	};

    var settings = $.extend({}, defaults, options);
	var fileTmpl = '';

	self.state = "newInit";

	self.orgFile = {
			fileId : 0,
			fileName : ''
	};

	self.fileData = {};

	self.init = function(options) {
		fileTmpl = $(settings.template).html();
		draw();
	};

	var fileElement = {};
	self.changeState = function(newState) {
		fileElement = $(settings.emSelector);
		self.state = newState;
		switch (newState) {
		case 'newInit' :
			self.clear();
			fileElement.find("button[name='fileSelectBtn']").show();
			fileElement.find("button[name='fileDeleteBtn']").hide();
			fileElement.find("button[name='fileCancelBtn']").hide();
			fileElement.find("button[name='fileDownloadBtn']").hide();
			fileElement.find("[name='fileDeleteMark']").hide();
			break;
		case 'newModified' :
			fileElement.find("button[name='fileSelectBtn']").show();
			fileElement.find("button[name='fileDeleteBtn']").hide();
			fileElement.find("button[name='fileCancelBtn']").show();
			fileElement.find("button[name='fileDownloadBtn']").hide();
			fileElement.find("[name='fileDeleteMark']").hide();
			break;
		case 'notModified' :
			fileElement.find("[name='fileSelectBtn']").show();
			fileElement.find("[name='fileDeleteBtn']").show();
			fileElement.find("[name='fileCancelBtn']").hide();
			fileElement.find("[name='fileDownloadBtn']").show();
			fileElement.find("[name='fileDeleteMark']").hide();
			fileElement.find("[name='fileName']").removeClass("cu_filedel_txt");
			break;
		case 'dataModified' :
			fileElement.find("[name='fileSelectBtn']").show();
			fileElement.find("[name='fileDeleteBtn']").hide();
			fileElement.find("[name='fileCancelBtn']").show();
			fileElement.find("[name='fileDownloadBtn']").hide();
			fileElement.find("[name='fileDeleteMark']").hide();
			break;
		case 'dataDeleted' :
			fileElement.find("[name='fileSelectBtn']").hide();
			fileElement.find("[name='fileDeleteBtn']").hide();
			fileElement.find("[name='fileCancelBtn']").show();
			fileElement.find("[name='fileDownloadBtn']").hide();
			fileElement.find("[name='fileDeleteMark']").show();
			fileElement.find("[name='fileName']").addClass("cu_filedel_txt");
			break;
		default :
		};
		// redraw();
	};

	self.undoState = function() {
		fileElement = $(settings.emSelector);

		switch (self.state) {
		case 'newInit' :
		case 'newModified' :
			self.changeState("newInit");
			break;
		case 'notModified'  :
		case 'dataModified' :
		case 'dataDeleted'  :
			fileElement.find("input[name='fileName']").val(self.orgFile.fileName);
			self.changeState("notModified");
			break;
		default :
		};
	};

	self.clear = function() {
		self.state = "newInit";
		self.orgFile.fileId = 0;
		self.orgFile.fileName = "";
		fileElement = $(settings.emSelector);
		fileElement.find("input[name='fileName']").val("");
		fileElement.find("input[name='fileData']").val("");
	};

	self.setFileData = function(file) {
		// 파일을 등록함
		self.fileData = file;
		// 파일명을 표시함
		// fileElement = $(settings.emSelector);
		// fileElement.find("input[name='fileName'").val(file.name);
		switch (self.state) {
		case 'newInit' :
			self.changeState("newModified");
			break;
		case 'newModified' :
			// 그대로
			break;
		case 'notModified' :
			self.changeState("dataModified");
			break;
		case 'dataModified' :
			// 그대로
			break;
		case 'dataDeleted' :
			// 불가능 상태
			break;
		default :
		};
	};

	self.setFileInfo = function (fileInfo) {
		self.orgFile.fileId = fileInfo.fileId;
		self.orgFile.fileName = fileInfo.fileName;
		fileElement = $(settings.emSelector);
		fileElement.find("input[name='fileName']").val(fileInfo.fileName);
		if (fileInfo.fileId !== 0) {
			self.changeState("notModified");
		} else {
			self.changeState("newInit");
		}
	};

	self.getFileInfo = function () {
		var fileElementName = settings.emSelector.replace("#","");
		var result = {};
		if (!(self.fileData instanceof File)) {
//			self.fileData = $(settings.emSelector).find("input[name='fileData']").val();
			self.fileData = document.getElementById("uploadFile");
		}
		result[fileElementName] = self.fileData;
		result[fileElementName + "State"] = self.state;
		result[fileElementName + "OrgFileId"] = self.orgFile.fileId;
		result[fileElementName + "OrgFileName"] = self.orgFileName;

		return result;
	};

	function draw(state) {
		var fileParam = {};

		Col.template(fileTmpl, fileParam, settings.emSelector);
		if (state !== undefined) {
			self.changeState(state);
		} else {
			self.changeState("newInit");
		}
		setEventHandler();
	}

	function setEventHandler() {
		var fileElement = $(settings.emSelector);

		fileElement.find("[name='fileSelectBtn']").on('click', function(){
			fileElement.find("input[name='fileData']").click();
			return false;
		});
		// 취소버튼 클릭
		fileElement.find("[name='fileCancelBtn']").on('click', function(){
			self.undoState();
			return false;
		});

		// 삭제버튼 클릭
		fileElement.find("[name='fileDeleteBtn']").on('click', function(){
			self.changeState("dataDeleted");
			return false;
		});

		// 다운로드버튼 클릭
		fileElement.find("[name='fileDownloadBtn']").on('click', function(){
			com.util.excelDownloadSubmit('/file/download.do', {'fileId':self.orgFile.fileId});
			return false;
		});

		fileElement.find("input[name='fileData']").on('change', function(event){
			var files = event.target.files;
			var upfile = files[0];
			var maxSize = settings.maxFileSize;
			if (upfile.size > settings.maxFileSize) {
				settings.validateFunc(1, "업로드 가능한 최대 파일크기는 " + Math.round(maxSize/1024/1024,1) + "MB 입니다.");
				return;
			}
			var fileName = upfile.name;
			var pos = fileName.lastIndexOf(".");
			var fileExt = fileName.substring(pos+1);
			var matchedExt = false;
			var availableExt = settings.fileExt.split(",");
			for (var i=0; i < availableExt.length; i++) {
				if (fileExt === availableExt[i]) {
					matchedExt = true;
				}
			}
			if (!matchedExt) {
				settings.validateFunc(1, "업로드 가능한 파일의 확장자는 " + availableExt.toString() + "입니다.");
				return;
			}

			fileElement.find("input[name='fileName']").val(upfile.name);
			self.setFileData(upfile);
		});
	}

	return self;
};


})(typeof top == "object" ? window.Col = {} : exports);





// doT.js
// 2011, Laura Doktorova, https://github.com/olado/doT
// Licensed under the MIT license.

(function() {
  "use strict";

  var doT = {
    version: '1.0.1',
    templateSettings: {
      evaluate:    /\{\{([\s\S]+?(\}?)+)\}\}/g,
      interpolate: /\{\{=([\s\S]+?)\}\}/g,
      encode:      /\{\{!([\s\S]+?)\}\}/g,
      use:         /\{\{#([\s\S]+?)\}\}/g,
      useParams:   /(^|[^\w$])def(?:\.|\[[\'\"])([\w$\.]+)(?:[\'\"]\])?\s*\:\s*([\w$\.]+|\"[^\"]+\"|\'[^\']+\'|\{[^\}]+\})/g,
      define:      /\{\{##\s*([\w\.$]+)\s*(\:|=)([\s\S]+?)#\}\}/g,
      defineParams:/^\s*([\w$]+):([\s\S]+)/,
      conditional: /\{\{\?(\?)?\s*([\s\S]*?)\s*\}\}/g,
      iterate:     /\{\{~\s*(?:\}\}|([\s\S]+?)\s*\:\s*([\w$]+)\s*(?:\:\s*([\w$]+))?\s*\}\})/g,
      varname:  'it',
      strip:    true,
      append:   true,
      selfcontained: false
    },
    template: undefined, //fn, compile template
    compile:  undefined  //fn, for express
  }, global;

  if (typeof module !== 'undefined' && module.exports) {
    module.exports = doT;
  } else if (typeof define === 'function' && define.amd) {
    define(function(){return doT;});
  } else {
    global = (function(){ return this || (0,eval)('this'); }());
    global.doT = doT;
  }

  function encodeHTMLSource() {
    var encodeHTMLRules = { "&": "&#38;", "<": "&#60;", ">": "&#62;", '"': '&#34;', "'": '&#39;', "/": '&#47;' },
      matchHTML = /&(?!#?\w+;)|<|>|"|'|\//g;
    return function() {
      return this ? this.replace(matchHTML, function(m) {return encodeHTMLRules[m] || m;}) : this;
    };
  }
  String.prototype.encodeHTML = encodeHTMLSource();

  var startend = {
    append: { start: "'+(",      end: ")+'",      endencode: "||'').toString().encodeHTML()+'" },
    split:  { start: "';out+=(", end: ");out+='", endencode: "||'').toString().encodeHTML();out+='"}
  }, skip = /$^/;

  function resolveDefs(c, block, def) {
    return ((typeof block === 'string') ? block : block.toString())
    .replace(c.define || skip, function(m, code, assign, value) {
      if (code.indexOf('def.') === 0) {
        code = code.substring(4);
      }
      if (!(code in def)) {
        if (assign === ':') {
          if (c.defineParams) value.replace(c.defineParams, function(m, param, v) {
            def[code] = {arg: param, text: v};
          });
          if (!(code in def)) def[code]= value;
        } else {
          new Function("def", "def['"+code+"']=" + value)(def);
        }
      }
      return '';
    })
    .replace(c.use || skip, function(m, code) {
      if (c.useParams) code = code.replace(c.useParams, function(m, s, d, param) {
        if (def[d] && def[d].arg && param) {
          var rw = (d+":"+param).replace(/'|\\/g, '_');
          def.__exp = def.__exp || {};
          def.__exp[rw] = def[d].text.replace(new RegExp("(^|[^\\w$])" + def[d].arg + "([^\\w$])", "g"), "$1" + param + "$2");
          return s + "def.__exp['"+rw+"']";
        }
      });
      var v = new Function("def", "return " + code)(def);
      return v ? resolveDefs(c, v, def) : v;
    });
  }

  function unescape(code) {
    return code.replace(/\\('|\\)/g, "$1").replace(/[\r\t\n]/g, ' ');
  }

  doT.template = function(tmpl, c, def) {
    c = c || doT.templateSettings;
    var cse = c.append ? startend.append : startend.split, needhtmlencode, sid = 0, indv,
      str  = (c.use || c.define) ? resolveDefs(c, tmpl, def || {}) : tmpl;

    str = ("var out='" + (c.strip ? str.replace(/(^|\r|\n)\t* +| +\t*(\r|\n|$)/g,' ')
          .replace(/\r|\n|\t|\/\*[\s\S]*?\*\//g,''): str)
      .replace(/'|\\/g, '\\$&')
      .replace(c.interpolate || skip, function(m, code) {
        return cse.start + unescape(code) + cse.end;
      })
      .replace(c.encode || skip, function(m, code) {
        needhtmlencode = true;
        return cse.start + unescape(code) + cse.endencode;
      })
      .replace(c.conditional || skip, function(m, elsecase, code) {
        return elsecase ?
          (code ? "';}else if(" + unescape(code) + "){out+='" : "';}else{out+='") :
          (code ? "';if(" + unescape(code) + "){out+='" : "';}out+='");
      })
      .replace(c.iterate || skip, function(m, iterate, vname, iname) {
        if (!iterate) return "';} } out+='";
        sid+=1; indv=iname || "i"+sid; iterate=unescape(iterate);
        return "';var arr"+sid+"="+iterate+";if(arr"+sid+"){var "+vname+","+indv+"=-1,l"+sid+"=arr"+sid+".length-1;while("+indv+"<l"+sid+"){"
          +vname+"=arr"+sid+"["+indv+"+=1];out+='";
      })
      .replace(c.evaluate || skip, function(m, code) {
        return "';" + unescape(code) + "out+='";
      })
      + "';return out;")
      .replace(/\n/g, '\\n').replace(/\t/g, '\\t').replace(/\r/g, '\\r')
      .replace(/(\s|;|\}|^|\{)out\+='';/g, '$1').replace(/\+''/g, '')
      .replace(/(\s|;|\}|^|\{)out\+=''\+/g,'$1out+=');

    if (needhtmlencode && c.selfcontained) {
      str = "String.prototype.encodeHTML=(" + encodeHTMLSource.toString() + "());" + str;
    }
    try {
      return new Function(c.varname, str);
    } catch (e) {
      if (typeof console !== 'undefined') console.log("Could not create a template function: " + str);
      throw e;
    }
  };

  doT.compile = function(tmpl, def) {
    return doT.template(tmpl, null, def);
  };
}());
