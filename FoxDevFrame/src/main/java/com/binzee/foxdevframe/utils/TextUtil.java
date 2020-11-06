package com.binzee.foxdevframe.utils;

import android.util.Patterns;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 文字工具类
 *
 * @author 狐彻
 * 2020/11/06 15:11
 */
public class TextUtil {

    @NonNull
    private final String text;  //目标文字

    public TextUtil(@NonNull String text) {
        this.text = text;
    }

    public static TextUtil get(String text) {
        return new TextUtil(text);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 业务方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 是否是整数
     *
     * @author 狐彻 2020/11/06 15:13
     */
    public boolean isInteger() {
        try {
            Long.parseLong(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 是否是数字
     *
     * @author 狐彻 2020/11/06 15:15
     */
    public boolean isNumber() {
        try {
            Double.parseDouble(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 是否是奇数
     *
     * @author 狐彻 2020/11/06 15:15
     */
    public boolean isObb() {
        if (!isInteger()) return false;
        return Long.parseLong(text) % 2 == 1;
    }

    /**
     * 是否包含中文
     *
     * @author 狐彻 2020/11/06 15:17
     */
    public boolean hasChinese() {
        if (text.isEmpty()) return false;
        for (char c : text.toCharArray()) {
            if (c >= 0x4E00 && c <= 0x9FA5)
                return true;
        }
        return false;
    }

    /**
     * 是合法网络Url
     *
     * @author binze 2020/7/3 15:43
     */
    public boolean isWebUrl() {
        return Patterns.WEB_URL.matcher(text).matches();
    }

    /**
     * 是否是中华人民共和国居民身份证号
     *
     * @author 狐彻 2020/11/06 15:19
     */
    public boolean isIDCardNum() {
        Pattern pattern = Pattern.compile("\\d{15}(\\d{2}[0-9xX])?");
        return pattern.matcher(text).matches();
    }

    /**
     * 获取身份证号工具类
     *
     * @return 身份证工具
     * @throws RuntimeException 当目标字符非合法身份证号，则抛出异常
     * @author 狐彻 2020/11/06 15:22
     */
    public IDCardUtil getIDCardUtil() {
        if (!isIDCardNum()) throw new RuntimeException("字符非合法身份证号");
        return new IDCardUtil();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 身份证工具
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 身份证工具
     *
     * @author 狐彻 2020/11/06 15:24
     */
    public class IDCardUtil {
        private final Map<String, String> cCityMap = new HashMap<>();    // 身份证的地理字典

        private IDCardUtil() {
            cCityMap.put("11", "北京");
            cCityMap.put("12", "天津");
            cCityMap.put("13", "河北");
            cCityMap.put("14", "山西");
            cCityMap.put("15", "内蒙古");
            cCityMap.put("21", "辽宁");
            cCityMap.put("22", "吉林");
            cCityMap.put("23", "黑龙江");
            cCityMap.put("31", "上海");
            cCityMap.put("32", "江苏");
            cCityMap.put("33", "浙江");
            cCityMap.put("34", "安徽");
            cCityMap.put("35", "福建");
            cCityMap.put("36", "江西");
            cCityMap.put("37", "山东");
            cCityMap.put("41", "河南");
            cCityMap.put("42", "湖北");
            cCityMap.put("43", "湖南");
            cCityMap.put("44", "广东");
            cCityMap.put("45", "广西");
            cCityMap.put("46", "海南");
            cCityMap.put("50", "重庆");
            cCityMap.put("51", "四川");
            cCityMap.put("52", "贵州");
            cCityMap.put("53", "云南");
            cCityMap.put("54", "西藏");
            cCityMap.put("61", "陕西");
            cCityMap.put("62", "甘肃");
            cCityMap.put("63", "青海");
            cCityMap.put("64", "宁夏");
            cCityMap.put("65", "新疆");
            cCityMap.put("71", "台湾");
            cCityMap.put("81", "香港");
            cCityMap.put("82", "澳门");
            cCityMap.put("91", "境外");
        }

        /**
         * 获取该身份证城市名称
         *
         * @author binze 2019/12/13 16:21
         */
        public String getCityName() {
            return cCityMap.get(text.substring(0, 2));
        }

        /**
         * 获取该身份证生日信息
         *
         * @author binze 2019/12/13 16:22
         */
        public Date getBirthDay() {
            String birthday = text.substring(6, 14);

            int year; // 从中间变量birthday来截取
            year = Integer.parseInt(birthday.substring(0, 4)); // 字符串转成整数
            System.out.println(year);

            int month;
            System.out.println(birthday.substring(4, 6));
            month = Integer.parseInt(birthday.substring(4, 6));
            System.out.println(month);

            int day;
            day = Integer.parseInt(birthday.substring(6));
            System.out.println(day);

            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.set(year, month, day);
            return calendar.getTime();
        }

        /**
         * 判断是否为男性
         *
         * @author binze 2019/12/13 16:29
         */
        public boolean isMale() {
            String sex = "";
            sex = text.substring(text.length() - 2, text.length() - 1);
            return Long.parseLong(sex) % 2 == 1;
        }
    }
}
