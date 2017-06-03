package com.bjtu.algorithm.wavelet.slice;

/**
 * 空的分解拦截处理
 * @author 14301036
 *
 */
public class BaseDecompositeSlice implements DecompositeSlice {
	/** 默认分解拦截处理器 */
	public static BaseDecompositeSlice DEFAULT_SLICE = new BaseDecompositeSlice();
	@Override
	public void highSlice(double[] high, int n) {
		// Do nothing
	}
	@Override
	public void lowSlice(double[] low, int n) {
		// Do nothing
	}
}
