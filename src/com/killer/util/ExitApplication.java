package com.killer.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class ExitApplication extends Application {
	// ����������ɾ������add��remove��LinedList�Ƚ�ռ���ƣ���ΪArrayListʵ���˻��ڶ�̬��������ݽṹ��Ҫ�ƶ����ݡ�LinkedList������������ݽṹ,��������ɾ��
	private List<Activity> activityList = new LinkedList<Activity>();
	private static ExitApplication instance;

	private ExitApplication() {
	}

	// ����ģʽ�л�ȡΨһ��MyApplicationʵ��
	public static ExitApplication getInstance() {
		if (null == instance) {
			instance = new ExitApplication();
		}
		return instance;
	}

	// ���Activity��������
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}
