package com.bjtu.algorithm.ecg.transform;

import java.util.Arrays;

import com.bjtu.algorithm.ecg.transform.config.GlobalConfig;
import com.bjtu.algorithm.ecg.transform.slice.WaveletDenoiseRecSlice;
import com.bjtu.algorithm.ecg.transform.slice.WaveletGetRWaveDecSlice;
import com.bjtu.algorithm.wavelet.WaveletDec;
import com.bjtu.algorithm.wavelet.WaveletDenoise;
import com.bjtu.algorithm.wavelet.kit.Kit;

/**
 * ECG数据的各种处理算法
 * 与全局配置GlobalConfig有耦合
 * @author 14301036
 *
 */
public class ECGTransform {
	/**
	 * 对ECG数据进行去噪
	 * @param ecg 	ECG数据
	 * @return		去噪结果
	 */
	public static double[] denoise(double[] ecg) {
		WaveletDenoiseRecSlice slice = new WaveletDenoiseRecSlice(GlobalConfig.LO_IGNORE);
		WaveletDenoise wltdn = new WaveletDenoise(GlobalConfig.LO_D, GlobalConfig.HI_D, GlobalConfig.LO_R, GlobalConfig.HI_R);
		return wltdn.denoise(ecg, GlobalConfig.DEC_LAYER, slice);
	}
	
	/**
	 * 从ECG数据中提取R波位置
	 * @param ecg	ECG数据
	 * @return		R波位置序列，数组长度即为R波数量
	 */
	public static double[] getRWavePos(double[] ecg) {
		WaveletGetRWaveDecSlice slice = new WaveletGetRWaveDecSlice(ecg.length);
		new WaveletDec(GlobalConfig.LO_D, GlobalConfig.HI_D).decomposite(ecg, GlobalConfig.DEC_LAYER, slice);
		return slice.getRWaveIdx();
	}
	
	/**
	 * 从R波位置中提取每分钟心跳
	 * @param rWavePos	R波位置序列
	 * @param dataHz	原ECG数据的采样频率
	 * @return			从R波位置中能获得的每分钟心跳数，下标i对应的数值即为第i分钟的心率（次/分），从0开始计数
	 */
	public static int[] getHeartbeatPerSecond(double[] rWavePos, int dataHz) {
		int[] hbPerSec = new int[rWavePos.length / 60];
		int tot = 0;
		int pointPerSec = dataHz * 60;
		
		int pos = 0;
		for (int i = 0; pos < rWavePos.length; ++i) {
			int beat = 0;
			while (pos < rWavePos.length && rWavePos[pos] >= i * pointPerSec && rWavePos[pos] <= i * pointPerSec + pointPerSec) {
				++pos;
				++beat;
			}

			if (tot == hbPerSec.length) {
				// 重新分配空间
				hbPerSec = Arrays.copyOf(hbPerSec, (int) (hbPerSec.length * 1.5) + 1);
			}
			hbPerSec[tot++] = beat;
		}
		
		return Kit.getPart(hbPerSec, tot, 'l');
	}
}
