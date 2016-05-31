package cn.ifavor.networkutil.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/3/1.
 */
public class URLUtil {

    /**
     * 检查 URL 是否合法
     * @param url
     * @return true 合法，false 非法
     */
    public static boolean isNetworkUrl(String url) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(url);
        return matcher.matches();
    }
}
