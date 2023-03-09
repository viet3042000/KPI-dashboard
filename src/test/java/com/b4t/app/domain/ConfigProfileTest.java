package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigProfileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigProfile.class);
        ConfigProfile configProfile1 = new ConfigProfile();
        configProfile1.setId(1L);
        ConfigProfile configProfile2 = new ConfigProfile();
        configProfile2.setId(configProfile1.getId());
        assertThat(configProfile1).isEqualTo(configProfile2);
        configProfile2.setId(2L);
        assertThat(configProfile1).isNotEqualTo(configProfile2);
        configProfile1.setId(null);
        assertThat(configProfile1).isNotEqualTo(configProfile2);
    }
}
