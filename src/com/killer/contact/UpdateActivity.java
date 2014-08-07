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
import android.util.Log;
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

public class UpdateActivity extends Activity implements OnClickListener {
	private int id;
	private EditText name, addr, mobile, home, office, email;
	private Button btn_update, btn_cancel, btn_back;
	private Bitmap bm;
	private ImageView img;
	private Uri uri = null;
	private ContactService service = new ContactService(UpdateActivity.this);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_update);
		ExitApplication.getInstance().addActivity(this);
		img = (ImageView) findViewById(R.id.update_img);
		name = (EditText) findViewById(R.id.update_name);
		name.getBackground().setAlpha(150);
		addr = (EditText) findViewById(R.id.update_addr);
		addr.getBackground().setAlpha(150);
		mobile = (EditText) findViewById(R.id.update_mobile);
		mobile.getBackground().setAlpha(150);
		home = (EditText) findViewById(R.id.update_home);
		home.getBackground().setAlpha(150);
		office = (EditText) findViewById(R.id.update_office);
		office.getBackground().setAlpha(150);
		email = (EditText) findViewById(R.id.update_email);
		email.getBackground().setAlpha(150);
		btn_update = (Button) findViewById(R.id.btn_update);
		btn_update.getBackground().setAlpha(150);
		btn_update.setOnClickListener(this);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.getBackground().setAlpha(150);
		btn_cancel.setOnClickListener(this);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		img.setOnClickListener(this);
		id = this.getIntent().getExtras().getInt("id");
		Log.i("++++++++++++++++id", id + "");
		Contact contact = service.findById(id);
		Log.i("name++++++++++++++++++++", contact.getName());
		Log.i("mobile++++++++++++++++++++", contact.getMobile());
		Log.i("home++++++++++++++++++++", contact.getHome());
		Log.i("office++++++++++++++++++++", contact.getOffice());
		Log.i("email++++++++++++++++++++", contact.getEmail());
		Log.i("addr++++++++++++++++++++", contact.getAddr());
		if (contact.getImg() != null) {
			try {
				uri = Uri.parse(contact.getImg());
				ContentResolver contentResolver = this.getContentResolver();
				bm = BitmapFactory.decodeStream(contentResolver
						.openInputStream(uri));
				img.setImageBitmap(bm);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			img.setImageResource(R.drawable.ic_launcher);
			uri = null;
		}
		name.setText(contact.getName());
		mobile.setText(contact.getMobile());
		home.setText(contact.getHome());
		office.setText(contact.getOffice());
		email.setText(contact.getEmail());
		addr.setText(contact.getAddr());
	}

	private void update() {
		Contact contact = new Contact();
		if (uri == null) {
			contact.setImg(null);
		} else {
			contact.setImg(uri.toString());
		}
		contact.setId(id);
		contact.setName(name.getText().toString());
		contact.setAddr(addr.getText().toString());
		contact.setMobile(mobile.getText().toString());
		contact.setHome(home.getText().toString());
		contact.setOffice(office.getText().toString());
		contact.setEmail(email.getText().toString());
		service.update(contact);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.update_user, menu);
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (data != null) {
				try {
					uri = data.getData();
					ContentResolver contentResolver = this.getContentResolver();
					InputStream is = contentResolver.openInputStream(uri);
					bm = BitmapFactory.decodeStream(is);
					img.setImageBitmap(bm);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
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
		if (v == btn_update) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					UpdateActivity.this);

			// ���öԻ�����⡢���ݡ���ť,set����ÿ�η���this����dialog����
			builder.setIcon(R.drawable.check);// ���ñ���ͼƬ
			builder.setTitle("�޸���ϵ�ˣ� ");
			builder.setMessage("ȷ���޸���?");
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener()// �˴���listener������İ�ťlistener�����ڲ�ͬ��
					{
						public void onClick(DialogInterface dialog, int which) {
							update();
							Intent intent = new Intent(UpdateActivity.this,
									MainActivity.class);
							startActivity(intent);
							UpdateActivity.this.finish();

						}
					});

			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// ���ȡ�����˳�����
							dialog.cancel();

						}
					});

			AlertDialog dialog = builder.create();
			dialog.show();// �ǵü���show()����

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
		} else if (v == btn_back) {
			Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
			startActivity(intent);
			UpdateActivity.this.finish();
		} else if (v == btn_cancel) {
			Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
			startActivity(intent);
			UpdateActivity.this.finish();
		}
	}

}
