angular.module('homeApp').controller('dataCtrl', function($scope, $http, Upload){
// angular.module('dataApp', []).controller('dataCtrl', function($scope, $http){
	var datasetOrigin1 = [], datasetOrigin2 = [];
	var width = $('.data-show-chart').width();
	var height = width/2;
	var padding = { top: 50, right: 50, bottom: 50, left: 50 };
	var maxRate = 125;
	var minRate = 80;
	$scope.filterParam1 = 10;
	$scope.circleR1 = 4;
	$scope.grid1 = true;
	$scope.assist1 = true;
	$scope.filterParam2 = 10;
	$scope.circleR2 = 4;
	$scope.grid2 = true;
	$scope.noResult = true;
	$scope.outcome1 = {};

	$scope.toSubmit = function() {
        $scope.drawChart();
	};

	$scope.drawChart = function () {
        Upload.upload({
            url: '/toUploadFile',
            data: {
                'patientName': $scope.patientName,
                'patientDate': $scope.patientDate,
                'file': $scope.file
            }
        }).then(function(req){
			for(let i = 0; i < req.data.lineChart.length; i++){
				datasetOrigin1.push({
					x: i,
					y: req.data.lineChart[i]
				})
			}
			for(let i = 0; i < req.data.scatterPlot.length; i=i+2){
				datasetOrigin2.push({
					x: req.data.scatterPlot[i],
					y: req.data.scatterPlot[i+1]
				})
			}
			$scope.filterParamChgLine($scope.filterParam1, $scope.circleR1, $scope.grid1, $scope.assist1);
			$scope.filterParamChgScatter($scope.filterParam2, $scope.circleR2, $scope.grid2);
			$scope.outcome1 = req.data.outcome;
			var datasetPie1 = [
				[$scope.outcome1[1]['name'], $scope.outcome1[1]['value'].substr(0,$scope.outcome1[1]['value'].length - 1)],
				[$scope.outcome1[2]['name'], $scope.outcome1[2]['value'].substr(0,$scope.outcome1[2]['value'].length - 1)],
				['正常时间', (100-parseFloat($scope.outcome1[1]['value'].substr(0,$scope.outcome1[1]['value'].length - 1))-parseFloat($scope.outcome1[2]['value'].substr(0,$scope.outcome1[2]['value'].length - 1)))]
			];
			$scope.drawPie(datasetPie1);
            $scope.noResult = false;
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
	$scope.filterParamChgScatter = function (filterParam, circleR, hasGrid) {
		d3.select('#data-show-poincare .data-show-chart svg').remove();
		let dataset2 = [];
		for(let i = 0; i < datasetOrigin2.length; i=i+filterParam){
			dataset2.push({
				x: datasetOrigin2[i].x,
				y: datasetOrigin2[i].y
			})
		}
		var minX = 0;
		var maxX = d3.max(dataset2, function(d){
			return d.x;
		});
		var minY = 0;
		var maxY = d3.max(dataset2, function(d){
			return d.y;
		});
		var scatterWidth = width * 2 / 3;
		var xScale = d3.scaleLinear()
						.domain([minX, maxX])
						.rangeRound([0, scatterWidth - padding.right - padding.left]);
		var yScale = d3.scaleLinear()
						.domain([minY, maxY])
						.rangeRound([scatterWidth - padding.top - padding.bottom, 0]);

		var svg = d3.select('#data-show-poincare .data-show-chart')
					.append('svg')
					.attr('width', scatterWidth + 'px')
					.attr('height', scatterWidth + 'px');
		var xAxis = d3.axisBottom()
						.scale(xScale)
						.ticks(10);
		var yAxis = d3.axisLeft()
						.scale(yScale)
						.ticks(10);

		svg.append('g')
			.attr('class', 'axis')
			.attr('transform', 'translate(' + padding.left + ', ' + (scatterWidth - padding.bottom) + ')')
			.call(xAxis);
		svg.append('g')
			.attr('class', 'axis')
			.attr('transform', 'translate(' + padding.left + ', ' + padding.top + ')')
			.call(yAxis);
		if(hasGrid){
			d3.selectAll('#data-show-poincare .data-show-chart svg g:nth-child(1) g line')
				.attr('stroke', '#eee')
				.attr('y2', '-' + (scatterWidth - padding.bottom - padding.top));
			d3.selectAll('#data-show-poincare .data-show-chart svg g:nth-child(2) g line')
				.attr('stroke', '#eee')
				.attr('x2', '' + (scatterWidth - padding.left - padding.right));
		}

		svg.append('g')
			.selectAll('circle')
			.data(dataset2)
			.enter()
			.append('circle')
			.attr('r', circleR)
			.attr('transform', function(d){
				return 'translate(' + (xScale(d.x) + padding.left) + ',' + (yScale(d.y) + padding.top) + ')'
			})
			.attr('fill', 'rgb(84, 172, 174)');
	};
	$scope.drawPie = function (dataset) {
		// var dataset = [['第1个', 72.5], ['第2个', 5.3], ['第3个', 16.3], ['第4个', 3.5], ['第5个', 1.0], ['第6个', 1.4]];
		var pie = d3.pie()
								.sort(null)
								.value(function(d){
									return d[1];
								});
		var pieWidth = height;
		var outerRadius = pieWidth / 4;
		var innerRadius = 0;

		var arc = d3.arc()
		            .outerRadius(outerRadius)
		            .innerRadius(innerRadius);

		var colors = d3.schemeCategory10;

		var svg = d3.select('#data-show-pie .data-show-chart')
					.append('svg')
					.attr('width', pieWidth + 'px')
					.attr('height', pieWidth + 'px');

		var arcs = svg.selectAll('g')
		              .data(pie(dataset))
		              .enter()
		              .append('g')
		              .attr('transform', 'translate(' + pieWidth / 2 + ',' + pieWidth / 2 + ')');

		arcs.append('path')
		    .attr('fill', function(d, i){
		      return colors[i];
		    })
		    .attr('d', function(d){
		      return arc(d);
		    });

		arcs.append('text')
				.attr('transform', function(d, i){
					var x = arc.centroid(d)[0] * 2.8;
					var y = arc.centroid(d)[1] * 2.8;
					if(i === 4) {
						return 'translate(' + (x * 1.2) + ', ' + (y * 1.2) + ')';
					} else if(i === 3) {
					  return 'translate(' + (x - 40) + ', ' + y + ')';
					} else if(i === 5) {
						return 'translate(' + (x + 40) + ', ' + y + ')';
					}
					return 'translate(' + x + ', ' + y + ')';
				})
				.attr('text-anchor', 'middle')
				.text(function(d){
					var percent = Number(d.value) / d3.sum(dataset, function(d){
						return d[1];
					}) * 100;
					return d.data[0] + ' ' + percent.toFixed(1) + '%';
				})

		arcs.append('line')
				.attr('stroke', 'black')
				.attr('x1', function(d){ return arc.centroid(d)[0] * 2; })
				.attr('y1', function(d){ return arc.centroid(d)[1] * 2; })
				.attr('x2', function(d, i){
					if(i === 4) {
						return arc.centroid(d)[0] * 3.2;
					}
					return arc.centroid(d)[0] * 2.5;
				})
				.attr('y2', function(d, i){
					if(i === 4) {
						return arc.centroid(d)[1] * 3.2;
					}
					return arc.centroid(d)[1] * 2.5;
				});
	};
});
