package com.b4t.app.web.rest.errors;

import com.b4t.app.commons.Translator;

public class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, Translator.toLocale("email.activation.aready"), "userManagement", "emailexists");
    }
}
