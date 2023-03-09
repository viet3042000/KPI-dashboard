package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ClassifyColorGmapKpiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassifyColorGmapKpi.class);
        ClassifyColorGmapKpi classifyColorGmapKpi1 = new ClassifyColorGmapKpi();
        classifyColorGmapKpi1.setId(1L);
        ClassifyColorGmapKpi classifyColorGmapKpi2 = new ClassifyColorGmapKpi();
        classifyColorGmapKpi2.setId(classifyColorGmapKpi1.getId());
        assertThat(classifyColorGmapKpi1).isEqualTo(classifyColorGmapKpi2);
        classifyColorGmapKpi2.setId(2L);
        assertThat(classifyColorGmapKpi1).isNotEqualTo(classifyColorGmapKpi2);
        classifyColorGmapKpi1.setId(null);
        assertThat(classifyColorGmapKpi1).isNotEqualTo(classifyColorGmapKpi2);
    }
}
