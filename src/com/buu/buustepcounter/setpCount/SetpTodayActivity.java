package com.buu.buustepcounter.setpCount;


import java.util.ArrayList;
import java.util.List;

import com.buu.buustepcounter.R;
import com.buu.buustepcounter.dao.UserStepDao;
import com.buu.buustepcounter.entity.UserStep;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.database.Cursor;

public class SetpTodayActivity extends Activity {

	UserStepDao dao;
	Button delete;
	List<UserStep> list;
	RecordAdapter adapter;
	List<UserStep> isCheckedArr;
	ListView lvRecord;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setp_today);
		delete=(Button) findViewById(R.id.btDelete);
		
		isCheckedArr=new ArrayList<UserStep>();
		
		list=new ArrayList<UserStep>();
		initItem();
		lvRecord=(ListView) findViewById(R.id.lvRecord);
		adapter=new RecordAdapter(list);
		lvRecord.setAdapter(adapter);
		delete.setOnClickListener(new OnClickListener() {
			int _id;
			@Override
			public void onClick(View v) {
				for (int i = 0; i < isCheckedArr.size(); i++) {
					_id=isCheckedArr.get(i).get_id();
					
					dao.delData(_id);
				
				}
				list.clear();
				initItem();
				adapter.notifyDataSetChanged();
			}
		});
//		adapter.notifyDataSetChanged();
		
	}
	
	class RecordAdapter extends BaseAdapter{
		List<UserStep> lists;

		public RecordAdapter(List<UserStep> list) {
			lists=list;
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int i) {
			return lists.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		
		@Override
		public View getView(final int i, View view, ViewGroup viewGroup) {
			ViewHolder holder;
			if(view==null){
				holder=new ViewHolder();
				view=View.inflate(getApplicationContext(), R.layout.record_item, null);
				holder.sT=(TextView) view.findViewById(R.id.tvST);
				holder.eT=(TextView) view.findViewById(R.id.tvET);
				holder.step=(TextView) view.findViewById(R.id.tvStep);
				holder.cbIsCh=(CheckBox) view.findViewById(R.id.cbIsCh);
				view.setTag(holder);
			}
			else{
				holder=(ViewHolder)view.getTag();
			}
		
			holder.sT.setText(lists.get(i).getsT().trim());
			holder.eT.setText(lists.get(i).geteT().trim());
			holder.step.setText(lists.get(i).getStep()+"");
			holder.cbIsCh.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						isCheckedArr.add(lists.get(i));
					}
					else{
						if(isCheckedArr.size()!=0){
						isCheckedArr.remove(lists.get(i));
						}
					}
				}
			});
			
			return view;
		}
		class ViewHolder{
			TextView sT;
			TextView eT;
			TextView step;
			CheckBox cbIsCh;
		}
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	public void initItem() {
		dao=new UserStepDao(getApplicationContext());
		Cursor cursor=dao.select();
		
		while(cursor.moveToNext()){
			int _id=cursor.getInt(cursor.getColumnIndex("_id"));
			String sT=cursor.getString(cursor.getColumnIndex("sT"));
			String eT=cursor.getString(cursor.getColumnIndex("eT"));
			int step=cursor.getInt(cursor.getColumnIndex("step"));
			UserStep userStep=new UserStep();
			userStep.set_id(_id);
			userStep.setsT(sT);
			userStep.seteT(eT);
			userStep.setStep(step);
			list.add(userStep);
		}
		cursor.close();
	}

}
