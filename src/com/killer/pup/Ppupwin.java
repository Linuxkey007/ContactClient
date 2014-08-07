package com.killer.pup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.killer.contact.R;

public class Ppupwin extends PopupWindow {

	private Button btn_del, btn_add, btn_call, btn_sms, btn_perUpdate,
			btn_exit;
	private View mMenuView;

	public Ppupwin(Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.activity_popup, null);
		btn_del = (Button) mMenuView.findViewById(R.id.btn_del);
		btn_del.getBackground().setAlpha(150);
		btn_add = (Button) mMenuView.findViewById(R.id.btn_add);
		btn_add.getBackground().setAlpha(150);
		btn_call = (Button) mMenuView.findViewById(R.id.btn_call);
		btn_call.getBackground().setAlpha(150);
		btn_sms = (Button) mMenuView.findViewById(R.id.btn_sms);
		btn_sms.getBackground().setAlpha(150);
		btn_perUpdate = (Button) mMenuView.findViewById(R.id.btn_perUpdate);
		btn_perUpdate.getBackground().setAlpha(150);
		btn_exit = (Button) mMenuView.findViewById(R.id.exit);
		btn_exit.getBackground().setAlpha(150);

		// ���ð�ť����
		btn_add.setOnClickListener(itemsOnClick);
		btn_del.setOnClickListener(itemsOnClick);
		btn_perUpdate.setOnClickListener(itemsOnClick);
		btn_call.setOnClickListener(itemsOnClick);
		btn_sms.setOnClickListener(itemsOnClick);
		btn_exit.setOnClickListener(itemsOnClick);
		// ����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		// ����SelectPicPopupWindow��������Ŀ�
		this.setWidth(LayoutParams.MATCH_PARENT);
		// ����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// ����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		// ����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.AppBaseTheme);
		// ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// ����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
		// mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

}
