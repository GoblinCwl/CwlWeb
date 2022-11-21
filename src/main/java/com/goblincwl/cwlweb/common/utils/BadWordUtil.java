package com.goblincwl.cwlweb.common.utils;

import com.goblincwl.cwlweb.manager.entity.BadWords;
import com.goblincwl.cwlweb.manager.service.BadWordsService;
import com.goblincwl.cwlweb.manager.entity.KeyValueOptions;
import com.goblincwl.cwlweb.manager.service.KeyValueOptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * 敏感词过滤工具类
 *
 * @author yqwang75457
 */
@Component
@RequiredArgsConstructor
public class BadWordUtil {

    private final BadWordsService badWordsService;
    private final KeyValueOptionsService keyValueOptionsService;

    @PostConstruct
    public void init() {
        List<BadWords> badWordsList = this.badWordsService.list();
        //添加网站管理密码也作为违禁词，防止失误泄露
        KeyValueOptions loginPassword = keyValueOptionsService.getById("loginPassword");
        BadWords loginPasswordWord = new BadWords();
        loginPasswordWord.setWord(loginPassword.getOptValue());
        badWordsList.add(loginPasswordWord);
        BadWordUtil.words = new HashSet<>();
        badWordsList.forEach(badWords -> BadWordUtil.words.add(badWords.getWord()));
        addBadWordToHashMap(BadWordUtil.words);
    }


    //敏感词库文件路径
    public static Set<String> words;
    public static Map<String, String> wordMap;
    //最小匹配规则
    public static int minMatchTYpe = 1;
    //最大匹配规则
    public static int maxMatchType = 2;

    public static Set<String> readTxtByLine(String path) {
        Set<String> keyWordSet = new HashSet<String>();
        File file = new File(path);
        //文件流是否存在
        if (!file.exists()) {
            return keyWordSet;
        }
        BufferedReader reader = null;
        String temp = null;
        //int line=1;  
        try {
            reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8));
            while ((temp = reader.readLine()) != null) {
                keyWordSet.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return keyWordSet;
    }

    /**
     * 检查文字中是否包含敏感字符，检查规则如下：<br>
     *
     * @param txt
     * @param beginIndex
     * @param matchType
     * @return 如果存在，则返回敏感词字符的长度，不存在返回0
     * @version 1.0
     */
    @SuppressWarnings({"rawtypes"})
    public static int checkBadWord(String txt, int beginIndex, int matchType) {
        //敏感词结束标识位：用于敏感词只有1位的情况
        boolean flag = false;
        //匹配标识数默认为0
        int matchFlag = 0;
        char word = 0;
        Map nowMap = wordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            //获取指定key
            nowMap = (Map) nowMap.get(word);
            //存在，则判断是否为最后一个
            if (nowMap != null) {
                //找到相应key，匹配标识+1
                matchFlag++;
                //如果为最后一个匹配规则,结束循环，返回匹配标识数
                if ("1".equals(nowMap.get("isEnd"))) {
                    //结束标志位为true
                    flag = true;
                    //最小规则，直接返回,最大规则还需继续查找
                    if (minMatchTYpe == matchType) {
                        break;
                    }
                }
                //不存在，直接返回
            } else {
                break;
            }
        }
        /*“粉饰”匹配词库：“粉饰太平”竟然说是敏感词
         * “个人”匹配词库：“个人崇拜”竟然说是敏感词
         * if(matchFlag < 2 && !flag){     
            matchFlag = 0;
        }*/
        if (!flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }

    /**
     * 判断文字是否包含敏感字符
     *
     * @param txt       文字
     * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
     * @return 若包含返回true，否则返回false
     */
    public static boolean isContaintBadWord(String txt, int matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkBadWord(txt, i, matchType); //判断是否包含敏感字符
            if (matchFlag > 0) {    //大于0存在，返回true
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 替换敏感字字符
     *
     * @param txt
     * @param matchType
     * @param replaceChar 替换字符，默认*
     * @version 1.0
     */
    public static String replaceBadWord(String txt, int matchType, String replaceChar) {
        String resultTxt = txt;
        //获取所有的敏感词
        Set<String> set = getBadWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt       文字
     * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
     * @return
     * @version 1.0
     */
    public static Set<String> getBadWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<>();

        for (int i = 0; i < txt.length(); i++) {
            //判断是否包含敏感字符
            int length = checkBadWord(txt, i, matchType);
            //存在,加入list中
            if (length > 0) {
                sensitiveWordList.add(txt.substring(i, i + length));
                //减1的原因，是因为for会自增
                i = i + length - 1;
            }
        }

        return sensitiveWordList;
    }

    /**
     * 获取替换字符串
     *
     * @param replaceChar
     * @param length
     * @return
     * @version 1.0
     */
    private static String getReplaceChars(String replaceChar, int length) {
        String resultReplace = replaceChar;
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }

        return resultReplace;
    }

    /**
     * TODO 将我们的敏感词库构建成了一个类似与一颗一颗的树，这样我们判断一个词是否为敏感词时就大大减少了检索的匹配范围。
     *
     * @param keyWordSet 敏感词库
     * @author yqwang0907
     * @date 2018年2月28日下午5:28:08
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addBadWordToHashMap(Set<String> keyWordSet) {
        //初始化敏感词容器，减少扩容操作
        wordMap = new HashMap(keyWordSet.size());
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();    //关键字
            nowMap = wordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取

                if (wordMap != null) {        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
    }


//    public static void main(String[] args) {
//        Set<String> s = BadWordUtil.words;
//        Map<String, String> map = BadWordUtil.wordMap;
//
//        System.out.println("敏感词的数量：" + BadWordUtil.wordMap.size());
//        String string = "太多的伤感情怀也许只局限于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
//                + "然后法轮功 我们的扮演的角色就是跟随着主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
//                + "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 深人静的晚上，关上电话静静的发呆着。QQ123";
//        System.out.println("待检测语句字数：" + string.length());
//        long beginTime = System.currentTimeMillis();
//        Set<String> set = BadWordUtil.getBadWord(string, 2);
//        Boolean i = BadWordUtil.isContaintBadWord(string, 2);
//        Boolean i2 = BadWordUtil.isContaintBadWord("粉饰太平", 2);
//        Boolean i22 = BadWordUtil.isContaintBadWord("粉饰太平", 1);
//        Boolean i3 = BadWordUtil.isContaintBadWord("粉饰", 2);
//        Boolean i33 = BadWordUtil.isContaintBadWord("粉饰", 1);
//        Boolean i4 = BadWordUtil.isContaintBadWord("太平", 2);
//        Boolean i44 = BadWordUtil.isContaintBadWord("太平", 1);
//        Boolean i5 = BadWordUtil.isContaintBadWord("个人崇拜", 2);
//        Boolean i55 = BadWordUtil.isContaintBadWord("个人崇拜", 1);
//        Boolean i6 = BadWordUtil.isContaintBadWord("个人", 2);
//        Boolean i66 = BadWordUtil.isContaintBadWord("个人", 1);
//        Boolean i7 = BadWordUtil.isContaintBadWord("崇拜", 2);
//        Boolean i77 = BadWordUtil.isContaintBadWord("崇拜", 1);
//        long endTime = System.currentTimeMillis();
//        System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
//        System.out.println("总共消耗时间为：" + (endTime - beginTime));
//    }
}