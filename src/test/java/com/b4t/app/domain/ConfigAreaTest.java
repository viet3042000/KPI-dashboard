package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigAreaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigArea.class);
        ConfigArea configArea1 = new ConfigArea();
        configArea1.setId(1L);
        ConfigArea configArea2 = new ConfigArea();
        configArea2.setId(configArea1.getId());
        assertThat(configArea1).isEqualTo(configArea2);
        configArea2.setId(2L);
        assertThat(configArea1).isNotEqualTo(configArea2);
        configArea1.setId(null);
        assertThat(configArea1).isNotEqualTo(configArea2);
    }
}
