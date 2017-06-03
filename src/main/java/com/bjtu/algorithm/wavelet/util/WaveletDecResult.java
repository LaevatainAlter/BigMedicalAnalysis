package com.bjtu.algorithm.wavelet.util;

/**
 * 小波分解的结果
 * @author 14301036
 *
 */
public class WaveletDecResult {
	/** 存储n个高频系数与1个低频系数 */
	private double[][] waveList;
	/** 原始信号长度 */
	private int originLength;
	
	/**
	 * 构造函数
	 * @param waveList		系数列表
	 * @param originLength	原始信号长度
	 */
	public WaveletDecResult(double[][] waveList, int originLength) {
		this.waveList = waveList;
		this.originLength = originLength;
	}

	/**
	 * 获得系数列表
	 * @return 系数列表
	 */
	public double[][] getWaveList() {
		return waveList;
	}

	/**
	 * 获得原始信号长度
	 * @return 原始信号长度
	 */
	public int getOriginLength() {
		return originLength;
	}
}
