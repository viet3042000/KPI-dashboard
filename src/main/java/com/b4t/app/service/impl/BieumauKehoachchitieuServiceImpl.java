package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.repository.BieumauKehoachchitieuCustomRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.BieumauKehoachchitieuService;
import com.b4t.app.domain.BieumauKehoachchitieu;
import com.b4t.app.repository.BieumauKehoachchitieuRepository;
import com.b4t.app.service.CatGraphKpiService;
import com.b4t.app.service.CatItemService;
import com.b4t.app.service.FlagRunQueryKpiService;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.mapper.BieumauKehoachchitieuMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Service Implementation for managing {@link BieumauKehoachchitieu}.
 */
@Service
@Transactional
public class BieumauKehoachchitieuServiceImpl implements BieumauKehoachchitieuService {

    private final Logger log = LoggerFactory.getLogger(BieumauKehoachchitieuServiceImpl.class);

    private final BieumauKehoachchitieuRepository bieumauKehoachchitieuRepository;
    private final BieumauKehoachchitieuCustomRepository bieumauKehoachchitieuCustomRepository;
    private final CatGraphKpiService catGraphKpiService;
    private final BieumauKehoachchitieuMapper bieumauKehoachchitieuMapper;
    private final CatItemService catItemService;
    private final FlagRunQueryKpiService flagRunQueryKpiService;

    public BieumauKehoachchitieuServiceImpl(BieumauKehoachchitieuRepository bieumauKehoachchitieuRepository, BieumauKehoachchitieuCustomRepository bieumauKehoachchitieuCustomRepository, CatGraphKpiService catGraphKpiService, BieumauKehoachchitieuMapper bieumauKehoachchitieuMapper, CatItemService catItemService, FlagRunQueryKpiService flagRunQueryKpiService) {
        this.bieumauKehoachchitieuRepository = bieumauKehoachchitieuRepository;
        this.bieumauKehoachchitieuCustomRepository = bieumauKehoachchitieuCustomRepository;
        this.catGraphKpiService = catGraphKpiService;
        this.bieumauKehoachchitieuMapper = bieumauKehoachchitieuMapper;
        this.catItemService = catItemService;

        this.flagRunQueryKpiService = flagRunQueryKpiService;
    }

