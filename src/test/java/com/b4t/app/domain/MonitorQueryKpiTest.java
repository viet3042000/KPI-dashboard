package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class MonitorQueryKpiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitorQueryKpi.class);
        MonitorQueryKpi monitorQueryKpi1 = new MonitorQueryKpi();
        monitorQueryKpi1.setId(1L);
        MonitorQueryKpi monitorQueryKpi2 = new MonitorQueryKpi();
        monitorQueryKpi2.setId(monitorQueryKpi1.getId());
        assertThat(monitorQueryKpi1).isEqualTo(monitorQueryKpi2);
        monitorQueryKpi2.setId(2L);
        assertThat(monitorQueryKpi1).isNotEqualTo(monitorQueryKpi2);
        monitorQueryKpi1.setId(null);
        assertThat(monitorQueryKpi1).isNotEqualTo(monitorQueryKpi2);
    }
}
