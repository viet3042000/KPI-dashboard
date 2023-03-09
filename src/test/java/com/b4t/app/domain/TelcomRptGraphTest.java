package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class TelcomRptGraphTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelcomRptGraph.class);
        TelcomRptGraph telcomRptGraph1 = new TelcomRptGraph();
        telcomRptGraph1.setId(1L);
        TelcomRptGraph telcomRptGraph2 = new TelcomRptGraph();
        telcomRptGraph2.setId(telcomRptGraph1.getId());
        assertThat(telcomRptGraph1).isEqualTo(telcomRptGraph2);
        telcomRptGraph2.setId(2L);
        assertThat(telcomRptGraph1).isNotEqualTo(telcomRptGraph2);
        telcomRptGraph1.setId(null);
        assertThat(telcomRptGraph1).isNotEqualTo(telcomRptGraph2);
    }
}
