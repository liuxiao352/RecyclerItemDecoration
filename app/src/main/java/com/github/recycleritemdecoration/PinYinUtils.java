package com.github.recycleritemdecoration;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @data 2018-09-26
 * @desc
 */

public class PinYinUtils {

    public static String getFirstLetter(String str){
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        //得到第一个字符的大写
        String pinyinStr = getPinYin(str).toUpperCase();
        char firstChart = pinyinStr.charAt(0);
        //不是A-Z字母
        if (firstChart > 90 || firstChart < 65) {
            return "#";
        }else{//代表A-Z
            return String.valueOf(firstChart);
        }
    }

    /**
     * 得到一个字符串的拼音读音
     * @param cheineseStr
     * @return
     */
    public static String getPinYin(String cheineseStr){
        StringBuffer sb = new StringBuffer();
        //将汉字拆分成一个个chart
        char[] chars = cheineseStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            try {
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(chars[i], getDefaultFormat());
                if (pinyins == null) {
                    sb.append(chars[i]);
                }else {
                    sb.append(pinyins[0]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 设置默认的输出格式
     *
     * @return
     */
    public static HanyuPinyinOutputFormat getDefaultFormat() {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        // 去除声调
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // 小写
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 包含Unicode特殊字符
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        return outputFormat;
    }

}
