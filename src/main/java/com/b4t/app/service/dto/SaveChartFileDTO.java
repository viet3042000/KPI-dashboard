package com.b4t.app.service.dto;

import org.springframework.web.multipart.MultipartFile;

public class SaveChartFileDTO {
    private SaveChartDTO data;
    private MultipartFile file;

    public SaveChartDTO getData() {
        return data;
    }

    public void setData(SaveChartDTO data) {
        this.data = data;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
