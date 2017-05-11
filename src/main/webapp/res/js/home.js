$(document).ready(function(){
	$('.side-li').each(function(index){
		$(this).click(function(){
			$('.side-li').children().removeClass('li-active');
			$('.side-li').eq(index).children().addClass('li-active');
		});
	});
	$('.container-title a:first-child').click(function(){
		$('.side-bar').toggle('fast');
		$('.container').toggleClass('all-width');
	});
	$('.side-li:first').children().addClass('li-active');
});

angular.module('homeApp', []).controller('homeCtrl', function($scope){
	$scope.mode = 'person.html';
	$scope.chgPage = function(index){
		if(index == 0){
			$scope.mode = 'person.html';
		}else if(index == 1){
			$scope.mode = 'data-show.html';
		}else if(index == 2){
			$scope.mode = 'data-list.html';
		}
	};
});

// angular.module('infoApp', []);
