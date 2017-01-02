package com.talengu.wordwarrior.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.talengu.wordwarrior.model.Eword;

import java.util.ArrayList;
import java.util.List;

public class WordDAO {

    public Context context;
    private SqlHelper helper;

    public WordDAO(Context context) {
        helper = new SqlHelper();
        this.context = context;
    }

    /**
     * TODO Read words to List<Eword>
     */
    public List<Eword> Queryword(int isOK) {
        Cursor cursor = null;
        if (isOK == -1) {
            cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, null, null, null, null, null);
        } else {
            // cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, "topicid=" + topicid + " AND " + "isOK=" + isOK, null, null, null, null);
            cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, "isOK=" + isOK, null, null, null, null);
        }// query("table",new String[]{"sid"})
        List<Eword> list = new ArrayList<Eword>();
        if (cursor.moveToFirst()) {
            do {
                // here add Eword one by one
                Eword bl = new Eword();
                bl.setId(cursor.getInt(0));
                bl.setWordSpell(cursor.getString(2));
                //bl.setWordMeaning(cursor.getString(2));
                bl.setIsOK(cursor.getInt(3));
                list.add(bl);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;

    }

    /**
     * TODO Update Word's  isOK
     */
    public void UpdateWordisOK(String word, int isOK) {
        ContentValues cv = new ContentValues();

        cv.put("isOK", isOK);

        helper.Update(context, SqlHelper.WORDLIST_TABLE_TMPWORD, cv, " word_spell ='" + word + "'", null);
    }


    /**
     * TODO add word  have spelling and isOK is 0.
     */
    public void Insertword(String word_spell) {
        ContentValues cv = new ContentValues();
        cv.put("word_spell", word_spell);
        cv.put("isOK", 0);
        if (IsHaveWord(word_spell) == false)
            helper.Insert(context, SqlHelper.WORDLIST_TABLE_TMPWORD, cv);

    }

    /**
     * TODO delete
     */
    public void Deleteword(String word_spell) {
        if (IsHaveWord(word_spell) == true)
            helper.Delete(context, SqlHelper.WORDLIST_TABLE_TMPWORD,
                    "word_spell ='" + word_spell + "'", null);
    }
    /**
     * TODO delete table data
     */
    public void DeletetableTMP() {
        helper.Delete(context, SqlHelper.WORDLIST_TABLE_TMPWORD,
                null, null);
    }

    /**
     * TODO count all
     */
    public String CountDo() {
        Cursor cursor = null;
        cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, null, null, null, null, null);
        int all = cursor.getCount();

        cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, "isOK=" + 1, null, null, null, null);
        int isok = cursor.getCount();
        cursor.close();
        return isok + "/" + all;
        // return "11/12";
    }

    /**
     * TODO word only count one time
     */
    public boolean IsHaveWord(String word_spell) {
        Cursor cursor = null;
        cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null,
                "word_spell= '" + word_spell + "'", null, null, null, null);
        int ishave = cursor.getCount();
        cursor.close();
        return ishave == 1 ? true : false;
    }

    /**
     * TODO TmpWord and MyWord SyncMyWordtoTmpWord
     * MyWord isOK=1,则TmpWord isOK=1
     * 在程序开始前
     */
    public void dataSyncMtoT() {

        //读取TmpWord，遍历里面的单词，比较与MyWord的不同
        Cursor cursor = null;
        cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                String tmp_word_spell = cursor.getString(2);


                Cursor tmpc = null;
                //其中cursor.getString(2)为单词拼写也就是TmpWord的第三列单词拼写
                tmpc = helper.Query(context, SqlHelper.WORDLIST_TABLE_MYWORD, null,
                        "word_spell= '" + tmp_word_spell + "'", null, null, null, null);
                int ishave = tmpc.getCount();

                if (ishave == 0){
                    //向MyWord中插入数据
                    ContentValues cv = new ContentValues();
                    cv.put("word_spell", tmp_word_spell);
                    cv.put("isOK", 0);
                    helper.Insert(context, SqlHelper.WORDLIST_TABLE_MYWORD, cv);
                }
                if (ishave == 1) {
                    tmpc.moveToFirst();
                    //更新TmpWord中对应的单词的isOK为1，如果myword里isOK为1
                    if (tmpc.getInt(3) == 1) {
                        ContentValues cvupdate = new ContentValues();
                        cvupdate.put("isOK", 1);
                        helper.Update(context, SqlHelper.WORDLIST_TABLE_TMPWORD, cvupdate, " word_spell ='" + tmp_word_spell + "'", null);
                    }

                }


                tmpc.close();


            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    /**
     * TODO TmpWord and MyWord TmpWordtoSyncMyWord
     * TmpWord isOK=? MyWord isOK=?
     * <p>
     * 在程序选择同步时
     */
    public void dataSyncTtoM() {
        Cursor cursor = null;

        cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                String tmp_word_spell = cursor.getString(2);
                ContentValues cvupdate = new ContentValues();
                cvupdate.put("isOK", cursor.getInt(3));
                //更新myword上的is
                helper.Update(context, SqlHelper.WORDLIST_TABLE_MYWORD, cvupdate, " word_spell ='" + tmp_word_spell + "'", null);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }
}
