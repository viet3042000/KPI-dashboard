package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class CatGroupChartDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatGroupChartDTO.class);
        CatGroupChartDTO catGroupChartDTO1 = new CatGroupChartDTO();
        catGroupChartDTO1.setId(1L);
        CatGroupChartDTO catGroupChartDTO2 = new CatGroupChartDTO();
        assertThat(catGroupChartDTO1).isNotEqualTo(catGroupChartDTO2);
        catGroupChartDTO2.setId(catGroupChartDTO1.getId());
        assertThat(catGroupChartDTO1).isEqualTo(catGroupChartDTO2);
        catGroupChartDTO2.setId(2L);
        assertThat(catGroupChartDTO1).isNotEqualTo(catGroupChartDTO2);
        catGroupChartDTO1.setId(null);
        assertThat(catGroupChartDTO1).isNotEqualTo(catGroupChartDTO2);
    }
}
