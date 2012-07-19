package jenkinscalling.client;


import jenkinscalling.client.comm.push.rabbitmq.MessageConsumer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class MainActivity extends Activity {
	private TextView mOutput;
	private Button fakeButton;
	private CheckBox listeningCheckBox;
	public static final String PREFERENCES = "prefs";
	protected static final String LOG_TAG = "MainActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
        boolean listening = settings.getBoolean("listening", false);
        
        // Interface elements
        mOutput =  (TextView) findViewById(R.id.output);
        fakeButton = (Button) findViewById(R.id.fake);
        fakeButton.setOnClickListener(fakeListener);
        listeningCheckBox = (CheckBox) findViewById(R.id.listener);
        listeningCheckBox.setOnClickListener(listeningListener);
        listeningCheckBox.setChecked(listening);
        if(listeningCheckBox.isChecked()){
    		startService(new Intent(getApplicationContext(), JenkinsService.class));
        }        

    }

    @Override
	protected void onResume() {
		super.onPause();
//		mConsumer.connectToRabbitMQ();
	}

	@Override
	protected void onPause() {
		super.onPause();
//		mConsumer.dispose();
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		SharedPreferences settings = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("listening", listeningCheckBox.isChecked());
		editor.commit();
	}
	
	private OnClickListener fakeListener = new OnClickListener(){
		public void onClick(View v) {
			startActivity(new Intent(getApplicationContext(), BuildFailActivity.class));			
		}
	};	
	
	private OnClickListener listeningListener = new OnClickListener(){		
		public void onClick(View v){
			Log.d(LOG_TAG, "ListeningListener");
			if(listeningCheckBox.isChecked()){
				startService(new Intent(getApplicationContext(), JenkinsService.class));
			}else{
				stopService(new Intent(getApplicationContext(), JenkinsService.class));
			}
		}
	};
	
}

