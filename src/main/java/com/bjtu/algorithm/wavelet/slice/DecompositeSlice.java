package com.bjtu.algorithm.wavelet.slice;

/**
 * 小波分解时的拦截处理接口
 * 分解时自低层往高层，所以n从1开始递增
 * 抛出DecompositeStop异常意味着提前中止分解
 * @author 14301036
 *
 */
public interface DecompositeSlice {
	/**
	 * 高频系数处理
	 * @param high				第n层的高频系数
	 * @param n					层数
	 * @throws DecompositeStop	中止分解
	 */
	public void highSlice(double[] high, int n) throws DecompositeStop;
	
	/**
	 * 低频系数处理
	 * @param high				第n层的低频系数
	 * @param n					层数
	 * @throws DecompositeStop	中止分解
	 */
	public void lowSlice(double[] low, int n) throws DecompositeStop;
}
