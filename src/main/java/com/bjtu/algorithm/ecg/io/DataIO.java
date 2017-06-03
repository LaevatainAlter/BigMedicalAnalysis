package com.bjtu.algorithm.ecg.io;

import java.io.*;

/**
 * ECG数据读写静态方法类
 * @author 14301036
 *
 */
public class DataIO {
	private DataIO() {}
	
	/**
	 * 从filePath中的文件读取ECG数据，文件数据为二进制，2个字节存储一个整数且文件为小端模式
	 * @param filePath		文件路径
	 * @return				读取结果的数组
	 * @throws IOException
	 */
	public static short[] readTwoByteECGFile(String filePath) throws IOException {
		return readTwoByteECGFile(filePath, true);
	}

	/**
	 * 从filePath中的文件读取ECG数据，文件数据为二进制，2个字节存储一个整数
	 * @param filePath		文件路径
	 * @param littleEndian  文件是否为小端模式
	 * @return				读取结果的数组
	 * @throws IOException
	 */
	public static short[] readTwoByteECGFile(String filePath, boolean litteEndian) throws IOException {
		return readTwoByteECGFile(new File(filePath), litteEndian);
	}
	
	/**
	 * 从file中读取ECG数据，文件数据为二进制，2个字节存储一个整数且文件为小端模式
	 * @param file			文件
	 * @param littleEndian  文件是否为小端模式
	 * @return				读取结果的数组
	 * @throws IOException
	 */
	public static short[] readTwoByteECGFile(File file) throws IOException {
		return readTwoByteECGFile(file, true);
	}
	
	/**
	 * 从file中读取ECG数据，文件数据为二进制，2个字节存储一个整数
	 * @param file			文件
	 * @param littleEndian  文件是否为小端模式
	 * @return				读取结果的数组
	 * @throws IOException
	 */
	public static short[] readTwoByteECGFile(File file, boolean litteEndian) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		
		// 将内容全部读入内存
		byte[] buffer = new byte[(int) file.length()];
		bis.read(buffer);
		bis.close();
		
		short[] data = new short[buffer.length / 2];
		for (int i = 0; i < data.length; ++i) {
			// 取两个字节
			int b1 = buffer[2 * i] & 0xff;
			int b2 = buffer[2 * i + 1] & 0xff;
			
			if (litteEndian) {
				data[i] = (short) (b1 | (b2 << 8));
			} else {
				data[i] = (short) ((b1 << 8) | b2);
			}
		}
		
		return data;
	}
	
	/**
	 * 向文件中写入序列的文本数据，用换行隔开
	 * @param data
	 * @throws FileNotFoundException 
	 */
	public static void writeTxtFile(double[] data, String filePath) throws FileNotFoundException {
		PrintWriter output = new PrintWriter(filePath);
		for (int i = 0; i < data.length; ++i) {
			output.println(data[i]);
		}
		output.close();
	}
	
	/**
	 * 向文件中写入序列的文本数据，用换行隔开
	 * @param data
	 * @throws FileNotFoundException 
	 */
	public static void writeTxtFile(int[] data, String filePath) throws FileNotFoundException {
		PrintWriter output = new PrintWriter(filePath);
		for (int i = 0; i < data.length; ++i) {
			output.println(data[i]);
		}
		output.close();
	}
	
	/**
	 * 向文件中写入序列的文本数据，用换行隔开
	 * @param data
	 * @throws FileNotFoundException 
	 */
	public static void writeTxtFile(short[] data, String filePath) throws FileNotFoundException {
		PrintWriter output = new PrintWriter(filePath);
		for (int i = 0; i < data.length; ++i) {
			output.println(data[i]);
		}
		output.close();
	}
}
