$(document).ready(function(){
	$('.person-li').each(function(index){//切换个人信息的导航
		$(this).click(function(){
			$('.person-nav').removeClass('change-person-li0').removeClass('change-person-li1').removeClass('change-person-li2').addClass('change-person-li' + index);
			$('.person-li').children().removeClass('active');
			$('.person-li').eq(index).children().addClass('active');
			$('#person-content0').hide();
			$('#person-content1').hide();
			$('#person-content2').hide();
			$('#person-content' + index).show();
		});
	});
	$('.person-info-edit').each(function(index){
		$(this).click(function(){
			$('.person-info-content').css('display', 'block');
			$('.person-info-edit').css('display', 'block');
			$('.person-info-input').css('display', 'none');
			$('.person-info-certain').css('display', 'none');
			$('.person-info-content').eq(index).css('display', 'none');
			$('.person-info-edit').eq(index).css('display', 'none');
			$('.person-info-input').eq(index).css('display', 'block');
			$('.person-info-certain').eq(index).css('display', 'block');
		});
	});
	$('.person-info-certain').each(function(index){
		$(this).click(function(){
			$('.person-info-content').eq(index).css('display', 'block');
			$('.person-info-edit').eq(index).css('display', 'block');
			$('.person-info-input').eq(index).css('display', 'none');
			$('.person-info-certain').eq(index).css('display', 'none');
		});
	});
});

angular.module('infoApp', []).controller('infoCtrl', function($scope, $http){
	$scope.getPerInfo = function(){
		$http({
			method: 'GET',
			params: {
				'userId': '123456'
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
			'userId': '123456',
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
			'userId': '123456',
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
			'userId': '123456',
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
				'userId': '123456'
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
});