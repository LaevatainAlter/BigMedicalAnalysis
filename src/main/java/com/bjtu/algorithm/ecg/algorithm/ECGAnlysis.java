package com.bjtu.algorithm.ecg.algorithm;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bjtu.algorithm.ecg.io.DataIO;
import com.bjtu.algorithm.ecg.transform.ECGTransform;
import com.bjtu.algorithm.ecg.transform.config.GlobalConfig;

/**
 * 对ECG数据进行各种分析，
 * 采用Lazy处理方式，对象创建时不会进行数据操作，只有在实际需要数据时才会进行文件读取和数据分析，
 * 所以创建对象时不会抛出异常，在进行get操作时可能会抛出IO异常，
 * @author 14301036
 *
 */
public class ECGAnlysis {
	private static final int AVERAGE_HB_MIN = 70;
	private static final int AVERAGE_HB_MAX = 90;
	private static final int QUIET_HB = 70;
	private static final int HAPPY_HB = 110;
	
	/** 待分析的文件 */
	private File dataFile;
	
	/** 原始数据 */
	private short[] originData;
	
	/** R波位置 */
	private double[] rWavePosition;
	
	/**
	 * 构造函数
	 * @param filePath	要分析的文件路径
	 */
	public ECGAnlysis(String filePath) {
		this(new File(filePath));
	}
	
	/**
	 * 构造函数
	 * @param file		要分析的文件
	 */
	public ECGAnlysis(File file) {
		this.dataFile = file;
	}
	
	/**
	 * 实际进行的数据分析
	 * 算法步骤：读取二进制数据->数据分窗口->数据去噪->R波位置提取
	 * @throws IOException	IO读写异常
	 */
	private void dataProcess() throws IOException {
		// 读取数据
		this.originData = DataIO.readTwoByteECGFile(dataFile);
		
		/* 分窗口处理数据 */
		// 窗口大小
		int winSize = GlobalConfig.WIN_SIZE;
		
		// 分组大小
		int group = originData.length / winSize;
		
		// 存储不同分组的R波位置
		double[][] rPart = new double[group][];
		// R波总个数
		int totRLength = 0;
		
		// 用于存储分组的数据
		double[] part = new double[winSize];

		for (int i = 0; i < group; ++i) {
			for (int j = 0; j < winSize; ++j) {
				part[j] = originData[i * winSize + j];
			}
			// 去噪
			double[] denoise = ECGTransform.denoise(part);

			rPart[i] = ECGTransform.getRWavePos(denoise);
			totRLength += rPart[i].length;
		}
		
		// 将分组的R波位置合并
		this.rWavePosition = new double[totRLength];
		int idx = 0;
		for (int i = 0; i < group; ++i) {
			for (int j = 0; j < rPart[i].length; ++j) {
				rWavePosition[idx++] = rPart[i][j] + i * winSize;
			}
		}
	}
	
	/**
	 * 获得原始数据
	 * @return 				原始数据
	 * @throws IOException	读写异常
	 */
	public short[] getOriginData() throws IOException {
		if (originData == null) {
			dataProcess();
		}
		return Arrays.copyOf(originData, originData.length);
	}
	
	/**
	 * 获得R波位置
	 * @return 				R波位置，下标i为第i个R波在原始数据的位置（带小数），i从0开始
	 * @throws IOException	读写异常
	 */
	public double[] getRWavePosition() throws IOException {
		if (rWavePosition == null) {
			dataProcess();
		}
		return Arrays.copyOf(rWavePosition, rWavePosition.length);
	}
	
	/**
	 * 获得心率
	 * @return 				心率，下标为i的值为第i分钟的心率，i从0开始
	 * @throws IOException	读写异常
	 */
	public int[] getHeartBeatPerSecond() throws IOException {
		if (rWavePosition == null) {
			dataProcess();
		}
		return ECGTransform.getHeartbeatPerSecond(rWavePosition, GlobalConfig.SAMPLE_HZ);
	}

	/**
	 * 获得RR间期
	 * @return				RR间期
	 * @throws IOException	读写异常
	 */
	public double[] getRRDistance() throws IOException {
		if (rWavePosition == null) {
			dataProcess();
		}
		double[] rrDistance = new double[Math.max(0, rWavePosition.length - 1)];
		for (int i = 0; i < rWavePosition.length - 1; ++i) {
			rrDistance[i] = rWavePosition[i + 1] - rWavePosition[i];
		}
		return rrDistance;
	}
	
	/**
	 * 获得指标
	 */
	public List<Map<String, Object>> getNumerics() throws IOException {
		int[] hbs = getHeartBeatPerSecond();
		int sum = 0, happy = 0, quiet = 0;
		
		String[] names = new String[] {"平均心率", "安静时间", "兴奋时间"};
		BigDecimal[][] ranges = new BigDecimal[][] {
				{BigDecimal.valueOf(AVERAGE_HB_MIN), BigDecimal.valueOf(AVERAGE_HB_MAX)},
				{null, null},
				{null, null}
		};
		BigDecimal[] values = new BigDecimal[names.length];

		for (Integer hb : hbs) {
			sum += hb;
			if (hb > HAPPY_HB) {
				++happy;
			} else if (hb < QUIET_HB) {
				++quiet;
			}
		}
	
		values[0] = BigDecimal.valueOf(sum * 1.0).divide(BigDecimal.valueOf(hbs.length), 2, BigDecimal.ROUND_HALF_EVEN);
		values[1] = BigDecimal.valueOf(quiet);
		values[2] = BigDecimal.valueOf(happy);
		
		List<Map<String, Object>> list = new ArrayList<>();

		for (int i = 0; i < names.length; ++i) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("name", names[i]);
			if (ranges[i][0] != null) {
				map.put("range", ranges[i][0] + "-" + ranges[i][1]);

				map.put("prompt", ranges[i][0].compareTo(values[i]) <= 0 && ranges[i][1].compareTo(values[i]) >= 0 ? 0 :
					(ranges[i][0].compareTo(values[i]) > 0 ? -1 : 1));
			} else {
				map.put("range", "");
				map.put("prompt", 2);
			}
			switch (names[i]) {
			case "平均心率":
				map.put("value", values[i].toPlainString());
				break;
			case "安静时间": case "兴奋时间":
				map.put("value", values[i].multiply(BigDecimal.valueOf(100).divide(
						BigDecimal.valueOf(hbs.length), 2, BigDecimal.ROUND_HALF_EVEN)).toPlainString());
				break;
			}
			list.add(map);
		}
		
		return list;
	}
}
