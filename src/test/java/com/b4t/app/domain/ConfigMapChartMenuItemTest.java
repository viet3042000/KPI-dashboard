package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigMapChartMenuItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigMapChartMenuItem.class);
        ConfigMapChartMenuItem configMapChartMenuItem1 = new ConfigMapChartMenuItem();
        configMapChartMenuItem1.setId(1L);
        ConfigMapChartMenuItem configMapChartMenuItem2 = new ConfigMapChartMenuItem();
        configMapChartMenuItem2.setId(configMapChartMenuItem1.getId());
        assertThat(configMapChartMenuItem1).isEqualTo(configMapChartMenuItem2);
        configMapChartMenuItem2.setId(2L);
        assertThat(configMapChartMenuItem1).isNotEqualTo(configMapChartMenuItem2);
        configMapChartMenuItem1.setId(null);
        assertThat(configMapChartMenuItem1).isNotEqualTo(configMapChartMenuItem2);
    }
}
