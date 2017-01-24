package com.buu.buustepcounter;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.buu.buustepcounter.setting.StepDetector;

public class StepCounterService extends Service {

	public static Boolean FLAG = false;// �������б�־

	private SensorManager mSensorManager;// ����������
	private StepDetector detector;// ��������������


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


@Override
public int onStartCommand(Intent intent, int flags, int startId) {
	FLAG = true;// ���Ϊ������������

	// �����������࣬ʵ������������
	detector = new StepDetector(this);

	// ��ȡ�������ķ��񣬳�ʼ��������
	mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
	// ע�ᴫ������ע�������
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
		FLAG = false;// ����ֹͣ
		if (detector != null) {
			mSensorManager.unregisterListener(detector);
		}
	}

}
