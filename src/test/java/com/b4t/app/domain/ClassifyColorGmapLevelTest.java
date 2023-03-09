package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ClassifyColorGmapLevelTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassifyColorGmapLevel.class);
        ClassifyColorGmapLevel classifyColorGmapLevel1 = new ClassifyColorGmapLevel();
        classifyColorGmapLevel1.setId(1L);
        ClassifyColorGmapLevel classifyColorGmapLevel2 = new ClassifyColorGmapLevel();
        classifyColorGmapLevel2.setId(classifyColorGmapLevel1.getId());
        assertThat(classifyColorGmapLevel1).isEqualTo(classifyColorGmapLevel2);
        classifyColorGmapLevel2.setId(2L);
        assertThat(classifyColorGmapLevel1).isNotEqualTo(classifyColorGmapLevel2);
        classifyColorGmapLevel1.setId(null);
        assertThat(classifyColorGmapLevel1).isNotEqualTo(classifyColorGmapLevel2);
    }
}
