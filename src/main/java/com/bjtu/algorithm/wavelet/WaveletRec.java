package com.bjtu.algorithm.wavelet;

import com.bjtu.algorithm.wavelet.kit.Kit;
import com.bjtu.algorithm.wavelet.slice.BaseRecompositeSlice;
import com.bjtu.algorithm.wavelet.slice.RecompositeSlice;
import com.bjtu.algorithm.wavelet.util.WaveletDecResult;

/**
 * 小波重构
 * @author 14301036
 *
 */
public class WaveletRec {
	/** 低通重构滤波器 */
	private double[] lo_r;

	/** 高通重构滤波器 */
	private double[] hi_r;
	
	/**
	 * 构造函数
	 * @param lo_r 低通重构滤波器
	 * @param hi_r 高通重构滤波器
	 */
	public WaveletRec(double[] lo_r, double[] hi_r) {
		if (lo_r.length != hi_r.length) {
			throw new IllegalArgumentException();
		}
		this.lo_r = lo_r;
		this.hi_r = hi_r;
	}
	
	/**
	 * 进行小波重构
	 * @param wave	小波分解结果
	 * @param slice	拦截处理
	 * @return 		小波重构结果
	 */
	public double[] recomposite(WaveletDecResult wave, RecompositeSlice slice) {
		double[][] waveList = wave.getWaveList();
		int len = waveList.length;
		if (len == 0 || slice == null) {
			throw new IllegalArgumentException();
		}
		double[] res = waveList[len - 1];
		for (int i = len - 2; i >= 0; --i) {
			// 当前迭代欲重构的长度
			int oLen = i > 0 ? waveList[i - 1].length : wave.getOriginLength();
			
			double[] w = waveList[i];

			// 切片拦截处理
			slice.lowSlice(res, i + 1, len - 1);
			slice.highSlice(w, i + 1, len - 1);
			
			// 上取样，左右补0卷积，根据重构长度取中间序列
			double[] a = Kit.getPart(Kit.conv(Kit.upSample(res), lo_r), oLen, 'c');
			w = Kit.getPart(Kit.conv(Kit.upSample(w), hi_r), oLen, 'c');
			
			// 重构序列
			double[] newRes = new double[a.length];
			for (int j = 0; j < a.length; ++j) {
				newRes[j] = a[j] + w[j];
			}
			
			// 保存结果
			res = newRes;
		}
		
		return res;
	}
	
	/**
	 * 进行小波重构
	 * @param wave 	小波分解结果
	 * @return 		小波重构结果
	 */

	public double[] recomposite(WaveletDecResult wave) {
		return recomposite(wave, BaseRecompositeSlice.DEFAULT_SLICE);
	}
}
