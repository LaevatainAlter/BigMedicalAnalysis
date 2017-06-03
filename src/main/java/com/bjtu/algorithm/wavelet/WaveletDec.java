package com.bjtu.algorithm.wavelet;

import com.bjtu.algorithm.wavelet.kit.Kit;
import com.bjtu.algorithm.wavelet.slice.BaseDecompositeSlice;
import com.bjtu.algorithm.wavelet.slice.DecompositeSlice;
import com.bjtu.algorithm.wavelet.slice.DecompositeStop;
import com.bjtu.algorithm.wavelet.util.WaveletDecResult;

/**
 * 小波分解
 * @author 14301036
 *
 */
public class WaveletDec {
	/** 低通分解滤波器 */
	private double[] lo_d;
	
	/** 高通分解滤波器 */
	private double[] hi_d;
	
	/**
	 * 构造函数
	 * @param lo_d 低通分解滤波器
	 * @param hi_d 高通分解滤波器
	 */
	public WaveletDec(double[] lo_d, double[] hi_d) {
		if (lo_d.length != hi_d.length) {
			throw new IllegalArgumentException();
		}
		this.lo_d = lo_d;
		this.hi_d = hi_d;
	}
	
	/**
	 * 进行小波分解
	 * @param data 	源数据
	 * @param n 	分解层数
	 * @param slice 拦截处理
	 * @return 		小波分解结果
	 */
	public WaveletDecResult decomposite(double[] data, int n, DecompositeSlice slice) {
		double[][] res = new double[n + 1][];
		int originLen = data.length;
		for (int tot = 0; tot < n; ++tot) {
			// 将序列进行偶延拓
			int extLen = lo_d.length - 1;
			double[] ext = Kit.extend(data, extLen, "sym");

			// 有效范围内的卷积，下取样
			double[] a = Kit.downSample(Kit.conv(ext, lo_d, true), false);
			double[] w = Kit.downSample(Kit.conv(ext, hi_d, true), false);
			
			// 保存细节（高频）系数
			res[tot] = w;
			// 保存近似（低频）系数
			data = a;
			
			try {
				// 切片，对每一轮迭代结果进行拦截处理
				slice.lowSlice(a, tot + 1);
				slice.highSlice(w, tot + 1);
			} catch (DecompositeStop e) {	// 中止信号
				double[][] newRes = new double[tot + 2][];
				for (int i = 0; i < tot + 1; ++i) {
					newRes[i] = res[i];
				}
				newRes[tot + 1] = data;
				return new WaveletDecResult(newRes, originLen);
			}
		}
		res[n] = data;
		return new WaveletDecResult(res, originLen);
	}
	
	/**
	 * 进行小波分解
	 * @param data 	源数据
	 * @param n 	分解数
	 * @return 		小波分解结果
	 */
	public WaveletDecResult decomposite(double[] data, int n) {
		return decomposite(data, n, BaseDecompositeSlice.DEFAULT_SLICE);
	}
}
