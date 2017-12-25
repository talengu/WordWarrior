
package com.talengu.wordwarrior.wordfun;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Talen on 2017/1/1.
 * 这里是处理文字文档的方法
 */

public class txt2word {

    //输入数据buffer
    private BufferedReader br;

    //输出
    public static ArrayList<Map.Entry<String, Integer>> list;


    public txt2word(String txtstring) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(txtstring.getBytes());
        this.br = new BufferedReader(new InputStreamReader(is));
        this.txtsort(br);

    }

    public txt2word(FileReader mFileReader) throws IOException {
        // BufferedReader br = new BufferedReader(new FileReader("D:/test.txt"));
        this.br = new BufferedReader(mFileReader);
        this.txtsort(br);
    }

    private void txtsort(BufferedReader br) throws IOException {

        //存储过滤后单词的列表
        List<String> lists = new ArrayList<String>();

        String readLine = null;

        while ((readLine = br.readLine()) != null) {
            //过滤出只含有字母的
            String[] wordsArr1 = readLine.split("[^a-zA-Z]");
            //去除长度为0的行且长度大于26的单词
            for (String word : wordsArr1) {
                if (word.length() != 0 && word.length() < 27) {
                    lists.add(word);
                }
            }
        }
        br.close();
        // 存储单词计数信息，key值为单词，value为单词数
        Map<String, Integer> wordsCount = new TreeMap<String, Integer>();
        //单词的词频统计
        for (String li : lists) {
            if (wordsCount.get(li) != null) {
                wordsCount.put(li, wordsCount.get(li) + 1);
            } else {
                wordsCount.put(li, 1);
            }

        }

        SortMap(wordsCount);    //按值进行排序

    }

    //方法
    //按value的大小进行排序
    private void SortMap(Map<String, Integer> oldmap) {

        list = new ArrayList<Map.Entry<String, Integer>>(oldmap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();  //降序
            }
        });

        //测试输出
        // for (int i = 0; i < list.size(); i++) {
        //     System.out.println(list.get(i).getKey() + ": " + list.get(i).getValue());
        // }

    }

    public static void txtprint() {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getKey() + ": " + list.get(i).getValue());
        }
    }
    public static String list2string() {
        String mString="";
        for (int i = 0; i < list.size(); i++) {
            mString+=list.get(i).getKey() + ": " + list.get(i).getValue()+"\n";
        }
        return mString;
    }
}
