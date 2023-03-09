package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ItRptGraphDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItRptGraphDTO.class);
        ItRptGraphDTO itRptGraphDTO1 = new ItRptGraphDTO();
        itRptGraphDTO1.setId(1L);
        ItRptGraphDTO itRptGraphDTO2 = new ItRptGraphDTO();
        assertThat(itRptGraphDTO1).isNotEqualTo(itRptGraphDTO2);
        itRptGraphDTO2.setId(itRptGraphDTO1.getId());
        assertThat(itRptGraphDTO1).isEqualTo(itRptGraphDTO2);
        itRptGraphDTO2.setId(2L);
        assertThat(itRptGraphDTO1).isNotEqualTo(itRptGraphDTO2);
        itRptGraphDTO1.setId(null);
        assertThat(itRptGraphDTO1).isNotEqualTo(itRptGraphDTO2);
    }
}
