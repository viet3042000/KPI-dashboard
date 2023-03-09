package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ClassifyColorGmapTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassifyColorGmap.class);
        ClassifyColorGmap classifyColorGmap1 = new ClassifyColorGmap();
        classifyColorGmap1.setId(1L);
        ClassifyColorGmap classifyColorGmap2 = new ClassifyColorGmap();
        classifyColorGmap2.setId(classifyColorGmap1.getId());
        assertThat(classifyColorGmap1).isEqualTo(classifyColorGmap2);
        classifyColorGmap2.setId(2L);
        assertThat(classifyColorGmap1).isNotEqualTo(classifyColorGmap2);
        classifyColorGmap1.setId(null);
        assertThat(classifyColorGmap1).isNotEqualTo(classifyColorGmap2);
    }
}
