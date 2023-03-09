package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigInputKpiQueryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigInputKpiQuery.class);
        ConfigInputKpiQuery configInputKpiQuery1 = new ConfigInputKpiQuery();
        configInputKpiQuery1.setId(1L);
        ConfigInputKpiQuery configInputKpiQuery2 = new ConfigInputKpiQuery();
        configInputKpiQuery2.setId(configInputKpiQuery1.getId());
        assertThat(configInputKpiQuery1).isEqualTo(configInputKpiQuery2);
        configInputKpiQuery2.setId(2L);
        assertThat(configInputKpiQuery1).isNotEqualTo(configInputKpiQuery2);
        configInputKpiQuery1.setId(null);
        assertThat(configInputKpiQuery1).isNotEqualTo(configInputKpiQuery2);
    }
}
