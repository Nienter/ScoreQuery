package com.mike.scorequery.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogTools {

	private String tag = "TFTPAY";
	public static int logLevel = 0;

	public static boolean writeFileFlag = false;
	public static String logFile = "";
	public static boolean isPrint = true;

	public static FileOutputStream fileinput = null;

	public LogTools() {
		String status = Environment.getExternalStorageState();

		if (writeFileFlag && status.equals(Environment.MEDIA_MOUNTED)) {
			logFile = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/Logger/" + tag + ".log";
			File file = new File(logFile);

			if (file.exists()) {
				file.delete();
			}
			try {
				file.createNewFile();
				fileinput = new FileOutputStream(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public LogTools(String tag) {
		this.tag = tag;
		String status = Environment.getExternalStorageState();

		if (writeFileFlag && status.equals(Environment.MEDIA_MOUNTED)) {
			logFile = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/Logger/" + tag + ".log";

			File file = new File(logFile);
			if (file.exists()) {
				file.delete();
			}
			try {
				file.createNewFile();
				fileinput = new FileOutputStream(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();

		if (sts == null) {
			return null;
		}

		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(this.getClass().getName())) {
				continue;
			}

			return "[ " + Thread.currentThread().getId() + ": "
					+ st.getFileName() + ":" + st.getLineNumber() + " ]";
		}

		return null;
	}

	public void info(Object str) {

		if (logLevel <= Log.INFO) {
			String name = getFunctionName();

			String ls = (name == null ? str.toString() : (name + " - " + str));
			if (isPrint) {
				
			}
			if (writeFileFlag) {
				writeToFile("Info", tag, name + " - " + str);
			}
		}
	}

	public void i(Object str) {
		info(str);
	}

	public void verbose(Object str) {
		if (logLevel <= Log.VERBOSE) {
			String name = getFunctionName();

			String ls = (name == null ? str.toString() : (name + " - " + str));
			if (isPrint) {
				Log.v(tag, ls);
			}
			
			if (writeFileFlag) {
				writeToFile("Verbose", tag, ls);
			}
		}
	}

	public void v(Object str) {
		verbose(str);
	}

	public void warn(Object str) {
		if (logLevel <= Log.WARN) {
			String name = getFunctionName();

			String ls = (name == null ? str.toString() : (name + " - " + str));
			if (isPrint) {
				Log.w(tag, ls);
			}
			if (writeFileFlag) {
				writeToFile("Warn", tag, ls);
			}
		}
	}

	public void w(Object str) {
		warn(str);
	}

	public void error(Object str) {
		if (logLevel <= Log.ERROR) {
			String name = getFunctionName();

			String ls = (name == null ? str.toString() : (name + " - " + str));
			if (isPrint) {
				Log.e(tag, ls);
			}
			if (writeFileFlag) {
				writeToFile("Error", tag, ls);
			}
		}
	}

	public void error(Exception ex) {
		if (logLevel <= Log.ERROR) {

			StringBuffer sb = new StringBuffer();
			String name = getFunctionName();

			StackTraceElement[] sts = ex.getStackTrace();

			if (name != null) {
				sb.append(name + " - " + ex + "\r\n");
			} else {
				sb.append(ex + "\r\n");
			}

			if (sts != null && sts.length > 0) {
				for (StackTraceElement st : sts) {
					if (st != null) {
						sb.append("[ " + st.getFileName() + ":"
								+ st.getLineNumber() + " ]\r\n");
					}
				}
			}
			if (isPrint) {
				Log.e(tag, sb.toString());
			}

			if (writeFileFlag)
				writeToFile("Excep", tag, sb.toString());
		}
	}

	public void e(Object str) {
		error(str);
	}

	public void e(Exception ex) {
		error(ex);
	}

	public void debug(Object str) {

		if (logLevel <= Log.DEBUG) {
			String name = getFunctionName();
			String ls = (name == null ? str.toString() : (name + " - " + str));
			if (isPrint) {
				Log.d(tag, ls);
			}
			if (writeFileFlag) {
				writeToFile("Debug", tag, ls);
			}
		}
	}

	public void d(Object str) {
		debug(str);
	}

	private void writeToFile(String level, String tag, String info) {
		String status = Environment.getExternalStorageState();

		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return;
		}

		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd   hh:mm:ss");
		String date = sDateFormat.format(new Date());
		String msg = date + "  " + level + "--" + tag + ":" + info;

		try {
			fileinput.write(msg.toString().getBytes());
			fileinput.write("\r\n".getBytes());
			fileinput.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
