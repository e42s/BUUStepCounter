package com.buu.buustepcounter.regist;

import com.buu.buustepcounter.R;
import com.buu.buustepcounter.selfsetting.SelfSettingActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class RegistActivity extends Activity {

	Button btnRgSure;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		btnRgSure=(Button) findViewById(R.id.btRgSure);
		btnRgSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegistActivity.this,SelfSettingActivity.class);
				startActivity(intent);
			}
		});
	}


}
