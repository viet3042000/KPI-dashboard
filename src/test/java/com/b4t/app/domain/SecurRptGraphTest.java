package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class SecurRptGraphTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecurRptGraph.class);
        SecurRptGraph securRptGraph1 = new SecurRptGraph();
        securRptGraph1.setId(1L);
        SecurRptGraph securRptGraph2 = new SecurRptGraph();
        securRptGraph2.setId(securRptGraph1.getId());
        assertThat(securRptGraph1).isEqualTo(securRptGraph2);
        securRptGraph2.setId(2L);
        assertThat(securRptGraph1).isNotEqualTo(securRptGraph2);
        securRptGraph1.setId(null);
        assertThat(securRptGraph1).isNotEqualTo(securRptGraph2);
    }
}
