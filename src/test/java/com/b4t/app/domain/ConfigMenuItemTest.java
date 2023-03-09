package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigMenuItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigMenuItem.class);
        ConfigMenuItem configMenuItem1 = new ConfigMenuItem();
        configMenuItem1.setId(1L);
        ConfigMenuItem configMenuItem2 = new ConfigMenuItem();
        configMenuItem2.setId(configMenuItem1.getId());
        assertThat(configMenuItem1).isEqualTo(configMenuItem2);
        configMenuItem2.setId(2L);
        assertThat(configMenuItem1).isNotEqualTo(configMenuItem2);
        configMenuItem1.setId(null);
        assertThat(configMenuItem1).isNotEqualTo(configMenuItem2);
    }
}
