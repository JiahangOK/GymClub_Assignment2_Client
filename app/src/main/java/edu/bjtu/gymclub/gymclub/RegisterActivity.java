package edu.bjtu.gymclub.gymclub;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Register);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        Button registOK = (Button) findViewById(R.id.submit_register);


        //点击注册按钮，做出相应事件，向服务器发送用户信息，进行验证
        registOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //电话号
                EditText userPhoneNumberEdit = (EditText) findViewById(R.id.register_phone_num);
                //电子邮箱
                EditText userEmailEdit = (EditText) findViewById(R.id.register_email);
                //用户名
                EditText userNameEdit = (EditText) findViewById(R.id.register_username);
                //密码
                EditText userPasswordEdit = (EditText) findViewById(R.id.register_password);
                //密码确认
                EditText userPasswordConfirmEdit = (EditText) findViewById(R.id.register_password_confirm);


                String userPhoneNumber = userPhoneNumberEdit.getText().toString();
                String userEmail = userEmailEdit.getText().toString();
                String userName = userNameEdit.getText().toString();
                String userPassword = userPasswordEdit.getText().toString();
                String userPasswordConfirm = userPasswordConfirmEdit.getText().toString();


                String url = "http://10.0.2.2:8080/register";
                registerNameWordToServer(url, userPhoneNumber, userEmail, userName, userPassword);


            }
        });


    }

    private void registerNameWordToServer(String url, String userPhoneNumber, String userEmail, String userName, String userPassword) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();

        formBuilder.add("user_phone_number", userPhoneNumber);
        formBuilder.add("user_email", userEmail);
        formBuilder.add("username", userName);
        formBuilder.add("password", userPassword);

        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this,"服务器错误",Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (res.equals("0")) {
                            Toast.makeText(RegisterActivity.this,"regist failed!",Toast.LENGTH_SHORT).show();

                            //注册失败

                        } else {
                            //注册成功
                            Intent intent;
                            intent = new Intent();
                            intent.setClass(RegisterActivity.this,MainActivity.class);
                            startActivity(intent);
                        }

                    }
                });
            }
        });

    }


}
