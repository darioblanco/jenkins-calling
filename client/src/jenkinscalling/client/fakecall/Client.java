package jenkinscalling.client.fakecall;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import jenkinscalling.client.BuildFailActivity;
import jenkinscalling.client.BuildSuccessActivity;
import jenkinscalling.client.comm.websockets.WebSocketClient;

import org.apache.http.message.BasicNameValuePair;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Client {
	private static final String LOG_TAG = "Client";
	private Context c;
	private Application app;
	private WebSocketClient client;
			
	public Client(Context c, Application app){
		this.c = c;
	}
	
	public void connect(){
		List<BasicNameValuePair> extraHeaders = Arrays.asList(
			    new BasicNameValuePair("Cookie", "session=abcd")
		);
		
		client = new WebSocketClient(URI.create("ws://ci.edelight.net:8888/websocket"), new WebSocketClient.Handler() {
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
	
	public void disconnect(){
		try {
			client.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void startBuildFailActivity(){
		Intent dialogIntent = new Intent(c, BuildFailActivity.class);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(dialogIntent);
	}
	
	private void startBuildSuccessActivity(){
		Intent dialogIntent = new Intent(c, BuildSuccessActivity.class);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(dialogIntent);
	}
}
