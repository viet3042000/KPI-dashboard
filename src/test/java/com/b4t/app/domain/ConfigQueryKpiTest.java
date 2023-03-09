package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigQueryKpiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigQueryKpi.class);
        ConfigQueryKpi configQueryKpi1 = new ConfigQueryKpi();
        configQueryKpi1.setId(1L);
        ConfigQueryKpi configQueryKpi2 = new ConfigQueryKpi();
        configQueryKpi2.setId(configQueryKpi1.getId());
        assertThat(configQueryKpi1).isEqualTo(configQueryKpi2);
        configQueryKpi2.setId(2L);
        assertThat(configQueryKpi1).isNotEqualTo(configQueryKpi2);
        configQueryKpi1.setId(null);
        assertThat(configQueryKpi1).isNotEqualTo(configQueryKpi2);
    }
}
