package com.example.smartcity;

import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main extends Activity {
	EditText roadID,rate;
	TextView successMssg;
	String userID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		roadID = (EditText) findViewById(R.id.road);
		rate = (EditText) findViewById(R.id.rate);
		successMssg = (TextView) findViewById(R.id.successMssg);
		Button b = (Button) findViewById(R.id.submit);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new HandleRequest().execute();
				userID = getIntent().getStringExtra("USERID");
			}
		});
	}
	
	private class HandleRequest extends AsyncTask<Void, Void, JsonObject> {
		String road,score;
		@Override 
		protected void onPreExecute() {
			road = roadID.getText().toString();
			score = rate.getText().toString();
		}
		@Override
		protected JsonObject doInBackground(Void... params) {
			JsonObject j = Json.createObjectBuilder().add("RequestType","RatingRoad").add("RequestDetails", Json.createObjectBuilder().add("UserId", userID)
					.add("RoadId", road).add("Rating", score)).build();
			JsonObject reply = null;
			try {
				Socket s = new Socket("192.168.43.142",5555);
				JsonWriter writer = Json.createWriter(s.getOutputStream());
				writer.writeObject(j);
				JsonReader reader = Json.createReader(s.getInputStream());
				reply = reader.readObject();
				s.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return reply;
		}
		@Override 
		protected void onPostExecute(JsonObject reply) {
			if(reply != null)
				successMssg.setText(reply.getString("RequestReply"));
		}
	}
}