package com.killer.contact;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.killer.db.ContactService;
import com.killer.entity.Contact;
import com.killer.util.ExitApplication;

public class AddActivity extends Activity implements OnClickListener {
	private EditText name, addr, mobile, home, office, email;
	private Button btn_save, btn_cancel, btn_back;
	private String nameStr, mobileStr, homeStr, officeStr, emailStr, addrStr;
	private ImageView img;
	private Uri uri = null;
	private Bitmap bm;;
	private Contact contact = new Contact();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add);
		ExitApplication.getInstance().addActivity(this);
		img = (ImageView) findViewById(R.id.img);
		name = (EditText) findViewById(R.id.name);
		name.getBackground().setAlpha(150);
		addr = (EditText) findViewById(R.id.addr);
		addr.getBackground().setAlpha(150);
		mobile = (EditText) findViewById(R.id.mobile);
		mobile.getBackground().setAlpha(150);
		home = (EditText) findViewById(R.id.home);
		home.getBackground().setAlpha(150);
		office = (EditText) findViewById(R.id.office);
		office.getBackground().setAlpha(150);
		email = (EditText) findViewById(R.id.email);
		email.getBackground().setAlpha(150);
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_save.getBackground().setAlpha(150);
		btn_save.setOnClickListener(this);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.getBackground().setAlpha(150);
		btn_cancel.setOnClickListener(this);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		img.setOnClickListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_user, menu);
		return true;
	}

	// ������л�ȡ��Ƭ
	protected void getImageFromAlbum() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");// ��Ƭ����
		startActivityForResult(intent, 0);
	}

	// �����
	protected void getImageFromCamera() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent getImageByCamera = new Intent(
					"android.media.action.IMAGE_CAPTURE");
			startActivityForResult(getImageByCamera, 1);
		} else {
			Toast.makeText(getApplicationContext(), "��ȷ���Ѿ�����SD��",
					Toast.LENGTH_LONG).show();
		}
	}

	// ȡ����Ƭ
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (data != null) {
				uri = data.getData();
				img.setTag(uri); // ͨ��tag��ֵ
				long time = System.currentTimeMillis();
				try {
					ContentResolver contentResolver = this.getContentResolver();
					InputStream is = contentResolver.openInputStream(uri);
					bm = BitmapFactory.decodeStream(is);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				long time1 = System.currentTimeMillis();
				System.out.println("����:(" + (time1 - time));
				img.setImageBitmap(bm);

			}
		} else if (requestCode == 1) {
			if (data != null) {
				uri = data.getData();
				if (uri == null) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						bm = (Bitmap) bundle.get("data"); // get bitmap
						uri = Uri.parse(MediaStore.Images.Media.insertImage(
								getContentResolver(), bm, null, null));
						img.setImageBitmap(bm);
					} else {
						Toast.makeText(getApplicationContext(), "err****",
								Toast.LENGTH_LONG).show();
						return;
					}
				} else {
					try {
						// ͨ��tag��ֵ
						ContentResolver contentResolver = this
								.getContentResolver();
						bm = BitmapFactory.decodeStream(contentResolver
								.openInputStream(uri));
						img.setImageBitmap(bm);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	public void onClick(View v) {
		nameStr = name.getText().toString().trim();
		addrStr = addr.getText().toString().trim();
		mobileStr = mobile.getText().toString().trim();
		homeStr = home.getText().toString().trim();
		officeStr = office.getText().toString().trim();
		emailStr = email.getText().toString().trim();

		if (v == btn_save) {
			//�ж���ϵ��������Ϣ
			if ((TextUtils.isEmpty(name.getText().toString().trim()) && TextUtils
					.isEmpty(mobile.getText().toString().trim()))
					|| (TextUtils.isEmpty(name.getText().toString().trim()) && TextUtils
							.isEmpty(home.getText().toString().trim()))
					|| (TextUtils.isEmpty(name.getText().toString().trim()) && TextUtils
							.isEmpty(office.getText().toString().trim()))) {
				Toast.makeText(AddActivity.this, "��������ϵ�������͵绰",
						Toast.LENGTH_SHORT).show();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						AddActivity.this);

				// ���öԻ�����⡢���ݡ���ť,set����ÿ�η���this����dialog����
				builder.setIcon(R.drawable.check);// ���ñ���ͼƬ
				builder.setTitle("�����ϵ�ˣ� ");
				builder.setMessage("ȷ�������?");
				builder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener()// �˴���listener������İ�ťlistener�����ڲ�ͬ��
						{
							public void onClick(DialogInterface dialog,
									int which) {
								ContactService service = new ContactService(
										AddActivity.this);
								if (name.getText().toString().trim() != null) {
									if (uri == null) {
										Contact contact = new Contact(null,
												null, nameStr, mobileStr,
												homeStr, officeStr, emailStr,
												addrStr);
										service.save(contact);
									} else {
										Contact contact = new Contact(null, uri
												.toString(), nameStr,
												mobileStr, homeStr, officeStr,
												emailStr, addrStr);
										service.save(contact);
									}
								}
								Intent intent = new Intent(AddActivity.this,
										MainActivity.class);
								startActivity(intent);
								AddActivity.this.finish();
							}
						});

				builder.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// ���ȡ�����˳�����
								dialog.cancel();

							}
						});

				AlertDialog dialog = builder.create();
				dialog.show();// �ǵü���show()����
			}

		} else if (v == img) {
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("ѡ����ϵ��ͼ����Դ:");
			builder.setItems(new String[] { "ͼ��", "���" },
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							switch (item) {
							case 0:
								getImageFromAlbum();
								break;
							case 1:
								getImageFromCamera();
								break;
							}
						}
					}).create().show();
		}

		if (v == btn_cancel) {
			Intent intent = new Intent(AddActivity.this, MainActivity.class);
			startActivity(intent);
			AddActivity.this.finish();

		}
		if (v == btn_back) {
			Intent intent = new Intent(AddActivity.this, MainActivity.class);
			startActivity(intent);
			AddActivity.this.finish();
		}

	}

}
