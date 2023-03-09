package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigDisplayQueryDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigDisplayQueryDTO.class);
        ConfigDisplayQueryDTO configDisplayQueryDTO1 = new ConfigDisplayQueryDTO();
        configDisplayQueryDTO1.setId(1L);
        ConfigDisplayQueryDTO configDisplayQueryDTO2 = new ConfigDisplayQueryDTO();
        assertThat(configDisplayQueryDTO1).isNotEqualTo(configDisplayQueryDTO2);
        configDisplayQueryDTO2.setId(configDisplayQueryDTO1.getId());
        assertThat(configDisplayQueryDTO1).isEqualTo(configDisplayQueryDTO2);
        configDisplayQueryDTO2.setId(2L);
        assertThat(configDisplayQueryDTO1).isNotEqualTo(configDisplayQueryDTO2);
        configDisplayQueryDTO1.setId(null);
        assertThat(configDisplayQueryDTO1).isNotEqualTo(configDisplayQueryDTO2);
    }
}
