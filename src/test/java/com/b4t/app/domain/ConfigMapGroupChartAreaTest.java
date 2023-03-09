package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigMapGroupChartAreaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigMapGroupChartArea.class);
        ConfigMapGroupChartArea configMapGroupChartArea1 = new ConfigMapGroupChartArea();
        configMapGroupChartArea1.setId(1L);
        ConfigMapGroupChartArea configMapGroupChartArea2 = new ConfigMapGroupChartArea();
        configMapGroupChartArea2.setId(configMapGroupChartArea1.getId());
        assertThat(configMapGroupChartArea1).isEqualTo(configMapGroupChartArea2);
        configMapGroupChartArea2.setId(2L);
        assertThat(configMapGroupChartArea1).isNotEqualTo(configMapGroupChartArea2);
        configMapGroupChartArea1.setId(null);
        assertThat(configMapGroupChartArea1).isNotEqualTo(configMapGroupChartArea2);
    }
}
