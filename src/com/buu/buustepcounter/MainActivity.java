package com.buu.buustepcounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.buu.buustepcounter.login.LoginActivity;
import com.buu.buustepcounter.regist.RegistActivity;


public class MainActivity extends Activity {

	Button btnIn;
	RadioButton rbLogin;
	RadioButton rbRegist;
	RadioButton rbVisitor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rbLogin=(RadioButton) findViewById(R.id.rbLogin);
        rbRegist=(RadioButton) findViewById(R.id.rbRegist);
        rbVisitor=(RadioButton) findViewById(R.id.rbVisitor);
        btnIn=(Button) findViewById(R.id.btnIn);
        btnIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(rbLogin.isChecked()){
					Intent intent1=new Intent(MainActivity.this,LoginActivity.class);
					startActivity(intent1);
				}
				if(rbRegist.isChecked())
				{
					Intent intent2=new Intent(MainActivity.this,RegistActivity.class);
					startActivity(intent2);
				}
				if(rbVisitor.isChecked()){
					Intent intent3=new Intent(MainActivity.this,StepCounter.class);
					intent3.putExtra("run", false);
					startActivity(intent3);
				}
				
			}
		});
        
        
        
    }

	 

  public static void DisplayToast(Context context , String str , int time){
          if (time == 1){
                  //短时间显示Toast
                  Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
          }else if (time == 2){
                  //长时间显示Toast
                  Toast.makeText(context, str, Toast.LENGTH_LONG).show();
          }
  }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {

            finish();

        }
        return super.onKeyDown(keyCode, event);

    }

}
