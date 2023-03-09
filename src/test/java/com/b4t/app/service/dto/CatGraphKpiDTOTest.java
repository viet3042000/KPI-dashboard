package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class CatGraphKpiDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatGraphKpiDTO.class);
        CatGraphKpiDTO catGraphKpiDTO1 = new CatGraphKpiDTO();
        catGraphKpiDTO1.setId(1L);
        CatGraphKpiDTO catGraphKpiDTO2 = new CatGraphKpiDTO();
        assertThat(catGraphKpiDTO1).isNotEqualTo(catGraphKpiDTO2);
        catGraphKpiDTO2.setId(catGraphKpiDTO1.getId());
        assertThat(catGraphKpiDTO1).isEqualTo(catGraphKpiDTO2);
        catGraphKpiDTO2.setId(2L);
        assertThat(catGraphKpiDTO1).isNotEqualTo(catGraphKpiDTO2);
        catGraphKpiDTO1.setId(null);
        assertThat(catGraphKpiDTO1).isNotEqualTo(catGraphKpiDTO2);
    }
}
