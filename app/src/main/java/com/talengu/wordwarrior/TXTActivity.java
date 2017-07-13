package com.talengu.wordwarrior;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.talengu.wordwarrior.sql.WordDAO;
import com.talengu.wordwarrior.wordfun.html2txt;
import com.talengu.wordwarrior.wordfun.txt2word;

import java.io.IOException;

public class TXTActivity extends Activity {
    private static String TAG=TXTActivity.class.getSimpleName();
    private Button mbutton_add,mbutton_del_add,mbutton_html_add;
    private EditText mEditText;

    private Context context = this;
    private  WordDAO wDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt);

        wDao = new WordDAO(context);

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
                wDao.DeletetableTMP();
                Toast.makeText(getApplicationContext(), R.string.txt_html_add_toast, Toast.LENGTH_SHORT).show();

                txt2word mtxtfun=null;
                try {
                    //从html url 获取txt
                    String url=mEditText.getText().toString();
                    String txt= html2txt.getStringFromHtml(url);
                    mtxtfun = new txt2word(txt);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "html add onClick e: "+e.toString());
                }
                for (int i = 0; i < mtxtfun.list.size(); i++) {
                    // System.out.println(mtxtfun.list.get(i).getKey() + ": " + mtxtfun.list.get(i).getValue());
                    wDao.Insertword(mtxtfun.list.get(i).getKey());
                }

                Toast.makeText(getApplicationContext(), R.string.txt_add_toast1+mtxtfun.list.size()+R.string.txt_add_toast2, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
