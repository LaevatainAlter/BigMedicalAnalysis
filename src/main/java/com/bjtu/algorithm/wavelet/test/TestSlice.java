package com.bjtu.algorithm.wavelet.test;

import java.util.Arrays;

import com.bjtu.algorithm.wavelet.kit.Kit;
import com.bjtu.algorithm.wavelet.slice.RecompositeSlice;

public class TestSlice implements RecompositeSlice {

	@Override
	public void highSlice(double[] high, int n, int maxN) {
		double middle = Kit.absmedian(high) / 0.6745;
		double[] arr = new double[high.length];
		for (int i = 0; i < arr.length; ++i) {
			double x = high[i] / middle;
			arr[i] = x * x;
		}
		Arrays.sort(arr);

		double tot = arr.length;
		double sum = 0.0;
		double minRisk = 10E32;
		double lambda = arr[0];
		for (int i = 0; i < arr.length; ++i) {
			double p = arr[i];
			sum += p;
			double risk = (tot - 2 * (i + 1) + (tot - i - 1) * p + sum) / tot;
			if (risk < minRisk) {
				minRisk = risk;
				lambda = p;
			}
			//System.out.println(risk);
		}

		lambda = Math.sqrt(lambda) * middle;
		//lambda = Math.sqrt(2 * Math.log(high.length)) * middle;
		//System.out.println("l: " + lambda);
		
		for (int i = 0; i < high.length; ++i) {
			double x = high[i];
			double result = (Math.abs(x) - lambda) + Math.abs(Math.abs(x) - lambda);
			high[i] = Math.signum(x) * result / 2.0;
		}
	}

	@Override
	public void lowSlice(double[] low, int n, int maxN) {
		if (n == maxN) {
			for (int i = 0; i < low.length; ++i) {
				low[i] = 0.0;
			}
		}
	}
}
