package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class CatGraphKpiOriginDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatGraphKpiOriginDTO.class);
        CatGraphKpiOriginDTO catGraphKpiOriginDTO1 = new CatGraphKpiOriginDTO();
        catGraphKpiOriginDTO1.setId(1L);
        CatGraphKpiOriginDTO catGraphKpiOriginDTO2 = new CatGraphKpiOriginDTO();
        assertThat(catGraphKpiOriginDTO1).isNotEqualTo(catGraphKpiOriginDTO2);
        catGraphKpiOriginDTO2.setId(catGraphKpiOriginDTO1.getId());
        assertThat(catGraphKpiOriginDTO1).isEqualTo(catGraphKpiOriginDTO2);
        catGraphKpiOriginDTO2.setId(2L);
        assertThat(catGraphKpiOriginDTO1).isNotEqualTo(catGraphKpiOriginDTO2);
        catGraphKpiOriginDTO1.setId(null);
        assertThat(catGraphKpiOriginDTO1).isNotEqualTo(catGraphKpiOriginDTO2);
    }
}
