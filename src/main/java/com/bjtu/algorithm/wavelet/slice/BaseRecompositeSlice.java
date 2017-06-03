package com.bjtu.algorithm.wavelet.slice;

/**
 * 空的重构拦截处理器
 * @author 14301036
 *
 */
public class BaseRecompositeSlice implements RecompositeSlice {
	/** 默认重构拦截处理器 */
	public static BaseRecompositeSlice DEFAULT_SLICE = new BaseRecompositeSlice();
	@Override
	public void highSlice(double[] high, int n, int maxN) {
		// Do nothing
	}
	
	@Override
	public void lowSlice(double[] low, int n, int maxN) {
		// Do nothing
	}
}
