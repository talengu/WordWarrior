package com.talengu.wordwarrior;

import com.talengu.wordwarrior.wordfun.txt2word;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        //assertEquals(4, 2 + 2);
        //System.out.println(html2txt.getStringFromHtml("https://jsoup.org/"));
        txt2word m_txt2word=new txt2word(html2txt.getStringFromHtml("https://jsoup.org/"));
        m_txt2word.txtprint();

    }
}