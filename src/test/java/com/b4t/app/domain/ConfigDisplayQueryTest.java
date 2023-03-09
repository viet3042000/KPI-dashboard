package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigDisplayQueryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigDisplayQuery.class);
        ConfigDisplayQuery configDisplayQuery1 = new ConfigDisplayQuery();
        configDisplayQuery1.setId(1L);
        ConfigDisplayQuery configDisplayQuery2 = new ConfigDisplayQuery();
        configDisplayQuery2.setId(configDisplayQuery1.getId());
        assertThat(configDisplayQuery1).isEqualTo(configDisplayQuery2);
        configDisplayQuery2.setId(2L);
        assertThat(configDisplayQuery1).isNotEqualTo(configDisplayQuery2);
        configDisplayQuery1.setId(null);
        assertThat(configDisplayQuery1).isNotEqualTo(configDisplayQuery2);
    }
}
