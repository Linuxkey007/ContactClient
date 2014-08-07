package com.killer.contact;

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.killer.util.ExitApplication;
import com.killer.util.MyApplication;

public class BackupActivity extends Activity implements OnClickListener {
	private Button btn_backup, btn_reduce, btn_back;
	private Spinner sp;
	private ArrayAdapter<String> adapter;
	private static String[] arr = { "最近7天", "最近15天", "最近30天",
			"最近90天", "最近180天", "最近365天" };
	private Bundle bundle = new Bundle();
	private Intent intent = new Intent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_backup);
		ExitApplication.getInstance().addActivity(this);
		btn_backup = (Button) findViewById(R.id.backup);
		btn_backup.getBackground().setAlpha(150);
		btn_backup.setOnClickListener(this);
		btn_reduce = (Button) findViewById(R.id.reduce);
		btn_reduce.getBackground().setAlpha(150);
		btn_reduce.setOnClickListener(this);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		sp = (Spinner) findViewById(R.id.backup_list);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr);
		sp.setAdapter(adapter);
		sp.setVisibility(View.VISIBLE);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	public void onClick(View v) {
		if (v == btn_backup) {
			Upload();
		} else if (v == btn_back) {
			Intent main = new Intent(BackupActivity.this, MainActivity.class);
			startActivity(main);
			BackupActivity.this.finish();
		} else if (v == btn_reduce) {
			if ("最近7天".equals(sp.getSelectedItem().toString())) {
				bundle.putString("select_date", "7");
			} else if ("最近15天".equals(sp.getSelectedItem().toString())) {
				bundle.putString("select_date", "15");
			} else if ("最近30天".equals(sp.getSelectedItem().toString())) {
				bundle.putString("select_date", "30");
			} else if ("最近90天".equals(sp.getSelectedItem().toString())) {
				bundle.putString("select_date", "90");
			} else if ("最近180天".equals(sp.getSelectedItem().toString())) {
				bundle.putString("select_date", "180");
			} else if ("最近365天".equals(sp.getSelectedItem().toString())) {
				bundle.putString("select_date", "365");
			}
			intent.setClass(BackupActivity.this, BackupListActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);

		}

	}

	private void Upload() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					File dataFile = new File("/data/data/"
							+ BackupActivity.this.getPackageName()
							+ "/databases/killer.db");
					// 定义上传服务器的地址
					String url = "http://10.0.2.2:8080/ContactServer/cloud!upload.action";
					// 定义上传PostMethod对 象方法
					PostMethod filePost = new PostMethod(url);
					// 定义当前上传人的姓名
					StringPart sp1 = new StringPart("userName",
							MyApplication.currLoginName);
					// 定义上传到文件名
					StringPart sp2 = new StringPart("contactName", dataFile
							.getName());
					// 定义上传的文件
					Log.i("我需要的值是", dataFile.length() + "");
					FilePart sp3 = new FilePart("contact", dataFile);
					// 定义上传的文件和上传的其他信息
					Part[] parts = new Part[] { sp1, sp2, sp3 };
					// 将参数设置给filePost
					filePost.setRequestEntity(new MultipartRequestEntity(parts,
							filePost.getParams()));
					// 注：这里的HttpClient并非以前使用的HttpClient,而是来自于commons-httpclient.jar包下的
					HttpClient client = new HttpClient();
					// 设置请求的时间
					client.getHttpConnectionManager().getParams()
							.setConnectionTimeout(5000);
					// 调用上传功能，并获得返回码
					int result = client.executeMethod(filePost);
					Log.i("我需要的值是", result + "");
					if (result == HttpStatus.SC_OK) {
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(-1);
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(-2);
				}

			}
		}).start();

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				Toast.makeText(BackupActivity.this, "上传成功", 3000).show();
			} else if (msg.what == -1) {
				Toast.makeText(BackupActivity.this, "上传失败", 3000).show();
			} else {
				Toast.makeText(BackupActivity.this, "无法连接服务器", 3000).show();
			}
		};
	};

}
