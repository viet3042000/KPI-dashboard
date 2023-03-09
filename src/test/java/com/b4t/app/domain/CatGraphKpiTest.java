package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class CatGraphKpiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatGraphKpi.class);
        CatGraphKpi catGraphKpi1 = new CatGraphKpi();
        catGraphKpi1.setId(1L);
        CatGraphKpi catGraphKpi2 = new CatGraphKpi();
        catGraphKpi2.setId(catGraphKpi1.getId());
        assertThat(catGraphKpi1).isEqualTo(catGraphKpi2);
        catGraphKpi2.setId(2L);
        assertThat(catGraphKpi1).isNotEqualTo(catGraphKpi2);
        catGraphKpi1.setId(null);
        assertThat(catGraphKpi1).isNotEqualTo(catGraphKpi2);
    }
}
