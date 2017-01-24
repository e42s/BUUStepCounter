package com.buu.buustepcounter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.buu.buustepcounter.dao.UserStepDao;
import com.buu.buustepcounter.setpCount.SetpTodayActivity;
import com.buu.buustepcounter.setting.SettingsActivity;
import com.buu.buustepcounter.setting.StepDetector;

@SuppressLint("HandlerLeak")
public class StepCounter extends Activity {

	// �����ı���ؼ�
	private TextView tv_show_step;// ����

	private Button btn_start;// ��ʼ��ť
	private Button btn_stop;// ֹͣ��ť

	private boolean isRun = false;

	private long timer = 0;// �˶�ʱ��
	private long startTimer = 0;// ��ʼʱ��

	private long tempTime = 0;

	int step_length = 0; // ����
	int weight = 0; // ����
	private static int total_step = 0; // �ߵ��ܲ���

	private Thread thread; // �����̶߳���

	private TextView step_counter;
	
	
	SharedPreferences sp;
	SharedPreferences.Editor editor;
	
	String tStart="0";
	String tEnd="0";
	
	private Button btShowScore;

	// ������һ���µ�Handlerʵ��ʱ, ����󶨵���ǰ�̺߳���Ϣ�Ķ�����,��ʼ�ַ�����
	// Handler����������, (1) : ��ʱִ��Message��Runnalbe ����
	// (2): ��һ������,�ڲ�ͬ���߳���ִ��.

