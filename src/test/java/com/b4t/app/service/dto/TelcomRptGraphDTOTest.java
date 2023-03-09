package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class TelcomRptGraphDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelcomRptGraphDTO.class);
        TelcomRptGraphDTO telcomRptGraphDTO1 = new TelcomRptGraphDTO();
        telcomRptGraphDTO1.setId(1L);
        TelcomRptGraphDTO telcomRptGraphDTO2 = new TelcomRptGraphDTO();
        assertThat(telcomRptGraphDTO1).isNotEqualTo(telcomRptGraphDTO2);
        telcomRptGraphDTO2.setId(telcomRptGraphDTO1.getId());
        assertThat(telcomRptGraphDTO1).isEqualTo(telcomRptGraphDTO2);
        telcomRptGraphDTO2.setId(2L);
        assertThat(telcomRptGraphDTO1).isNotEqualTo(telcomRptGraphDTO2);
        telcomRptGraphDTO1.setId(null);
        assertThat(telcomRptGraphDTO1).isNotEqualTo(telcomRptGraphDTO2);
    }
}
