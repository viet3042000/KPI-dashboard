package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class CatGraphKpiOriginTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatGraphKpiOrigin.class);
        CatGraphKpiOrigin catGraphKpiOrigin1 = new CatGraphKpiOrigin();
        catGraphKpiOrigin1.setId(1L);
        CatGraphKpiOrigin catGraphKpiOrigin2 = new CatGraphKpiOrigin();
        catGraphKpiOrigin2.setId(catGraphKpiOrigin1.getId());
        assertThat(catGraphKpiOrigin1).isEqualTo(catGraphKpiOrigin2);
        catGraphKpiOrigin2.setId(2L);
        assertThat(catGraphKpiOrigin1).isNotEqualTo(catGraphKpiOrigin2);
        catGraphKpiOrigin1.setId(null);
        assertThat(catGraphKpiOrigin1).isNotEqualTo(catGraphKpiOrigin2);
    }
}
