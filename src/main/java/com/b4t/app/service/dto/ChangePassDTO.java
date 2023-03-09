package com.b4t.app.service.dto;

public class ChangePassDTO {
    private String currentPassword;
    private String password;
    private String confirmPassword;
    private String recaptchaReactive;

    public String getRecaptchaReactive() {
        return recaptchaReactive;
    }

    public void setRecaptchaReactive(String recaptchaReactive) {
        this.recaptchaReactive = recaptchaReactive;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
