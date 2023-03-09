package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigInputTableQueryKpiDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigInputTableQueryKpiDTO.class);
        ConfigInputTableQueryKpiDTO configInputTableQueryKpiDTO1 = new ConfigInputTableQueryKpiDTO();
        configInputTableQueryKpiDTO1.setId(1L);
        ConfigInputTableQueryKpiDTO configInputTableQueryKpiDTO2 = new ConfigInputTableQueryKpiDTO();
        assertThat(configInputTableQueryKpiDTO1).isNotEqualTo(configInputTableQueryKpiDTO2);
        configInputTableQueryKpiDTO2.setId(configInputTableQueryKpiDTO1.getId());
        assertThat(configInputTableQueryKpiDTO1).isEqualTo(configInputTableQueryKpiDTO2);
        configInputTableQueryKpiDTO2.setId(2L);
        assertThat(configInputTableQueryKpiDTO1).isNotEqualTo(configInputTableQueryKpiDTO2);
        configInputTableQueryKpiDTO1.setId(null);
        assertThat(configInputTableQueryKpiDTO1).isNotEqualTo(configInputTableQueryKpiDTO2);
    }
}
