package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class MonitorQueryKpiDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitorQueryKpiDTO.class);
        MonitorQueryKpiDTO monitorQueryKpiDTO1 = new MonitorQueryKpiDTO();
        monitorQueryKpiDTO1.setId(1L);
        MonitorQueryKpiDTO monitorQueryKpiDTO2 = new MonitorQueryKpiDTO();
        assertThat(monitorQueryKpiDTO1).isNotEqualTo(monitorQueryKpiDTO2);
        monitorQueryKpiDTO2.setId(monitorQueryKpiDTO1.getId());
        assertThat(monitorQueryKpiDTO1).isEqualTo(monitorQueryKpiDTO2);
        monitorQueryKpiDTO2.setId(2L);
        assertThat(monitorQueryKpiDTO1).isNotEqualTo(monitorQueryKpiDTO2);
        monitorQueryKpiDTO1.setId(null);
        assertThat(monitorQueryKpiDTO1).isNotEqualTo(monitorQueryKpiDTO2);
    }
}
