package com.example.smartcity;

import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity{
	
	EditText inputFirstName;
    EditText inputLastName;
    EditText inputUsername;
    EditText inputEmail;
    EditText inputPassword;
    Button btnRegister;
    TextView registerErrorMsg;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		inputFirstName = (EditText) findViewById(R.id.fname);
        inputLastName = (EditText) findViewById(R.id.lname);
        inputUsername = (EditText) findViewById(R.id.uname);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pword);
        btnRegister = (Button) findViewById(R.id.register);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);
        
        Button login = (Button) findViewById(R.id.bktologin);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Login.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });
        
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if (  ( !inputUsername.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) && ( !inputFirstName.getText().toString().equals("")) && ( !inputLastName.getText().toString().equals("")) && ( !inputEmail.getText().toString().equals("")) )
                {
                    if ( inputUsername.getText().toString().length() > 4 ) {
                    new RegisterUser().execute();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Username should be minimum 5 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        
	}
	
	private class RegisterUser extends AsyncTask<Void,Void,JsonObject> {
		String email,password,firstName,lastName,userName;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			email = inputEmail.getText().toString();
			password = inputPassword.getText().toString();
			firstName = inputFirstName.getText().toString();
			lastName = inputLastName.getText().toString();
			userName = inputUsername.getText().toString();
		}
		@Override
		protected JsonObject doInBackground(Void... params) {
			JsonObject j = Json.createObjectBuilder().add("RequestType","NewUserAccount").add("RequestDetails", Json.createObjectBuilder().add("UserId", email).add("Password", password)).build();
			JsonObject reply = null;
			try {
				Socket s = new Socket("192.168.43.142",5555);
				JsonWriter writer = Json.createWriter(s.getOutputStream());
				writer.writeObject(j);
				JsonReader reader = Json.createReader(s.getInputStream());
				reply = reader.readObject();
				if(! reply.getString("RequestReply").equals("AccountCreated")) {
					s.close();
					return reply;
				}
				else {
					j = Json.createObjectBuilder().add("RequestType","UpdateUserInfo").add("RequestDetails", Json.createObjectBuilder().add("UserId", userName).add("Address", lastName)
							.add("PhoneNumber", "899").add("EmailId", email).add("Name", firstName)).build();
					reply = null;
					writer.writeObject(j);
					reply = reader.readObject();
				}
				s.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return reply;
		}
		@Override
		protected void onPostExecute(JsonObject reply) {
			if(reply != null)
				registerErrorMsg.setText(reply.get("RequestReply").toString());
		}
	}

}
