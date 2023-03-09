package com.b4t.app.commons;

import com.b4t.app.commons.export.HTMLtoExcel;
import com.b4t.app.config.Constants;
import com.b4t.app.service.dto.ConfigReportDataImport;
import com.b4t.app.service.dto.ExcelColumn;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ExcelUtils {
    private final Logger log = LoggerFactory.getLogger(ExcelUtils.class);
    private final DecimalFormat doubleFormat = new DecimalFormat("#.##");
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    HTMLtoExcel htmlToExcel;
    private final Environment env;

    @Value("${server.pathOutput}")
    String pathOutput;

    public ExcelUtils(Environment env) {
        this.env = env;
    }

    public File writeImportFileResult(MultipartFile multipartFile, int startRow, int startCol, List<Map<String, String>> lstMapData, String tableName) throws Exception {
        String pathOut = getPathSaveFileExport(this.pathOutput, "result_" + tableName);

        InputStream inputStream = multipartFile.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        Row header = sheet.getRow(startRow);
        List<String> headers = getHeaders(header, startCol);
        int maxCol = headers.size() - 1;

        CellStyle cellStyleHeader = createStyleHeader(workbook);
        Row rowHeader = sheet.getRow(startRow);
        rowHeader.setHeight((short) 500);

        Cell cellHeader = rowHeader.createCell(maxCol + 1);
        cellHeader.setCellValue(Translator.toLocale("common.result"));
        cellHeader.setCellStyle(cellStyleHeader);


        Map<String, String> mapData;
        for (int i = 0; i < lstMapData.size(); i++) {
            mapData = lstMapData.get(i);
            Row row = sheet.getRow(startRow + 1 + i);
            row.createCell(maxCol + 1).setCellValue(mapData.get(Constants.RESULT_VALIDATE_MSG));
        }

        try (FileOutputStream fileOut = new FileOutputStream(pathOut)) {
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return new File(pathOut);

    }

    public ConfigReportDataImport readImportFile(MultipartFile multipartFile, int startRow, int startCol, Map<String, String> mapTitleColumn) throws Exception {
        InputStream inputStream = multipartFile.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        Row header = sheet.getRow(startRow);
        if (header == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.templateInvalid"), "param", "configReport.templateInvalid");
        }
        List<String> headers = getHeaders(header, startCol);
        int maxCol = headers.size() - 1;

        int maxRow = sheet.getLastRowNum();

        List<List<String>> lstData = new ArrayList<>();
        List<Map<String, String>> mapData = new ArrayList<>();

        for (int rowIdx = startRow + 1; rowIdx <= maxRow; rowIdx++) {
            Row row = sheet.getRow(rowIdx);
            List<String> lstRowData = new ArrayList<>();
            Map<String, String> data = new HashMap<>();

            for (int colIdx = startCol; colIdx <= maxCol; colIdx++) {
                String value = "";
                if (row.getCell(colIdx) != null) {
                    CellType type = row.getCell(colIdx).getCellType();

                    if (CellType.NUMERIC.equals(type)) {
                        value = String.valueOf(row.getCell(colIdx).getNumericCellValue());
                        if (value.contains(",")) {
                            value = value.replace(".", "").replace(",", ".");
                        }
                    } else if (CellType.STRING.equals(type)) {
                        value = row.getCell(colIdx).getStringCellValue().trim();
                        if (value.endsWith(".0")) {
                            value = value.substring(0, value.lastIndexOf("."));
                        } else if (value.endsWith(",0")) {
                            value = value.substring(0, value.lastIndexOf(","));
                        }
                    } else if (CellType.FORMULA.equals(type)) {
                        try {
                            value = String.valueOf(row.getCell(colIdx).getNumericCellValue());
                            if (value.contains(",")) {
                                value = value.replace(".", "").replace(",", ".");
                            }
                        } catch (Exception ex) {
                            log.error(ex.getMessage(), ex);
                            value = row.getCell(colIdx).getStringCellValue();
                        }
                    }
                }
                String title = headers.get(colIdx - startCol);

                if(mapTitleColumn.containsKey(title)) {
                    String column = mapTitleColumn.get(title);
                    data.put(column, value);

                }
                lstRowData.add(value);
            }
            mapData.add(data);
            lstData.add(lstRowData);
        }
        ConfigReportDataImport configReportDataImport = new ConfigReportDataImport();
        configReportDataImport.setHeaders(headers);
        configReportDataImport.setLstData(lstData);
        configReportDataImport.setMapData(mapData);
        return configReportDataImport;
    }

    private List<String> getHeaders(Row row, int startCol) {
        List<String> lstHeader = new ArrayList<>();
        for (int i = startCol; i < 100; i++) {
            if (row.getCell(i) != null && !DataUtil.isNullOrEmpty(row.getCell(i).getStringCellValue())) {
                lstHeader.add(row.getCell(i).getStringCellValue());
            } else {
                break;
            }
        }
        return lstHeader;
    }

    private CellStyle createCellStyleHeader(Workbook workbook) {
        CellStyle cellStyleHeader = workbook.createCellStyle();
        cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
        cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleHeader.setBorderLeft(BorderStyle.THIN);
        cellStyleHeader.setBorderBottom(BorderStyle.THIN);
        cellStyleHeader.setBorderRight(BorderStyle.THIN);
        cellStyleHeader.setBorderTop(BorderStyle.THIN);
        cellStyleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
        cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyleHeader.setWrapText(true);
        return cellStyleHeader;
    }

    private Font createFontHeader(Workbook workbook) {
        Font hSSFFontHeader = workbook.createFont();
        hSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFontHeader.setFontHeightInPoints((short) 10);
        hSSFFontHeader.setBold(true);
        return hSSFFontHeader;
    }

    private CellStyle createStyleHeader(Workbook workbook) {
        CellStyle cellStyleHeader = createCellStyleHeader(workbook);
        Font hSSFFontHeader = createFontHeader(workbook);
        hSSFFontHeader.setColor(IndexedColors.BLUE.index);
        cellStyleHeader.setFont(hSSFFontHeader);
        return cellStyleHeader;
    }

    public File downloadTemplate(List<ExcelColumn> lstColumn, String title, String fileName, int startRow, int startCol) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:templates/Template.xlsx");
        InputStream input = resource.getInputStream();

        Workbook workbook = new XSSFWorkbook(input);
        Sheet sheet = workbook.getSheetAt(0);

        String pathOut = getPathSaveFileExport(this.pathOutput, fileName);

        writeHeader(sheet, title, startRow, startCol, workbook, lstColumn);


        try (FileOutputStream fileOut = new FileOutputStream(pathOut)) {
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return new File(pathOut);
    }

    public File exportData(List<ExcelColumn> lstColumn, List<Object> lstData, String title, String fileName, int startRow, int startCol) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:templates/Template.xlsx");
        InputStream input = resource.getInputStream();

        Workbook workbook = new XSSFWorkbook(input);
        Sheet sheet = workbook.getSheetAt(0);

//        String folder = "../logs/export_out/";
        String pathOut = getPathSaveFileExport(this.pathOutput, fileName);

        writeHeader(sheet, title, startRow, startCol, workbook, lstColumn);

        //trai
        CellStyle cellStyleLeft = getCellStyle(workbook, HorizontalAlignment.LEFT);

        for (int i = 0; i < lstData.size(); i++) {
            Object[] rowData = (Object[]) lstData.get(i);
            Row row = sheet.createRow(i + startRow + 1);
            String text;
            for (int j = 0; j < rowData.length; j++) {
                Object value = rowData[j];
                Cell cell = row.createCell(j + startCol);
                if (value instanceof Double) {
                    text = doubleToString((Double) value);
                } else {
                    text = objectToString(value);
                }
                cell.setCellValue(text);
                cell.setCellStyle(cellStyleLeft);
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream(pathOut)) {
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return new File(pathOut);
    }

    private void writeHeader(Sheet sheet, String title, Integer startRow, Integer startCol,
                             Workbook workbook, List<ExcelColumn> lstColumn) {
        if (!DataUtil.isNullOrEmpty(title)) {
            int rowTitle = startRow > 2 ? startRow - 2 : 0;
            Row rowMainTitle = sheet.createRow(rowTitle);
            Cell mainCellTitle = rowMainTitle.createCell(startCol + 2);
            mainCellTitle.setCellValue(title.toUpperCase());
            CellStyle cellStyleTitle = getCellStyleTitle(workbook);
            mainCellTitle.setCellStyle(cellStyleTitle);
        }

        //Header
        Row rowHeader = sheet.createRow(startRow);
        rowHeader.setHeight((short) 500);

        CellStyle cellStyleHeader = createStyleHeader(workbook);

        for (int i = 0; i < lstColumn.size(); i++) {
            Cell cellHeader = rowHeader.createCell(i + startCol);
            cellHeader.setCellValue(lstColumn.get(i).getTitle());
            cellHeader.setCellStyle(cellStyleHeader);
        }
    }

    /**
     * Export report
     *
     * @param lstColumn
     * @param lstData
     * @param startRow
     * @param startCol
     * @param title
     * @return
     * @throws Exception
     */
    public File onExport(List<ExcelColumn> lstColumn, List<?> lstData, int startRow, int startCol, String title, String fileName) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
//        String folder = env.getProperty("export-tmp.kpi-export");
        String pathOut = getPathSaveFileExport(this.pathOutput, fileName);


        Row rowHeader = createFileTitle(startRow, startCol, title, workbook, sheet, (short) 500);

        CellStyle cellStyleHeader = createStyleHeader(workbook);

        for (int i = -1; i < lstColumn.size(); i++) {
            Cell cellHeader = rowHeader.createCell(i + startCol + 1);
            if (i == -1) {
                cellHeader.setCellValue("STT");
            } else {
                cellHeader.setCellValue(lstColumn.get(i).getTitle());
            }
            cellHeader.setCellStyle(cellStyleHeader);
        }

        //trai
        createFileOutput(lstColumn, lstData, startRow, startCol, workbook, sheet, pathOut);
        return new File(pathOut);
    }

    private Row createFileTitle(int startRow, int startCol, String title, Workbook workbook, Sheet sheet, short rowHeight) {
        if (!DataUtil.isNullOrEmpty(title)) {
            int rowTitle = startRow > 2 ? startRow - 2 : 0;
            int startColTitle = startCol + 2;
            Row rowMainTitle = sheet.createRow(rowTitle);
            Cell mainCellTitle = rowMainTitle.createCell(startCol + 2);
            mainCellTitle.setCellValue(title.toUpperCase());
            CellStyle cellStyleTitle = getCellStyleTitle(workbook);
            mainCellTitle.setCellStyle(cellStyleTitle);
            sheet.addMergedRegion(new CellRangeAddress(rowTitle, rowTitle, startColTitle, startColTitle + 3));
        }

        //Header
        Row rowHeader = sheet.createRow(startRow);
        rowHeader.setHeight(rowHeight);
        return rowHeader;
    }

    public File onExportTemplate(List<ExcelColumn> lstColumn, List<?> lstData, int startRow, int startCol, String title, String fileName) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

//        String folder = env.getProperty("export-tmp.kpi-export");
        String pathOut = getPathSaveFileExport(this.pathOutput, fileName);

        Row rowHeader = createFileTitle(startRow, startCol, title, workbook, sheet, (short) 900);

        CellStyle cellStyleHeader = createCellStyleHeader(workbook);
        Font hSSFFontHeader = createFontHeader(workbook);
//        hSSFFontHeader.setColor(IndexedColors.BLUE.index);
        cellStyleHeader.setFont(hSSFFontHeader);
        RichTextString contentRich;
        for (int i = -1; i < lstColumn.size(); i++) {
            Cell cellHeader = rowHeader.createCell(i + startCol + 1);
            if (i == -1) {
                contentRich = htmlToExcel.fromHtmlToCellValue("<b>STT</b>", workbook);
                cellHeader.setCellValue(contentRich);
            } else {
                contentRich = htmlToExcel.fromHtmlToCellValue(lstColumn.get(i).getTitle(), workbook);
                cellHeader.setCellValue(contentRich);
            }
            cellHeader.setCellStyle(cellStyleHeader);
        }
        createFileOutput(lstColumn, lstData, startRow, startCol, workbook, sheet, pathOut);

        return new File(pathOut);
    }

    private void createFileOutput(List<ExcelColumn> lstColumn, List<?> lstData, int startRow, int startCol, Workbook workbook, Sheet sheet, String pathOut) throws IllegalAccessException {
        //trai
        CellStyle cellStyleLeft = getCellStyle(workbook, HorizontalAlignment.LEFT);
        //phai
        CellStyle cellStyleRight = getCellStyle(workbook, HorizontalAlignment.RIGHT);
        //giua
        CellStyle cellStyleCenter = getCellStyle(workbook, HorizontalAlignment.CENTER);

        buildCellAndColumn(lstColumn, lstData, startRow, startCol, sheet, cellStyleLeft, cellStyleRight, cellStyleCenter);

        try (FileOutputStream fileOut = new FileOutputStream(pathOut)) {
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private void buildCellAndColumn(List<ExcelColumn> lstColumn, List<?> lstData, int startRow, int startCol, Sheet sheet, CellStyle cellStyleLeft, CellStyle cellStyleRight, CellStyle cellStyleCenter) throws IllegalAccessException {
        if (lstData != null && !lstData.isEmpty()) {
            Object firstRow = lstData.get(0);
            Map<String, Field> mapField = new HashMap<String, Field>();
            for (int j = 0; j < lstColumn.size(); j++) {

                String header = lstColumn.get(j).getColumn();
                Field[] fs = ReflectorUtil.getAllFields(firstRow.getClass());
                for (Field f : fs) {
                    f.setAccessible(true);
                    if (f.getName().equals(header)) {
                        mapField.put(header, f);
                    }
                }
            }

            for (int i = 0; i < lstData.size(); i++) {
                Row row = sheet.createRow(i + startRow + 1);
                for (int j = -1; j < lstColumn.size(); j++) {

                    Cell cell = row.createCell(j + startCol + 1);
                    if (j == -1) {
                        cell.setCellValue(i + 1);
                        cell.setCellStyle(cellStyleCenter);
                    } else {
                        ExcelColumn column = lstColumn.get(j);
                        Object obj = lstData.get(i);
                        Field f = mapField.get(column.getColumn());
                        if (f != null) {
                            Object value = f.get(obj);
                            String text;
                            if (value instanceof Double) {
                                text = doubleToString((Double) value);
                            } else {
                                text = objectToString(value);
                            }

                            cell.setCellValue(text);
                            if (ExcelColumn.ALIGN_MENT.CENTER.equals(column.getAlign())) {
                                cell.setCellStyle(cellStyleCenter);
                            }
                            if (ExcelColumn.ALIGN_MENT.LEFT.equals(column.getAlign())) {
                                cell.setCellStyle(cellStyleLeft);
                            }
                            if (ExcelColumn.ALIGN_MENT.RIGHT.equals(column.getAlign())) {
                                cell.setCellStyle(cellStyleRight);
                            }
                        }

                    }

                }
            }
        }
    }

    private String objectToString(Object value) {
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

    private String doubleToString(Double value) {
        if (value == null) {
            return "";
        }
        String result = doubleFormat.format(value);
        if (result.endsWith(".00")) {
            result = result.split("\\.")[0];
        }
        return result;
    }

    private CellStyle getCellStyle(Workbook workbook, HorizontalAlignment horizontalAlignment) {
        CellStyle cellStyleCenter = workbook.createCellStyle();
        cellStyleCenter.setAlignment(horizontalAlignment);
        cellStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleCenter.setBorderLeft(BorderStyle.THIN);
        cellStyleCenter.setBorderBottom(BorderStyle.THIN);
        cellStyleCenter.setBorderRight(BorderStyle.THIN);
        cellStyleCenter.setBorderTop(BorderStyle.THIN);
        cellStyleCenter.setWrapText(false);
        cellStyleCenter.setDataFormat((short) BuiltinFormats.getBuiltinFormat("@"));
        return cellStyleCenter;
    }

    private CellStyle getCellStyleTitle(Workbook workbook) {
        CellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(HorizontalAlignment.LEFT);
        cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);

        Font hSSFFont = workbook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 20);
        hSSFFont.setBold(true);
        hSSFFont.setColor(IndexedColors.BLACK.index);
        cellStyleTitle.setFont(hSSFFont);
        return cellStyleTitle;
    }

    private String getPathSaveFileExport(String folder, String fileNameOut) {
        String pathOut = folder;
        File folderOut = new File(folder);
        if (!folderOut.exists()) {
            folderOut.mkdir();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM_ddHHmmss");
        String strCurTimeExp = dateFormat.format(new Date());
        pathOut = pathOut + fileNameOut + "_" + strCurTimeExp + ".xlsx";

        return pathOut;
    }


}
