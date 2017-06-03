package com.bjtu.algorithm.wavelet.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;

import com.bjtu.algorithm.wavelet.WaveletDec;
import com.bjtu.algorithm.wavelet.WaveletRec;
import com.bjtu.algorithm.wavelet.util.WaveletDecResult;

public class Test {
	public static short[] getHeartData(String path) throws IOException {	
		File file = new File(path);
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
		int length = (int) file.length();
		byte[] buffer = new byte[length];
		input.read(buffer);
		input.close();
		
		int wantLength = length / 2;
		short[] res = new short[wantLength];
		
		for (int i = 0; i < wantLength; ++i) {
			int num = 0;
			int b1 = buffer[2 * i] & 0xff;
			int b2 = buffer[2 * i + 1] & 0xff;
			num = 0xffff & (b1 | (b2 << 8));
			res[i] = (short) num;
		}
		
		return res;
	}
	
	public static short[] getTxtData(String path) throws FileNotFoundException {
		List<Short> list = new ArrayList<Short>();
		Scanner input = new Scanner(new File(path));
		while (input.hasNextInt()) {
			list.add((short) input.nextInt());
		}
		input.close();
		int wantLength = list.size();
		short[] res = new short[wantLength];
		for (int i = 0; i < wantLength; ++i) {
			res[i] = list.get(i);
		}
		return res;
	}
	
	public static double[] getData() throws IOException {
		short[] list = getHeartData("./2015-06-06-13-11-11温暖-正常人.txt");
		//short[] list = getTxtData("./result1of5000.txt");
		double[] res = new double[list.length];
		for (int i = 0; i < list.length; ++i) {
			res[i] = list[i];
		}
		return res;
	}
	
	public static void main(String[] args) throws Exception {
		double[] lo_d = new double[] {
				0.066291260736239, -0.198873782208717, -0.154679608384557, 0.994368911043583,
				0.994368911043583, -0.154679608384557, -0.198873782208717, 0.066291260736239
		};
		double[] hi_d = new double[] {
				-0.0, 0.0, -0.176776695296637, 0.530330085889911,
				-0.530330085889911, 0.176776695296637, 0.0, 0.0
		};
		double[] lo_r = new double[] {
				0.0, 0.0 ,0.176776695296637 ,0.530330085889911,
				0.530330085889911, 0.176776695296637, 0.0, 0.0
		};
		double[] hi_r = new double[] {
				0.066291260736239, 0.198873782208717, -0.154679608384557, -0.994368911043583,
				0.994368911043583, 0.154679608384557, -0.198873782208717, -0.066291260736239
		};
		
		double[] data = getData();
		int winSize = 500;
		int group = data.length / winSize;
		double[][] winData = new double[group][winSize];
		for (int i = 0; i < group; ++i) {
			for (int j = 0; j < winSize; ++j) {
				winData[i][j] = data[i * winSize + j];
			}
		}
		System.out.println("data get");
		System.out.println("group " + group);

		WaveletDec wld = new WaveletDec(lo_d, hi_d);
		WaveletRec wlr = new WaveletRec(lo_r, hi_r);
		
		//WaveletDecResult result = wld.decomposite(data, 8);
		//double[] elpsy = wlr.recomposite(result, new TestSlice());
		//drawImage(elpsy, "./origin.png");
		
		double[][] nonoise = new double[group][];
		for (int i = 0; i < group; ++i) {
			//for (int j = 0; j < winSize; ++j) {
			//	winData[i][j] = elpsy[i * winSize + j];
			//}
			WaveletDecResult res = wld.decomposite(winData[i], 8);
			nonoise[i] = wlr.recomposite(res, new TestSlice());
		}
		
		int ttotal = 0;
		int total = 0;
		int cnt = 0;
		int oneSecCnt = 60 / (winSize / 250);
		List<Integer> heartBeat = new ArrayList<Integer>();
		for (int i = 0; i < group; ++i) {
			//drawImage(nonoise[i], "./image/" + i + ".png");
			TestDecSlice s = new TestDecSlice(nonoise[i].length);
			wld.decomposite(nonoise[i], 10, s);
			total += s.total;
			ttotal += s.total;
			if (++cnt >= oneSecCnt) {
				heartBeat.add(total);
				cnt = 0;
				total = 0;
			}
		}
		System.out.println(ttotal / (data.length / 250.0));
		double[] tmp = new double[heartBeat.size()];
		for (int i = 0; i < tmp.length; ++i) {
			tmp[i] = heartBeat.get(i);
		}
		drawImage(tmp, "./hb.png");
		
		PrintWriter foutput = new PrintWriter(new File("./hb.txt"));
		int step = 1;
		for (int i = 0; i < tmp.length - step + 1; i += step) {
			int sum = 0;
			for (int j = 0; j < step; ++j) {
				sum += tmp[i + j];
			}
			sum /= step;
			foutput.println(sum);
		}
		foutput.close();
	}
	
	public static void drawImage(double[] list, String path) throws IOException {
		int width = 1920;
		int height = 1080;
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) bi.getGraphics();
		g2.setBackground(Color.WHITE);
		g2.clearRect(0, 0, width, height);
		
		int tot = list.length;
		
		double max = 0, min = 0;
		if (tot > 0) {
			max = min = list[0];
		}
		for (int i = 1; i < tot; ++i) {
			double num = list[i];
			if (min > num) {
				min = num;
			}
			if (max < num) {
				max = num;
			}
		}
		
		int oWidth = 2, oHeight = 2;
		g2.setColor(Color.BLACK);
		int old_x = 0, old_y = 0;
		for (int i = 0; i < tot; ++i) {
			int x = (int) Math.round(((double) i / tot * width));
			int y = (int) Math.round((max - list[i]) / (max - min) * height);
			//g2.drawLine(x, y, x, height);
			g2.drawOval(x - oWidth / 2, y - oHeight / 2, oWidth, oHeight);
			if (i > 0) {
				g2.drawLine(x, y, old_x, old_y);
			}
			old_x = x;
			old_y = y;
		}
		
		int zeroY = (int) Math.round((max - 0) / (max - min) * height);
		g2.setColor(Color.RED);
		g2.drawLine(0, zeroY, width, zeroY);

		ImageIO.write(bi, "png", new File(path));
	}
}
