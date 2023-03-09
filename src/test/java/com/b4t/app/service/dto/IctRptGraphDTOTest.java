package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class IctRptGraphDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IctRptGraphDTO.class);
        IctRptGraphDTO ictRptGraphDTO1 = new IctRptGraphDTO();
        ictRptGraphDTO1.setId(1L);
        IctRptGraphDTO ictRptGraphDTO2 = new IctRptGraphDTO();
        assertThat(ictRptGraphDTO1).isNotEqualTo(ictRptGraphDTO2);
        ictRptGraphDTO2.setId(ictRptGraphDTO1.getId());
        assertThat(ictRptGraphDTO1).isEqualTo(ictRptGraphDTO2);
        ictRptGraphDTO2.setId(2L);
        assertThat(ictRptGraphDTO1).isNotEqualTo(ictRptGraphDTO2);
        ictRptGraphDTO1.setId(null);
        assertThat(ictRptGraphDTO1).isNotEqualTo(ictRptGraphDTO2);
    }
}
