angular.module('homeApp').controller('dataCtrl', function($scope, $http, Upload){
	var dataset1 = new Array(), dataset2 = new Array();
	var width = $('.data-show-chart').width();
	var height = width/2;
	var padding = { top: 50, right: 50, bottom: 50, left: 50 };
	$scope.filterParam = 10;
	$scope.analyze;
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

	$scope.drawLine = function() {
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
				.attr('r', 4)
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
				.attr('height', '40px')
				.attr('width', '40px')
				.attr('display', 'none')
				.attr('transform', function(d){
					return 'translate(' + (xScale(d.x) + padding.left) + ',' + (yScale(d.y) + padding.top) + ')'
				})
				.style('fill', 'white')
				.style('stroke', 'red');

			svg.append('g')
				.selectAll('text')
				.data(dataset1)
				.enter()
				.append('text')
				.attr('height', '40px')
				.attr('width', '40px')
				.attr('display', 'none')
				.attr('transform', function(d){
					return 'translate(' + (xScale(d.x) + padding.left + 20) + ',' + (yScale(d.y) + padding.top + 20) + ')'
				})
				.style('fill', 'white')
				.style('stroke', 'red')
				.text(function(d){
					return 'as';
				});
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
				return ((d.x % 1) == 0);
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
});
