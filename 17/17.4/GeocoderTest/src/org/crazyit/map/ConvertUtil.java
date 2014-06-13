/**
 *
 */
package org.crazyit.map;

import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/**
 * Description:
 * <br/>网站: <a href="http://www.crazyit.org">疯狂Java联盟</a>
 * <br/>Copyright (C), 2001-2014, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class ConvertUtil
{
	// 根据地址获取对应的经纬度
	public static double[] getLocationInfo(final String address)
	{
		FutureTask<double[]> task = new FutureTask<double[]>(
			new Callable<double[]>()
		{
			public double[] call() throws Exception
			{
				// 定义一个HttpClient，用于向指定地址发送请求
				HttpClient client = new DefaultHttpClient();
				// 向指定地址发送GET请求
				HttpGet httpGet = new HttpGet("http://maps.google.com/maps/"
					+ "api/geocode/json?address=" + address
					+ "ka&sensor=false");
				// 用于模拟该请求的区域来自于简体中文环境，保证返回的响应为简体中文地址
				httpGet.addHeader("Accept-Charset" , "GBK;q=0.7,*;q=0.3");
				httpGet.addHeader("Accept-Language" , "zh-CN,zh;q=0.8");
				StringBuilder sb = new StringBuilder();
				// 获取服务器的响应
				HttpResponse response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				// 获取服务器响应的字符串
				InputStreamReader br = new InputStreamReader(
					entity.getContent() , "utf-8");
				int b;
				while ((b = br.read()) != -1)
				{
					sb.append((char) b);
				}
				// 将服务器返回的字符串转换为JSONObject对象
				JSONObject jsonObject = new JSONObject(sb.toString());
				// 从JSONObject对象中取出代表位置的location属性
				JSONObject location = jsonObject.getJSONArray("results")
					.getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location");
				// 获取经度信息
				double longitude = location.getDouble("lng");
				// 获取纬度信息
				double latitude = location.getDouble("lat");
				// 将经度、纬度信息组成double[]数组
				return new double[]{longitude , latitude};
			}
		});
		new Thread(task).start();
		try
		{
			return task.get();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	// 根据经纬度获取对应的地址
	public static String getAddress(final double longitude
		, final double latitude)
	{
		FutureTask<String> task = new FutureTask<String>(
			new Callable<String>()
		{
			public String call() throws Exception
			{
				// 定义一个HttpClient，用于向指定地址发送请求
				HttpClient client = new DefaultHttpClient();
				// 向指定地址发送GET请求
				HttpGet httpGet = new HttpGet("http://maps.google.com/maps/"
					+ "api/geocode/json?latlng="
					+ latitude + "," + longitude
					+ "&sensor=false");
				// 用于模拟该请求的区域来自于简体中文环境，保证返回的响应为简体中文地址
				httpGet.addHeader("Accept-Charset" , "GBK;q=0.7,*;q=0.3");
				httpGet.addHeader("Accept-Language" , "zh-CN,zh;q=0.8");
				StringBuilder sb = new StringBuilder();
				// 执行请求
				HttpResponse response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				// 获取服务器响应的字符串
				InputStreamReader br = new InputStreamReader(
					entity.getContent() , "utf-8");
				int b;
				while ((b = br.read()) != -1)
				{
					sb.append((char) b);
				}
				// 把服务器相应的字符串转换为JSONObject
				JSONObject jsonObj = new JSONObject(sb.toString());
				// 解析出响应结果中的地址数据
				return jsonObj.getJSONArray("results").getJSONObject(0)
					.getString("formatted_address");
			}
		});
		new Thread(task).start();
		try
		{
			return task.get();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
