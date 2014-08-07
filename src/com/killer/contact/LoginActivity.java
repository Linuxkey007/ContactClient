package com.killer.contact;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.killer.util.ExitApplication;
import com.killer.util.MyApplication;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText user, pwd;
	private Button btn_login, btn_reg, btn_back;
	private CheckBox ck_save;
	private SharedPreferences sp;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		ExitApplication.getInstance().addActivity(this);
		user = (EditText) findViewById(R.id.user);
		user.getBackground().setAlpha(150);
		pwd = (EditText) findViewById(R.id.pwd);
		pwd.getBackground().setAlpha(150);
		btn_login = (Button) findViewById(R.id.login);
		btn_login.getBackground().setAlpha(150);
		btn_login.setOnClickListener(this);
		btn_reg = (Button) findViewById(R.id.reg);
		btn_reg.getBackground().setAlpha(150);
		btn_reg.setOnClickListener(this);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		ck_save = (CheckBox) findViewById(R.id.ck_save);
		sp = getSharedPreferences("myFile", 0);
		String nameStr = sp.getString("et_name", "");
		String pwdStr = sp.getString("et_pwd", "");
		user.setText(nameStr);
		pwd.setText(pwdStr);
		if (!user.getText().toString().equals("")) {
			ck_save.setChecked(true);
		}
	}

	public void onClick(View v) {
		MyApplication.currLoginName = user.getText().toString().trim();
		if (ck_save.isChecked()) {
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("et_name", user.getText().toString());
			editor.putString("et_pwd", pwd.getText().toString());
			editor.commit();
		} else {
			SharedPreferences.Editor editor = sp.edit();
			editor.remove("et_name");
			editor.remove("et_pwd");
			editor.commit();
		}
		if (v == btn_login) {

			login(user.getText().toString(), pwd.getText().toString());

		} else if (v == btn_reg) {
			Intent reg = new Intent(LoginActivity.this, RegActivity.class);
			startActivity(reg);
			LoginActivity.this.finish();
		} else if (v == btn_back) {
			Intent main = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(main);
			LoginActivity.this.finish();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				Intent backup = new Intent(LoginActivity.this,
						BackupActivity.class);
				startActivity(backup);
				LoginActivity.this.finish();
				Toast.makeText(LoginActivity.this, "µÇÂ½³É¹¦", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(LoginActivity.this, "µÇÂ½Ê§°Ü", Toast.LENGTH_LONG)
						.show();
			}
		};
	};

	public void login(final String login_user, final String login_pwd) {
		new Thread(new Runnable() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				String url = "http://10.0.2.2:8080/ContactServer/cloud!login.action?userName="
						+ login_user + "&userPwd=" + login_pwd;
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
	}
}
