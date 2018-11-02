package ua.com.info.sqldb;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import ua.com.info.common.MainActivity;

import static ua.com.info.data.DataBase.db;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static String server;
    public static String database;
    public static String instance;

    Button button;
    ProgressBar progressBar;
    TextView msg;
    EditText login;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (!isTaskRoot()) {
//            finish();
//            return;
//        }
        setContentView(R.layout.activity_login);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        msg = (TextView) findViewById(R.id.message);
    }

    @Override
    public void onClick(View view) {
        //goToStartMenu();
        CheckLogin checkLogin = new CheckLogin();
        checkLogin.execute();
    }

    private void goToStartMenu() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        LoginActivity.this.startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }

    private void InitDb(String login, String password) {
        if (login.equals("")) {
            login = "test";
            password = "Lviv2017+";
        }
        //db = new SQLDataBase("5.58.75.23:17100", "IDS", "test", login, password); // "test", "Lviv2017+");
        db = new SQLDataBase(server,  instance, database, login, password); // "test", "Lviv2017+");
    }

    private class CheckLogin extends AsyncTask<Void, Void, String> {
        String error = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            login.setEnabled(false);
            password.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBar.setVisibility(View.GONE);
            if (isSuccess) {

                goToStartMenu();}
            else msg.setText(error);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                InitDb("", "");
                isSuccess = (((SQLDataBase) db).isConnected());
            } catch (Exception ex) {
                isSuccess = false;
                error = ex.getMessage();
            }
            return error;
        }
    }
    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onResume(){
        super.onResume();
    }

    private void savePreferences(){
    }


}
