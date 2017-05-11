angular.module('homeApp').controller('dataCtrl', function($scope, $http){
// angular.module('dataApp', []).controller('dataCtrl', function($scope, $http){
	var dataset;
	var width = $('.data-show-chart').width();
	var height = width/2;
	var padding = { top: 50, right: 50, bottom: 50, left: 50 };
	$scope.filterParam = 10;

	$scope.reflesh = function(){
		$http.post('/toQuery', {

		}).then(function(req){
			dataset = req.data;

			dataset = dataset.filter(function(d){
				return ((d.minute % 20) == 0);
			});

			var minEcg = d3.min(dataset, function(d){
				return d.ecgdata;
			});
			var maxEcg = d3.max(dataset, function(d){
				return d.ecgdata;
			});
			var maxMinute = dataset[dataset.length - 1].minute;
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
							.x(function(d){ return xScale(d.minute); })
							.y(function(d){ return yScale(d.ecgdata); })
							.curve(d3.curveCatmullRom.alpha(0.5));

			svg.append('g')
				.append('path')
				.attr('class', 'line-path')
				.attr('transform', 'translate(' + padding.left + ',' + padding.top + ')')
				.attr('d', linePath(dataset))
			  	.attr('fill', 'none')
				.attr('stroke-width', 1)
				.attr('stroke', 'green');

			svg.append('g')
				.selectAll('circle')
				.data(dataset)
				.enter()
				.append('circle')
				.attr('r', 3)
				.attr('transform', function(d){
				return 'translate(' + (xScale(d.minute) + padding.left) + ',' + (yScale(d.ecgdata) + padding.top) + ')'
				})
				.attr('fill', 'green');
		}, function(error){

		});
	};

	$scope.reflesh();

	$(window).resize(function(){
		width = $('.data-show-chart').width();
		height = width/2;
		d3.select('#data-show-heart-rate .data-show-chart svg').remove();
		var minEcg = d3.min(dataset, function(d){
			return d.ecgdata;
		});
		var maxEcg = d3.max(dataset, function(d){
			return d.ecgdata;
		});
		var maxMinute = dataset[dataset.length - 1].minute;
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
						.x(function(d){ return xScale(d.minute); })
						.y(function(d){ return yScale(d.ecgdata); })
						.curve(d3.curveCatmullRom.alpha(0.5));

		svg.append('g')
			.append('path')
			.attr('class', 'line-path')
			.attr('transform', 'translate(' + padding.left + ',' + padding.top + ')')
			.attr('d', linePath(dataset))
		  	.attr('fill', 'none')
			.attr('stroke-width', 1)
			.attr('stroke', 'green');

		svg.append('g')
			.selectAll('circle')
			.data(dataset)
			.enter()
			.append('circle')
			.attr('r', 3)
			.attr('transform', function(d){
			return 'translate(' + (xScale(d.minute) + padding.left) + ',' + (yScale(d.ecgdata) + padding.top) + ')'
			})
			.attr('fill', 'green');
	});
});
