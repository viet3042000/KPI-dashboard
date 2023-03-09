package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigReportDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigReportDTO.class);
        ConfigReportDTO configReportDTO1 = new ConfigReportDTO();
        configReportDTO1.setId(1L);
        ConfigReportDTO configReportDTO2 = new ConfigReportDTO();
        assertThat(configReportDTO1).isNotEqualTo(configReportDTO2);
        configReportDTO2.setId(configReportDTO1.getId());
        assertThat(configReportDTO1).isEqualTo(configReportDTO2);
        configReportDTO2.setId(2L);
        assertThat(configReportDTO1).isNotEqualTo(configReportDTO2);
        configReportDTO1.setId(null);
        assertThat(configReportDTO1).isNotEqualTo(configReportDTO2);
    }
}
