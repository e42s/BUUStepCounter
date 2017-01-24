package com.buu.buustepcounter;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.buu.buustepcounter.setting.StepDetector;

public class StepCounterService extends Service {

	public static Boolean FLAG = false;// 服务运行标志

	private SensorManager mSensorManager;// 传感器服务
	private StepDetector detector;// 传感器监听对象


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


@Override
public int onStartCommand(Intent intent, int flags, int startId) {
	FLAG = true;// 标记为服务正在运行

	// 创建监听器类，实例化监听对象
	detector = new StepDetector(this);

	// 获取传感器的服务，初始化传感器
	mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
	// 注册传感器，注册监听器
	mSensorManager.registerListener(detector,
			mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
			SensorManager.SENSOR_DELAY_FASTEST);
	return Service.START_STICKY;
}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		FLAG = false;// 服务停止
		if (detector != null) {
			mSensorManager.unregisterListener(detector);
		}
	}

}
