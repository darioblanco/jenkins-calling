package jenkinscalling.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class StartupReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent){
		SharedPreferences settings = context.getSharedPreferences(MainActivity.PREFERENCES, MainActivity.MODE_PRIVATE);
		if(settings.getBoolean("listening", false))
			context.startService(new Intent(context, JenkinsService.class));
		else
			context.stopService(new Intent(context, JenkinsService.class));
	}
}
