package com.b4t.app.repository;

import com.b4t.app.domain.KpiReport;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface KpiReportSearchESRepository extends ElasticsearchRepository<KpiReport, String> {
}
