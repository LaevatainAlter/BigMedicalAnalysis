angular.module('loginApp', []).controller('loginCtrl', function($scope, $http){
	// $scope.error = {
	// 	errorField: '',
	// 	errorMsg: ''
	// };

	$scope.toLogin = function(username, userpass, usercode){
		$http({
            method: 'POST',
            url:'/login', data:'username='+username +
            '&password=' + userpass +
            '&code=' + usercode
        ,  headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }}
        ).then(function(req){
			if(req.data.status){
				console.log('success');
				window.location.href = 'home';
			}else{
				$scope.error.errorField = req.data.errorField;
				$scope.error.errorMsg = req.data.errorMsg;
			}
		}, function(error){

		});
	};
}).config(function($httpProvider){
	$httpProvider.defaults.withCredentials = true;
});

angular.module('registerApp', []).controller('registerCtrl', function($scope, $http){
	$scope.toRegister = function(username, password, repeatPass, usercode){
		$http.post('/register', {
			'username': username,
			'password': password,
			'repeat-password': repeatPass,
			'code': usercode
		}).then(function(req){
			if(req.data.status){
				console.log('success');
				window.location.href = 'home';
			}else{
				$scope.error.errorField = req.data.errorField;
				$scope.error.errorMsg = req.data.errorMsg;
			}
		}, function(error){

		});
	};
}).config(function($httpProvider){
	$httpProvider.defaults.withCredentials = true;
	$httpProvider.defaults.headers.post = {
		'Content-type': 'application/json'
	};
});