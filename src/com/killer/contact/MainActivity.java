package com.killer.contact;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.killer.db.ContactService;
import com.killer.entity.Contact;
import com.killer.pup.Ppupwin;
import com.killer.util.AsyncLoader;
import com.killer.util.AsyncLoader.ImageCallBack;
import com.killer.util.ExitApplication;

public class MainActivity extends Activity implements OnClickListener{
	private ListView contact_list;
	private List<Contact> list=new ArrayList<Contact>();
	private ContactAdapter ada;
	private View v;
	private Ppupwin menuWindow;
	private OnClickListener itemsOnClick;
	private EditText userName;
	private AsyncLoader asyncLoader = new AsyncLoader();
	private Context context;
	private ImageButton img_add;
	private TextView contact_null;
 


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		context = MainActivity.this;
		ExitApplication.getInstance().addActivity(this);
		contact_list = (ListView) findViewById(R.id.contact_list);
		userName = (EditText) findViewById(R.id.userName);
		userName.getBackground().setAlpha(150);
		img_add=(ImageButton)findViewById(R.id.img_add);
		img_add.getBackground().setAlpha(0);
		img_add.setOnClickListener(this);
		contact_null=(TextView)findViewById(R.id.contact_null);
		
		list = new ContactService(MainActivity.this).findAll();
		ada = new ContactAdapter();
		if(list.size()==0){
			contact_null.setText("无联系人，请添加！");
		}
		contact_list.setAdapter(ada);
		/**
		 * 监听文本框变化，实现及时搜索
		 */
		userName.addTextChangedListener(new TextWatcher() {

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 0) {
					String name = userName.getText().toString();
					if (name.equals("")) {
						list = new ContactService(MainActivity.this).findAll();
					} else {
						list = new ContactService(MainActivity.this)
								.findByName(name);
					}
					ada = new ContactAdapter();
					contact_list.setAdapter(ada);
				}
			}

			public void afterTextChanged(Editable s) {

			}
		});
	}

	public class ContactAdapter extends BaseAdapter {

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final int positions = position;
			Contact contact = list.get(positions);
			v = getLayoutInflater().inflate(R.layout.contact_item, null);
			ImageView tv_img = (ImageView) v.findViewById(R.id.tv_img);
			TextView tv_name = (TextView) v.findViewById(R.id.tv_name);
			TextView tv_mobile = (TextView) v.findViewById(R.id.tv_mobile);
			v.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					// 实例化SelectPicPopupWindow
					menuWindow = new Ppupwin(MainActivity.this,
							itemsOnClick = new OnClickListener() {

								public void onClick(View v) {
									menuWindow.dismiss();
									switch (v.getId()) {
									case R.id.btn_add:
										Intent add = new Intent();
										add.setClass(getApplicationContext(),
												AddActivity.class);
										startActivity(add);
										MainActivity.this.finish();
										break;
									case R.id.btn_call:
										if (!"".equals(list.get(positions)
												.getMobile())) {
											Intent call = new Intent(
													Intent.ACTION_CALL,
													Uri.parse("tel:"
															+ list.get(
																	positions)
																	.getMobile()
																	.toString()));
											startActivity(call);
										} else if (!"".equals(list.get(
												positions).getHome())) {
											Intent call = new Intent(
													Intent.ACTION_CALL,
													Uri.parse("tel:"
															+ list.get(
																	positions)
																	.getHome()
																	.toString()));
											startActivity(call);
										} else if (!"".equals(list.get(
												positions).getOffice())) {
											Intent call = new Intent(
													Intent.ACTION_CALL,
													Uri.parse("tel:"
															+ list.get(
																	positions)
																	.getOffice()
																	.toString()));
											startActivity(call);
										}

										MainActivity.this.finish();
										break;

									case R.id.btn_sms:
										String number = list.get(positions)
												.getMobile().trim();
										if (number.length() == 11) {
											Intent sms = new Intent(
													Intent.ACTION_SENDTO,
													Uri.parse("smsto:"
															+ list.get(
																	positions)
																	.getMobile()
																	.toString()));
											startActivity(sms);
											MainActivity.this.finish();
											
										} else {
											Toast.makeText(MainActivity.this,
													"此类型号码不能发送信息",
													Toast.LENGTH_LONG).show();
										}
										break;
									case R.id.exit:
										ExitApplication.getInstance().exit();
										break;
									case R.id.btn_del:
										AlertDialog.Builder builder = new AlertDialog.Builder(
												MainActivity.this);

										// 设置对话框标题、内容、按钮,set方法每次返回this，即dialog本身
										builder.setIcon(R.drawable.check);// 设置标题图片
										builder.setTitle("删除联系人： ");
										builder.setMessage("确定删除吗?");
										builder.setPositiveButton(
												"确定",
												new DialogInterface.OnClickListener()// 此处的listener与上面的按钮listener来自于不同包
												{
													public void onClick(
															DialogInterface dialog,
															int which) {

														ContactService service = new ContactService(
																MainActivity.this);
														service.delete(list
																.get(positions)
																.getId());
														list = new ContactService(
																MainActivity.this)
																.findAll();
														if(list.size()==0){
															contact_null.setText("无联系人，请添加！");
														}
														ada.notifyDataSetChanged();

													}
												});

										builder.setNegativeButton(
												"取消",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														// 点击取消后退出程序
														dialog.cancel();

													}
												});

										AlertDialog dialog = builder.create();
										dialog.show();// 记得加上show()方法
										break;
									case R.id.btn_perUpdate:
										Intent update = new Intent(
												MainActivity.this,
												UpdateActivity.class);
										Bundle b = new Bundle();
										b.putInt("id", list.get(positions)
												.getId());
										Log.i("++++++++++++++++++",
												list.get(positions).getId()
														+ "");
										update.putExtras(b);
										startActivity(update);
										MainActivity.this.finish();
										break;
									default:
										break;
									}
								}
							});
					// 显示窗口
					menuWindow.showAtLocation(
							MainActivity.this.findViewById(R.id.contact_list),
							Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
					return false;
				}
			});

			Drawable dr = asyncLoader.loadDrawable(context, contact.getImg(),
					tv_img, new ImageCallBack() {
						public void drawCall(Drawable dr, ImageView iv) {
							iv.setImageDrawable(dr);
							int i = 0;
							++i;
						}
					});
			if (dr == null) {
				tv_img.setImageResource(R.drawable.ic_launcher);
			}
			tv_img.setImageDrawable(dr);
			System.out.println(positions + "=============");
			System.out.println(list.get(positions).getMobile()
					+ "=============");
			tv_name.setText(list.get(positions).getName());
			if (!"".equals(list.get(positions).getMobile())) {
				tv_mobile.setText("手机:" + list.get(positions).getMobile());
				Log.i("++++++++手机", list.get(positions).getMobile());
			} else if (!"".equals(list.get(positions).getHome())) {
				tv_mobile.setText("家庭:" + list.get(positions).getHome());
				Log.i("++++++++家庭", list.get(positions).getHome());
			} else if (!"".equals(list.get(positions).getOffice())) {
				tv_mobile.setText("办公:" + list.get(positions).getOffice());
				Log.i("++++++++办公", list.get(positions).getOffice());
			}
			return v;
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.login:
			Intent intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			startActivity(intent);
			MainActivity.this.finish();
		case R.id.exit:
			ExitApplication.getInstance().exit();
		}
		return true;

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if(v==img_add){
			Intent add=new Intent(MainActivity.this,AddActivity.class);
			startActivity(add);
			MainActivity.this.finish();
		}
		
	}

}
