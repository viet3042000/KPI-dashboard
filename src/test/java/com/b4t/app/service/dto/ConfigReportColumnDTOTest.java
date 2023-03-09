package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigReportColumnDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigReportColumnDTO.class);
        ConfigReportColumnDTO configReportColumnDTO1 = new ConfigReportColumnDTO();
        configReportColumnDTO1.setId(1L);
        ConfigReportColumnDTO configReportColumnDTO2 = new ConfigReportColumnDTO();
        assertThat(configReportColumnDTO1).isNotEqualTo(configReportColumnDTO2);
        configReportColumnDTO2.setId(configReportColumnDTO1.getId());
        assertThat(configReportColumnDTO1).isEqualTo(configReportColumnDTO2);
        configReportColumnDTO2.setId(2L);
        assertThat(configReportColumnDTO1).isNotEqualTo(configReportColumnDTO2);
        configReportColumnDTO1.setId(null);
        assertThat(configReportColumnDTO1).isNotEqualTo(configReportColumnDTO2);
    }
}
