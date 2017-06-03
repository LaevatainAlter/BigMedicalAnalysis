package com.bjtu.algorithm.ecg.transform.slice;

import java.util.Arrays;

import com.bjtu.algorithm.wavelet.kit.Kit;
import com.bjtu.algorithm.wavelet.slice.RecompositeSlice;

/**
 * 在小波重构时对参数进行调整
 * @author 14301036
 *
 */
public class WaveletDenoiseRecSlice implements RecompositeSlice {
	private int lower_n;
	public WaveletDenoiseRecSlice(int lower_n) {
		this.lower_n = lower_n;
	}
	@Override
	public void highSlice(double[] high, int n, int maxN) {
		// 方差值
		double middle = Kit.absmedian(high) / 0.6745;
		
		if (middle == 0) {
			middle = 1.0;
		}
		
		// 取系数除以方差后的平方并从小到大排序
		double[] arr = new double[high.length];
		for (int i = 0; i < arr.length; ++i) {
			double x = Math.abs(high[i]) / middle;
			arr[i] = x * x;
		}
		Arrays.sort(arr);
		
		// 以下办法可以优化0.5s左右的运行时间
		/*
		double[] arr = new double[high.length];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = Math.abs(high[i]);
		}
		Arrays.sort(arr);
		double middle = arr[arr.length / 2];
		if (arr.length % 2 == 0) {
			middle = (middle + arr[arr.length / 2 - 1]) / 2.0;
		}
		middle /= 0.6745;
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = arr[i] * arr[i] / middle / middle;
		}
		*/

		// 找风险最小的系数
		double tot = arr.length;
		double sum = 0.0;
		double minRisk = 10E32;
		double lambda = arr[0];
		for (int i = 0; i < arr.length; ++i) {
			double p = arr[i];
			sum += p;
			// 风险计算公式 risk_i = (N - 2i + (N - i) * p + (前i项p的和)) / N   此处i从1开始数
			double risk = (tot - 2 * (i + 1) + (tot - i - 1) * p + sum) / tot;
			if (risk < minRisk) {
				minRisk = risk;
				lambda = p;
			}
			//System.out.println(risk);
		}

		// 得到阀值
		lambda = Math.sqrt(lambda) * middle;
		//lambda = Math.sqrt(2 * Math.log(high.length)) * middle;
		//System.out.println("l: " + lambda);
		
		// 软阀值处理
		for (int i = 0; i < high.length; ++i) {
			double x = high[i];
			double result = (Math.abs(x) - lambda) + Math.abs(Math.abs(x) - lambda);
			high[i] = Math.signum(x) * result / 2.0;
		}
	}

	@Override
	public void lowSlice(double[] low, int n, int maxN) {
		if (n >= lower_n) {
			for (int i = 0; i < low.length; ++i) {
				low[i] = 0.0;
			}
		}
	}
}
