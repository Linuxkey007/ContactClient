package com.killer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		super(context, "killer.db", null, 2);
	}

	public void onCreate(SQLiteDatabase db) {// 数据库第一次被创建的时候被调用的
		db.execSQL("CREATE TABLE Contact(id integer primary key autoincrement,img varchar(50),name varchar(20),mobile varchar(20),home varchar(20),office varchar(20),email varchar(40),addr varchar(20))");

	}

	// 当版本号发生改变创建
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("ALTER TABLE Users Add phone varchar(20) NULL");
	}

}
