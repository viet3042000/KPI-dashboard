package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ClassifyColorGmapLevelDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassifyColorGmapLevelDTO.class);
        ClassifyColorGmapLevelDTO classifyColorGmapLevelDTO1 = new ClassifyColorGmapLevelDTO();
        classifyColorGmapLevelDTO1.setId(1L);
        ClassifyColorGmapLevelDTO classifyColorGmapLevelDTO2 = new ClassifyColorGmapLevelDTO();
        assertThat(classifyColorGmapLevelDTO1).isNotEqualTo(classifyColorGmapLevelDTO2);
        classifyColorGmapLevelDTO2.setId(classifyColorGmapLevelDTO1.getId());
        assertThat(classifyColorGmapLevelDTO1).isEqualTo(classifyColorGmapLevelDTO2);
        classifyColorGmapLevelDTO2.setId(2L);
        assertThat(classifyColorGmapLevelDTO1).isNotEqualTo(classifyColorGmapLevelDTO2);
        classifyColorGmapLevelDTO1.setId(null);
        assertThat(classifyColorGmapLevelDTO1).isNotEqualTo(classifyColorGmapLevelDTO2);
    }
}
