package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigReportColumnTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigReportColumn.class);
        ConfigReportColumn configReportColumn1 = new ConfigReportColumn();
        configReportColumn1.setId(1L);
        ConfigReportColumn configReportColumn2 = new ConfigReportColumn();
        configReportColumn2.setId(configReportColumn1.getId());
        assertThat(configReportColumn1).isEqualTo(configReportColumn2);
        configReportColumn2.setId(2L);
        assertThat(configReportColumn1).isNotEqualTo(configReportColumn2);
        configReportColumn1.setId(null);
        assertThat(configReportColumn1).isNotEqualTo(configReportColumn2);
    }
}
