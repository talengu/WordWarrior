package com.talengu.wordwarrior;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.talengu.wordwarrior.sql.WordDAO;
import com.talengu.wordwarrior.wordfun.txtfun;

import java.io.IOException;

public class TXTActivity extends Activity {
    private Button mbutton;
    private EditText mEditText;

    private Context context = this;
    private  WordDAO wDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt);

        wDao = new WordDAO(context);

        mEditText=(EditText)findViewById(R.id.editText);
        mbutton=(Button)findViewById(R.id.button);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wDao.DeletetableTMP();
                Toast.makeText(getApplicationContext(), "删除tmp成功", Toast.LENGTH_SHORT).show();

                txtfun mtxtfun=null;
                try {
                    mtxtfun = new txtfun(mEditText.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < mtxtfun.list.size(); i++) {
                    // System.out.println(mtxtfun.list.get(i).getKey() + ": " + mtxtfun.list.get(i).getValue());
                    wDao.Insertword(mtxtfun.list.get(i).getKey());
                }

                Toast.makeText(getApplicationContext(), "保存成功，总计"+mtxtfun.list.size()+"个", Toast.LENGTH_SHORT).show();



            }
        });
    }

}
