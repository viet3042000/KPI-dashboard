package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class PostalRptGraphTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostalRptGraph.class);
        PostalRptGraph postalRptGraph1 = new PostalRptGraph();
        postalRptGraph1.setId(1L);
        PostalRptGraph postalRptGraph2 = new PostalRptGraph();
        postalRptGraph2.setId(postalRptGraph1.getId());
        assertThat(postalRptGraph1).isEqualTo(postalRptGraph2);
        postalRptGraph2.setId(2L);
        assertThat(postalRptGraph1).isNotEqualTo(postalRptGraph2);
        postalRptGraph1.setId(null);
        assertThat(postalRptGraph1).isNotEqualTo(postalRptGraph2);
    }
}
