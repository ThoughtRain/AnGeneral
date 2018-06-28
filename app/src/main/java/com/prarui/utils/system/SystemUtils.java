package com.prarui.utils.system;

import android.content.Context;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by prarui on 2015/8/28.
 */
public class SystemUtils {
	/**
	 * got the phone's name
	 *
	 */
	private static Context context;

	public static void build(Context context){
	   SystemUtils.context=context;
	}
	public static String getPhoneName() {
		return Build.BRAND + "\t" + Build.MODEL;
	}

	/**
	 * got the phone's model
	 */
	public static String getPhoneModel() {
		return "Android\t\t" + getPhoneSystemVersion();
	}

	public static String getPhoneSystemVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * got the phone's CPU name;
	 */
	public static String getPhoneCpuName() {
		String cpuName = null;
		try {
			FileReader fr = new FileReader("/proc/cpuinfo");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			String[] array = text.split(":\\s+");
			cpuName = array[1];
		} catch (FileNotFoundException ignored) {
		} catch (IOException ignored) {

		}
		return cpuName;
	}

	/**
	 * got the phone's cup number;
	 */
	public static int getPhoneCpuNumber() {
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				return Pattern.matches("cpu[0-9]", pathname.getName());
			}
		}
		int length = 0;
		try {
			File file = new File("/sys/devices/system/cpu/");
			File[] files = file.listFiles(new CpuFilter());
			length = files.length;
		} catch (Exception igno) {
		}
		return length;
	}

	/**
	 * got the phone's pixels
	 */
	public static String getResolution() {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return metrics.widthPixels + "*" + metrics.heightPixels;
	}

	/**
	 * got the phone's cameraPixel;
	 */
	public static long getCameraPixel() {
		long maxSize = 0;
		try {
			Camera camera = Camera.open();
			Camera.Parameters parameters = camera.getParameters();
			List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
			for (Camera.Size size : sizes) {
				if (maxSize < size.height * size.width) {
					maxSize = size.height * size.width;
				}
			}
			camera.release();
		} catch (Exception ignored) {

		}
		return maxSize;
	}


	public static String getPhoneBaseBandVerison() {
		String result = null;
		try {
			Class<?> cl = Class.forName("android.os.SystemProperties");
			Object invoker = cl.newInstance();
			Method m = cl.getMethod("get", new Class[] { String.class,
					String.class });
			result = m.invoke(invoker,
					new Object[] { "gsm.version.baseband", "no message" })
					.toString();
		} catch (Exception e) {
		}
		return result;
	}

	public static boolean getIsRoot() {
		boolean result = false;
		String[] pathList = new String[] { "/sbin/", "/system/bin/",
				"/system/xbin/", "/data/local/xbin/", "/data/local/bin/",
				"/system/sd/xbin/", "/system/bin/failsafe/", "/data/loacl/" };
		for (String path : pathList) {
			File file = new File(path + "su");
			if (file.exists()) {
				result = true;
				break;

			}
		}
		return result;
	}


	public static String getDeivceId() {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		String uuid = "";
		if (!"9774d56d682e549c".equals(androidId)) {
			uuid = androidId;
		} else {
			uuid = UUID.randomUUID().toString();
		}
		return uuid;
	}


	public static String getImei() {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();

	}


	public static String getLocalMacAddress() {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}


	public static boolean isNetworkConnected() {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

}
