package com.b4t.app.web.rest.errors;

import com.b4t.app.commons.Translator;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, Translator.toLocale("user.activation.aready"), "userManagement", "userexists");
    }
}
