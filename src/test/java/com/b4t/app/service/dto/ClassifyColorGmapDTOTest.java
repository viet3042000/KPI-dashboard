package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ClassifyColorGmapDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassifyColorGmapDTO.class);
        ClassifyColorGmapDTO classifyColorGmapDTO1 = new ClassifyColorGmapDTO();
        classifyColorGmapDTO1.setId(1L);
        ClassifyColorGmapDTO classifyColorGmapDTO2 = new ClassifyColorGmapDTO();
        assertThat(classifyColorGmapDTO1).isNotEqualTo(classifyColorGmapDTO2);
        classifyColorGmapDTO2.setId(classifyColorGmapDTO1.getId());
        assertThat(classifyColorGmapDTO1).isEqualTo(classifyColorGmapDTO2);
        classifyColorGmapDTO2.setId(2L);
        assertThat(classifyColorGmapDTO1).isNotEqualTo(classifyColorGmapDTO2);
        classifyColorGmapDTO1.setId(null);
        assertThat(classifyColorGmapDTO1).isNotEqualTo(classifyColorGmapDTO2);
    }
}
