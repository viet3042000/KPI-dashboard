package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class CatKpiSynonymTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatKpiSynonym.class);
        CatKpiSynonym catKpiSynonym1 = new CatKpiSynonym();
        catKpiSynonym1.setId(1L);
        CatKpiSynonym catKpiSynonym2 = new CatKpiSynonym();
        catKpiSynonym2.setId(catKpiSynonym1.getId());
        assertThat(catKpiSynonym1).isEqualTo(catKpiSynonym2);
        catKpiSynonym2.setId(2L);
        assertThat(catKpiSynonym1).isNotEqualTo(catKpiSynonym2);
        catKpiSynonym1.setId(null);
        assertThat(catKpiSynonym1).isNotEqualTo(catKpiSynonym2);
    }
}
