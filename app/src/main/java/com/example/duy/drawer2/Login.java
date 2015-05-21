package com.example.duy.drawer2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;


public class Login extends Activity{

    Button btlogin;
    TextView tvStatus;
    SimpleFacebook mSimpleFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSimpleFacebook = SimpleFacebook.getInstance(this);
        btlogin=(Button)findViewById(R.id.btLogin);
        tvStatus=(TextView)findViewById(R.id.tvStatus);

        setLogin();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setLogin() {
        // Login listener
        final Intent i=new Intent(this, MainActivity.class);
        final OnLoginListener onLoginListener = new OnLoginListener() {

            @Override
            public void onFail(String reason) {
                tvStatus.setText(reason);

            }

            @Override
            public void onException(Throwable throwable) {
                tvStatus.setText("Exception: " + throwable.getMessage());
            }

            @Override
            public void onThinking() {
                // show progress bar or something to the user while login is
                // happening
                tvStatus.setText("Thinking...");
            }


            @Override
            public void onLogin() {
                // change the state of the button or do whatever you want
                tvStatus.setText("Logged in");
                startActivity(i);
            }

            @Override
            public void onNotAcceptingPermissions(Permission.Type type) {
                tvStatus.setText("Logged out");

            }
        };


        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mSimpleFacebook.login(onLoginListener);
            }
        });
    }


}
