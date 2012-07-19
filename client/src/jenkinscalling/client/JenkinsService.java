package jenkinscalling.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import jenkinscalling.client.comm.websockets.WebSocketClient;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class JenkinsService extends Service{
	private static final String LOG_TAG = "JenkinsService";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		Log.d(LOG_TAG, "OnCreate");
		webSocketsClient();		
	}
	
	@Override
	public void onDestroy(){
		Log.d(LOG_TAG, "OnDestroy");
	}
	
	private void startBuildFailActivity(){
		Intent dialogIntent = new Intent(getBaseContext(), BuildFailActivity.class);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplication().startActivity(dialogIntent);
	}
	
	private void startBuildSuccessActivity(){
		Intent dialogIntent = new Intent(getBaseContext(), BuildSuccessActivity.class);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplication().startActivity(dialogIntent);
	}
	
	private void webSocketsClient(){
		List<BasicNameValuePair> extraHeaders = Arrays.asList(
			    new BasicNameValuePair("Cookie", "session=abcd")
		);
		
		WebSocketClient client = new WebSocketClient(URI.create("ws://ci.edelight.net:8888/websocket"), new WebSocketClient.Handler() {
		    public void onConnect() {
		        Log.d(LOG_TAG, "Connected!");
		    }

		    public void onMessage(String message) {
		        Log.d(LOG_TAG, String.format("Got string message! %s", message));
		        if (message.equals("red")){
					startBuildFailActivity();
		        }else if(message.equals("green")){
		        	startBuildSuccessActivity();
		        }else{
		        	System.out.println("no color");
		        }
		    }

		    public void onMessage(byte[] data) {
		        Log.d(LOG_TAG, String.format("Got binary message! %s", data));
		    }

		    public void onDisconnect(int code, String reason) {
		        Log.d(LOG_TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
		    }

		    public void onError(Exception error) {
		        Log.e(LOG_TAG, "Error!", error);
		    }
		}, extraHeaders);

		client.connect();
	}
	
}
