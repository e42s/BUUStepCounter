package com.buu.buustepcounter.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	public static String db_name="userInfo.db";
	public static int version=1;
	public static String step_table="user_step";
	public DBHelper(Context context) {
		super(context, db_name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase sd) {
		String sql="create table "+step_table+"(_id integer primary key,sT text,eT text,step integer)";
		sd.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sd, int i, int i1) {
	}

}
