package com.bjtu.algorithm.wavelet.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bjtu.algorithm.ecg.transform.config.GlobalConfig;
import com.bjtu.algorithm.ecg.transform.slice.WaveletDenoiseRecSlice;
import com.bjtu.algorithm.wavelet.WaveletDenoise;
import com.bjtu.algorithm.wavelet.slice.BaseRecompositeSlice;

public class WaveletDenoiseTest {

	@Test(expected=java.lang.IllegalArgumentException.class)
	public void emptyArray() {
		double[] arr = new double[] {};
		WaveletDenoise denoise = new WaveletDenoise(GlobalConfig.LO_D, GlobalConfig.HI_D, GlobalConfig.LO_R, GlobalConfig.HI_R);
		double[] res = denoise.denoise(arr, GlobalConfig.DEC_LAYER, new WaveletDenoiseRecSlice(GlobalConfig.LO_IGNORE));
	}
	
	@Test
	public void constantArray() {
		double[] arr = new double[] {1.0, 1.0, 1.0, 1.0, 1.0};
		WaveletDenoise denoise = new WaveletDenoise(GlobalConfig.LO_D, GlobalConfig.HI_D, GlobalConfig.LO_R, GlobalConfig.HI_R);
		double[] res = denoise.denoise(arr, GlobalConfig.DEC_LAYER, new BaseRecompositeSlice());
		assertArrayEquals(res, arr, 10E-12);
	}
	
	@Test
	public void singleArray() {
		double[] arr = new double[] {100.0};
		WaveletDenoise denoise = new WaveletDenoise(GlobalConfig.LO_D, GlobalConfig.HI_D, GlobalConfig.LO_R, GlobalConfig.HI_R);
		double[] res = denoise.denoise(arr, GlobalConfig.DEC_LAYER, new BaseRecompositeSlice());
		assertArrayEquals(res, arr, 10E-12);
	}
	
	@Test
	public void normalTest() {
		double[] arr = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
		WaveletDenoise denoise = new WaveletDenoise(GlobalConfig.LO_D, GlobalConfig.HI_D, GlobalConfig.LO_R, GlobalConfig.HI_R);
		double[] res = denoise.denoise(arr, GlobalConfig.DEC_LAYER, new BaseRecompositeSlice());
		assertArrayEquals(res, arr, 10E-12);
	}
	
	@Test(timeout=10000)
	public void bigdataTest() {
		int length = 10000000;
		double[] arr = new double[length];
		for (int i = 0; i < length; ++i) {
			arr[i] = Math.random();
		}
		WaveletDenoise denoise = new WaveletDenoise(GlobalConfig.LO_D, GlobalConfig.HI_D, GlobalConfig.LO_R, GlobalConfig.HI_R);
		denoise.denoise(arr, GlobalConfig.DEC_LAYER, new WaveletDenoiseRecSlice(GlobalConfig.LO_IGNORE));
	}
	
	@Test(expected=java.lang.IllegalArgumentException.class)
	public void errorTest1() {
		double[] arr = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
		WaveletDenoise denoise = new WaveletDenoise(GlobalConfig.LO_D, GlobalConfig.HI_D, GlobalConfig.LO_R, GlobalConfig.HI_R);
		double[] res = denoise.denoise(arr, -100, new BaseRecompositeSlice());
	}
	
	@Test
	public void boundaryTest() {
		double[] arr = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
		WaveletDenoise denoise = new WaveletDenoise(GlobalConfig.LO_D, GlobalConfig.HI_D, GlobalConfig.LO_R, GlobalConfig.HI_R);
		double[] res = denoise.denoise(arr, 0, new BaseRecompositeSlice());
		assertArrayEquals(res, arr, 10E-12);
	}
}
