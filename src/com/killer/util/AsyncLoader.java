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

public class AsyncLoader {//�첽������
	//ͼƬ����  SoftReference ---���ڴ治������ʱ,�Զ��ͷ��ڴ�.
	private static HashMap<String, SoftReference<Drawable>> imageCache=new HashMap<String, SoftReference<Drawable>>();
	//����ͼƬ
	public Drawable loadDrawable(final Context context,final String imageUrl,final ImageView iv,final ImageCallBack callback){
		//���жϻ������Ƿ��и�Drawable
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable dr=softReference.get();
			if(dr!=null){  
				Log.e("info", "�ӻ�����ȡ��ͼƬ");
				return dr;
			}
		}
		
		final Handler mh=new Handler(){
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				callback.drawCall((Drawable)msg.obj,iv);  //���õ���drawable����ǰ̨activity
			}
		};
		//����ڻ�����û���ҵ�ͼƬ,����ҪȥSDcard������
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
	//�ص�����
	public interface ImageCallBack{  
		public void drawCall(Drawable dr,ImageView iv);  //����activity��Ҫ�Ĳ���  
	}
}