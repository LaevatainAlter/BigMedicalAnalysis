angular.module('homeApp').controller('dataCtrl', function($scope, $http, Upload){
// angular.module('dataApp', []).controller('dataCtrl', function($scope, $http){
	var datasetOrigin1 = new Array(), datasetOrigin2 = new Array();
	var width = $('.data-show-chart').width();
	var height = width/2;
	var padding = { top: 50, right: 50, bottom: 50, left: 50 };
	var maxRate = 125;
	var minRate = 80;
	$scope.filterParam1 = 10;
	$scope.circleR1 = 4;
	$scope.grid1 = true;
	$scope.assist1 = true;
	$scope.noResult = true;

	$scope.toSubmit = function() {
		Upload.upload({
			url: '/toUploadFile',
			data: {
				'patientName': $scope.patientName,
				'patientDate': $scope.patientDate,
				'file': $scope.file
			},
		}).then(function (resp) {
			$scope.noResult = false;
			$scope.drawLine();
			console.log('Success');
		}, function (resp) {
		  console.log('Error status');
		});
	};

	$scope.drawLine = function () {
		$http({
			url: '/upload/Alice/data/out.txt',
			method: 'GET',
			responseType: 'arraybuffer'
		}).then(function(req){
			let dataview = new DataView(req.data);
			let ints = new Uint8ClampedArray(req.data.byteLength);
			for (var i = 0; i < ints.length; i++) {
      	ints[i] = dataview.getUint8(i);
        datasetOrigin1[i] = {
					x: i,
					y: ints[i]
				};
      }
			$scope.filterParamChgLine($scope.filterParam1, $scope.circleR1, $scope.grid1, $scope.assist1);
		});
	};
	$scope.filterParamChgLine = function (filterParam, circleR, hasGrid, assist) {
		d3.select('#data-show-heart-rate .data-show-chart svg').remove();
		let dataset1 = datasetOrigin1.filter(function(d){
			return ((d.x % filterParam) == 0);
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
						.scale(xScale)
						.ticks(20);
		var yAxis = d3.axisLeft()
						.scale(yScale)
						.ticks(20);

		svg.append('g')
			.attr('class', 'axis')
			.attr('transform', 'translate(' + padding.left + ', ' + (height - padding.bottom) + ')')
			.call(xAxis);
		svg.append('g')
			.attr('class', 'axis')
			.attr('transform', 'translate(' + padding.left + ', ' + padding.top + ')')
			.call(yAxis);
		if (hasGrid) {
			d3.selectAll('#data-show-heart-rate .data-show-chart svg g:nth-child(1) g line')
				.attr('stroke', '#eee')
				.attr('y2', '-' + (height - padding.bottom - padding.top));
			d3.selectAll('#data-show-heart-rate .data-show-chart svg g:nth-child(2) g line')
				.attr('stroke', '#eee')
				.attr('x2', '' + (width - padding.left - padding.right));
		}

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
			.attr('stroke', 'rgb(84, 172, 174)');

		svg.append('g')
			.selectAll('circle')
			.data(dataset1)
			.enter()
			.append('circle')
			.attr('r', circleR)
			.attr('transform', function(d){
				return 'translate(' + (xScale(d.x) + padding.left) + ',' + (yScale(d.y) + padding.top) + ')'
			})
			.attr('fill', 'rgb(84, 172, 174)')
			.on('mouseover', function(d, i){
				d3.select('#data-show-heart-rate .data-show-chart svg g:nth-child(5) rect:nth-child(' + (i + 1) + ')')
					.attr('display', 'block');
				d3.select('#data-show-heart-rate .data-show-chart svg g:nth-child(6) text:nth-child(' + (i + 1) + ')')
					.attr('display', 'block');
			})
			.on('mouseout', function(d, i){
				d3.select('#data-show-heart-rate .data-show-chart svg g:nth-child(5) rect:nth-child(' + (i + 1) + ')')
					.attr('display', 'none');
				d3.select('#data-show-heart-rate .data-show-chart svg g:nth-child(6) text:nth-child(' + (i + 1) + ')')
					.attr('display', 'none');
			});

		svg.append('g')
			.selectAll('rect')
			.data(dataset1)
			.enter()
			.append('rect')
			.attr('height', '30px')
			.attr('width', '90px')
			.attr('display', 'none')
			.attr('transform', function(d){
				return 'translate(' + (xScale(d.x) + padding.left - 45) + ',' + (yScale(d.y) + padding.top + 10) + ')'
			})
			.style('fill', 'white')
			.style('stroke', 'rgb(84, 172, 174)');

		svg.append('g')
			.selectAll('text')
			.data(dataset1)
			.enter()
			.append('text')
			.attr('height', '40px')
			.attr('width', '40px')
			.attr('display', 'none')
			.attr('transform', function(d){
				return 'translate(' + (xScale(d.x) + padding.left + 5 - 45) + ',' + (yScale(d.y) + padding.top + 20 + 10) + ')'
			})
			.style('fill', 'black')
			.style('stroke', 'black')
			.text(function(d){
				return 'x:' + d.x + ' y:' + d.y;
			});
		if (assist) {
			if(maxEcg >= maxRate) {
				svg.append('g')
					.append('line')
					.attr('transform', 'translate(' + (padding.top) + ', ' + (padding.bottom + yScale(maxRate)) + ')')
					.attr('x2', '' + (width - padding.left - padding.right))
					.attr('y1', '0.5')
					.attr('y2', '0.5')
					.attr('stroke', 'red');
			}
			if(minEcg <= minRate) {
				svg.append('g')
					.append('line')
					.attr('transform', 'translate(' + (padding.top) + ', ' + (padding.bottom + yScale(minRate)) + ')')
					.attr('x2', '' + (width - padding.left - padding.right))
					.attr('y1', '0.5')
					.attr('y2', '0.5')
					.attr('stroke', 'red');
			}
		}
	};
});
