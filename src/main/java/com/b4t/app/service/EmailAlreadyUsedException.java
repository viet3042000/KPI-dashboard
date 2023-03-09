package com.b4t.app.service;

import com.b4t.app.commons.Translator;

public class EmailAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(Translator.toLocale("email.activation.aready"));
    }

}
