package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigMenuTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigMenu.class);
        ConfigMenu configMenu1 = new ConfigMenu();
        configMenu1.setId(1L);
        ConfigMenu configMenu2 = new ConfigMenu();
        configMenu2.setId(configMenu1.getId());
        assertThat(configMenu1).isEqualTo(configMenu2);
        configMenu2.setId(2L);
        assertThat(configMenu1).isNotEqualTo(configMenu2);
        configMenu1.setId(null);
        assertThat(configMenu1).isNotEqualTo(configMenu2);
    }
}
