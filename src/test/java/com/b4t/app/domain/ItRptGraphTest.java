package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ItRptGraphTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItRptGraph.class);
        ItRptGraph itRptGraph1 = new ItRptGraph();
        itRptGraph1.setId(1L);
        ItRptGraph itRptGraph2 = new ItRptGraph();
        itRptGraph2.setId(itRptGraph1.getId());
        assertThat(itRptGraph1).isEqualTo(itRptGraph2);
        itRptGraph2.setId(2L);
        assertThat(itRptGraph1).isNotEqualTo(itRptGraph2);
        itRptGraph1.setId(null);
        assertThat(itRptGraph1).isNotEqualTo(itRptGraph2);
    }
}
