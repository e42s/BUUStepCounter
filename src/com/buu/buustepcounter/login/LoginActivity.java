package com.buu.buustepcounter.login;

import com.buu.buustepcounter.R;
import com.buu.buustepcounter.StepCounter;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class LoginActivity extends Activity {

	private Button btnLogIn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		btnLogIn=(Button) findViewById(R.id.btnLogIn);
		btnLogIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this,StepCounter.class);
				intent.putExtra("run", false);
				startActivity(intent);
				finish();				
			}
		});
		
	}



}
