package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class CatGroupChartTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatGroupChart.class);
        CatGroupChart catGroupChart1 = new CatGroupChart();
        catGroupChart1.setId(1L);
        CatGroupChart catGroupChart2 = new CatGroupChart();
        catGroupChart2.setId(catGroupChart1.getId());
        assertThat(catGroupChart1).isEqualTo(catGroupChart2);
        catGroupChart2.setId(2L);
        assertThat(catGroupChart1).isNotEqualTo(catGroupChart2);
        catGroupChart1.setId(null);
        assertThat(catGroupChart1).isNotEqualTo(catGroupChart2);
    }
}
