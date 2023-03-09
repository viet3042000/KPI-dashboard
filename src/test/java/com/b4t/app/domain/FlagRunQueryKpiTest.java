package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class FlagRunQueryKpiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlagRunQueryKpi.class);
        FlagRunQueryKpi flagRunQueryKpi1 = new FlagRunQueryKpi();
        flagRunQueryKpi1.setId(1L);
        FlagRunQueryKpi flagRunQueryKpi2 = new FlagRunQueryKpi();
        flagRunQueryKpi2.setId(flagRunQueryKpi1.getId());
        assertThat(flagRunQueryKpi1).isEqualTo(flagRunQueryKpi2);
        flagRunQueryKpi2.setId(2L);
        assertThat(flagRunQueryKpi1).isNotEqualTo(flagRunQueryKpi2);
        flagRunQueryKpi1.setId(null);
        assertThat(flagRunQueryKpi1).isNotEqualTo(flagRunQueryKpi2);
    }
}
