package com.talengu.wordwarrior;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.talengu.wordwarrior.sql.WordDAO;
import com.talengu.wordwarrior.wordfun.html2txt;
import com.talengu.wordwarrior.wordfun.txt2word;

import java.io.IOException;

public class TXTActivity extends Activity {
    private static String TAG=TXTActivity.class.getSimpleName();
    private Button mbutton_add,mbutton_del_add,mbutton_html_add;
    private EditText mEditText;
    private TextView mTextView_test;

    private Handler uiHandler;

    private Context context = this;
    private  WordDAO wDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt);

        wDao = new WordDAO(context);
        mTextView_test=(TextView)findViewById(R.id.textView_test);

        //uiHandler在主线程中创建，所以自动绑定主线程
        uiHandler = new Handler();

        mEditText=(EditText)findViewById(R.id.editText);
        mbutton_add=(Button)findViewById(R.id.button_add);
        mbutton_del_add=(Button)findViewById(R.id.button_del_add);
        mbutton_html_add=(Button)findViewById(R.id.button_html_add);
        mbutton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt2word mtxtfun=null;
                try {
                    mtxtfun = new txt2word(mEditText.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < mtxtfun.list.size(); i++) {
                    // System.out.println(mtxtfun.list.get(i).getKey() + ": " + mtxtfun.list.get(i).getValue());
                    wDao.Insertword(mtxtfun.list.get(i).getKey());
                }

                               Toast.makeText(getApplicationContext(), R.string.txt_add_toast1+mtxtfun.list.size()+R.string.txt_add_toast2, Toast.LENGTH_SHORT).show();



            }
        });

        mbutton_del_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wDao.DeletetableTMP();
                Toast.makeText(getApplicationContext(), R.string.txt_del_add_toast, Toast.LENGTH_SHORT).show();

                txt2word mtxtfun=null;
                try {
                    mtxtfun = new txt2word(mEditText.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < mtxtfun.list.size(); i++) {
                    // System.out.println(mtxtfun.list.get(i).getKey() + ": " + mtxtfun.list.get(i).getValue());
                    wDao.Insertword(mtxtfun.list.get(i).getKey());
                }

                Toast.makeText(getApplicationContext(), R.string.txt_add_toast1+mtxtfun.list.size()+R.string.txt_add_toast2, Toast.LENGTH_SHORT).show();




            }
        });
        mbutton_html_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //wDao.DeletetableTMP();
                Toast.makeText(getApplicationContext(), R.string.txt_html_add_toast, Toast.LENGTH_SHORT).show();


//              mTextView_test.setText(url);
                DownloadThread downloadThread = new DownloadThread();
                downloadThread.start();

//                txt2word mtxtfun=null;
//                try {
//                    //从html url 获取txt
//                    String url=mEditText.getText().toString();
//                    String txt= html2txt.getStringFromHtml(url);
//                    mtxtfun = new txt2word(txt);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.d(TAG, "html add onClick e: "+e.toString());
//                }
//                for (int i = 0; i < mtxtfun.list.size(); i++) {
//                    // System.out.println(mtxtfun.list.get(i).getKey() + ": " + mtxtfun.list.get(i).getValue());
//                   // wDao.Insertword(mtxtfun.list.get(i).getKey());
//                }

               // Toast.makeText(getApplicationContext(), R.string.txt_add_toast1+mtxtfun.list.size()+R.string.txt_add_toast2, Toast.LENGTH_SHORT).show();
            }
        });
    }
    class DownloadThread extends Thread{
        @Override
        public void run() {
            try{
                //ref: http://blog.csdn.net/iispring/article/details/47115879
                Log.d(TAG, "run: DownloadThread id"+ Thread.currentThread().getId());


                String url=mEditText.getText().toString();
                Log.d(TAG, "onClick: httml url"+url);

                final String html_txt= html2txt.getStringFromHtml(url);
                final txt2word mtxtfun = new txt2word(html_txt);
                final String liststring=txt2word.list2string();


                Log.d(TAG, "run: "+"ok");

                //update UI
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        wDao.DeletetableTMP();
                        System.out.println("Runnable thread id " + Thread.currentThread().getId());
                        TXTActivity.this.mTextView_test.setText(liststring);
                        for (int i = 0; i < mtxtfun.list.size(); i++) {
                            // System.out.println(mtxtfun.list.get(i).getKey() + ": " + mtxtfun.list.get(i).getValue());
                            wDao.Insertword(mtxtfun.list.get(i).getKey());
                        }
                    }
                };
                uiHandler.post(runnable);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
