package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigInputTableQueryKpiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigInputTableQueryKpi.class);
        ConfigInputTableQueryKpi configInputTableQueryKpi1 = new ConfigInputTableQueryKpi();
        configInputTableQueryKpi1.setId(1L);
        ConfigInputTableQueryKpi configInputTableQueryKpi2 = new ConfigInputTableQueryKpi();
        configInputTableQueryKpi2.setId(configInputTableQueryKpi1.getId());
        assertThat(configInputTableQueryKpi1).isEqualTo(configInputTableQueryKpi2);
        configInputTableQueryKpi2.setId(2L);
        assertThat(configInputTableQueryKpi1).isNotEqualTo(configInputTableQueryKpi2);
        configInputTableQueryKpi1.setId(null);
        assertThat(configInputTableQueryKpi1).isNotEqualTo(configInputTableQueryKpi2);
    }
}
