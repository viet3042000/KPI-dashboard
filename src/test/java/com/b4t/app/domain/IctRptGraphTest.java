package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class IctRptGraphTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IctRptGraph.class);
        IctRptGraph ictRptGraph1 = new IctRptGraph();
        ictRptGraph1.setId(1L);
        IctRptGraph ictRptGraph2 = new IctRptGraph();
        ictRptGraph2.setId(ictRptGraph1.getId());
        assertThat(ictRptGraph1).isEqualTo(ictRptGraph2);
        ictRptGraph2.setId(2L);
        assertThat(ictRptGraph1).isNotEqualTo(ictRptGraph2);
        ictRptGraph1.setId(null);
        assertThat(ictRptGraph1).isNotEqualTo(ictRptGraph2);
    }
}
