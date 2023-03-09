package com.b4t.app.aop.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogUtis {
    private static final Logger logger = LoggerFactory.getLogger(LogUtis.class);

    public void printLog(String clazz, String method, String actor, long startTime, long endTime) {
        logger.info("{}|{}|{}|{}", clazz, method, actor, startTime);
    }
}
