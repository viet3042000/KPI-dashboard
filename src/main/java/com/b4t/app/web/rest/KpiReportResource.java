package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.ExcelUtils;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.BaseRptGraph;
import com.b4t.app.domain.BaseRptGraphES;
import com.b4t.app.domain.ObjectRptGraphES;
import com.b4t.app.service.BaseRptGraphESService;
import com.b4t.app.service.KpiReportSearchESService;
import com.b4t.app.service.KpiReportSearchService;
import com.b4t.app.service.KpiReportService;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class KpiReportResource {
    private final Logger log = LoggerFactory.getLogger(KpiReportResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private KpiReportService kpiReportService;

    @Autowired
    BaseRptGraphESService baseRptGraphESService;

    @Autowired
    private KpiReportSearchService kpiReportSearchService;

    @Autowired
    KpiReportSearchESService kpiReportSearchESService;

    @Autowired
    ExcelUtils excelUtils;

    @PostMapping("/kpi-report/export-report-kpi")
    public ResponseEntity<Resource> exportReportKpi(@RequestBody ReportKpiDTO reportKpiDTO) throws Exception {
        List<BaseRptGraphESDTO> lstData = new ArrayList<>();
        ReportKpiDTO condition = kpiReportSearchService.buildCondition(reportKpiDTO.getConditionData());
        this.updateCodition(reportKpiDTO, condition);
        if (!DataUtil.isNullOrEmpty(reportKpiDTO.getKpiIds())) {
            BaseRptGraph baseRptGraph = kpiReportSearchESService.getLastData(reportKpiDTO);
            if (baseRptGraph != null) {
                this.updateForm(reportKpiDTO, baseRptGraph);
                kpiReportService.updateTimeOfForm(reportKpiDTO, baseRptGraph);
                lstData = kpiReportService.findRptGraphFull(reportKpiDTO, null);
            }
        }
        List<ExcelColumn> lstColumn = new ArrayList<>();
        lstColumn.add(new ExcelColumn("timeTypeName", Translator.toLocale("configReport.timeTypeName"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("dateTime", Translator.toLocale("configReport.dateTime"), ExcelColumn.ALIGN_MENT.CENTER));
        lstColumn.add(new ExcelColumn("kpiId", Translator.toLocale("configReport.kpiId"), ExcelColumn.ALIGN_MENT.RIGHT));
        lstColumn.add(new ExcelColumn("kpiName", Translator.toLocale("configReport.kpiName"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("inputLevelName", Translator.toLocale("configReport.inputLevelName"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("objNameFull", Translator.toLocale("configReport.objCode"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("val", Translator.toLocale("configReport.val"), ExcelColumn.ALIGN_MENT.RIGHT));
        lstColumn.add(new ExcelColumn("valAcc", Translator.toLocale("configReport.valAcc"), ExcelColumn.ALIGN_MENT.RIGHT));
        lstColumn.add(new ExcelColumn("valTotal", Translator.toLocale("configReport.valTotal"), ExcelColumn.ALIGN_MENT.RIGHT));
        lstColumn.add(new ExcelColumn("unitName", Translator.toLocale("configReport.unitName"), ExcelColumn.ALIGN_MENT.CENTER));
        lstColumn.add(new ExcelColumn("valPlanYear", Translator.toLocale("configReport.valPlanYear"), ExcelColumn.ALIGN_MENT.RIGHT));
        String title = Translator.toLocale("configReport.report.title");

        File fileOut = excelUtils.onExport(lstColumn, lstData, 3, 0, title, "kpi_report");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(fileOut));

        return ResponseEntity.ok()
            .contentLength(fileOut.length())
            .header("filename", fileOut.getName())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(resource);
    }


    @PostMapping("/kpi-report/get-report-kpi-chart")
    public ResponseEntity<BaseRptGraphChartDTO> getReportKpiChart(@RequestBody ReportKpiDTO reportKpiDTO, String chartType) throws Exception {
        ReportKpiDTO condition = kpiReportSearchService.buildCondition(reportKpiDTO.getConditionData());
        List<BaseRptGraphESDTO> lstData = new ArrayList<>();
        this.updateCodition(reportKpiDTO, condition);
        if (!DataUtil.isNullOrEmpty(reportKpiDTO.getKpiIds())) {
            BaseRptGraph baseRptGraph = kpiReportSearchESService.getLastData(reportKpiDTO);
            if (baseRptGraph != null) {
                this.updateForm(reportKpiDTO, baseRptGraph);
                kpiReportService.updateTimeOfForm(reportKpiDTO, baseRptGraph);
                lstData = kpiReportService.findRptGraphFull(reportKpiDTO, null);
            }
        }
        List<BaseRptSeries> series = new ArrayList<>();

        BaseRptGraphChartDTO baseRptGraphChartDTO = new BaseRptGraphChartDTO();
        baseRptGraphChartDTO.setChartType(reportKpiDTO.getOutputSearch());


        List<String> xAxis = kpiReportService.buildXAxis(reportKpiDTO);
        if (!DataUtil.isNullOrEmpty(lstData)) {
            if (Constants.OUTPUT_SEARCH.GROUP_BAR.equals(reportKpiDTO.getOutputSearch())) {
                String xAxisPie = lstData.stream().max(Comparator.comparing(BaseRptGraphESDTO::getPrdId)).map(BaseRptGraphESDTO::getDateTime).get(); //xAxis.get(xAxis.size() - 1);
                List<BaseRptGraphESDTO> lstDataGroupBar = lstData.stream().filter(e -> e.getDateTime().equals(xAxisPie)).collect(Collectors.toList());
                baseRptGraphChartDTO.setTitle(xAxisPie);
                //ObjectNam & listData of objectNam
                List<String> xAxisGroupBar = lstDataGroupBar.stream().map(BaseRptGraphESDTO::getObjNameFull).distinct().collect(Collectors.toList());

                Map<String, Map<String, BaseRptGraphESDTO>> mapData = lstDataGroupBar.stream().collect(Collectors.groupingBy(BaseRptGraphESDTO::groupKpiGroupBar, Collectors.toMap(BaseRptGraphESDTO::getObjNameFull, Function.identity(), (o1, o2) -> o1)));
                mapData.forEach((kpiName, mapValue) -> {
                    BaseRptSeries baseRptSeries = this.createSeriesGroupBar(chartType, kpiName, mapValue, xAxisGroupBar);
                    series.add(baseRptSeries);
                });

                baseRptGraphChartDTO.setxAxis(xAxisGroupBar);

            } else {

                Map<String, Map<String, BaseRptGraphESDTO>> mapData = lstData.stream().collect(Collectors.groupingBy(BaseRptGraphESDTO::groupKpiObj, Collectors.toMap(BaseRptGraphESDTO::getDateTime, Function.identity(), (o1, o2) -> o1)));
                if (Constants.OUTPUT_SEARCH.PIE.equals(reportKpiDTO.getOutputSearch())) {
                    BaseRptSeries baseRptSeries = new BaseRptSeries();
                    baseRptSeries.setType(chartType);
                    String xAxisPie = lstData.stream().max(Comparator.comparing(BaseRptGraphESDTO::getPrdId)).map(BaseRptGraphESDTO::getDateTime).get();
                    baseRptSeries.setName(xAxisPie);
                    List<Object> lstDataPie = new ArrayList<>();
                    BaseRptTooltip tooltip = new BaseRptTooltip();
                    mapData.forEach((objKpi, mapValue) -> {
                        Double value = 0.0d;
                        if (mapValue.containsKey(xAxisPie)) {
                            value = mapValue.get(xAxisPie).getValue();
                        }
                        String[] groupKey = objKpi.split("###");
                        lstDataPie.add(new BaseRptDataPie(groupKey[0] + " (" + groupKey[1] + ")", value));
                        tooltip.setValueSuffix(groupKey[2]);
                    });

                    baseRptSeries.setTooltip(tooltip);
                    baseRptSeries.setData(lstDataPie);
                    series.add(baseRptSeries);
                    baseRptGraphChartDTO.setTitle(xAxisPie);
                } else {
                    mapData.forEach((objKpi, mapValue) -> {
                        BaseRptSeries baseRptSeries = new BaseRptSeries();
                        baseRptSeries.setType(chartType);
                        baseRptSeries.setData(this.buildSeriesData(xAxis, mapValue));
                        String[] groupKey = objKpi.split("###");
                        baseRptSeries.setName(groupKey[0] + " (" + groupKey[1] + ")");
                        baseRptSeries.setTooltip(new BaseRptTooltip(groupKey[2]));
                        series.add(baseRptSeries);
                    });
                    baseRptGraphChartDTO.setxAxis(xAxis);
                }
            }
        }
        if (series.size() > 64) {
            baseRptGraphChartDTO.setSeries(series.subList(0, 64));
        } else {
            baseRptGraphChartDTO.setSeries(series);
        }
        return ResponseEntity.ok().body(baseRptGraphChartDTO);
    }

    private BaseRptSeries createSeriesGroupBar(String chartType, String kpiName, Map<String, BaseRptGraphESDTO> mapValue, List<String> xAxisGroupBar) {
        BaseRptTooltip tooltip = new BaseRptTooltip();
        String[] groupKey = kpiName.split("###");
        BaseRptSeries baseRptSeries = new BaseRptSeries();
        baseRptSeries.setType(chartType);
        baseRptSeries.setData(this.buildSeriesData(xAxisGroupBar, mapValue));
        baseRptSeries.setName(groupKey[0]);
        tooltip.setValueSuffix(groupKey[1]);
        baseRptSeries.setTooltip(tooltip);
        return baseRptSeries;
    }

    @PostMapping("/kpi-report/find-report-kpi")
    public ResponseEntity<List<BaseRptGraphESDTO>> findReportKpi(@RequestBody ReportKpiDTO reportKpiDTO, Pageable pageable) throws Exception {
        Page<BaseRptGraphESDTO> page = new PageImpl<>(new ArrayList<>(), pageable, 0);
        ReportKpiDTO condition = kpiReportSearchService.buildCondition(reportKpiDTO.getConditionData());
        this.updateCodition(reportKpiDTO, condition);
        if (!DataUtil.isNullOrEmpty(reportKpiDTO.getKpiIds())) {
            BaseRptGraph baseRptGraph = kpiReportSearchESService.getLastData(reportKpiDTO);
            if (baseRptGraph != null) {
                this.updateForm(reportKpiDTO, baseRptGraph);
                kpiReportService.updateTimeOfForm(reportKpiDTO, baseRptGraph);
                page = kpiReportService.findRptGraph(reportKpiDTO, pageable);
            }
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/kpi-report/update-time-report")
    public ResponseEntity<ReportKpiDTO> updateTimeReport(@RequestBody ReportKpiDTO reportKpiDTO) throws Exception {
        kpiReportService.updateTimeOfForm(reportKpiDTO, null);
        return ResponseEntity.ok().body(reportKpiDTO);
    }

    @PostMapping("/kpi-report/find-object-report")
    public ResponseEntity<List<TreeDTO>> findObjectReport(@RequestBody ReportKpiDTO reportKpiDTO) throws Exception {

        List<TreeDTO> lstTree = new ArrayList<>();
        ReportKpiDTO condition = kpiReportSearchService.buildCondition(reportKpiDTO.getConditionData());
        this.updateConditionWithoutValidate(reportKpiDTO, condition);
        BaseRptGraph baseRptGraph = kpiReportSearchESService.getLastData(reportKpiDTO);
        if (baseRptGraph != null) {
            this.updateForm(reportKpiDTO, baseRptGraph);
            kpiReportService.updateTimeOfForm(reportKpiDTO, baseRptGraph);
        }

        if (!DataUtil.isNullOrEmpty(reportKpiDTO.getKpiIds())
            && !DataUtil.isNullOrEmpty(reportKpiDTO.getTimeType())
            && !DataUtil.isNullOrEmpty(reportKpiDTO.getInputLevels())) {
            List<ObjectRptGraphES> lstData = kpiReportSearchESService.getAllObject(reportKpiDTO);
            lstTree = buildTreeFromGroupObj(lstData);
        }
        return ResponseEntity.ok().body(lstTree);
    }

    @GetMapping("/kpi-report/find-tree-kpi")
    public ResponseEntity<List<TreeDTO>> findTreeKpi() {
        List<CatKpiReportDTO> lstData = kpiReportService.findCatKpiReport();
        Map<String, List<CatKpiReportDTO>> mapGroupDomain = lstData.stream().filter(e -> e != null).collect(Collectors.groupingBy(CatKpiReportDTO::domainGroup, Collectors.toList()));
        List<TreeDTO> lstTree = buildTreeFromGroup(mapGroupDomain, true, true);
        System.out.println(lstTree.size());
        return ResponseEntity.ok().body(lstTree);
    }

    public List<TreeDTO> buildTreeFromGroupObj(List<ObjectRptGraphES> lstData) {
        Map<String, TreeDTO> hm = new HashMap<>();

        for (ObjectRptGraphES obj : lstData) {
            TreeDTO child;
            if (hm.containsKey(obj.getObjCodeFull())) {
                child = hm.get(obj.getObjCodeFull());
            } else {
                child = new TreeDTO();
                hm.put(obj.getObjCodeFull(), child);
            }
            child.setValue(obj.getObjCodeFull());
            child.setParent(obj.getParentCode());
            child.setText(obj.getObjName());
            if (!DataUtil.isNullOrEmpty(obj.getInputLevel())) {
                child.setPosition(obj.getInputLevel().intValue());
            }

            if (!DataUtil.isNullOrEmpty((obj.getParentCode()))) {
                TreeDTO parent;
                if (hm.containsKey(obj.getParentCode())) {
                    parent = hm.get(obj.getParentCode());
                } else {
                    parent = new TreeDTO();
                    hm.put(obj.getParentCode(), parent);
                }
                parent.setValue(obj.getParentCode());
                parent.setText(obj.getParentName());
                parent.setParent(null);
                parent.setPosition(1);

                parent.addChildItem(child);
            }
        }
        // Get List Root
        List<TreeDTO> lstRoot = new ArrayList<>();
        for (TreeDTO treeDTO : hm.values()) {
            if (treeDTO.getParent() == null) {
                lstRoot.add(treeDTO);
            }
        }
        this.sort(lstRoot);
        return lstRoot;
    }

    public void sort(List<TreeDTO> lstData) {
        lstData.sort((o1, o2) -> {
            if (DataUtil.isNullOrZero(o1.getPosition()) || DataUtil.isNullOrZero(o2.getPosition())) {
                return DataUtil.isNullOrZero(o1.getPosition()) ? 1 : -1;
            }
            if (!o1.getPosition().equals(o2.getPosition())) {
                return o2.getPosition().compareTo(o1.getPosition());
            } else {
                try {
                    return extractInt(o1.getText()) - extractInt(o2.getText());
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                    return o1.getText().compareTo(o2.getText());
                }
            }
        });
        for (TreeDTO treeDTO : lstData) {
            if (!DataUtil.isNullOrEmpty(treeDTO.getChildren())) {
                this.sort(treeDTO.getChildren());
            }
        }
    }

    int extractInt(String s) {
        String num = s.replaceAll("\\D", "");
        // return 0 if no digits found
        return num.isEmpty() ? 0 : Integer.parseInt(num);
    }

    private List<TreeDTO> buildTreeFromGroup(Map<String, List<CatKpiReportDTO>> mapGroup, boolean hasChild, boolean isRoot) {
        List<TreeDTO> lstTree = new ArrayList<>();
        mapGroup.forEach((key, values) -> {


            String[] groups = key.split(Constants.SEPARATE_CHARACTER);
            TreeDTO treeDTO = new TreeDTO();
            treeDTO.setChecked(false);
            treeDTO.setValue(new TreeValue(groups[0]));
            treeDTO.setText(groups[1]);
            Integer position;
            if (isRoot) {
                treeDTO.setCollapsed(true);
                position = values.get(0).getdPosition();
            } else {
                position = values.get(0).getcPosition();
            }
            treeDTO.setPosition(position);

            if (hasChild) {
                Map<String, List<CatKpiReportDTO>> mapGroupChild = values.stream().collect(Collectors.groupingBy(CatKpiReportDTO::parentGroup, Collectors.toList()));
                List<TreeDTO> lstChild = buildTreeFromGroup(mapGroupChild, false, false);
                treeDTO.setChildren(lstChild);
            } else {
                List<TreeDTO> lstChild = values.stream().map(e -> {
                    TreeDTO tree = new TreeDTO();
                    tree.setChecked(false);
                    tree.setText(e.getKpiId() + "_" + e.getKpiName());
                    tree.setValue(new TreeValue(e.getKpiId(), e.getTableName(), e.getUnitName()));
                    return tree;
                }).collect(Collectors.toList());
                lstChild.sort(Comparator.comparing(TreeDTO::getText));
                treeDTO.setChildren(lstChild);
            }
            lstTree.add(treeDTO);
        });
        lstTree.sort(Comparator.comparing(TreeDTO::getPosition));
        return lstTree;
    }

    private void updateCodition(ReportKpiDTO reportKpiDTO, ReportKpiDTO condition) throws Exception {
        this.updateConditionWithoutValidate(reportKpiDTO, condition);
        if (DataUtil.isNullOrEmpty(reportKpiDTO.getKpiIds()) && !condition.getHaveKpiTag()) {
            throw new BadRequestAlertException(Translator.toLocale("error.kpiReport.kpiNotNull"), "kpiReport", "kpiReport.kpiNotNull");
        }

    }

    private void updateConditionWithoutValidate(ReportKpiDTO reportKpiDTO, ReportKpiDTO condition) throws Exception {
        if (!DataUtil.isNullOrEmpty(condition.getTimeType())) {
            reportKpiDTO.setTimeType(condition.getTimeType());
        }
        if (!DataUtil.isNullOrEmpty(condition.getInputLevels())) {
            List<String> lstInputLevel = !DataUtil.isNullOrEmpty(reportKpiDTO.getInputLevels()) ? reportKpiDTO.getInputLevels() : new ArrayList<>();
            lstInputLevel.addAll(condition.getInputLevels());
            reportKpiDTO.setInputLevels(lstInputLevel);
        }
        if (!DataUtil.isNullOrEmpty(condition.getKpiIds())) {
            List<TreeValue> lstKpi = !DataUtil.isNullOrEmpty(reportKpiDTO.getKpiIds()) ? reportKpiDTO.getKpiIds() : new ArrayList<>();
            lstKpi.addAll(condition.getKpiIds());
            reportKpiDTO.setKpiIds(lstKpi);
        }
        if (!DataUtil.isNullOrEmpty(condition.getObjects())) {
            List<String> lstObj = !DataUtil.isNullOrEmpty(reportKpiDTO.getObjects()) ? reportKpiDTO.getObjects() : new ArrayList<>();
            lstObj.addAll(condition.getObjects());
            reportKpiDTO.setObjects(lstObj);
        }
    }

    private List<Object> buildSeriesData(List<String> xAxis, Map<String, BaseRptGraphESDTO> mapValue) {
        List<Object> lstData = xAxis.stream().map(e -> {
            if (mapValue.containsKey(e)) {
                return mapValue.get(e).getValue();
            }
            return 0.0;
        }).collect(Collectors.toList());
        return lstData;
    }

    private void updateForm(ReportKpiDTO reportKpiDTO, BaseRptGraph baseRptGraph) {
        if (DataUtil.isNullOrEmpty(reportKpiDTO.getTimeType()) && !DataUtil.isNullOrEmpty(baseRptGraph.getTimeType())) {
            reportKpiDTO.setTimeType(baseRptGraph.getTimeType().toString());
        }
//        if (DataUtil.isNullOrEmpty(reportKpiDTO.getInputLevels()) && !DataUtil.isNullOrEmpty(baseRptGraph.getInputLevel())) {
//            List<String> lstInputLevel = new ArrayList<>();
//            lstInputLevel.add(baseRptGraph.getInputLevel().toString());
//            reportKpiDTO.setInputLevels(lstInputLevel);
//        }
    }

    @PostMapping("/kpi-report/sync-to-elasticsearch")
    public ResponseEntity<Void> onSynToElasticsearch(@RequestBody BaseRptGraphESSearch baseDTO) throws Exception {
        List<BaseRptGraphES> lstData = kpiReportService.findBaseRptGraph(baseDTO);
        if (!DataUtil.isNullOrEmpty(lstData)) {
            baseRptGraphESService.pushDataToElasticsearch(lstData);
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, "base_rpt_graph", baseDTO.toString())).build();
    }

}
