package com.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 通用工具类
 * 提供随机用户名、手机号等生成方法
 */
public class  GeneralUtils {

    private static final String MIXED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 生成随机注册用户名
     * 格式：test + MMdd + 3位混合字符
     * 示例：test0326A7f
     */
    public static String generateRandomUsername() {
        String datePart = new SimpleDateFormat("MMdd").format(new Date()); // 0326
        String suffix = generateRandomMixedString(3);
        return "test" + datePart + suffix;
    }

    /**
     * 生成随机手机号（以13开头，后接9位随机数字）
     */
    public static String generateRandomPhone() {
        StringBuilder sb = new StringBuilder("13");
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            sb.append(random.nextInt(10)); // 0-9
        }
        return sb.toString();
    }

    /**
     * 生成指定长度的随机字符串（字母 + 数字混合）
     */
    public static String generateRandomMixedString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(MIXED_CHARS.charAt(random.nextInt(MIXED_CHARS.length())));
        }
        return sb.toString();
    }
    /*
     * @ Description //随机 nickName（昵称）
     * @ Param[]
     * @ return java.lang.String
     **/
    public static String generateRandomNickName() {
        // 可自定义昵称前缀
        String[] prefixPool = {"小明", "测试", "User", "张三", "测测", "爱测", "Demo", "test"};
        String prefix = prefixPool[new Random().nextInt(prefixPool.length)];

        // 后缀为 3 位随机字符+数字混合
        String suffix = generateRandomMixedString(3);

        return prefix + suffix;
    }
    /**
     * 生成随机邮箱地址
     * 例：test0326A8b@qq.com
     */
    public static String generateRandomEmail() {
        // 用户名前缀：test + MMdd + 3位随机字符
        String prefix = generateRandomUsername();

        // 常见邮箱域名池
        String[] domainPool = {"@qq.com", "@163.com", "@gmail.com", "@outlook.com", "@mail.com"};
        String domain = domainPool[new Random().nextInt(domainPool.length)];

        return prefix + domain;
    }


    private static final String[] MODULES = {"pms", "order", "sms", "ums"};
    private static final Random random = new Random();

    public static String getRandomModule() {
        int index = random.nextInt(MODULES.length);
        return MODULES[index]; // 一定是非空字符串
    }


}
