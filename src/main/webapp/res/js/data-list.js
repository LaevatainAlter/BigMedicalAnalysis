angular.module('homeApp').controller('listCtrl', function($scope, $http){
	$scope.getHistoryList = function () {
		$http({
			method: 'GET',
			params: {

			},
			url: '/toGetHistoryList'
		}).then(function(req){
			$scope.historyList = req.data.historyList;
		});
	};

	$scope.getHistoryList();
});