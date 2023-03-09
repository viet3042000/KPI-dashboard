package com.b4t.app.service.mapper;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigReport;
import com.b4t.app.repository.*;
import com.b4t.app.security.jwt.TokenProvider;
import com.b4t.app.service.*;
import com.b4t.app.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class DataLogService {

    private final Logger log = LoggerFactory.getLogger(DataLogService.class);

    private final DataLogRepository dataLogRepository;

    private final PasswordEncoder passwordEncoder;

//    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final EntityManager entityManager;

    private final UserMapper userMapper;

    private final UserCustomRepository userCustomRepository;

    @Autowired
    SysUserRoleRepository sysUserRoleRepository;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    ConfigReportUtilsService configReportUtilsService;

    @Autowired
    DataLogCustomRepository dataLogCustomRepository;

    @Autowired
    ConfigReportRepository configReportRepository;

    @Value("${user.pass-default}")
    String defaultPass;

    public DataLogService(DataLogRepository dataLogRepository, PasswordEncoder passwordEncoder,
                          CacheManager cacheManager, SysRoleService sysRoleService, EntityManager entityManager, UserMapper userMapper, UserCustomRepository userCustomRepository) {
        this.dataLogRepository = dataLogRepository;
        this.passwordEncoder = passwordEncoder;
        this.cacheManager = cacheManager;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.userCustomRepository = userCustomRepository;
        this.sysRoleService = sysRoleService;
    }

    public Page<DataLogDTO> searchDataLog(DataLogSearchDTO dataLogSearchDTO, Pageable pageable) {
        int time = Integer.valueOf(dataLogSearchDTO.getTimeType());
        //xử lý khoảng thời gian tìm kiếm
        LocalDate todate = LocalDate.now();
        LocalDate fromdate = null;
        if(time == Constants.DATA_LOG_SEARCH_TIME_TYPE.TUAN_NAY){
            // Go backward to get Monday
            fromdate = todate;
            while (fromdate.getDayOfWeek() != DayOfWeek.MONDAY) {
                fromdate = fromdate.minusDays(1);
            }
        }
        if(time == Constants.DATA_LOG_SEARCH_TIME_TYPE.TUAN_QUA){
            LocalDate monday = LocalDate.now();
            while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
                monday = monday.minusDays(1);
            }
            fromdate = monday.minusDays(7);
            todate = monday.minusDays(1);
        }
        if(time == Constants.DATA_LOG_SEARCH_TIME_TYPE.THANG_NAY){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            // set day to minimum
            calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            fromdate = calendar.getTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        }
        if(time == Constants.DATA_LOG_SEARCH_TIME_TYPE.THANG_QUA){
            Calendar aCalendar = Calendar.getInstance();
            // add -1 month to current month
            aCalendar.add(Calendar.MONTH, -1);
            // set DATE to 1, so first date of previous month
            aCalendar.set(Calendar.DATE, 1);
            fromdate = aCalendar.getTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
            // set actual maximum date of previous month
            aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            todate = aCalendar.getTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        }
        todate = todate.plusDays(1);

        String paramUpdateBy = null;
        if(dataLogSearchDTO.getUpdateBy() != null && !dataLogSearchDTO.getUpdateBy().equalsIgnoreCase("Tất cả")){
            paramUpdateBy = dataLogSearchDTO.getUpdateBy();
        }
        ConfigReport configReport = configReportRepository.getOne(dataLogSearchDTO.getReportId());
        if(time != Constants.DATA_LOG_SEARCH_TIME_TYPE.TAT_CA){
            return dataLogCustomRepository.searchDataLog(configReport.getDatabaseName(),
                dataLogSearchDTO.getTableName(),
                fromdate.toString(), todate.toString(),
                paramUpdateBy,
                pageable).map(obj -> {
                DataLogDTO dataLog = new DataLogDTO();
                dataLog.setId(DataUtil.safeToLong(obj[0]));
                dataLog.setTableName(DataUtil.safeToString(obj[1]));
                dataLog.setRecordId(DataUtil.safeToLong(obj[2]));
                if(obj[3] != null) {
                    dataLog.setMonkey(DataUtil.safeToInt(obj[3]));
                }
                if(obj[4] != null) {
                    dataLog.setQuarkey(DataUtil.safeToInt(obj[4]));
                }
                if(obj[5] != null) {
                    dataLog.setYearkey(DataUtil.safeToInt(obj[5]));
                }
                dataLog.setField(DataUtil.safeToString(obj[6]));
                dataLog.setFieldName(DataUtil.safeToString(obj[7]));
                dataLog.setOldValue(DataUtil.safeToString(obj[8]));
                dataLog.setNewValue(DataUtil.safeToString(obj[9]));
                dataLog.setActionType(DataUtil.safeToInt(obj[10]));
                dataLog.setModifiedBy(DataUtil.safeToString(obj[11]));
                if(obj[12] != null) {
                    String x = obj[12].toString();
                    x = x.substring(0, x.indexOf("."));
                    dataLog.setUpdateTime(x);
                    SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    try {
                        date = dateParser.parse(x);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dataLog.setModifiedTime(date);
                }
                dataLog.setSchemaName(DataUtil.safeToString(obj[13]));
                return dataLog;
            });
        } else {
            return dataLogCustomRepository.searchDataLog(configReport.getDatabaseName(),
                dataLogSearchDTO.getTableName(),
                null,null,
                paramUpdateBy,
                pageable).map(obj -> {
                DataLogDTO dataLog = new DataLogDTO();
                dataLog.setId(DataUtil.safeToLong(obj[0]));
                dataLog.setTableName(DataUtil.safeToString(obj[1]));
                dataLog.setRecordId(DataUtil.safeToLong(obj[2]));
                if(obj[3] != null) {
                    dataLog.setMonkey(DataUtil.safeToInt(obj[3]));
                }
                if(obj[4] != null) {
                    dataLog.setQuarkey(DataUtil.safeToInt(obj[4]));
                }
                if(obj[5] != null) {
                    dataLog.setYearkey(DataUtil.safeToInt(obj[5]));
                }
                dataLog.setField(DataUtil.safeToString(obj[6]));
                dataLog.setFieldName(DataUtil.safeToString(obj[7]));
                dataLog.setOldValue(DataUtil.safeToString(obj[8]));
                dataLog.setNewValue(DataUtil.safeToString(obj[9]));
                dataLog.setActionType(DataUtil.safeToInt(obj[10]));
                dataLog.setModifiedBy(DataUtil.safeToString(obj[11]));
                if(obj[12] != null) {
                    String x = obj[12].toString();
                    x = x.substring(0, x.indexOf("."));
                    dataLog.setUpdateTime(x);
                    SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    try {
                        date = dateParser.parse(x);

                        System.out.println(dateFormatter.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        dataLog.setModifiedTime(dateFormatter.parse(dateFormatter.format(date)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                dataLog.setSchemaName(DataUtil.safeToString(obj[13]));
                return dataLog;
            });
        }
    }
}
