package com.b4t.app.config;

public class ChatQueryConstant {
    public static final String QUERY_GET_LIST_USER_CHART = "select '#idUserLogin' as user_login_id,\n" +
        "       a.id          ,\n" +
        "       a.login        user_name,\n" +
        "       a.image_url    avatar,\n" +
        "       a.email,\n" +
        "       a.is_alarm_leader ,\n" +
        "       noder.time_newest ,\n" +
        "       noder.content ,\n" +
        "       nunread.num_notify_unread \n" +
        "from jhi_user a\n" +
        "         left join\n" +
        "     (\n" +
        "         select n.create_date as time_newest, n.user_id_sent, nu.user_id_recieved, n.content\n" +
        "         from notification n\n" +
        "                  inner join notification_user nu on n.id = nu.notify_id and nu.is_new = 1\n" +
        "     ) noder on ((a.id = noder.user_id_recieved and noder.user_id_sent = :idUserLogin)\n" +
        "         or (a.id = noder.user_id_sent and noder.user_id_recieved = :idUserLogin))\n" +
        "         left join\n" +
        "     (\n" +
        "         select count(distinct n.id) num_notify_unread, nu.user_id_recieved\n" +
        "         from notification n\n" +
        "                  inner join notification_user nu on n.id = nu.notify_id and nu.is_read = 0 and nu.is_deleted = 0\n" +
        "         where nu.user_id_recieved = :idUserLogin\n" +
        "         group by nu.user_id_recieved\n" +
        "     ) nunread on a.id = nunread.user_id_recieved\n" +
        "\n" +
        "where a.activated = 1\n" +
        "order by noder.time_newest desc, a.login ";

    public static final String COUNT_NUM_NOTICE_UN_READ = "select #idUserLogin as userLoginId, count(distinct n.id) num_notify_unread \n" +
        "from notification n\n" +
        "         inner join notification_user nu\n" +
        "                    on n.id = nu.notify_id and nu.is_read = 0 and nu.is_deleted = 0\n" +
        "where nu.user_id_recieved = :idUserLogin\n";
    public static final String QUERY_GET_USER_RECIEVED_ALARM = "select a.id as userId, a.login as username, a.email\n" +
        "from jhi_user a\n" +
        "         inner join jhi_user_domain b on a.id = b.user_id and a.activated = 1\n" +
        "         inner join config_chart cc on b.domain_code = cc.DOMAIN_CODE and cc.STATUS = 1\n" +
        "where cc.id = :chartId\n" +
        "  and a.is_alarm_leader = 1\n";

    public static final String GET_NOTICE_OF_CLIENT = "select " +
        "       n.id as id , \n" +
        "       n.title as title , \n" +
        "       n.content as content, \n" +
        "       n.screen_id as screen_id, \n" +
        "       n.image_path as image_path,\n" +
        "       n.file_attach_path,\n" +
        "       n.file_attach_name,\n" +
        "       n.create_date,\n" +
        "       n.user_id_sent,\n" +
        "       nu.user_id_recieved,\n" +
        "       nu.is_deleted,\n" +
        "       nu.is_read\n" +
        "from notification n\n" +
        "         inner join notification_user nu on n.id = nu.notify_id\n" +
        "WHERE nu.is_read = 0 and nu.is_deleted = 0 \n" +
        "  and ((n.user_id_sent = :userIdConversation and nu.user_id_recieved = :userLoginId)\n" +
        "    or (nu.user_id_recieved = :userIdConversation and n.user_id_sent = :userLoginId)\n" +
        "    )\n" +
        "  #extendCondition \n" +
        "order by n.create_date desc\n";

    public static final String GET_NOTICE_OF_CLIENT_COUNT = "select count(1) \n" +
        " from notification n\n" +
        "         inner join notification_user nu on n.id = nu.notify_id\n" +
        " WHERE nu.is_read = 0\n" +
        "  and ((n.user_id_sent = :userIdConversation and nu.user_id_recieved = :userLoginId)\n" +
        "    or (nu.user_id_recieved = :userIdConversation and n.user_id_sent = :userLoginId)\n" +
        "    )\n" +
        "  #extendCondition \n" +
        "order by n.create_date desc\n";

    public final static String COUNT_NOTICE_USER = "select count(*) " +
        " from jhi_user a" +
        " left join (" +
        "        select n.create_date as time_newest, n.user_id_sent, nu.user_id_recieved" +
        "        from notification n " +
        "        inner join notification_user nu on n.id = nu.notify_id and nu.is_new = 1" +
        " ) noder on ((a.id = noder.user_id_recieved and noder.user_id_sent = :userId)" +
        "            or (a.id = noder.user_id_sent and noder.user_id_recieved = :userId))" +
        " left join (" +
        "        select  count(distinct n.id) num_notify_unread, n.user_id_sent" +
        "        from notification n " +
        "        inner join notification_user nu on n.id = nu.notify_id and nu.is_read = 0 and nu.is_deleted = 0" +
        " where nu.user_id_recieved = :userId" +
        " group by n.user_id_sent" +
        " ) nunread on a.id = nunread.user_id_sent" +
        " where a.activated = 1";

    public static final String QUERY_USER_NOTICE = "select" +
        "    a.id user_id" +
        "    , a.login as username" +
        "    , a.image_url as avatar" +
//        "    , a.email" +
        "    , a.is_alarm_leader" +
        "    , noder.time_newest" +
        "    , num_notify_unread" +
        "    , content" +
        " from jhi_user a" +
        " left join (" +
        "        select n.create_date as time_newest, n.user_id_sent, nu.user_id_recieved, n.content" +
        "        from notification n " +
        "        inner join notification_user nu on n.id = nu.notify_id and nu.is_new = 1 and nu.is_deleted = 0" +
        " ) noder on ((a.id = noder.user_id_recieved and noder.user_id_sent = :userId)" +
        "            or (a.id = noder.user_id_sent and noder.user_id_recieved = :userId))" +
        " left join (" +
        "        select  count(distinct n.id) num_notify_unread, n.user_id_sent" +
        "        from notification n " +
        "        inner join notification_user nu on n.id = nu.notify_id and nu.is_read = 0 and nu.is_deleted = 0" +
        " where nu.user_id_recieved = :userId" +
        " group by n.user_id_sent" +
        " ) nunread on a.id = nunread.user_id_sent" +
        " where a.activated = 1";
}
