package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigMapChartAreaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigMapChartArea.class);
        ConfigMapChartArea configMapChartArea1 = new ConfigMapChartArea();
        configMapChartArea1.setId(1L);
        ConfigMapChartArea configMapChartArea2 = new ConfigMapChartArea();
        configMapChartArea2.setId(configMapChartArea1.getId());
        assertThat(configMapChartArea1).isEqualTo(configMapChartArea2);
        configMapChartArea2.setId(2L);
        assertThat(configMapChartArea1).isNotEqualTo(configMapChartArea2);
        configMapChartArea1.setId(null);
        assertThat(configMapChartArea1).isNotEqualTo(configMapChartArea2);
    }
}
