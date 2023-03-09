package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class RpReportTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpReport.class);
        RpReport rpReport1 = new RpReport();
        rpReport1.setId(1L);
        RpReport rpReport2 = new RpReport();
        rpReport2.setId(rpReport1.getId());
        assertThat(rpReport1).isEqualTo(rpReport2);
        rpReport2.setId(2L);
        assertThat(rpReport1).isNotEqualTo(rpReport2);
        rpReport1.setId(null);
        assertThat(rpReport1).isNotEqualTo(rpReport2);
    }
}
