package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class AreaGmapDataDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AreaGmapDataDTO.class);
        AreaGmapDataDTO areaGmapDataDTO1 = new AreaGmapDataDTO();
        areaGmapDataDTO1.setId(1L);
        AreaGmapDataDTO areaGmapDataDTO2 = new AreaGmapDataDTO();
        assertThat(areaGmapDataDTO1).isNotEqualTo(areaGmapDataDTO2);
        areaGmapDataDTO2.setId(areaGmapDataDTO1.getId());
        assertThat(areaGmapDataDTO1).isEqualTo(areaGmapDataDTO2);
        areaGmapDataDTO2.setId(2L);
        assertThat(areaGmapDataDTO1).isNotEqualTo(areaGmapDataDTO2);
        areaGmapDataDTO1.setId(null);
        assertThat(areaGmapDataDTO1).isNotEqualTo(areaGmapDataDTO2);
    }
}
