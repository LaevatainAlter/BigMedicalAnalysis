package com.bjtu.algorithm.wavelet;

import com.bjtu.algorithm.wavelet.slice.RecompositeSlice;
import com.bjtu.algorithm.wavelet.util.WaveletDecResult;

public class WaveletDenoise {
	private double[] lo_d;
	private double[] hi_d;
	private double[] lo_r;
	private double[] hi_r;
	
	public WaveletDenoise(double[] lo_d, double[] hi_d, double[] lo_r, double[] hi_r) {
		this.lo_d = lo_d;
		this.hi_d = hi_d;
		this.lo_r = lo_r;
		this.hi_r = hi_r;
	}
	
	public double[] denoise(double[] ecg, int layer, RecompositeSlice recSlice) {
		WaveletDec wld = new WaveletDec(lo_d, hi_d);
		WaveletDecResult decResult = wld.decomposite(ecg, layer);
		
		WaveletRec wlr = new WaveletRec(lo_r, hi_r);
		return wlr.recomposite(decResult, recSlice);
	}
}
