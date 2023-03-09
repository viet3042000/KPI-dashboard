package com.b4t.app.service.dto;

public class AreaAlarmDTO extends AreaGmapDataDTO {
    private Long alarmLevel;
    private String areaColor = "#e60932";
    private String borderColor = "#4a1822";

    public AreaAlarmDTO(AreaGmapDataDTO areaGmapDataDTO) {
        super(areaGmapDataDTO);
    }

    public AreaAlarmDTO() {
        super();
    }

    public Long getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(Long alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAreaColor() {
        return areaColor;
    }

    public void setAreaColor(String areaColor) {
        this.areaColor = areaColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }
}
