angular.module('homeApp').controller('infoCtrl', function($scope, $http){
// angular.module('infoApp', []).controller('infoCtrl', function($scope, $http){
	$scope.getPerInfo = function(){
		$http({
			method: 'GET',
			params: {
				// 'userId': '123456'
			},
			url: '/toGetPerInfo'
		}).then(function(req){
			$scope.userInfo = req.data;
		}, function(error){

		});
	};
	$scope.getPerInfo();

	$scope.changePerInfo = function(type, value){
		$http.post('/toChangePerInfo', {
			// 'userId': '123456',
			'type': type,
			'value': value
		}).then(function(req){
			$scope.userInfo[req.data.type] = req.data.value;
			if(!req.data.status){
				alert(req.data.errorMsg);
			}
		}, function(error){

		});
	};

	$scope.verifyPass = function(oldPass){
		$http.post('/toVerifyPass', {
			// 'userId': '123456',
			'oldPass': oldPass
		}).then(function(req){
			console.log(req);
			if(!req.data.status){
				alert(req.data.errorMsg);
			}else{
				$('.person-info-content-pass').css('display', 'none');
				$('.person-info-input-pass').css('display', 'block');
				$('#person-pass-change').css('cursor', 'Pointer')
					.css('color', 'rgb(51, 154, 229)')
					.removeClass('click-disable');
			}
		}, function(error){

		});
	};

	$scope.changePass = function(oldPass, newPass, repeatPass){
		$http.post('/toChangePass', {
			// 'userId': '123456',
			'oldPass': oldPass,
			'newPass': newPass,
			'repeatPass': repeatPass
		}).then(function(req){
			$('.person-info-content-pass').css('display', 'block');
			$('.person-info-input-pass').css('display', 'none');
			$('#person-pass-change').css('cursor', 'not-allowed')
				.css('color', 'rgb(137, 147, 152)')
				.addClass('click-disable');
			if(!req.data.status){
				alert(req.data.errorMsg);
			}
		}, function(error){

		});
	};

	$scope.getLoginRecord = function(){
		$http({
			method: 'GET',
			params: {
				// 'userId': '123456'
			},
			url: '/toGetLoginRecord'
		}).then(function(req){
			console.log(req.data);
			$scope.loginRecord = req.data.loginRecord;
		}, function(error){

		});
	};
	// $scope.test.year =
	// $scope.test.month = 12;
}).config(function($httpProvider){
	$httpProvider.defaults.withCredentials = true;
	$httpProvider.defaults.headers.post = {
		'Content-type': 'application/json'
	};
}).directive('timeChange', function(){
	return {
		restrict: 'AE',
		scope: {
			birth: '='
		},
		template: `
			<input type="number" min="1900" max="{{maxYear}}" ng-model="year"/>-
			<input type="number" min="1" max="12" ng-model="month"/>-
			<input type="number" min="1" max="{{maxDay}}" ng-model="day"/>
		`,
		link: function(scope, element, attrs){
			var date = new Date();
			var dayLimit = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

			scope.$watch('birth', function(){
				if(!scope.birth){
					scope.birth = '';
				}
				var splitDate = scope.birth.split('-');
				if((splitDate.length != 3) && angular.isNumber(parseInt(splitDate[0])) &&
					angular.isNumber(parseInt(splitDate[1])) && angular.isNumber(parseInt(splitDate[2]))){
					splitDate[0] = 1900;
					splitDate[1] = 1;
					splitDate[2] = 1;
					scope.birth = scope.year + '-' + scope.month + '-' + scope.day;
				}
				scope.maxYear = date.getFullYear();
				scope.year = parseInt(splitDate[0]);
				scope.month = parseInt(splitDate[1]);
				scope.day = parseInt(splitDate[2]);
				if((scope.year%4==0 && scope.year%100!=0)||(scope.year%400==0)){
					dayLimit[1] = 29;
				}
			});

			scope.$watch('year', function(){
				if((scope.year%4==0 && scope.year%100!=0)||(scope.year%400==0)){
					dayLimit[1] = 29;
				}else{
					dayLimit[1] = 28;
				}
				scope.maxDay = dayLimit[scope.month - 1];
				if(scope.day > dayLimit[scope.month - 1]){
					scope.day = scope.maxDay;
				}
				scope.birth = scope.year + '-' + scope.month + '-' + scope.day;
			});
			scope.$watch('month', function(){
				scope.maxDay = dayLimit[scope.month - 1];
				if(scope.day > dayLimit[scope.month - 1]){
					scope.day = scope.maxDay;
				}
				scope.birth = scope.year + '-' + scope.month + '-' + scope.day;
			});
			scope.$watch('day', function(){
				scope.birth = scope.year + '-' + scope.month + '-' + scope.day;
			});
		}
	};
});
