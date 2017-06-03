package com.bjtu.algorithm.ecg.transform.config;

/**
 * 存放一些全局参数
 * @author 14301036
 *
 */
public class GlobalConfig {
	/** Bior3.3小波 低通分解滤波器参数 */
	public static double[] LO_D = new double[] {
			0.066291260736239, -0.198873782208717, -0.154679608384557, 0.994368911043583,
			0.994368911043583, -0.154679608384557, -0.198873782208717, 0.066291260736239
	};

	/** Bior3.3小波 高通分解滤波器参数 */
	public static double[] HI_D = new double[] {
			-0.0, 0.0, -0.176776695296637, 0.530330085889911,
			-0.530330085889911, 0.176776695296637, 0.0, 0.0
	};

	/** Bior3.3小波 低通重构滤波器参数 */
	public static double[] LO_R = new double[] {
			0.0, 0.0 ,0.176776695296637 ,0.530330085889911,
			0.530330085889911, 0.176776695296637, 0.0, 0.0
	};

	/** Bior3.3小波 高通重构滤波器参数 */
	public static double[] HI_R = new double[] {
			0.066291260736239, 0.198873782208717, -0.154679608384557, -0.994368911043583,
			0.994368911043583, 0.154679608384557, -0.198873782208717, -0.066291260736239
	};
	
	/** 小波分解层数 */
	public static int DEC_LAYER = 8;
	
	/** ECG取样频率 */
	public static int SAMPLE_HZ = 250;

	/** 窗口大小 */
	public static int WIN_SIZE = 3 * SAMPLE_HZ;
	
	/** 低频去除阶数 */
	public static int LO_IGNORE = 6;
}
