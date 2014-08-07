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
	private static String[] arr = { "���7��", "���15��", "���30��",
			"���90��", "���180��", "���365��" };
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
			if ("���7��".equals(sp.getSelectedItem().toString())) {
				bundle.putString("select_date", "7");
			} else if ("���15��".equals(sp.getSelectedItem().toString())) {
				bundle.putString("select_date", "15");
			} else if ("���30��".equals(sp.getSelectedItem().toString())) {
				bundle.putString("select_date", "30");
			} else if ("���90��".equals(sp.getSelectedItem().toString())) {
				bundle.putString("select_date", "90");
			} else if ("���180��".equals(sp.getSelectedItem().toString())) {
				bundle.putString("select_date", "180");
			} else if ("���365��".equals(sp.getSelectedItem().toString())) {
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
					// �����ϴ��������ĵ�ַ
					String url = "http://10.0.2.2:8080/ContactServer/cloud!upload.action";
					// �����ϴ�PostMethod�� �󷽷�
					PostMethod filePost = new PostMethod(url);
					// ���嵱ǰ�ϴ��˵�����
					StringPart sp1 = new StringPart("userName",
							MyApplication.currLoginName);
					// �����ϴ����ļ���
					StringPart sp2 = new StringPart("contactName", dataFile
							.getName());
					// �����ϴ����ļ�
					Log.i("����Ҫ��ֵ��", dataFile.length() + "");
					FilePart sp3 = new FilePart("contact", dataFile);
					// �����ϴ����ļ����ϴ���������Ϣ
					Part[] parts = new Part[] { sp1, sp2, sp3 };
					// ���������ø�filePost
					filePost.setRequestEntity(new MultipartRequestEntity(parts,
							filePost.getParams()));
					// ע�������HttpClient������ǰʹ�õ�HttpClient,����������commons-httpclient.jar���µ�
					HttpClient client = new HttpClient();
					// ���������ʱ��
					client.getHttpConnectionManager().getParams()
							.setConnectionTimeout(5000);
					// �����ϴ����ܣ�����÷�����
					int result = client.executeMethod(filePost);
					Log.i("����Ҫ��ֵ��", result + "");
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
				Toast.makeText(BackupActivity.this, "�ϴ��ɹ�", 3000).show();
			} else if (msg.what == -1) {
				Toast.makeText(BackupActivity.this, "�ϴ�ʧ��", 3000).show();
			} else {
				Toast.makeText(BackupActivity.this, "�޷����ӷ�����", 3000).show();
			}
		};
	};

}
