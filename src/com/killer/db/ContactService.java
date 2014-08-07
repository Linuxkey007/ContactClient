package com.killer.db;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.killer.entity.Contact;

public class ContactService {
	private DBOpenHelper dbOpenHelper;

	public ContactService(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	/**
	 * 添加记录
	 * 
	 * @param person
	 */
	public void save(Contact contact) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"insert into Contact(img,name,mobile,home,office,email,addr) values(?,?,?,?,?,?,?)",
				new Object[] { contact.getImg(), contact.getName(),
						contact.getMobile(), contact.getHome(),
						contact.getOffice(), contact.getEmail(),
						contact.getAddr() });
		db.close();
	}

	/**
	 * 删除记录
	 * 
	 * @param id
	 */
	public void delete(Integer id) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from Contact where id=?", new Object[] { id });
		db.close();
	}

	/**
	 * 更新记录
	 * 
	 * @param person
	 */
	public void update(Contact contact) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"update Contact set img=?,name=?,mobile=?,home=?,office=?,email=?,addr=?  where id=?",
				new Object[] { contact.getImg(), contact.getName(),
						contact.getMobile(), contact.getHome(),
						contact.getOffice(), contact.getEmail(),
						contact.getAddr(),contact.getId()});
		db.close();
	}

	/**
	 * 通过Id获取整个对象
	 * 
	 * @param id
	 * @return
	 */
	public Contact findById(Integer id) {
		Contact contact = new Contact();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from Contact where id=?",
				new String[] { id + "" });
		while (cursor.moveToNext()) {
			contact.setId(cursor.getInt(cursor.getColumnIndex("id")));
			contact.setImg(cursor.getString(cursor.getColumnIndex("img")));
			contact.setName(cursor.getString(cursor.getColumnIndex("name")));
			contact.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
			contact.setHome(cursor.getString(cursor.getColumnIndex("home")));
			contact.setOffice(cursor.getString(cursor.getColumnIndex("office")));
			contact.setEmail(cursor.getString(cursor.getColumnIndex("email")));
			contact.setAddr(cursor.getString(cursor.getColumnIndex("addr")));
		}
		db.close();
		cursor.close();
		return contact;
	}

	/**
	 * 通过姓名关键字模糊查询
	 * 
	 * @param 参数
	 *            :"%"+userName+"%"
	 * @return
	 */

	 public List<Contact> findByName(String userName) {
	 List<Contact> contact = new ArrayList<Contact>();
	 SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
	 Cursor cursor = db.rawQuery(
	 "select * from Contact where name||mobile||home||office||email||addr like ?", new String[] { "%"
	 + userName + "%" });
	 while (cursor.moveToNext()) {
	 Integer id = cursor.getInt(cursor.getColumnIndex("id"));
	 String img=cursor.getString(cursor.getColumnIndex("img"));
	 String name = cursor.getString(cursor.getColumnIndex("name"));
	 String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
	 String home = cursor.getString(cursor.getColumnIndex("home"));
	 String office = cursor.getString(cursor.getColumnIndex("office"));
	 String email = cursor.getString(cursor.getColumnIndex("email"));
	 String addr = cursor.getString(cursor.getColumnIndex("addr"));
	 contact.add(new Contact(id, img, name, mobile, home, office,email, addr));
	 }
	
	 db.close();
	 cursor.close();
	 return contact;
	
	 }

	/**
	 * 查询所有记录
	 * 
	 * @return
	 */
	public List<Contact> findAll() {
		List<Contact> contact = new ArrayList<Contact>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from Contact", new String[] {});
		while (cursor.moveToNext()) {
			Integer id = cursor.getInt(cursor.getColumnIndex("id"));
			String img = cursor.getString(cursor.getColumnIndex("img"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
			String home = cursor.getString(cursor.getColumnIndex("home"));
			String office = cursor.getString(cursor.getColumnIndex("office"));
			String email = cursor.getString(cursor.getColumnIndex("email"));
			String addr = cursor.getString(cursor.getColumnIndex("addr"));
			contact.add(new Contact(id, img, name, mobile, home, office, email,
					addr));
		}

		db.close();
		cursor.close();
		return contact;
	}
}
