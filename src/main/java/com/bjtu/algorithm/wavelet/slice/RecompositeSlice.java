package com.bjtu.algorithm.wavelet.slice;

/**
 * 小波重构时的拦截处理接口
 * 重构时自高层往低层，所以n的变化从maxN -> 1
 * @author 14301036
 *
 */
public interface RecompositeSlice {
	/**
	 * 高频系数处理
	 * @param high	第n层的高频系数
	 * @param n		层数
	 * @param maxN	最大层数
	 */
	public void highSlice(double[] high, int n, int maxN);
	
	/**
	 * 低频系数处理
	 * @param high	第n层的低频系数
	 * @param n		层数
	 * @param maxN	最大层数
	 */
	public void lowSlice(double[] low, int n, int maxN);
}
