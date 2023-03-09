package com.b4t.app.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String ACTOR = "actor";
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "vi";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_SECRET_KEY = "123456a@";

    public static final Integer STATUS_DISABLED_INT = 0;
    public static final Integer STATUS_ACTIVE_INT = 1;
    public static final Long STATUS_DISABLED = 0L;
    public static final Long STATUS_ACTIVE = 1L;
    public static final Long SCREEN_SLIDE = 4L;

    public static final Long PROFILE_MAIN = 1L;
    public static final Long PROFILE_NORMAL = 0L;

    public static final Long SCREEN_DEFAULT = 1L;
    public static final Long SCREEN_NORMAL = 0L;
    public static final Long SCREEN_DETAIL_CHILD = 5L;

    public static final char DEFAULT_ESCAPE_CHAR = '&';

    public static final Integer TIME_TYPE_DATE = 1;
    public static final Integer TIME_TYPE_MONTH = 2;
    public static final Integer TIME_TYPE_QUARTER = 3;
    public static final Integer TIME_TYPE_YEAR = 4;

    public static final Integer DEFAULT_RANGE_TIME = -1;

    public static final String FROM_DATE_PARAM = "FROMDATE";
    public static final String TO_DATE_PARAM = "TODATE";
    public static final String BACK_DATE_PARAM = "BACKDATE"; // = TODATE - 1
    public static final String COUNT_PARAM = "COUNT"; // = so thang / quy cua TODATE
    public static final String TO_YEAR_PARAM = "TOYEAR";
    public static final String TO_MONTH_PARAM = "TOMONTH";
    public static final String TO_QUARTER_PARAM = "TOQUARTER";
    public static final String BACK_YEAR_PARAM = "BACKYEAR";
    public static final String BACK_MONTH_PARAM = "BACKMONTH";
    public static final String BACK_QUARTER_PARAM = "BACKQUARTER";
    public static final String MAX_YEAR_PARAM = "MAXYEAR";
    public static final String MAX_MONTH_PARAM = "MAXMONTH";
    public static final String MAX_QUARTER_PARAM = "MAXQUARTER";
    public static final String OBJECT_CODE = "OBJECTCODE";
    public static final String TABLE_NAME = "TABLE_NAME";
    public static final String TIME_TYPE_PARAM = "TIMETYPE";
    public static final String X_AXIS_PARAM = "X_AXIS";
    public static final String Y_AXIS_PARAM = "Y_AXIS";
    public static final String LEGEND_PARAM = "LEGEND";
    public static final String PRD_ID_PARAM = "PRDID";
    public static final String INPUT_LEVEL_PARAM = "INPUTLEVEL";
    public static final String KPI_IDS_PARAM = "KPIIDS";
    public static final String TABLE_NAME_PARAM = "TABLE_NAME";
    public static final String DATA_PRD_ID = "prd_id";
    public static final String DATA_TIME_TYPE = "time_type";
    public static final String DATA_TABLE_ALIAS = "data";
    public static final String KPI_TABLE_ALIAS = "kpi";
    public static final String OVERVIEW_DOMAIN_CODE = "TONG_QUAN";
    public static final String DEFAULT_TABLE_NAME = "telcom_rpt_graph";

    public static final String NDATE_CATITEM = "NDATE";
    public static final String NQUAR_CATITEM = "NQUAR";
    public static final String NMONTH_CATITEM = "NMONTH";
    public static final String NYEAR_CATITEM = "NYEAR";
    public static final String PARAM_CHART_CATITEM = "PARAM_CHART";
    public static final String TYPE_CHART_CATITEM = "TYPE_CHART";
    public static final String MAP_CHART_TYPE = "MAP_CHART";
    public static final String ITEM_MAP_CHART_TYPE = "ITEM_MAP";

    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String TIME_FORMAT_TO_SECOND = "yyyyMMddhhmmss";
    public static final String TIME_FORMAT_TO_MILISECOND = "yyyyMMddHHmmssSSS";

    public static final String KPI_ID_FIELD = "KPI_ID";
    public static final String COPY_SUFFIX_CODE = "_COPY_";
    public static final String COPY_FORMAT_NAME = "%s Copy(%s)";
    public static final String SEPARATE_CHARACTER = "_###_";
    public static final String SEPARATE_REGEX = "###";

    public static final String UNIT_DISPLAY = "UNIT_DISPLAY";
    public static final String FIELD_TYPE = "field";
    public static final String TEXT_TYPE = "text";
    public static final String FUNCTION_TYPE = "function";

    public static final String IN_OPERATOR = "IN";
    public static final String NOT_IN_OPERATOR = "NOT IN";

    public static final String ALLOW_EXTENSIONS = ".jpeg,.jpg,.png,.gif,.pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt";
    public static final String PRODUCER_SLEEP_INTERVAL_PARAM = "PRODUCER_SLEEP_INTERVAL_PARAM";
    public static final String AUTO_RELOAD_CHART_PARAM = "AUTO_RELOAD_CHART_PARAM";
    public static final String CHART_CODE_AUTO_INCREASE = "CHART_CODE_AUTO_INCREASE";

    public static final String FIREBASE_TOPIC_DEFAULT = "alarmToAll";
    public static final String TOPIC_DEFAULT_FIREBASE_CATITEM = "TOPIC_DEFAULT_FIREBASE";

    public static final Long COMMON_NO = 0L;
    public static final Long COMMON_YES = 1L;
    public static final String VALIDATE_OK = "OK";
    public static final String formInputYear = "2";

    // Module import du lieu_start
    public static final Integer IS_SHOW = 1;
    public static final Integer IS_HIDDEN = 0;
    public static final Integer IS_REQUIRED = 1;
    public static final Integer IS_TIME_COLUMN = 1;
    public static final Integer IS_PRIMARY_KEY = 1;
    public static final String PRIMARY_KEY = "PRI";
    public static final String AUTO_INCREMENT = "auto_increment";
    public static final String RESULT_VALIDATE_MSG = "RESULT_VALIDATE_MSG";
    //    public static final String TIME_STAMP = "timestamp";
    public static final String UPDATE_USER = "update_user";
    public static final String UPDATE_TIME = "update_time";
    public static final ArrayList<String> LIST_DEFAULT_COLUMN = new ArrayList<String>(Arrays.asList("id","update_user","update_time","yearkey","quarkey","monkey","status"));
    public static final ArrayList<String> LIST_CODE_MALV = new ArrayList<String>(Arrays.asList("ma_lv_cap1","ma_lv_cap2","ma_lv_cap3","ma_lv_cap4","ma_lv_cap5"));
    public interface ROLE {
        String ROLE_ADMIN = "ROLE_ADMIN";
        String ROLE_USER = "ROLE_USER";
    }
    // Module import du lieu_end

    public interface EXTENSION {
        String XLS = ".xls";
        String XLSX = ".xlsx";
    }

    public interface FILE_IMPORT {
        int START_COL = 0;
        int START_ROW = 3;
    }

    public interface CAT_GRAPH_KPI_PLAN_TYPE {
        Long YEAR = 4L;
    }

    public interface PARAM_CHART_DEFAULT {
        String CURRENT_DATE = "CURRENT_DATE";
        String MAX_DATE = "MAX_DATE";
        String MAX_DATE_NDATE = "MAX_DATE-NDATE";
        String MAX_DATE_NMONTH = "MAX_DATE-NMONTH";
        String MAX_DATE_NYEAR = "MAX_DATE-NYEAR";
        String MAX_DATE_NQUAR = "MAX_DATE-NQUAR";
        String START_OF_YEAR = "START_OF_YEAR";
        String CURRENT_DATE_RELATIVE_TIME = "CURRENT_DATE-RELATIVE_TIME";
        String CURRENT_DATE_RELATIVE_TIME_NDATE = "CURRENT_DATE-RELATIVE_TIME-NDATE";
        String CURRENT_DATE_RELATIVE_TIME_NMONTH = "CURRENT_DATE-RELATIVE_TIME-NMONTH";
        String CURRENT_DATE_RELATIVE_TIME_NQUAR = "CURRENT_DATE-RELATIVE_TIME-NQUAR";
        String CURRENT_DATE_RELATIVE_TIME_NYEAR = "CURRENT_DATE-RELATIVE_TIME-NYEAR";
        String BYMONTH = "BYMONTH";
        String BYYEAR = "BYYEAR";
        String BYQUARTER = "BYQUARTER";
        String MAX_DATE_TIME_TYPE_MONTH = "MAX_DATE_TIME_TYPE_MONTH";
    }

    public interface DATA_TYPE {
        String STRING = "STRING";
        String DATE = "DATE";
        String LONG = "LONG";
        String DOUBLE = "DOUBLE";
        String INT = "INT";
        String BIGINT = "BIGINT";
    }

    public interface DATA_DB_TYPE {
        String VARCHAR = "varchar";
        String DATE = "date";
        String DATETIME = "datetime";
        String TIMESTAMP = "timestamp";
        String LONG = "long";
        String DOUBLE = "double";
        String FLOAT = "float";
        String INT = "int";
        String BIGINT = "bigint";
    }

    public interface QUERY_TYPE {
        int MAX_PRD_ID = 0;
        int MAX_PRD_ID_TIME_TYPE = 3;
        int COUNT = 1;
        int QUERY_DATA = 2;
    }

    public interface VALUE_TYPE {
        String VAL = "VAL";
        String VAL_ACC = "VAL_ACC";
        String VAL_TOTAL = "VAL_TOTAL";
    }

    public interface OUTPUT_SEARCH {
        String TABLE = "TABLE";
        String LINE = "LINE";
        String COLUMN = "COLUMN";
        String PIE = "PIE";
        String AREA = "AREA";
        String GROUP_BAR = "GROUP_BAR";
        String ICON_CHART = "ICON_CHART";
        String STACK = "STACK";
        String CORRELATE_CHART = "CORRELATE_CHART";
    }

    public interface CATEGORY {
        Long TIME_TYPE = 22L;
        Long DOMAIN_TABLE = 24L;
        Long INPUT_LEVEL = 25L;
        String UNIT_CATEOGRY = "UNIT";
        String UNIT_IMPORT_CATEOGRY = "UNIT_IMPORT";
        String DOMAIN_CATEOGRY = "DOMAIN";
        String GROUP_KPI = "GROUP_KPI";
    }

    public interface KPI_TYPE {
        Long ORIGINAL = 0L;
        Long SYNTHETIC = 1L;
    }

    public interface THRESHOLDTYPE {
        Long POSITIVE = 0L;
        Long NEGATIVE = 1L;
    }

    public interface VALIDATE {
        String ID_NULL = "error.idnull";
    }

    public interface DATA_TABLE {
        String BIEUMAU_KEHOACHCHITIEU = "BIEUMAU_KEHOACHCHITIEU";
        String TABLE_KEHOACHCHITIEU = "MIC_DASHBOARD.BIEUMAU_KEHOACHCHITIEU";
    }

    public interface ES {
        String BASE_RPT_GRAPH = "base-rpt-graph";
        String OBJ_RPT_GRAPH = "obj-rpt-graph";
        String TYPE = "_doc";
    }

    public interface BASE_RPT_GRAPH {
        String INPUT_LEVEL = "inputLevel";
        String TIME_TYPE = "timeType";
        String PRD_ID = "prdId";
        String KPI_ID = "kpiId";
        String OBJ_CODE = "objCode";
        String OBJ_NAME = "objName";
        String OBJ_CODE_FULL = "objCodeFull";
        String PARENT_NAME = "parentName";
        String PARENT_CODE = "parentCode";
    }

