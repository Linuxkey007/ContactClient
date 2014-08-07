package com.killer.contact;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.killer.entity.Backup;
import com.killer.util.ExitApplication;
import com.killer.util.MyApplication;

@SuppressLint("ShowToast")
public class BackupListActivity extends Activity implements OnClickListener {
	private String select_date;
	private List<Backup> list = new ArrayList<Backup>();
	private MyAdapter myAdapter;
	private ListView backup_list;
	private String sd_path;
	private Button btn_back;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_backuplist);
		ExitApplication.getInstance().addActivity(this);
		backup_list = (ListView) findViewById(R.id.backup_list);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		Bundle bundle = this.getIntent().getExtras();
		select_date = bundle.getString("select_date");
		// 获得存储卡的路径

		new Thread(new Runnable() {
			public void run() {
				// 获取服务器数据
				HttpClient client = new DefaultHttpClient();

				String url = "http://10.0.2.2:8080/ContactServer/cloud!getBackupDbs.action?userName="
						+ MyApplication.currLoginName
						+ "&timeType="
						+ select_date;
				Log.i("aaaa", "url----" + url);
				HttpGet get = new HttpGet(url);

				try {
					HttpResponse resp = client.execute(get);

					if (resp.getStatusLine().getStatusCode() == 200) {
						// 创建装服务器返回备份列表的集合
						list = new ArrayList<Backup>();

						// 服务器返回的json数据
						String str = EntityUtils.toString(resp.getEntity());
						// 解析json字符串
						JSONObject jsonObj = new JSONObject(str);
						JSONArray jsonArr = jsonObj.getJSONArray("contacts");
						for (int i = 0; i < jsonArr.length(); i++) {
							// 取出每个json对象
							JSONObject o = jsonArr.getJSONObject(i);

							Backup b = new Backup();
							b.setContactId(o.getInt("contactId"));
							Log.i("+++++++++++++++++++++",
									o.getInt("contactId") + "");
							b.setContactName(o.getString("contactName"));
							Log.i("+++++++++++++++++++++",
									o.getString("contactName"));
							b.setContactSize(o.getString("contactSize"));
							Log.i("+++++++++++++++++++++",
									o.getString("contactSize"));
							b.setContactTime(o.getString("contactTime"));
							Log.i("+++++++++++++++++++++",
									o.getString("contactTime"));

							list.add(b);
						}
						if (list.size() > 0) {
							handler.sendEmptyMessage(0);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View arg1, ViewGroup arg2) {
			View view = getLayoutInflater().inflate(R.layout.backup_item, null);
			final Backup backup = list.get(position);
			TextView ContactId = (TextView) view.findViewById(R.id.contactId);
			TextView ContactName = (TextView) view
					.findViewById(R.id.contactName);
			TextView ContactSize = (TextView) view
					.findViewById(R.id.contactSize);
			TextView ContactTime = (TextView) view
					.findViewById(R.id.contactTime);
			ImageView DownBtn = (ImageView) view.findViewById(R.id.backup_down);
			DownBtn.getBackground().setAlpha(0);

			ContactId.setText(backup.getContactId() + "");
			ContactName.setText(backup.getContactName());
			ContactSize.setText(backup.getContactSize());
			ContactTime.setText(backup.getContactTime());
			DownBtn.setOnClickListener(new OnClickListener() {
				Backup backupId = list.get(position);

				public void onClick(View v) {
					downloadThisFile(backupId.getContactId());
					Log.i("执行到了", backupId.getContactId() + "");
					Toast.makeText(BackupListActivity.this,
							"您选择了" + list.get(position).getContactId(), 500)
							.show();
				}
			});

			return view;
		}

	}

	// 下载选中的备份文件到本地
	public void downloadThisFile(final int contactId) {
		new Thread(new Runnable() {
			public void run() {
				try {
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						// 判断SD卡是否存在，并且是否具有读写权限
						sd_path = Environment.getExternalStorageDirectory()
								+ "/";
						Log.i("+++++++++路径", sd_path);
						URL url = new URL(
								"http://10.0.2.2:8080/ContactServer/cloud!download.action?contactId="
										+ contactId);
						Log.i("++++++++++++++++++++++++++++此处的contactId是：",
								contactId + "");
						// 创建连接
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.connect();
						// 获取文件大小
						int length = conn.getContentLength();
						// 创建输入流
						InputStream is = conn.getInputStream();

						// 构建文件流,直接保存在根目录下
						File downFile = new File(sd_path + "killer.db");
						Log.i("执行到文件流", "++++++++");
						FileOutputStream fos = new FileOutputStream(downFile);

						byte buf[] = new byte[1024];
						int len = 0;
						while ((len = is.read(buf)) != -1) {
							fos.write(buf, 0, len);
						}
						fos.flush();
						fos.close();
						is.close();
						// 保存完毕,提示是否覆盖
						handler.sendEmptyMessage(4);
						Log.i("执行到发送消息", "hander.sendEmptyMessage(4);");
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(5);
					Log.i("执行到发送消息", "hander.sendEmptyMessage(5);");
				}
			}
		}).start();
	}

	public void isReplaceDatabase() {
		Builder b = new Builder(this);
		b.setTitle("系统提示");
		b.setMessage("下载完毕，您是否覆盖恢复通讯录，该操作会导致联系人信息丢失？");
		b.setNegativeButton("取消", null);
		b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				// 确定则覆盖文件
				File dataFile = new File("/data/data/"
						+ BackupListActivity.this.getPackageName()
						+ "/databases/killer.db");
				if (dataFile.exists()) {
					// 文件存在,删除，再复制
					dataFile.delete();
				}
				// 覆盖进去
				try {
					File downFile = new File(sd_path + "killer.db");
					FileInputStream fis = new FileInputStream(downFile);
					BufferedInputStream bufIs = new BufferedInputStream(fis);

					// 构建输出流写
					FileOutputStream fos = new FileOutputStream("/data/data/"
							+ BackupListActivity.this.getPackageName()
							+ "/databases/killer.db");
					BufferedOutputStream bufOs = new BufferedOutputStream(fos);

					byte[] b = new byte[1];
					while (bufIs.read(b) != -1) {
						bufOs.write(b);
					}
					bufOs.flush();
					bufOs.close();
					fos.close();
					// 关闭流
					bufIs.close();
					fis.close();
					rest();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		b.create().show();
	}
//重新启动通讯录
	public void rest() {
		Builder b = new Builder(this);
		b.setTitle("系统提示");
		b.setMessage("通讯录恢复完成，是否重启通讯录？");
		b.setNegativeButton("取消", null);
		b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {

				try {
					Thread.sleep(2000);
					Intent rest = getBaseContext().getPackageManager()
							.getLaunchIntentForPackage(
									getBaseContext().getPackageName());
					rest.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(rest);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		b.create().show();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 判断myAdapter是否为空，若不为空刷新，否则直接装在数据
			if (myAdapter != null) {
				myAdapter.notifyDataSetChanged();
			} else if (myAdapter == null) {
				myAdapter = new MyAdapter();
				backup_list.setAdapter(myAdapter);
			}
			if (msg.what == 4) {
				isReplaceDatabase();
				Toast.makeText(BackupListActivity.this, "下载成功",
						Toast.LENGTH_LONG).show();
				Log.i("+++++++++++++++++++++执行到", "成功");
			} else if (msg.what == 5) {
				Toast.makeText(BackupListActivity.this, "下载失败",
						Toast.LENGTH_LONG).show();
				Log.i("+++++++++++++++++++++执行到", "下载失败");
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if (v == btn_back) {
			Intent backup = new Intent(BackupListActivity.this,
					BackupActivity.class);
			startActivity(backup);
			BackupListActivity.this.finish();

		}
	}
}
