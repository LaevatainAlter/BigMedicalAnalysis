angular.module('homeApp').controller('dataCtrl', function($scope, $http, fileUpload){
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
	    	console.log(2);
	    	return;
	    }
	    var fd = new FormData();
		fd.append('file', file);
		$http.post('/toUploadFile', fd, {
			transformRequest: angular.identity,
			headers: { "Content-Type": undefined }
		}).then(function(req){
			$scope.analyze = req.data;
		}, function(error){

		});
	 };

	$scope.reflesh = function(){
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
				return ((d.y % 20) == 0);
			});
			var minEcg = d3.min(dataset1, function(d){
				return d.y;
			});
			var maxEcg = d3.max(dataset1, function(d){
				return d.y;
			});
			var maxMinute = dataset1[dataset1.length - 1].x;
			var xScale = d3.scaleLinear()
							.domain([0, maxMinute])
							.rangeRound([0, width - padding.right - padding.left]);
			var yScale = d3.scaleLinear()
							.domain([minEcg, maxEcg])
							.rangeRound([height - padding.top - padding.bottom, 0]);

			var svg = d3.select('#data-show-heart-rate .data-show-chart')
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

			var linePath = d3.line()
							.x(function(d){ return xScale(d.x); })
							.y(function(d){ return yScale(d.y); })
							.curve(d3.curveCatmullRom.alpha(0.5));

			svg.append('g')
				.append('path')
				.attr('class', 'line-path')
				.attr('transform', 'translate(' + padding.left + ',' + padding.top + ')')
				.attr('d', linePath(dataset1))
			  	.attr('fill', 'none')
				.attr('stroke-width', 1)
				.attr('stroke', 'green');

			svg.append('g')
				.selectAll('circle')
				.data(dataset1)
				.enter()
				.append('circle')
				.attr('r', 3)
				.attr('transform', function(d){
					return 'translate(' + (xScale(d.x) + padding.left) + ',' + (yScale(d.y) + padding.top) + ')'
				})
				.attr('fill', 'green');
		});
	};

	$scope.reflesh();

	$(window).resize(function(){
		width = $('.data-show-chart').width();
		height = width/2;
		d3.select('#data-show-heart-rate .data-show-chart svg').remove();
		var minEcg = d3.min(dataset1, function(d){
			return d.y;
		});
		var maxEcg = d3.max(dataset1, function(d){
			return d.y;
		});
		var maxMinute = dataset1[dataset1.length - 1].x;
		var xScale = d3.scaleLinear()
						.domain([0, maxMinute])
						.rangeRound([0, width - padding.right - padding.left]);
		var yScale = d3.scaleLinear()
						.domain([minEcg, maxEcg])
						.rangeRound([height - padding.top - padding.bottom, 0]);

		var svg = d3.select('#data-show-heart-rate .data-show-chart')
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

		var linePath = d3.line()
						.x(function(d){ return xScale(d.x); })
						.y(function(d){ return yScale(d.y); })
						.curve(d3.curveCatmullRom.alpha(0.5));

		svg.append('g')
			.append('path')
			.attr('class', 'line-path')
			.attr('transform', 'translate(' + padding.left + ',' + padding.top + ')')
			.attr('d', linePath(dataset1))
		  	.attr('fill', 'none')
			.attr('stroke-width', 1)
			.attr('stroke', 'green');

		svg.append('g')
			.selectAll('circle')
			.data(dataset1)
			.enter()
			.append('circle')
			.attr('r', 3)
			.attr('transform', function(d){
			return 'translate(' + (xScale(d.x) + padding.left) + ',' + (yScale(d.y) + padding.top) + ')'
			})
			.attr('fill', 'green');
	});
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
}).service( "fileUpload", function( $http ){
	this
});
