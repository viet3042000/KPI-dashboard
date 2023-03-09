package com.b4t.app;

public class Test {
    public static void main(String[] args) {
        String value = "1,0";
        if(value.endsWith(".0")) {
            value = value.substring(0, value.lastIndexOf("."));
        } else if(value.endsWith(",0")) {
            value = value.substring(0, value.lastIndexOf(","));
        }
        System.out.println(value);
    }
}
