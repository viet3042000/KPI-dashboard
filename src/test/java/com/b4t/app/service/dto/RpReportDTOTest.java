package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class RpReportDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpReportDTO.class);
        RpReportDTO rpReportDTO1 = new RpReportDTO();
        rpReportDTO1.setId(1L);
        RpReportDTO rpReportDTO2 = new RpReportDTO();
        assertThat(rpReportDTO1).isNotEqualTo(rpReportDTO2);
        rpReportDTO2.setId(rpReportDTO1.getId());
        assertThat(rpReportDTO1).isEqualTo(rpReportDTO2);
        rpReportDTO2.setId(2L);
        assertThat(rpReportDTO1).isNotEqualTo(rpReportDTO2);
        rpReportDTO1.setId(null);
        assertThat(rpReportDTO1).isNotEqualTo(rpReportDTO2);
    }
}
