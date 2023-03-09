package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigMapKpiQueryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigMapKpiQuery.class);
        ConfigMapKpiQuery configMapKpiQuery1 = new ConfigMapKpiQuery();
        configMapKpiQuery1.setId(1L);
        ConfigMapKpiQuery configMapKpiQuery2 = new ConfigMapKpiQuery();
        configMapKpiQuery2.setId(configMapKpiQuery1.getId());
        assertThat(configMapKpiQuery1).isEqualTo(configMapKpiQuery2);
        configMapKpiQuery2.setId(2L);
        assertThat(configMapKpiQuery1).isNotEqualTo(configMapKpiQuery2);
        configMapKpiQuery1.setId(null);
        assertThat(configMapKpiQuery1).isNotEqualTo(configMapKpiQuery2);
    }
}
