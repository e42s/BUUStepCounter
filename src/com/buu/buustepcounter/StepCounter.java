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

	// 定义文本框控件
	private TextView tv_show_step;// 步数

	private Button btn_start;// 开始按钮
	private Button btn_stop;// 停止按钮

	private boolean isRun = false;

	private long timer = 0;// 运动时间
	private long startTimer = 0;// 开始时间

	private long tempTime = 0;

	int step_length = 0; // 步长
	int weight = 0; // 体重
	private static int total_step = 0; // 走的总步数

	private Thread thread; // 定义线程对象

	private TextView step_counter;
	
	
	SharedPreferences sp;
	SharedPreferences.Editor editor;
	
	String tStart="0";
	String tEnd="0";
	
	private Button btShowScore;

	// 当创建一个新的Handler实例时, 它会绑定到当前线程和消息的队列中,开始分发数据
	// Handler有两个作用, (1) : 定时执行Message和Runnalbe 对象
	// (2): 让一个动作,在不同的线程中执行.

	Handler handler = new Handler() {// Handler对象用于更新当前步数,定时发送消息，调用方法查询数据用于显示？？？？？？？？？？
		// 主要接受子线程发送的数据, 并用此数据配合主线程更新UI
		// Handler运行在主线程中(UI线程中), 它与子线程可以通过Message对象来传递数据,
		// Handler就承担着接受子线程传过来的(子线程用sendMessage()方法传递Message对象，(里面包含数据)
		// 把这些消息放入主线程队列中，配合主线程进行更新UI。

		@Override
		// 这个方法是从父类/接口 继承过来的，需要重写一次
		public void handleMessage(Message msg) {
			super.handleMessage(msg); // 此处可以更新UI
			countStep(); // 调用步数方法
			Log.e("StepCounter","total_Steo:"+total_step);
			tv_show_step.setText(total_step + "");// 显示当前步数
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_step_counter); // 设置当前屏幕
		
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
		// 获取界面控件
		addView();
		// 初始化控件
		init();

		if (thread == null) {

			thread = new Thread() {// 子线程用于监听当前步数的变化

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
							handler.sendMessage(msg);// 通知主线程
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
	 * 获取Activity相关控件
	 */
	private void addView() {
		tv_show_step = (TextView) this.findViewById(R.id.show_step);
		btn_start = (Button) this.findViewById(R.id.start);
		btn_stop = (Button) this.findViewById(R.id.stop);
		step_counter = (TextView) findViewById(R.id.step_counter);
		if (isRun) {

			step_counter.setText("次数");
		}

		Intent service = new Intent(this, StepCounterService.class);
		stopService(service);
		
		
		
		tempTime = timer = 0;
		
		
		//*****获得历史的步数*******
		sp=StepCounter.this.getSharedPreferences("HISTORY_STEP", Context.MODE_PRIVATE);
		total_step= sp.getInt("CURRENT_STEP", 0);
//		Toast.makeText(getApplicationContext(), total_step+"", Toast.LENGTH_LONG).show();
		StepDetector.CURRENT_SETP = total_step;
		tv_show_step.setText(total_step+"");
		
		handler.removeCallbacks(thread);

	}

	/**
	 * 初始化界面
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
			//记录开始时间
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
					//保存到sqllite
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
	 * 实际的步数
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
                editor.commit();//编辑器提交
    		

        }
        return super.onKeyDown(keyCode, event);

    }

}
