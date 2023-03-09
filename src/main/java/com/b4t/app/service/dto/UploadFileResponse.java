package com.b4t.app.service.dto;

import java.util.List;

public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
    private boolean importSucces;
    private boolean validate;
    private String errorCode;
    private List<BieumauKehoachchitieuDTO> lstDuplicate;
    private List<BieumauKehoachchitieuDTOError> lstError;
    private String errorFile;

    public UploadFileResponse(String fileName, String fileType, long size) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
    }

    public UploadFileResponse() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isImportSucces() {
        return importSucces;
    }

    public void setImportSucces(boolean importSucces) {
        this.importSucces = importSucces;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<BieumauKehoachchitieuDTO> getLstDuplicate() {
        return lstDuplicate;
    }

    public void setLstDuplicate(List<BieumauKehoachchitieuDTO> lstDuplicate) {
        this.lstDuplicate = lstDuplicate;
    }

    public List<BieumauKehoachchitieuDTOError> getLstError() {
        return lstError;
    }

    public void setLstError(List<BieumauKehoachchitieuDTOError> lstError) {
        this.lstError = lstError;
    }

    public String getErrorFile() {
        return errorFile;
    }

    public void setErrorFile(String errorFile) {
        this.errorFile = errorFile;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }
}
