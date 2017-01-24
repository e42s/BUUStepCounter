package com.buu.buustepcounter.selfsetting;

import com.buu.buustepcounter.R;
import com.buu.buustepcounter.setpCount.SetpTodayActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class SelfSettingActivity extends Activity {

	Button sure;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_setting);
		sure=(Button) findViewById(R.id.btnSelfSettingSure);
		sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(SelfSettingActivity.this,SetpTodayActivity.class);
				startActivity(intent);
			}
		});
	}


}
