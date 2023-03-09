package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigScreenTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigScreen.class);
        ConfigScreen configScreen1 = new ConfigScreen();
        configScreen1.setId(1L);
        ConfigScreen configScreen2 = new ConfigScreen();
        configScreen2.setId(configScreen1.getId());
        assertThat(configScreen1).isEqualTo(configScreen2);
        configScreen2.setId(2L);
        assertThat(configScreen1).isNotEqualTo(configScreen2);
        configScreen1.setId(null);
        assertThat(configScreen1).isNotEqualTo(configScreen2);
    }
}
