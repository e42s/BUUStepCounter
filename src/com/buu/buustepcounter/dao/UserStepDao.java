package com.buu.buustepcounter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserStepDao {
	Context mContext;
	private SQLiteDatabase db;
	private DBHelper dbHelper;
	public UserStepDao(Context context) {
		mContext = context;
		dbHelper=new DBHelper(context);
		db=dbHelper.getWritableDatabase();
	}
	public void insertStep(String sT,String eT,int step) {
		ContentValues values=new ContentValues();
		values.put("sT", sT);
		values.put("eT", eT);
		values.put("step", step);
		db.insert(DBHelper.step_table,null,values);
	}
    public Cursor select(){
        String[]columns={"_id","sT","eT","step"};
        //表名 查询的列（显示的列(字段)）查询条件 查询条件的参数 分组 having(分组条件) 排序
        Cursor cursor=db.query(DBHelper.step_table,columns,null,null,null,null,null);
        return cursor;
    }
    public void delData(int _id)
    {
    	db.delete(DBHelper.step_table, "_id="+_id,null);
    }
	
	

}
