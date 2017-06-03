package com.bjtu.algorithm.wavelet.test;

import com.bjtu.algorithm.wavelet.slice.DecompositeSlice;
import com.bjtu.algorithm.wavelet.slice.DecompositeStop;

public class TestDecSlice implements DecompositeSlice {
	private double[] pre = null;
	private int length;
	
	public TestDecSlice(int length) {
		this.length = length;
	}
	
	public int total = 0;
	
	@Override
	public void highSlice(double[] high, int n) throws DecompositeStop {
	}
	@Override
	public void lowSlice(double[] low, int n) throws DecompositeStop {
		if (n == 1) {
			pre = low;
			return;
		}
		double[] now = low;
		
		double max1 = pre[0];
		double max2 = now[0];
		for (int i = 1; i < pre.length; ++i) {
			if (max1 < pre[i]) {
				max1 = pre[i];
			}
		}
		for (int i = 1; i < now.length; ++i) {
			if (max2 < now[i]) {
				max2 = now[i];
			}
		}
		
		max1 = Math.min(max1, 4000) * 0.6;
		max2 = Math.min(max2, 4000) * 0.6;
		
		//System.out.println("max: " + max1 + ", " + max2);
		
		int[] maximal1 = new int[pre.length];
		int[] maximal2 = new int[now.length];
		int tot1 = 0;
		int tot2 = 0;
		
		for (int i = 1; i < pre.length - 1; ++i) {
			double x = pre[i];
			double x1 = pre[i - 1];
			double x2 = pre[i + 1];
			if (x > max1 && x > x1 && x > x2) {
				maximal1[tot1++] = i;
			}
		}
		for (int i = 1; i < now.length - 1; ++i) {
			double x = now[i];
			double x1 = now[i - 1];
			double x2 = now[i + 1];
			if (x > max2 && x > x1 && x > x2) {
				maximal2[tot2++] = i;
			}
		}
		
		//System.out.println(n + ": " + tot1 + ", " + tot2);
		
		if (Math.abs(tot1 - tot2) < 3) {
			double sum = 0.0;
			for (int i = 0; i < Math.min(tot1, tot2); ++i) {
				double pos1 = (double) length / pre.length * maximal1[i];
				double pos2 = (double) length / now.length * maximal2[i];
				//System.out.println(Math.abs(pos1 - pos2));
				sum += Math.abs(pos1 - pos2);
				
			}
			if (sum / Math.min(tot1, tot2) > 25) {
				return;
			}
			total += tot1;
			//System.out.println("+");
			throw new DecompositeStop();
		}
	}
}
