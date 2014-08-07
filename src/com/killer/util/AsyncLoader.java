package com.killer.util;

import java.io.FileNotFoundException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import com.killer.contact.R;

public class AsyncLoader {//异步加载器
	//图片缓存  SoftReference ---在内存不够充足时,自动释放内存.
	private static HashMap<String, SoftReference<Drawable>> imageCache=new HashMap<String, SoftReference<Drawable>>();
	//加载图片
	public Drawable loadDrawable(final Context context,final String imageUrl,final ImageView iv,final ImageCallBack callback){
		//先判断缓存中是否有该Drawable
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable dr=softReference.get();
			if(dr!=null){  
				Log.e("info", "从缓存中取得图片");
				return dr;
			}
		}
		
		final Handler mh=new Handler(){
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				callback.drawCall((Drawable)msg.obj,iv);  //将得到的drawable传给前台activity
			}
		};
		//如果在缓存中没有找到图片,则需要去SDcard里面找
		new Thread(new Runnable() {
			public void run() {
				try {
					Message msg=Message.obtain();
					if(imageUrl!=null){
						ContentResolver contentResolver=context.getContentResolver();
						Bitmap bitmap=BitmapFactory.decodeStream(contentResolver.openInputStream(Uri.parse(imageUrl)));
						BitmapDrawable bd=new BitmapDrawable(bitmap);
						imageCache.put(imageUrl,new SoftReference<Drawable>(bd));
						msg.obj=bd;
					}else{
						Drawable db = context.getResources().getDrawable(R.drawable.ic_launcher);
						imageCache.put(imageUrl,new SoftReference<Drawable>(db));
						msg.obj=db;
					}
					mh.sendMessage(msg);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return null;
	}
	//回调方法
	public interface ImageCallBack{  
		public void drawCall(Drawable dr,ImageView iv);  //放入activity需要的参数  
	}
}