    /**
     * Save a bieumauKehoachchitieu.
     *
     * @param bieumauKehoachchitieuDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BieumauKehoachchitieuDTO save(BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO) {
        log.debug("Request to save BieumauKehoachchitieu : {}", bieumauKehoachchitieuDTO);
        BieumauKehoachchitieu bieumauKehoachchitieu = bieumauKehoachchitieuMapper.toEntity(bieumauKehoachchitieuDTO);
        bieumauKehoachchitieu.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        bieumauKehoachchitieu.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        bieumauKehoachchitieu = bieumauKehoachchitieuRepository.save(bieumauKehoachchitieu);
        createFlagRunData(bieumauKehoachchitieuDTO);
        return bieumauKehoachchitieuMapper.toDto(bieumauKehoachchitieu);
    }

    private void createFlagRunData(BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO) {
        FlagRunQueryKpiDTO flagRunQueryKpiDTO = new FlagRunQueryKpiDTO(Constants.DATA_TABLE.BIEUMAU_KEHOACHCHITIEU, bieumauKehoachchitieuDTO.getKpiId(), bieumauKehoachchitieuDTO.getPrdId());
        flagRunQueryKpiService.save(flagRunQueryKpiDTO);
    }

    public Optional<List<BieumauKehoachchitieu>> validateDuplicate(BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO) {
        Optional<List<BieumauKehoachchitieu>> optional = bieumauKehoachchitieuRepository.findBieumauKehoachchitieuByPrdIdAndKpiId(bieumauKehoachchitieuDTO.getPrdId(), bieumauKehoachchitieuDTO.getKpiId());
        return optional;
    }

    public Optional<List<BieumauKehoachchitieu>> validateDuplicateKpiCode(BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO) {
        Optional<List<BieumauKehoachchitieu>> optional = bieumauKehoachchitieuRepository.findBieumauKehoachchitieuByPrdIdAndKpiCode(bieumauKehoachchitieuDTO.getPrdId(), bieumauKehoachchitieuDTO.getKpiCode());
        return optional;
    }

    /**
     * Get all the bieumauKehoachchitieus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BieumauKehoachchitieuDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BieumauKehoachchitieus");
        return bieumauKehoachchitieuRepository.findAll(pageable)
            .map(bieumauKehoachchitieuMapper::toDto);
    }

    /**
     * Get all the bieumauKehoachchitieus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BieumauKehoachchitieuDTO> query(SearchBieuMauParram searchBieuMauParram, Pageable pageable) {
        log.debug("Request to get all BieumauKehoachchitieus");
        return bieumauKehoachchitieuCustomRepository.query(searchBieuMauParram, pageable);
    }

    /**
     * Get one bieumauKehoachchitieu by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BieumauKehoachchitieuDTO> findOne(Long id) {
        log.debug("Request to get BieumauKehoachchitieu : {}", id);
        Optional<BieumauKehoachchitieuDTO> opt = bieumauKehoachchitieuRepository.findById(id)
            .map(bieumauKehoachchitieuMapper::toDto);
        Optional<CatGraphKpiDTO> optCat = catGraphKpiService.findByCode(opt.get().getKpiCode());
        if (optCat.isPresent()) {
            opt.get().setDomainCode(optCat.get().getDomainCode());
            opt.get().setGroupKpiCode(optCat.get().getGroupKpiCode());
            opt.get().setAlarmPlanType(optCat.get().getAlarmPlanType());
        }
        return opt;
    }

    /**
     * Delete the bieumauKehoachchitieu by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BieumauKehoachchitieu : {}", id);
        bieumauKehoachchitieuRepository.deleteById(id);
    }

    public UploadFileResponse doImportFile(MultipartFile multipartFile, Long prdId) throws Exception {
        UploadFileResponse uploadFileResponse = new UploadFileResponse();
        List<BieumauKehoachchitieuDTO> lstObject = readFileToObject(multipartFile);
        updateInfor(lstObject, prdId);
        uploadFileResponse = validateInput(lstObject, uploadFileResponse);
        if (!uploadFileResponse.isValidate()) {
            uploadFileResponse.setImportSucces(false);
            uploadFileResponse.setErrorCode("VALIDATE_FAILE");
            return uploadFileResponse;
        }
        List<BieumauKehoachchitieuDTOError> listDuplicate = validateDuplicate(lstObject, uploadFileResponse);
        if (!DataUtil.isNullOrEmpty(listDuplicate)) {
            uploadFileResponse.setImportSucces(false);
            uploadFileResponse.setErrorCode("DUPLICATE");
            uploadFileResponse.setLstError(listDuplicate);
            return uploadFileResponse;
        }
        List<BieumauKehoachchitieu> entitys = bieumauKehoachchitieuMapper.toEntity(lstObject);
        List<BieumauKehoachchitieu> lsResults = bieumauKehoachchitieuRepository.saveAll(entitys);
        createFlagRunDatas(lsResults);
        uploadFileResponse.setImportSucces(true);
        return uploadFileResponse;
    }

    private void createFlagRunDatas(List<BieumauKehoachchitieu> lsResults) {
        if (DataUtil.isNullOrEmpty(lsResults)) return;
        lsResults.forEach(bean -> {
            FlagRunQueryKpiDTO flagRunQueryKpiDTO = new FlagRunQueryKpiDTO(Constants.DATA_TABLE.BIEUMAU_KEHOACHCHITIEU, bean.getKpiId(), bean.getPrdId());
            flagRunQueryKpiService.save(flagRunQueryKpiDTO);
        });
    }

    public UploadFileResponse validateInput(List<BieumauKehoachchitieuDTO> list, UploadFileResponse uploadFileResponse) {
        List<BieumauKehoachchitieuDTOError> errors = new ArrayList<>();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        uploadFileResponse.setValidate(true);
        List<BieumauKehoachchitieuDTO> lstDuplicate = findDuplidate(list);
        list.stream().forEach(bean -> {
            BieumauKehoachchitieuDTOError bieumauKehoachchitieuDTOError = new BieumauKehoachchitieuDTOError(bean);
            //validate input
            Set<ConstraintViolation<BieumauKehoachchitieuDTO>> violations = validator.validate(bean);
            if (!DataUtil.isNullOrEmpty(violations)) {
                List<String> er = new ArrayList<>();
                violations.forEach(voi -> {
                    String message = Translator.toLocale(voi.getPropertyPath().toString()) + " " + Translator.toLocale(voi.getMessage());
                    er.add(message);
                });
                bieumauKehoachchitieuDTOError.setErrorMessages(er);
                uploadFileResponse.setValidate(false);
            }
            if (DataUtil.isNullOrEmpty(bean.getKpiCode())) {
                uploadFileResponse.setValidate(false);
                bieumauKehoachchitieuDTOError.setErrorMessages(Arrays.asList(Translator.toLocale("label.file.error.kpiId.invalid", new Object[]{bean.getKpiId().toString()})));
            }
            //validate duplidate field in excel
            if (isDuplicateField(lstDuplicate, bean)) {
                uploadFileResponse.setValidate(false);
                bieumauKehoachchitieuDTOError.setErrorMessages(Arrays.asList(Translator.toLocale("label.file.error.duplicate.field", new Object[]{bean.getKpiName()})));
            }
            //validate logic
            if (Constants.CAT_GRAPH_KPI_PLAN_TYPE.YEAR.equals(bean.getAlarmPlanType()) && bean.getTotalRank() == null) {
                uploadFileResponse.setValidate(false);
                bieumauKehoachchitieuDTOError.setErrorMessages(Arrays.asList(Translator.toLocale("error.duplicate.field.kpiCode.require.totalRank")));
            }
            errors.add(bieumauKehoachchitieuDTOError);
        });
        uploadFileResponse.setLstError(errors);
        return uploadFileResponse;
    }

    public boolean isDuplicateField(List<BieumauKehoachchitieuDTO> lstDuplicate, BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO) {
        if (DataUtil.isNullOrEmpty(lstDuplicate)) return false;
        return lstDuplicate.stream().anyMatch(bean -> bean.getKpiId().equals(bieumauKehoachchitieuDTO.getKpiId()));
//        for (BieumauKehoachchitieuDTO bean : lstDuplicate) {
//            if (bean.getKpiId().equals(bieumauKehoachchitieuDTO.getKpiId())) {
//                return true;
//            }
//        }
//        return false;
    }

    public void updateInfor(List<BieumauKehoachchitieuDTO> bieumauKehoachchitieuDTOList, Long prdId) {
        Consumer<BieumauKehoachchitieuDTO> consumerUpdate = bean -> {
            bean.setPrdId(prdId);
            bean.setStatus(Constants.STATUS_ACTIVE);
            bean.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            bean.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
            Optional<CatGraphKpiDTO> optCat = catGraphKpiService.findByKpiId(bean.getKpiId());
            if (optCat.isPresent()) {
                bean.setKpiCode(optCat.get().getKpiCode());
                bean.setKpiName(optCat.get().getKpiName());
                bean.setAlarmPlanType(optCat.get().getAlarmPlanType());
            }
        };
        bieumauKehoachchitieuDTOList.forEach(consumerUpdate);
    }

    public List<BieumauKehoachchitieuDTOError> validateDuplicate(List<BieumauKehoachchitieuDTO> results, UploadFileResponse uploadFileResponse) throws Exception {
        Predicate<BieumauKehoachchitieuDTO> predicateDuplicte = bean -> validateDuplicateKpiCode(bean).isPresent();
        List<BieumauKehoachchitieuDTO> lstDuplicate = results.stream().filter(predicateDuplicte).collect(toList());
        results.removeIf(predicateDuplicte);
        return lstDuplicate.stream().map(bean -> {
            BieumauKehoachchitieuDTOError bieumauKehoachchitieuDTOError = new BieumauKehoachchitieuDTOError(bean);
            bieumauKehoachchitieuDTOError.setErrorMessages(Arrays.asList(Translator.toLocale("error.duplicate.field.kpiCode.prdId", new Object[]{bieumauKehoachchitieuDTOError.getKpiName().toString(), bieumauKehoachchitieuDTOError.getPrdId().toString().substring(0, 4)})));
            return bieumauKehoachchitieuDTOError;
        }).collect(toList());
    }

    public List<BieumauKehoachchitieuDTO> findDuplidate(List<BieumauKehoachchitieuDTO> list) {
        return list.stream().filter(i -> Collections.frequency(list, i) > 1).collect(Collectors.toList());

//        Map<BieumauKehoachchitieuDTO, Long> countingMap = list.stream().collect(Collectors.groupingBy(Function.identity(),
//            Collectors.counting()));
//        return countingMap.entrySet().stream()
//            .filter(e -> e.getValue() > 1L)
//            .map(Map.Entry::getKey)
//            .collect(toList());
    }


    private List<BieumauKehoachchitieuDTO> readFileToObject(MultipartFile multipartFile) throws IOException {
        List<BieumauKehoachchitieuDTO> results = new ArrayList<>();
        Workbook workbook = getWorkbook(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO;
        int r = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (r < 2) {
                r++;
                continue;
            }
            bieumauKehoachchitieuDTO = new BieumauKehoachchitieuDTO();
            int col = 1;
            try {
                try {
                    bieumauKehoachchitieuDTO.setKpiId(DataUtil.safeToDouble(getCellValue(row.getCell(col++))).longValue());
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
                try {
                    bieumauKehoachchitieuDTO.setKpiName(DataUtil.safeToString(getCellValue(row.getCell(col++))));
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
                col++;
                try {
                    bieumauKehoachchitieuDTO.setValPlan(DataUtil.safeToDouble(getCellValue(row.getCell(col++)), null));
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
                try {
                    bieumauKehoachchitieuDTO.setTotalRank(DataUtil.safeToDouble(getCellValue(row.getCell(col)), null));
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
                results.add(bieumauKehoachchitieuDTO);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
            r++;

        }
        return results;
    }

    private static Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
//        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            return new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            return new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

//        return workbook;
    }

    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case _NONE:
            case BLANK:
            case ERROR:
                break;
            default:
                break;
        }

        return cellValue;
    }

    public List<BieumauKehoachchitieuDTO> buildTemplate(BieumauKehoachchitieuTmpDTO bieumauKehoachchitieuTmpDTO) throws Exception {
        List<BieumauKehoachchitieuDTO> results = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(bieumauKehoachchitieuTmpDTO.getKpiCodes())) {
            bieumauKehoachchitieuTmpDTO.getKpiCodes().forEach(bean -> {
                BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO = new BieumauKehoachchitieuDTO(bieumauKehoachchitieuTmpDTO);
                Optional<CatGraphKpiDTO> opt = catGraphKpiService.findByCode(bean);
                if (opt.isPresent()) {
                    bieumauKehoachchitieuDTO.setKpiCode(opt.get().getKpiCode());
                    bieumauKehoachchitieuDTO.setKpiName(opt.get().getKpiName());
                    bieumauKehoachchitieuDTO.setKpiId(opt.get().getKpiId());
                    CatItemDTO catItemDTO = catItemService.findFirstByCategoryCodeAndItemCodeAndStatus(Constants.CATEGORY.UNIT_CATEOGRY, opt.get().getUnitKpi(), Constants.STATUS_ACTIVE);
                    if (catItemDTO != null) {
                        bieumauKehoachchitieuDTO.setUnitName(catItemDTO.getItemName());
                    }
                }
                results.add(bieumauKehoachchitieuDTO);
            });
        } else {
            if (DataUtil.isNullOrEmpty(bieumauKehoachchitieuTmpDTO.getDomainCode())) {
                return new ArrayList<>();
                /*List<CatItemDTO> lstDomain = catItemService.getCatItemByCategoryCode(Constants.CATEGORY.DOMAIN_CATEOGRY);
                if (DataUtil.isNullOrEmpty(lstDomain)) {
                    return null;
                }
                lstDomain.forEach(bean -> {
                    BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO = new BieumauKehoachchitieuDTO(bieumauKehoachchitieuTmpDTO);
                    bieumauKehoachchitieuDTO.setDomainCode(bean.getItemCode());
                    results.add(bieumauKehoachchitieuDTO);
                });*/
            } else {
                BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO = new BieumauKehoachchitieuDTO(bieumauKehoachchitieuTmpDTO);
                addAllGroupKpi(bieumauKehoachchitieuDTO, results);
            }
        }
        return results;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void multiDelete(List<BieumauKehoachchitieuDTO> data) {
        if (DataUtil.isNullOrEmpty(data)) {
            throw new BadRequestAlertException(Translator.toLocale("error.catItem.listItemIsEmpty"), "catItem", "catItem.listItemIsEmpty");
        }
        data.forEach(item -> delete(item.getId()));
    }

    public void addAllGroupKpi(BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO, List<BieumauKehoachchitieuDTO> results) {
        if (!DataUtil.isNullOrEmpty(bieumauKehoachchitieuDTO.getGroupKpiCode())) {
            BieumauKehoachchitieuDTO bieumauKehoachchitieu = new BieumauKehoachchitieuDTO(bieumauKehoachchitieuDTO);
            addKpiInfo(bieumauKehoachchitieu, results);
        } else {
            List<CatItemDTO> lstGroupKpis = catItemService.findCatItemByDomainCode(bieumauKehoachchitieuDTO.getDomainCode());
            if (DataUtil.isNullOrEmpty(lstGroupKpis)) return;
            lstGroupKpis.forEach(bean -> {
                BieumauKehoachchitieuDTO bieumauKehoachchitieu = new BieumauKehoachchitieuDTO(bieumauKehoachchitieuDTO);
                bieumauKehoachchitieu.setGroupKpiCode(bean.getItemCode());
                addKpiInfo(bieumauKehoachchitieu, results);
//            results.add(bieumauKehoachchitieu);
            });
        }
    }

    public void addKpiInfo(BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO, List<BieumauKehoachchitieuDTO> results) {
        List<CatGraphKpiDTO> lstCatGraphKpi = catGraphKpiService.getKpiMain(bieumauKehoachchitieuDTO.getGroupKpiCode(), bieumauKehoachchitieuDTO.getDomainCode());
        if (DataUtil.isNullOrEmpty(lstCatGraphKpi)) return;
        lstCatGraphKpi.forEach(bean -> {
            BieumauKehoachchitieuDTO bieumauKehoachchitieu = new BieumauKehoachchitieuDTO(bieumauKehoachchitieuDTO);
            bieumauKehoachchitieu.setKpiCode(bean.getKpiCode());
            bieumauKehoachchitieu.setKpiName(bean.getKpiName());
            bieumauKehoachchitieu.setKpiId(bean.getKpiId());
            CatItemDTO catItemDTO = catItemService.findFirstByCategoryCodeAndItemCodeAndStatus(Constants.CATEGORY.UNIT_CATEOGRY, bean.getUnitKpi(), Constants.STATUS_ACTIVE);
            if (catItemDTO != null) {
                bieumauKehoachchitieu.setUnitName(catItemDTO.getItemName());
            }
            results.add(bieumauKehoachchitieu);
        });
    }

}
