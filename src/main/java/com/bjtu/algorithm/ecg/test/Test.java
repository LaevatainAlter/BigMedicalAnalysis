package com.bjtu.algorithm.ecg.test;

import java.util.List;
import java.util.Map;

import com.bjtu.algorithm.ecg.algorithm.ECGAnlysis;
import com.bjtu.algorithm.ecg.io.DataIO;
import com.bjtu.algorithm.ecg.transform.ECGTransform;

public class Test {
	public static void main(String[] args) throws Exception {
		ECGAnlysis ecg = new ECGAnlysis("./2015-06-06-13-11-11温暖-正常人.txt");		// 输入
		double[] rr = ecg.getRRDistance();											// RR间期
		int[] heartBeat = ecg.getHeartBeatPerSecond();								// 心率
		
		List<Map<String, Object>> result = ecg.getAdvise();
		
		System.out.println(result);
	}
}
