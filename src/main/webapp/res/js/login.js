angular.module('loginApp', []).controller('loginCtrl', function($scope, $http){
	$scope.toLogin = function(username, userpass, usercode){
		$http.post('/login', $.param({
			'username': username,
			'password': userpass,
			'code': usercode
		}), {
			headers: {
				'Content-type': 'application/x-www-form-urlencoded'
			}
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
	$scope.hiddenError = function(){
		$scope.error = {
			errorField: '',
			errorMsg: ''
		};
	};
	// hiddenError();
});

angular.module('registerApp', []).controller('registerCtrl', function($scope, $http){
	$scope.error = {
		errorField: '',
		errorMsg: ''
	};

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
}).directive('passMatch', function(){
	return {
		restrict: 'A',
		require: 'ngModel',
		link: function(scope, element, attrs, ctrl){
			var targetCtrl = scope.$eval(attrs['passMatch']);
			targetCtrl.$parsers.push(function(viewValue){
				ctrl.$setValidity('passdiff', viewValue == ctrl.$viewValue);
				return viewValue;
			});
			ctrl.$parsers.push(function(viewValue){
				ctrl.$setValidity('passdiff', viewValue == targetCtrl.$viewValue);
				return viewValue;
			});
		}
	};
});