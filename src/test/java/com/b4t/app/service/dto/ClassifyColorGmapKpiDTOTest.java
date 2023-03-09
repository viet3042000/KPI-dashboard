package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ClassifyColorGmapKpiDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassifyColorGmapKpiDTO.class);
        ClassifyColorGmapKpiDTO classifyColorGmapKpiDTO1 = new ClassifyColorGmapKpiDTO();
        classifyColorGmapKpiDTO1.setId(1L);
        ClassifyColorGmapKpiDTO classifyColorGmapKpiDTO2 = new ClassifyColorGmapKpiDTO();
        assertThat(classifyColorGmapKpiDTO1).isNotEqualTo(classifyColorGmapKpiDTO2);
        classifyColorGmapKpiDTO2.setId(classifyColorGmapKpiDTO1.getId());
        assertThat(classifyColorGmapKpiDTO1).isEqualTo(classifyColorGmapKpiDTO2);
        classifyColorGmapKpiDTO2.setId(2L);
        assertThat(classifyColorGmapKpiDTO1).isNotEqualTo(classifyColorGmapKpiDTO2);
        classifyColorGmapKpiDTO1.setId(null);
        assertThat(classifyColorGmapKpiDTO1).isNotEqualTo(classifyColorGmapKpiDTO2);
    }
}
