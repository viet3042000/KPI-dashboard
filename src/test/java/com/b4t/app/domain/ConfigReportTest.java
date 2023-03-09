package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigReportTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigReport.class);
        ConfigReport configReport1 = new ConfigReport();
        configReport1.setId(1L);
        ConfigReport configReport2 = new ConfigReport();
        configReport2.setId(configReport1.getId());
        assertThat(configReport1).isEqualTo(configReport2);
        configReport2.setId(2L);
        assertThat(configReport1).isNotEqualTo(configReport2);
        configReport1.setId(null);
        assertThat(configReport1).isNotEqualTo(configReport2);
    }
}
