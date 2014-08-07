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

		// 设置按钮监听
		btn_add.setOnClickListener(itemsOnClick);
		btn_del.setOnClickListener(itemsOnClick);
		btn_perUpdate.setOnClickListener(itemsOnClick);
		btn_call.setOnClickListener(itemsOnClick);
		btn_sms.setOnClickListener(itemsOnClick);
		btn_exit.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AppBaseTheme);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
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
