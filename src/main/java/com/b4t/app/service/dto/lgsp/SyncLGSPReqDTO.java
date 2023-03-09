package com.b4t.app.service.dto.lgsp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SyncLGSPReqDTO implements Serializable {
    public SyncLGSPReqDTO() {
    }
    private String func;
    private String user;
    private String password;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("regime_code")
    private String programCode;

    @JsonProperty("org")
    private String orgCode;

    @JsonProperty("report_code")
    private String reportCode;

    @JsonProperty("ind_code")
    private String indCode;

    private String period;

    private String data;

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getIndCode() {
        return indCode;
    }

    public void setIndCode(String indCode) {
        this.indCode = indCode;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
