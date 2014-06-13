/**
 *
 */
package org.crazyit.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crazyit.service.IPet.Stub;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

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
public class ComplexService extends Service
{
	private PetBinder petBinder;
	private static Map<Person , List<Pet>> pets
		= new HashMap<Person , List<Pet>>();
	static
	{
		// 初始化pets Map集合
		ArrayList<Pet> list1 = new ArrayList<Pet>();
		list1.add(new Pet("旺财" , 4.3));
		list1.add(new Pet("来福" , 5.1));
		pets.put(new Person(1, "sun" , "sun") , list1);
		ArrayList<Pet> list2 = new ArrayList<Pet>();
		list2.add(new Pet("kitty" , 2.3));
		list2.add(new Pet("garfield" , 3.1));
		pets.put(new Person(2, "bai" , "bai") , list2);
	}
	// 继承Stub，也就是实现额IPet接口，并实现了IBinder接口
	public class PetBinder extends Stub
	{
		@Override
		public List<Pet> getPets(Person owner) throws RemoteException
		{
			// 返回Service内部的数据
			return pets.get(owner);
		}
	}
	@Override
	public void onCreate()
	{
		super.onCreate();
		petBinder = new PetBinder();
	}
	@Override
	public IBinder onBind(Intent arg0)
	{
		/* 返回catBinder对象
		 * 在绑定本地Service的情况下，该catBinder对象会直接
		 * 传给客户端的ServiceConnection对象
		 * 的onServiceConnected方法的第二个参数；
		 * 在绑定远程Service的情况下，只将catBinder对象的代理
		 * 传给客户端的ServiceConnection对象
		 * 的onServiceConnected方法的第二个参数；
		 */
		return petBinder; //①
	}
	@Override
	public void onDestroy()
	{
	}
}
