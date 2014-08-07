package com.killer.contact;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.killer.util.ExitApplication;

public class RegActivity extends Activity implements OnClickListener{
	private EditText reg_user, reg_pwd;
	private Button btn_reg, btn_cancel,btn_back;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reg);
		ExitApplication.getInstance().addActivity(this);
		reg_user = (EditText) findViewById(R.id.reg_user);
		reg_user.getBackground().setAlpha(150);
		reg_pwd = (EditText) findViewById(R.id.reg_pwd);
		reg_pwd.getBackground().setAlpha(150);
		btn_reg = (Button) findViewById(R.id.btn_reg);
		btn_reg.getBackground().setAlpha(150);
		btn_reg.setOnClickListener(this);
		btn_cancel = (Button) findViewById(R.id.cancel);
		btn_cancel.getBackground().setAlpha(150);
		btn_cancel.setOnClickListener(this);
		btn_back=(Button)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v == btn_reg) {
			new Thread(new Runnable() {
				public void run() {
					HttpClient client = new DefaultHttpClient();
					String url = "http://10.0.2.2:8080/ContactServer/cloud!register.action?user.userName="
							+ reg_user.getText().toString()
							+ "&user.userPwd="
							+ reg_pwd.getText().toString();
					HttpGet get = new HttpGet(url);
					try {
						HttpResponse resp = client.execute(get);
						if (resp.getStatusLine().getStatusCode() == 200) {
							String str = EntityUtils.toString(resp.getEntity());
							if (str.equals("1")) {

								handler.sendEmptyMessage(1);

							} else {
								handler.sendEmptyMessage(0);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}).start();

		}else if(v==btn_back){
			Intent main=new Intent(RegActivity.this,MainActivity.class);
			startActivity(main);
			RegActivity.this.finish();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				Toast.makeText(RegActivity.this, "注册成功，正在跳转登录界面", Toast.LENGTH_LONG)
						.show();
				Intent login=new Intent(RegActivity.this,LoginActivity.class);
				startActivity(login);
				RegActivity.this.finish();
			} else {
				Toast.makeText(RegActivity.this, "注册失败", Toast.LENGTH_LONG)
						.show();
			}
		};
	};
}
