package com.easyshu.shuhelper.utils;

import android.util.Pair;

import com.easyshu.shuhelper.model.Course;

import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shiyan on 2016/11/21.
 */

public class FormatUtils {

    /*根据node的text提取学生姓名*/
    public static String formatName(String preString){

        String[] dest = preString.split("：");
        if (dest.length > 1){
            return dest[1];
        }
        return preString;
    }

    public static Course formatCourse(Element x){
        Pattern pattern = Pattern.compile("20(.+?)-20(.+?)学年(.+?)$");
        Matcher m = pattern.matcher(x.text());
        if (m.find()){
            return new Course(m.group(3),m.group(1)+m.group(2),
                    x.attr("value"));
        }else return new Course();

    }

    public static Pair<String,String> formatTotalScore(Element x){
        Pattern pattern = Pattern.compile("总计学分：(.+?)平均绩点：(.+?)$");
        Matcher m = pattern.matcher(x.text());
        if (m.find()){
            return new Pair<>(m.group(1),m.group(2));
        }else return null;
    }
}
