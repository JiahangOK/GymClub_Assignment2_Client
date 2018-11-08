package edu.bjtu.gymclub.gymclub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button signInBtn = (Button) findViewById(R.id.signInBtn);
        Button signUpBtn = (Button) findViewById(R.id.signUpBtn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userNameEdit = (EditText) findViewById(R.id.login_username);
                EditText userPasswordEdit = (EditText) findViewById(R.id.login_password);

                String username = userNameEdit.getText().toString();
                String password = userPasswordEdit.getText().toString();
                String url = "http://192.168.223.1:8080/user";/*在此处改变你的服务器地址*/
                getCheckFromServer(url, username, password);

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent();
                intent.setClass(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 将用户名和密码发送到服务器进行比对，若成功则跳转到app主界面，若错误则刷新UI提示错误登录信息
     *
     * @param url      服务器地址
     * @param username 用户名
     * @param password 密码
     */
    private void getCheckFromServer(String url, final String username, String password) {

        OkHttpClient client = new OkHttpClient.Builder() .addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Connection", "close").build();
                return chain.proceed(request);
            }
        }).build();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", username);
        formBuilder.add("password", password);
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String imageStr = null;
                        if (res.equals("0")) {
                            Toast.makeText(MainActivity.this, "无此账号,请先注册", Toast.LENGTH_SHORT).show();
                        } else if (res.equals("1")) {
                            Toast.makeText(MainActivity.this, "密码不正确", Toast.LENGTH_SHORT).show();
                        } else//成功
                        {
                            JSONObject jsonObject = null;

                            try {
                                jsonObject = new JSONObject(res);

//                                JSONArray jsonArray=jsonObject.getJSONArray("image");//这里获取的是装载有所有pet对象的数组
//                                JSONObject jsonpet = jsonArray.getJSONObject(0);//获取这个数组中第一个pet对象
//
//                                String imageStr=jsonpet.getString("image");//获取pet对象的参数
//                                JSONObject jsonpet = jsonObject.getJSONObject();
                                imageStr = jsonObject.getString("image");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            byte[] bitmapArray;
                            bitmapArray = Base64.decode(imageStr, Base64.DEFAULT);
                            try {


                                for (int i = 0; i < bitmapArray.length; ++i) {
                                    if (bitmapArray[i] < 0) {//调整异常数据
                                        bitmapArray[i] += 256;
                                    }
                                }

                                //System.currentTimeMillis()
                                String imgFilePath = "./picture.jpg";//新生成的图片
                                OutputStream out = new FileOutputStream(imgFilePath);
                                out.write(bitmapArray);
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


//                            //将响应数据转化为输入流数据
//                            InputStream inputStream=resp.body().byteStream();
//                            //将输入流数据转化为Bitmap位图数据
//                            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                            File file=new File(getFilesDir().getPath().toString() + "picture.jpg");
//                            try {
//                                file.createNewFile();
//                                //创建文件输出流对象用来向文件中写入数据
//                                FileOutputStream out=new FileOutputStream(file);
//                                //将bitmap存储为jpg格式的图片
//                                bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
//                                //刷新文件流
//                                out.flush();
//                                out.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

                            Intent intent;
                            intent = new Intent();
                            intent.setClass(MainActivity.this, MainInterfaceActivity.class);
                            startActivity(intent);
                        }

                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawable_menu:
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


}
