angular.module('homeApp').controller('dataCtrl', function($scope, $http){
// angular.module('dataApp', []).controller('dataCtrl', function($scope, $http){
	var dataset1 = new Array(), dataset2 = new Array();
	var width = $('.data-show-chart').width();
	var height = width/2;
	var padding = { top: 50, right: 50, bottom: 50, left: 50 };
	$scope.filterParam = 10;
	$scope.analyze;

	$scope.sendFile = function(){
		var file = $scope.fileToUpload;
		if(!file){
	  	return;
	  }
	  var fd = new FormData();
		fd.append('file', file);
		$http.post('/toUploadFile', fd, {
			transformRequest: angular.identity,
			headers: { "Content-Type": undefined }
		}).then(function(req){
			$scope.analyze = req.data;
			$scope.reflesh1();
			// $scope.reflesh2();
		}, function(error){
		});
	 };

	$scope.reflesh1 = function(){
		$http({
			url: '/upload/Alice/data/out.txt',
			method: 'GET',
			responseType: 'arraybuffer'
		}).then(function(req){
			let dataview = new DataView(req.data);
			let ints = new Uint8ClampedArray(req.data.byteLength);
			for (var i = 0; i < ints.length; i++) {
      	ints[i] = dataview.getUint8(i);
        dataset1[i] = {
					x: i,
					y: ints[i]
				};
      }
			dataset1 = dataset1.filter(function(d){
				return ((d.x % 10) == 0);
			});
			new Chart(document.getElementById('myChart1').getContext('2d'), {
				type: 'line',
				data: {
					labels: dataset1.map(function(item){
						return item.x;
					}),
					datasets: [
						{
							label: '心率',
							fill: false,
            	borderColor: '#4bc0c0',
			        data : dataset1.map(function(item){
								return item.y;
							})
						}
					]
				}
			});
		})
	};
	$scope.reflesh2 = function(){
		$http({
			url: '/upload/Alice/data/out.txt',
			method: 'GET',
			responseType: 'arraybuffer'
		}).then(function(req){
			let dataview = new DataView(req.data);
			let ints = new Uint8ClampedArray(req.data.byteLength);
			for (var i = 0; i < ints.length; i++) {
      	ints[i] = dataview.getUint8(i);
        dataset2[i] = {
					x: i,
					y: ints[i]
				};
      }
			dataset2 = dataset2.filter(function(d){
				return ((d.x % 1) == 0);
			});
			var minEcg = d3.min(dataset2, function(d){
				return d.y;
			});
			var maxEcg = d3.max(dataset2, function(d){
				return d.y;
			});
			var maxMinute = dataset2[dataset2.length - 1].x;
			var xScale = d3.scaleLinear()
							.domain([0, maxMinute])
							.rangeRound([0, width - padding.right - padding.left]);
			var yScale = d3.scaleLinear()
							.domain([minEcg, maxEcg])
							.rangeRound([height - padding.top - padding.bottom, 0]);

			var svg = d3.select('#data-show-poincare .data-show-chart')
						.append('svg')
						.attr('width', width + 'px')
						.attr('height', height + 'px');
			var xAxis = d3.axisBottom()
							.scale(xScale);
			var yAxis = d3.axisLeft()
							.scale(yScale);

			svg.append('g')
				.attr('class', 'axis')
				.attr('transform', 'translate(' + padding.left + ', ' + (height - padding.bottom) + ')')
				.call(xAxis);
			svg.append('g')
				.attr('class', 'axis')
				.attr('transform', 'translate(' + padding.left + ', ' + padding.top + ')')
				.call(yAxis);

			svg.append('g')
				.selectAll('circle')
				.data(dataset2)
				.enter()
				.append('circle')
				.attr('r', 3)
				.attr('transform', function(d){
					return 'translate(' + (xScale(d.x) + padding.left) + ',' + (yScale(d.y) + padding.top) + ')'
				})
				.attr('fill', 'green');
		});
	};
}).directive( "fileModel", function( $parse ){
  return {
    restrict: "A",
    link: function( scope, element, attrs ){
      var model = $parse( attrs.fileModel );
      var modelSetter = model.assign;

      element.bind( "change", function(){
        scope.$apply( function(){
          modelSetter( scope, element[0].files[0] );
        } );
      } );
    }
  }
});
