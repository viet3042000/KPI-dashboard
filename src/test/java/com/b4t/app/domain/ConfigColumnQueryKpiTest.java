package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigColumnQueryKpiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigColumnQueryKpi.class);
        ConfigColumnQueryKpi configColumnQueryKpi1 = new ConfigColumnQueryKpi();
        configColumnQueryKpi1.setId(1L);
        ConfigColumnQueryKpi configColumnQueryKpi2 = new ConfigColumnQueryKpi();
        configColumnQueryKpi2.setId(configColumnQueryKpi1.getId());
        assertThat(configColumnQueryKpi1).isEqualTo(configColumnQueryKpi2);
        configColumnQueryKpi2.setId(2L);
        assertThat(configColumnQueryKpi1).isNotEqualTo(configColumnQueryKpi2);
        configColumnQueryKpi1.setId(null);
        assertThat(configColumnQueryKpi1).isNotEqualTo(configColumnQueryKpi2);
    }
}
