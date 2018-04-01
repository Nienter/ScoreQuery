package com.mike.scorequery.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SPUtils
{
	private static SharedPreferences mSp;
	private static final String SP_NAME	= "config";

	private static SharedPreferences getPreferences(Context context)
	{
		if (mSp == null)
		{
			mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		return mSp;
	}

	/**
	 * 获得boolean数据
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 *            :没有时的默认值
	 * @return
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue)
	{
		SharedPreferences sp = getPreferences(context);
		return sp.getBoolean(key, defValue);
	}

	/**
	 * 获得boolean数据，如果没有返回false
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(Context context, String key)
	{
		return getBoolean(context, key, false);
	}

	/**
	 * 设置boolean数据
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setBoolean(Context context, String key, boolean value)
	{
		SharedPreferences sp = getPreferences(context);
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 获得String数据
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 *            :没有时的默认值
	 * @return
	 */
	public static String getString(Context context, String key, String defValue)
	{
		SharedPreferences sp = getPreferences(context);
		return sp.getString(key, defValue);
	}

	/**
	 * 获得String数据，如果没有返回null
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getString(Context context, String key)
	{
		return getString(context, key, null);
	}

	/**
	 * 设置String数据
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setString(Context context, String key, String value)
	{
		SharedPreferences sp = getPreferences(context);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 获得long数据
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 *            :没有时的默认值
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue)
	{
		SharedPreferences sp = getPreferences(context);
		return sp.getLong(key, defValue);
	}

	/**
	 * 获得long数据，如果没有返回0
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static long getLong(Context context, String key)
	{
		return getLong(context, key, 0);
	}

	/**
	 * 设置long数据
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setLong(Context context, String key, long value)
	{
		SharedPreferences sp = getPreferences(context);
		Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	/**
	 * 清除数据
	 * @param context
	 */
	public static void clearData(Context context) {
		SharedPreferences sp = getPreferences(context);
		sp.edit().clear().commit();
	}
}
