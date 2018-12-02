package com.talengu.wordwarrior;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.talengu.wordwarrior.control.ListviewItemAdapter;
import com.talengu.wordwarrior.model.Eword;
import com.talengu.wordwarrior.model.ListviewItem;
import com.talengu.wordwarrior.others.TextToSpeechActivity;
import com.talengu.wordwarrior.sql.SqlHelper;
import com.talengu.wordwarrior.sql.WordDAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    private static String TAG=MainActivity.class.getSimpleName();
    private WordDAO wDao;
    private Context context = this;
    private SwipeMenuListView mListView;
    private List<Eword> mEwordList;

    private ListviewItemAdapter adapter;
    private List<ListviewItem> mListviewItemList;
    private CheckBox mCheckBox;


    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        init();
        wDao = new WordDAO(context);
        wDao.dataSyncMtoT();
        mEwordList = wDao.Queryword(0);
        // wDao.Insertword("nice");
        mListviewItemList = getData();

        mTextView = (TextView) findViewById(R.id.PtView);
        mTextView.setText(wDao.CountDo());

        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        adapter = new ListviewItemAdapter(this, mListviewItemList);
        mListView.setAdapter(adapter);

        //Here is Setting Spinner.
        Spinner s1 = (Spinner) findViewById(R.id.spinner1);
        String datain = getResources().getString(R.string.Dialog_input);
        String dataout = getResources().getString(R.string.Dialog_output);
        String txtin = getResources().getString(R.string.title_activity_txt);
        s1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, new String[]{"Word Warrior", datain, dataout, txtin,"TTS"}));

        s1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mCheckBox.setChecked(false);
                    mListviewItemList.clear();
                    mEwordList = wDao.Queryword(0);
                    mListviewItemList.addAll(getData());
                    adapter.notifyDataSetChanged();
                }
                if (position == 1)//"import"
                {
                    new AlertDialog.Builder(MainActivity.this).setTitle(R.string.Dialog_input)//设置对话框标题
                            .setMessage(R.string.Dialog_input_detail)//设置显示的内容
                            .setPositiveButton(R.string.Dialog_Button_OK, new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    copySDcradToDB();
                                    finish();
                                }
                            }).setNegativeButton(R.string.Dialog_Button_Cancel, new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }
                if (position == 2)//output
                {
                    new AlertDialog.Builder(MainActivity.this).setTitle(R.string.Dialog_output)//设置对话框标题
                            .setMessage(R.string.Dialog_output_detail)//设置显示的内容
                            .setPositiveButton(R.string.Dialog_Button_OK, new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    progressDialog(context);

                                }
                            }).setNegativeButton(R.string.Dialog_Button_Cancel, new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();

                }

                if (position == 3) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, TXTActivity.class);    //参数一为当前Package的context，t当前Activity的context就是this，其他Package可能用到createPackageContex()参数二为你要打开的Activity的类名
                    startActivity(intent);
                    finish();

                }
                if (position == 4){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, TextToSpeechActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Here is setting ChexBox
        mCheckBox = (CheckBox) findViewById(R.id.checkBox1);
        mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked == true) {
                    // Toast.makeText(getApplicationContext(), "All Words", Toast.LENGTH_SHORT).show();
                    mEwordList = wDao.Queryword(-1);

                    mListviewItemList.clear();
                    mListviewItemList.addAll(getData());
                    for (int i = 0; i < mListviewItemList.size(); i++) {
                        if (1 == mEwordList.get(i).getIsOK())
                            mListviewItemList.get(i).isdeletedo();
                    }
                    adapter.notifyDataSetChanged();
                }
                if (isChecked == false) {
                    mEwordList = wDao.Queryword(0);
                    mListviewItemList.clear();
                    mListviewItemList.addAll(getData());
                    adapter.notifyDataSetChanged();
                    // Toast.makeText(getApplicationContext(), "New Words", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ///////////////////
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                switch (menu.getViewType()) {
                    case 0:
                        createMenu1(menu);
                        break;
                    case 1:
                        createMenu2(menu);
                        break;
                }
            }

            private void createMenu1(SwipeMenu menu) {
                // create "Show" item
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                openItem.setWidth(dp2px(90));
                openItem.setTitle("Show");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }

            private void createMenu2(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                openItem.setWidth(dp2px(90));
                openItem.setTitle("Show");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

                // create "back" item
                SwipeMenuItem backItem = new SwipeMenuItem(getApplicationContext());
                backItem.setBackground(new ColorDrawable(Color.rgb(0x18, 0xB4, 0xED)));
                backItem.setWidth(dp2px(90));
                backItem.setIcon(R.drawable.ic_back);
                menu.addMenuItem(backItem);

            }
        };
        // set creator

        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                ListviewItem item = mListviewItemList.get(position);

                switch (index) {
                    case 0:
                        // open
                        Log.i("swipe", "clicked item 1");
                        //  String wordmeaningString = mEwordList.get(position).getWordMeaning();
                        String wordString = mEwordList.get(position).getWordSpell();
//                        if (wordmeaningString.length() > 20) {
//                            wordmeaningString = wordmeaningString.substring(0, 20) + "...";
//                        }

                        // item.fileName = (0 == item.change) ? wordmeaningString : wordString;
                        item.changedo();
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:

                        Log.i("swipe", "clicked item 2");
                        // delete
                        if (menu.getViewType() == 0) {
                            wDao.UpdateWordisOK(item.fileName, 1);
                            // Toast.makeText(getApplicationContext(), item.fileName + " deleted", Toast.LENGTH_SHORT).show();
                            item.isdeletedo();
                        }
                        // mListviewItemList.remove(position);
                        // back
                        if (menu.getViewType() == 1) {
                            wDao.UpdateWordisOK(item.fileName, 0);
                            // Toast.makeText(getApplicationContext(), item.fileName + " backed", Toast.LENGTH_SHORT).show();
                            item.isback();
                        }
                        adapter.notifyDataSetChanged();

                        // TODO: setText
                        mTextView.setText(wDao.CountDo());
                        break;
                }
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {// swipe start
            }

            @Override
            public void onSwipeEnd(int position) {// swipe end
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 打开google translate
                String word_string="";
                for (int i = 0; i < mEwordList.size(); i++) {
                    if (i>=position-10 && i<=position+10)
                    word_string+=mEwordList.get(i).getWordSpell()+".\n";
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PROCESS_TEXT);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, true);
                    intent.putExtra(Intent.EXTRA_PROCESS_TEXT, word_string);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplication(), "Sorry, No Google Translation Installed",
                            Toast.LENGTH_SHORT).show();
                }



                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String word_string=mEwordList.get(position).getWordSpell();
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PROCESS_TEXT);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, true);
                    intent.putExtra(Intent.EXTRA_PROCESS_TEXT, word_string);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplication(), "Sorry, No Google Translation Installed",
                            Toast.LENGTH_SHORT).show();
                }


                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                // 将文本内容放到系统剪贴板里。
                cm.setText(mEwordList.get(position).getWordSpell());
                //Toast.makeText(getApplication(),"已复制",Toast.LENGTH_SHORT).show();


            }
        });
    }

    private List<ListviewItem> getData() {
        List<ListviewItem> data = new ArrayList<ListviewItem>();
        for (int i = 0; i < mEwordList.size(); i++) {
            data.add(new ListviewItem("" + mEwordList.get(i).getWordSpell()));
        }
        Log.d("swipe", "getData() OK");
        return data;
    }

    //init -- copy the database
    private void init() {
        File dir = new File(SqlHelper.DB_PATH);
        if (!dir.exists()) dir.mkdir();
        if (!(new File(SqlHelper.DB_NAME)).exists()) {
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(SqlHelper.DB_NAME);
                byte[] buffer = new byte[8192];
                int count = 0;
                InputStream is = getResources().openRawResource(R.raw.wordwarrior);
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //other calc functions
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, getResources().getDisplayMetrics());
    }

    private void copyDBToSDcrad() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        String DATABASE_NAME = "wordwarrior" + str + ".db";
        String oldPath = SqlHelper.DB_NAME;

        //String savepath=getExternalFilesDir(null) + File.separator+"wordwarrior";
        String newPath =  getExternalFilesDir(null)+File.separator+ DATABASE_NAME;

        Log.d(TAG, "copyDBToSDcrad: "+newPath);
        copyFile(oldPath, newPath);
    }

    private void copySDcradToDB() {
        String DATABASE_NAME = "new.db";

        String oldPath = Environment.getExternalStorageDirectory()+ File.separator+ DATABASE_NAME;
        Log.d(TAG, "copySDcradToDB: ");
        String newPath = SqlHelper.DB_NAME;
        copyFile(oldPath, newPath);


    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newfile = new File(newPath);
            if (!newfile.exists()) {
                newfile.createNewFile();
            }
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            // System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 进度条Dialog
     */
    private void progressDialog(final Context mContext) {
        final ProgressDialog mProgress;
        mProgress = new ProgressDialog(mContext);
        // mProgress.setIcon(R.drawable.ic_launcher);
        mProgress.setTitle(R.string.process_Dialog_output);
        // mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        mProgress.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                wDao.dataSyncTtoM();//先同步tmpword和myword的数据
                copyDBToSDcrad();//然后导出
                finish();
                //Toast.makeText(mContext, R.string.process_Dialog_output_success, Toast.LENGTH_SHORT).show();
            }
        }).start();
    }
}
