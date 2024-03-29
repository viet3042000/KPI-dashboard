package com.b4t.app.config;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * author: tamdx
 * Trim String cho @ModelAttribute
 */
@ControllerAdvice
public class ControllerConfig {

    @InitBinder
    public void initBinder ( WebDataBinder binder ) {
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(false);
        binder.registerCustomEditor(String.class, stringtrimmer);
    }
}
