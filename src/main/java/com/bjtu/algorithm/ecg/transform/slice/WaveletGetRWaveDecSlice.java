package com.bjtu.algorithm.ecg.transform.slice;

import com.bjtu.algorithm.wavelet.slice.DecompositeSlice;
import com.bjtu.algorithm.wavelet.slice.DecompositeStop;

/**
 * 利用分解过程的低频信号进行R波提取
 * @author 14301036
 *
 */
public class WaveletGetRWaveDecSlice implements DecompositeSlice {
	/** 数据原始长度 */
	private int originLength;
	
	/** 存储R波位置 */
	private double[] rWaveIdx;
	private int totRWave = 0;
	
	private double[] pre;
	
	public WaveletGetRWaveDecSlice(int originLength) {
		this.originLength = Math.max(originLength, 0);
		this.rWaveIdx = new double[this.originLength];
	}
	@Override
	public void highSlice(double[] high, int n) throws DecompositeStop {
	}

	@Override
	public void lowSlice(double[] low, int n) throws DecompositeStop {
		/* 
		 * 每次取第i尺度程第i+1尺度的低频信号
		 * 分别求极大值，进行个数比较，若个数相差小于3，则认为极大值位置与R波位置近似
		 */
		
		// 第1尺度，保存
		if (n == 1) {
			pre = low;
			return;
		}
		double[] now = low;
		
		// 找最大值
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
		
		max1 *= 0.5;
		max2 *= 0.5;
		
		//System.out.println("max: " + max1 + ", " + max2);
		
		// 找极大值
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
		
		//System.out.println(tot1 + ", " + tot2);
		
		// 相差小于3
		if (Math.abs(tot1 - tot2) < 3) {
			// 根据原数据长度与当前长度求原始位置
			for (int i = 0; i < Math.min(tot1, tot2); ++i) {
				double pos1 = (double) originLength / pre.length * maximal1[i];
				//double pos2 = (double) originLength / now.length * maximal2[i];
				rWaveIdx[totRWave++] = pos1;
			}
			
			// 使停止分解
			throw new DecompositeStop();
		}
		
		// 保存当前系数为下一次处理所用
		pre = now;
	}
	
	/**
	 * 获得R波位置，当本对象被当作参数传入小波分解并运行后才有结果
	 * @return R波位置
	 */
	public double[] getRWaveIdx() {
		// 压缩空间
		if (rWaveIdx.length > totRWave) {
			double[] res = new double[totRWave];
			for (int i = 0; i < totRWave; ++i) {
				res[i] = rWaveIdx[i];
			}
			rWaveIdx = res;
		}
		return rWaveIdx;
	}
}
