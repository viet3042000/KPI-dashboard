package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class AreaGmapDataTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AreaGmapData.class);
        AreaGmapData areaGmapData1 = new AreaGmapData();
        areaGmapData1.setId(1L);
        AreaGmapData areaGmapData2 = new AreaGmapData();
        areaGmapData2.setId(areaGmapData1.getId());
        assertThat(areaGmapData1).isEqualTo(areaGmapData2);
        areaGmapData2.setId(2L);
        assertThat(areaGmapData1).isNotEqualTo(areaGmapData2);
        areaGmapData1.setId(null);
        assertThat(areaGmapData1).isNotEqualTo(areaGmapData2);
    }
}
