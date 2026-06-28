package com.shop.utils;

public class Variables {

    private static ThreadLocal<String> registeredEmail = new ThreadLocal<>();
    private static ThreadLocal<String> registeredPassword = new ThreadLocal<>();

    public static void setRegisteredEmail(String email) {
        registeredEmail.set(email);
    }

    public static String getRegisteredEmail() {
        return registeredEmail.get();
    }

    public static void setRegisteredPassword(String password) {
        registeredPassword.set(password);
    }

    public static String getRegisteredPassword() {
        return registeredPassword.get();
    }

    public static void clear() {
        registeredEmail.remove();
        registeredPassword.remove();
    }
}
