package com.example.smartcity;

import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Login extends Activity {
	Button btnLogin;
    Button Btnregister;
    Button passreset;
    EditText inputEmail;
    EditText inputPassword;
    private TextView loginErrorMsg;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pword);
        Btnregister = (Button) findViewById(R.id.registerbtn);
        btnLogin = (Button) findViewById(R.id.login);
        passreset = (Button)findViewById(R.id.passres);
        loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);
        loginErrorMsg.setText("Hello");
        passreset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            Intent myIntent = new Intent(view.getContext(), PasswordReset.class);
            startActivityForResult(myIntent, 0);
            finish();
        }});
        
        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Register.class);
                startActivityForResult(myIntent, 0);
                finish();
        }});
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (  ( !inputEmail.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) ) {
                    new LoginUser().execute();
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field cant be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        
	}
	
	private class LoginUser extends AsyncTask<Void,Void,JsonObject> {
		String email,password;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			email = inputEmail.getText().toString();
			password = inputPassword.getText().toString();
		}
		@Override
		protected JsonObject doInBackground(Void... params) {
			JsonObject j = Json.createObjectBuilder().add("RequestType","Login").add("RequestDetails", Json.createObjectBuilder().add("UserId", email).add("Password", password)).build();
			JsonObject reply = null;
			try {
				System.out.println("1");
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
		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(JsonObject reply) {
			if(reply != null) {
				if(reply.get("RequestReply").equals("AccessGranted")) {
					Intent upanel = new Intent(getApplicationContext(), Main.class);
					upanel.putExtra("USERID", email);
					startActivity(upanel);
					finish();
				}
				else 	loginErrorMsg.setText(reply.get("RequestReply").toString());

			}
		}
	}

}
