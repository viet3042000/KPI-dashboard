package com.b4t.app.web.rest;

import com.b4t.app.service.dto.*;
import com.b4t.app.service.mapper.DataLogService;
import io.github.jhipster.web.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.*;

@RestController
@RequestMapping("/api")
public class DataLogResource {

    private final Logger log = LoggerFactory.getLogger(DataLogResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataLogService dataLogService;

    public DataLogResource(DataLogService dataLogService) {
        this.dataLogService = dataLogService;
    }

    @PostMapping("/dataLog/searchDataLog")
    public ResponseEntity<List<DataLogDTO>> searchDataLog(@RequestBody DataLogSearchDTO dataLogSearchDTO, Pageable pageable) {
        final Page<DataLogDTO> page = dataLogService.searchDataLog(dataLogSearchDTO, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