	Handler handler = new Handler() {// Handler�������ڸ��µ�ǰ����,��ʱ������Ϣ�����÷�����ѯ����������ʾ��������������������
		// ��Ҫ�������̷߳��͵�����, ���ô�����������̸߳���UI
		// Handler���������߳���(UI�߳���), �������߳̿���ͨ��Message��������������,
		// Handler�ͳе��Ž������̴߳�������(���߳���sendMessage()��������Message����(�����������)
		// ����Щ��Ϣ�������̶߳����У�������߳̽��и���UI��

		@Override
		// ��������ǴӸ���/�ӿ� �̳й����ģ���Ҫ��дһ��
		public void handleMessage(Message msg) {
			super.handleMessage(msg); // �˴����Ը���UI
			countStep(); // ���ò�������
			Log.e("StepCounter","total_Steo:"+total_step);
			tv_show_step.setText(total_step + "");// ��ʾ��ǰ����
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_step_counter); // ���õ�ǰ��Ļ
		
		btShowScore=(Button) findViewById(R.id.btShowScore);
		btShowScore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(StepCounter.this,SetpTodayActivity.class);
				startActivity(intent);
				
			}
		});

		if (SettingsActivity.sharedPreferences == null) {
			SettingsActivity.sharedPreferences = this.getSharedPreferences(
					SettingsActivity.SETP_SHARED_PREFERENCES,
					Context.MODE_PRIVATE);
		}

		
		Intent intent=getIntent();
		isRun=intent.getBooleanExtra("run", false);
		// ��ȡ����ؼ�
		addView();
		// ��ʼ���ؼ�
		init();

		if (thread == null) {

			thread = new Thread() {// ���߳����ڼ�����ǰ�����ı仯

				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					int temp = 0;
					while (true) {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (StepCounterService.FLAG) {
							Message msg = new Message();
							if (temp != StepDetector.CURRENT_SETP) {
								temp = StepDetector.CURRENT_SETP;
							}
							if (startTimer != System.currentTimeMillis()) {
								timer = tempTime + System.currentTimeMillis()
										- startTimer;
							}
							handler.sendMessage(msg);// ֪ͨ���߳�
						}
					}
				}
			};
			thread.start();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.i("APP", "on resuame.");
	
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * ��ȡActivity��ؿؼ�
	 */
	private void addView() {
		tv_show_step = (TextView) this.findViewById(R.id.show_step);
		btn_start = (Button) this.findViewById(R.id.start);
		btn_stop = (Button) this.findViewById(R.id.stop);
		step_counter = (TextView) findViewById(R.id.step_counter);
		if (isRun) {

			step_counter.setText("����");
		}

		Intent service = new Intent(this, StepCounterService.class);
		stopService(service);
		
		
		
		tempTime = timer = 0;
		
		
		//*****�����ʷ�Ĳ���*******
		sp=StepCounter.this.getSharedPreferences("HISTORY_STEP", Context.MODE_PRIVATE);
		total_step= sp.getInt("CURRENT_STEP", 0);
//		Toast.makeText(getApplicationContext(), total_step+"", Toast.LENGTH_LONG).show();
		StepDetector.CURRENT_SETP = total_step;
		tv_show_step.setText(total_step+"");
		
		handler.removeCallbacks(thread);

	}

	/**
	 * ��ʼ������
	 */
	private void init() {

		step_length = SettingsActivity.sharedPreferences.getInt(
				SettingsActivity.STEP_LENGTH_VALUE, 70);
		weight = SettingsActivity.sharedPreferences.getInt(
				SettingsActivity.WEIGHT_VALUE, 50);

		countStep();

		tv_show_step.setText(total_step + "");

		btn_start.setEnabled(!StepCounterService.FLAG);
		btn_stop.setEnabled(StepCounterService.FLAG);

		if (StepCounterService.FLAG) {
			btn_stop.setText(getString(R.string.pause));
		} else if (StepDetector.CURRENT_SETP > 0) {
			btn_stop.setEnabled(true);
			btn_stop.setText(getString(R.string.cancel));
		}
	}

	public void onClick(View view) {
		Intent service = new Intent(this, StepCounterService.class);
		switch (view.getId()) {
		case R.id.start:
			startService(service);

			btn_start.setEnabled(false);
			btn_stop.setEnabled(true);
			btn_stop.setText(getString(R.string.pause));
			startTimer = System.currentTimeMillis();
			tempTime = timer;
			//��¼��ʼʱ��
			Time timeSt=new Time();
			timeSt.setToNow();
			tStart=timeSt.format("%Y-%m-%d %H:%M:%S");
			break;

		case R.id.stop:
			stopService(service);
			if (StepCounterService.FLAG && StepDetector.CURRENT_SETP > 0) {
				btn_stop.setText(getString(R.string.cancel));
				
			} else {
				if(btn_stop.getText().toString().equals(getString(R.string.cancel)))
				{
					//���浽sqllite
					StepDetector.CURRENT_SETP = 0;
				   
					Time timeEd=new Time();
					timeEd.setToNow();
					tEnd=timeEd.format("%Y-%m-%d %H:%M:%S");
					UserStepDao dao=new UserStepDao(getApplicationContext());
					dao.insertStep(tStart, tEnd, total_step);
					total_step=0;
					tv_show_step.setText("0");
//					System.out.println(currentTime+"ET");
				}
				
				tempTime = timer = 0;
				btn_stop.setText(getString(R.string.pause));
				btn_stop.setEnabled(false);

                System.out.println(sp.getInt("CURRENT_STEP", 2));
				
			    
				handler.removeCallbacks(thread);
				
			}
			btn_start.setEnabled(true);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_step, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;

		case R.id.ment_information:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ʵ�ʵĲ���
	 */
	private void countStep() {

		if (StepDetector.CURRENT_SETP % 2 == 0) {
			total_step = StepDetector.CURRENT_SETP;
		} else {
			total_step = StepDetector.CURRENT_SETP + 1;
		}

		total_step = StepDetector.CURRENT_SETP;
		Log.e("StepCounter","total_Steo:"+total_step);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
    		
        	
    			sp=StepCounter.this.getSharedPreferences("HISTORY_STEP", Context.MODE_PRIVATE);
    			editor=sp.edit();
    			editor.putInt("CURRENT_STEP",total_step);
                editor.commit();//�༭���ύ
    		

        }
        return super.onKeyDown(keyCode, event);

    }

}
