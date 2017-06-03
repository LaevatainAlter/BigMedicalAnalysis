package com.bjtu.algorithm.wavelet.kit;

/**
 * 小波处理静态方法类
 * @author 14301036
 *
 */
public class Kit {
	/**
	 * 下取样
	 * @param list 		原序列
	 * @param keepEven 	若为true，则保留偶数项；若为false，则保存留奇数项
	 * @return 			下取样结果
	 */
	public static double[] downSample(double[] list, boolean keepEven) {
		double[] res = new double[(list.length + (keepEven ? 1 : 0)) / 2];
		int i = keepEven ? 0 : 1;
		for (; i < list.length; i += 2) {
			res[i / 2] = list[i];
		}
		return res;
	}
	
	/**
	 * 保留偶数项下取样
	 * @param list 	原序列
	 * @return 		下取样结果
	 */
	public static double[] downSample(double[] list) {
		return downSample(list, true);
	}
	
	/**
	 * 上取样，已有序列两两元素之间插0
	 * @param 	list 原序列
	 * @return 	上取样结果
	 */
	public static double[] upSample(double[] list) {
		double[] res = new double[Math.max(list.length * 2 - 1, 0)];
		if (list.length > 0) {
			res[0] = list[0];
		}
		for (int i = 1; i < list.length; ++i) {
			res[2 * i] = list[i];
		}
		return res;
	}

	
	/**
	 * 对原double序列进行部分截取
	 * @param list		原序列
	 * @param length	截取长度
	 * @param type		截取类型
	 * 					将序列开头靠左，结尾靠右。若为'l'，则靠左截取；若为'r'，则靠右截取；若为'c'或其它，则居中截取（偏左）
	 * @return			截取结果
	 */
	public static double[] getPart(double[] list, int length, char type) {
		length = Math.min(length, list.length);
		double[] res = new double[length];
		switch (Character.toLowerCase(type)) {
		case 'l':
			for (int i = 0; i < length; ++i) {
				res[i] = list[i];
			}
			break;
		case 'r':
			for (int i = 0; i < length; ++i) {
				res[i] = list[list.length - i - 1];
			}
			break;
		case 'c': default:
			int start = (list.length - length) / 2;
			for (int i = start; i < start + length; ++i) {
				res[i - start] = list[i];
			}
			break;
		}
		
		return res;
	}
	
	/**
	 * 对原int序列进行部分截取
	 * @param list		原序列
	 * @param length	截取长度
	 * @param type		截取类型
	 * 					将序列开头靠左，结尾靠右。若为'l'，则靠左截取；若为'r'，则靠右截取；若为'c'或其它，则居中截取（偏左）
	 * @return			截取结果
	 */
	public static int[] getPart(int[] list, int length, char type) {
		length = Math.min(length, list.length);
		int[] res = new int[length];
		switch (Character.toLowerCase(type)) {
		case 'l':
			for (int i = 0; i < length; ++i) {
				res[i] = list[i];
			}
			break;
		case 'r':
			for (int i = 0; i < length; ++i) {
				res[i] = list[list.length - i - 1];
			}
			break;
		case 'c': default:
			int start = (list.length - length) / 2;
			for (int i = start; i < start + length; ++i) {
				res[i - start] = list[i];
			}
			break;
		}
		
		return res;
	}
	
	/**
	 * 对原序列两端进行扩展
	 * @param list		原序列
	 * @param length	其中一端的扩展长度
	 * @param type		扩展方式
	 * 					若为"zero"，则进行补0扩展；若为"sym"，则进行偶延拓
	 * @return			扩展结果（长度为list.length + 2 * length）
	 */
	public static double[] extend(double[] list, int length, String type) {
		double[] res = new double[list.length + 2 * length];
		switch (type.toLowerCase()) {
		case "zero":
			for (int i = length; i < length + list.length; ++i) {
				res[i] = list[i - length];
			}
			break;
		case "sym": default:
			int way = ((length - 1) / list.length) % 2;
			int part = (length - 1) % list.length;
			int step;
			int start;
			if (way == 0) {
				step = -1;
				start = part;
			} else {
				step = 1;
				start = list.length - part - 1;
			}
			for (int i = 0; i < length; ++i) {
				res[i] = list[start];
				start += step;
				if (start == -1 || start == list.length) {
					step *= -1;
					start += step;
				}
			}
			for (int i = 0; i < list.length; ++i) {
				res[i + length] = list[i];
			}
			step = -1;
			start = list.length - 1;
			for (int i = 0; i < length; ++i) {
				res[list.length + length + i] = list[start];
				start += step;
				if (start == -1 || start == list.length) {
					step *= -1;
					start += step;
				}
			}
			break;
		}
		return res;
	}

	/**
	 * 卷积
	 * @param l 	主序列
	 * @param x		副序列
	 * @param valid 是否只取主序列有效部分
	 * @return		卷积结果
	 */
	public static double[] conv(double[] l, double[] x, boolean valid) {
		double[] res = new double[l.length + (valid ? -1 : 1) * (x.length - 1)];
		int len = x.length;
		int ridx = 0;
		for (int i = -(len - 1); i < l.length; ++i) {
			if (valid && (i < 0 || i >= l.length - len + 1)) {
				continue;
			}
			double sum = 0.0;
			for (int j = 0; j < len; ++j) {
				int idx = i + j;
				double val = idx >= 0 && idx < l.length ? l[idx] : 0.0;
				sum += val * x[len - j - 1];
			}
			res[ridx++] = sum;
		}
		return res;
	}
	
	/**
	 * 卷积，对主序列不存在的部分进行补0
	 * @param l 	主序列
	 * @param x		副序列
	 * @return		卷积结果
	 */
	public static double[] conv(double[] l, double[] x) {
		return conv(l, x, false);
	}
	
	/**
	 * 交换double数组的两个元素
	 */
	private static void swap(double[] arr, int a, int b) {
		if (a == b) {
			return;
		}
		double tmp = arr[a];
		arr[a] = arr[b];
		arr[b] = tmp;
	}
	
	/**
	 * 取第k大元素（从0开始计数），用快序排序的思想，算法复杂度O(n)
	 * @param num
	 * @param lower
	 * @param upper
	 * @param k
	 * @return
	 */
	private static double kth(double[] num, int lower, int upper, int k) {
		int left = lower, right = upper;
		while (left < right - 1) {
			int master = (int) (((long) left + right) / 2);
			swap(num, left, master);
			
			int head = left;
			for (int i = left + 1; i < right; ++i) {
				if (num[i] <= num[left]) {
					swap(num, i, ++head);
				}
			}
			swap(num, head, left);
			if (head == k) {
				return num[head];
			} else if (head < k) {
				left = head + 1;
			} else {
				right = head;
			}
		}
		
		return num[left];
	}
	
	/**
	 * 取序列的绝对值下的中位数
	 * @param num 	序列
	 * @return 		中位数
	 */
	public static double absmedian(double[] num) {
		double[] arr = new double[num.length];
		for (int i = 0; i < num.length; ++i) {
			arr[i] = Math.abs(num[i]);
		}
		if (num.length % 2 == 0) {
			return (kth(arr, 0, arr.length, arr.length / 2 - 1) + kth(arr, 0, arr.length, arr.length / 2)) / 2.0;
		} else {
			return kth(arr, 0, arr.length, arr.length / 2);
		}
		
	}
}
