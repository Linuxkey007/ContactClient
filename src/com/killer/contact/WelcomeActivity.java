package com.killer.contact;

import com.killer.util.ExitApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class WelcomeActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		ExitApplication.getInstance().addActivity(this);
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(3000);
					Intent intent=new Intent(WelcomeActivity.this, MainActivity.class);
					startActivity(intent);
					WelcomeActivity.this.finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}

}
