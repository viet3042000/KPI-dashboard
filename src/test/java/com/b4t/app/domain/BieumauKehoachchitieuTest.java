package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class BieumauKehoachchitieuTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BieumauKehoachchitieu.class);
        BieumauKehoachchitieu bieumauKehoachchitieu1 = new BieumauKehoachchitieu();
        bieumauKehoachchitieu1.setId(1L);
        BieumauKehoachchitieu bieumauKehoachchitieu2 = new BieumauKehoachchitieu();
        bieumauKehoachchitieu2.setId(bieumauKehoachchitieu1.getId());
        assertThat(bieumauKehoachchitieu1).isEqualTo(bieumauKehoachchitieu2);
        bieumauKehoachchitieu2.setId(2L);
        assertThat(bieumauKehoachchitieu1).isNotEqualTo(bieumauKehoachchitieu2);
        bieumauKehoachchitieu1.setId(null);
        assertThat(bieumauKehoachchitieu1).isNotEqualTo(bieumauKehoachchitieu2);
    }
}
