package com.mike.scorequery.network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

//异步任务+接口回调
public class MyNetwork {

	public void getData(String urlStr,
						final SuccessCallBack successCallBack,
						final FailCallBack failCallBack) {
		// 异步任务
		new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... arg0) {
				StringBuffer result = new StringBuffer();
				InputStream inputStream = null;
				try {
					URL url = new URL(arg0[0]);
					URLConnection urlConnection = url.openConnection();
					urlConnection.setRequestProperty("Authorization" , "APPCODE 059fbae46c294b3ba8682995adf2b655");
					inputStream = urlConnection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(inputStream));
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						result.append(line);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (inputStream!=null) {
							inputStream.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return result.toString();
			}
			@Override
			protected void onPostExecute(String result) {

				if (result != null) {
					if (successCallBack != null) {
						successCallBack.onSuccess(result);
					}
				} else {
					if (failCallBack != null) {
						failCallBack.onFail();
					}
				}
			}
		}.execute(urlStr);
	}

	public interface SuccessCallBack {
		void onSuccess(String result);
	}

	public interface FailCallBack {
		void onFail();
	}
}