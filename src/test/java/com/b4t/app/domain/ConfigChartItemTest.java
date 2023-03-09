package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigChartItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigChartItem.class);
        ConfigChartItem configChartItem1 = new ConfigChartItem();
        configChartItem1.setId(1L);
        ConfigChartItem configChartItem2 = new ConfigChartItem();
        configChartItem2.setId(configChartItem1.getId());
        assertThat(configChartItem1).isEqualTo(configChartItem2);
        configChartItem2.setId(2L);
        assertThat(configChartItem1).isNotEqualTo(configChartItem2);
        configChartItem1.setId(null);
        assertThat(configChartItem1).isNotEqualTo(configChartItem2);
    }
}
