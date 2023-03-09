package com.b4t.app.commons;

import com.b4t.app.config.Constants;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * Chuyen tieng viet khong dau
     *
     * @param str
     * @return
     */
    public static String removeAccent(String str) {
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Map trang thai
     *
     * @return
     */
    public static Map<Integer, String> getMapStatus() {
        Map<Integer, String> mapStatus = new HashMap<>();
        mapStatus.put(Constants.STATUS_DISABLED.intValue(), Translator.toLocale("common.status.0"));
        mapStatus.put(Constants.STATUS_ACTIVE.intValue(), Translator.toLocale("common.status.1"));
        return mapStatus;
    }

    public static String getSafePath(String path) {
     return path != null ? path.replaceAll("\\.+/+","").replaceAll("\\.+\\\\+",""): "";
    }

    public static String getSafeFileName(String fileName) {
//        if(DataUtil.isNullOrEmpty(fileName)) {
//            return "";
//        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fileName.length(); i++) {
            char c = fileName.charAt(i);
            if (c != '/' && c != '\\' && c != 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
