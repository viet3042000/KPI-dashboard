package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class CatKpiSynonymDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatKpiSynonymDTO.class);
        CatKpiSynonymDTO catKpiSynonymDTO1 = new CatKpiSynonymDTO();
        catKpiSynonymDTO1.setId(1L);
        CatKpiSynonymDTO catKpiSynonymDTO2 = new CatKpiSynonymDTO();
        assertThat(catKpiSynonymDTO1).isNotEqualTo(catKpiSynonymDTO2);
        catKpiSynonymDTO2.setId(catKpiSynonymDTO1.getId());
        assertThat(catKpiSynonymDTO1).isEqualTo(catKpiSynonymDTO2);
        catKpiSynonymDTO2.setId(2L);
        assertThat(catKpiSynonymDTO1).isNotEqualTo(catKpiSynonymDTO2);
        catKpiSynonymDTO1.setId(null);
        assertThat(catKpiSynonymDTO1).isNotEqualTo(catKpiSynonymDTO2);
    }
}
