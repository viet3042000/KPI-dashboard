package com.b4t.app.service.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaveChartDTO extends ConfigChartDTO implements Serializable {
    private MultipartFile image;
    private ConfigChartDTO chartNextto;
    private List<SaveChartItemDTO> items = new ArrayList<>();

//    public SaveChartDTO() {
//        items = new ArrayList<>();
//    }

    public SaveChartDTO() {
    }

    public SaveChartDTO(ConfigChartDTO config) {
        this.setId(config.getId());
        this.setChartCode(config.getChartCode());
        this.setChartName(config.getChartName());
        this.setChartUrl(config.getChartUrl());
        this.setDescription(config.getDescription());
        this.setDomainCode(config.getDomainCode());
        this.setGroupChartId(config.getGroupChartId());
        this.setGroupKpiCode(config.getGroupKpiCode());
        this.setOrderIndex(config.getOrderIndex());
        this.setChartIdNextto(config.getChartIdNextto());
        this.setRelativeTime(config.getRelativeTime());
        this.setStatus(config.getStatus());
        this.setTimeTypeDefault(config.getTimeTypeDefault());
        this.setTitleChart(config.getTitleChart());
        this.setTypeChart(config.getTypeChart());
        this.setUpdateTime(config.getUpdateTime());
        this.setUpdateUser(config.getUpdateUser());
        this.setChartConfig(config.getChartConfig());
        this.setChildChart(config.getChildChart());
        this.setScreenDetailId(config.getScreenDetailId());
    }


    public ConfigChartDTO getChartNextto() {
        return chartNextto;
    }

    public void setChartNextto(ConfigChartDTO chartNextto) {
        this.chartNextto = chartNextto;
    }

    public List<SaveChartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<SaveChartItemDTO> items) {
        this.items = items;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
