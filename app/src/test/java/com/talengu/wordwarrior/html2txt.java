package com.talengu.wordwarrior;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by talen on 17-7-13.
 */

public class html2txt {
    //参考 https://segmentfault.com/q/1010000002448667
    //    http://www.open-open.com/jsoup/parsing-a-document.htm
    //添加 compile 'org.jsoup:jsoup:1.10.3'

    public static  String getStringFromHtml(String url) throws IOException {
        String html= Jsoup.connect(url).get().html();
        String clean=Jsoup.clean(html,"",new Whitelist());
        return clean;
    }
}
