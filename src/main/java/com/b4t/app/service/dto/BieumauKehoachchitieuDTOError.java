package com.b4t.app.service.dto;

import com.b4t.app.commons.DataUtil;

import java.util.ArrayList;
import java.util.List;

public class BieumauKehoachchitieuDTOError extends BieumauKehoachchitieuDTO {


    List<String> errorMessages = new ArrayList<>();
    private String showError = "";

    public BieumauKehoachchitieuDTOError(BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO) {
        super(bieumauKehoachchitieuDTO);
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
        for (String s : errorMessages) {
            showError += s + " \n ";
        }
        setShowError(showError);
    }

    public String getShowError() {
        return showError;
    }

    public void setShowError(String showError) {
        this.showError = showError;
    }
}