//    public interface RPT_GRAPH_TABLES {
//        String BASE_RPT_GRAPH = "BASE_RPT_GRAPH";
//        String DOMAIN_RPT_GRAPH = "DOMAIN_RPT_GRAPH";
//        String ICT_RPT_GRAPH = "ICT_RPT_GRAPH";
//        String IT_RPT_GRAPH = "IT_RPT_GRAPH";
//        String JOURNAL_RPT_GRAPH = "JOURNAL_RPT_GRAPH";
//        String POSTAL_RPT_GRAPH = "POSTAL_RPT_GRAPH";
//        String SECUR_RPT_GRAPH = "SECUR_RPT_GRAPH";
//        String TELCOM_RPT_GRAPH = "TELCOM_RPT_GRAPH";
//    }

    public static final List<String> RPT_GRAPH_TABLES = Arrays.asList("BASE_RPT_GRAPH", "DOMAIN_RPT_GRAPH", "ICT_RPT_GRAPH", "IT_RPT_GRAPH", "JOURNAL_RPT_GRAPH", "POSTAL_RPT_GRAPH", "SECUR_RPT_GRAPH", "TELCOM_RPT_GRAPH");
    public static final String SCHEMA_NAME = "mic_dashboard";

    private Constants() {
    }

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String GOOGLE_RECAPTCHA_FALSE = "google.recaptcha.false";
    public static final String DETAIL = "detail";
    public static final String TOKEN_ERROR = "token.error";
    public static final String TOKEN_ERROR_PASS = "token.error.pass";
    public static final String RESET_KEY_NOTEXISTED = "token.error.notExisted";
    public static final String DATA = "data";
    public static final String LOGIN_USER_REQUIRED = "login.user.required";
    public static final String LOGIN_PASS_REQUIRED = "login.pass.required";
    public static final String LOGIN_PASS_MIN = "login.pass.min";
    public static final String LOGIN_PASS_MAX = "login.pass.max";
    public static final String LOGIN_PASS_OLL_REQUIRED = "login.pass.old.required";
    public static final String LOGIN_PASS_OLL_MIN = "login.pass.old.min";
    public static final String LOGIN_PASS_OLL_MAX = "login.pass.old.max";
    public static final String LOGIN_PASS_COM_REQUIRED = "login.pass.com.required";
    public static final String LOGIN_PASS_COM_MIN = "login.pass.com.min";
    public static final String LOGIN_PASS_COM_MAX = "login.pass.com.max";
    public static final String URL_PART = "url.part";
    public static final String LOGIN_FALSE = "login.pass.false";
    public static final String LOGIN_EMAIL_FALSE = "login.email.false";
    public static final String LOGIN_PASS_CHANGE_FALSE = "login.pass.change.false";
    public static final String LOGIN_PASS_OLD_SS_FALSE = "login.pass.old.ss.false";
    public static final String LOGIN_PASS_NEW_SS_FALSE = "login.pass.new.ss.false";
    public static final String CHANGE_PASS_SUS = "change.pass.sus";
    public static final String CHANGE_PASS_FALSE = "change.pass.false";
    public static final String CHANGE_NAME_FALSE = "change.name.false";
    public static final String LOGIN_NAME_FALSE = "login.name.false";
    public static final String URL_PART_FALSE = "url.part.false";
    public static final String EMAIL_CONTENT1 = "email.conten1";
    public static final String EMAIL_CONTENT3 = "email.content3";
    public static final String EMAIL_START = "email.hello";
    public static final String EMAIL_CONTENT2 = "email.conten2";
    public static final String EMAIL_SUBJECT = "email.subject";
    public static final String EMAIL_PATH = "email.path";
    public static final String EMAIL_FALSE = "email.false";
    public static final String EMAIL_TRUE = "email.true";


    //data log const
    public interface DATA_LOG_SEARCH_TIME_TYPE{
        Integer TAT_CA = 0;
        Integer TUAN_NAY = 1;
        Integer TUAN_QUA = 2;
        Integer THANG_NAY = 3;
        Integer THANG_QUA = 4;

    }
}
