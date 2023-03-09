package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class RawDataFromMicUnitDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RawDataFromMicUnitDTO.class);
        RawDataFromMicUnitDTO rawDataFromMicUnitDTO1 = new RawDataFromMicUnitDTO();
        rawDataFromMicUnitDTO1.setId(1L);
        RawDataFromMicUnitDTO rawDataFromMicUnitDTO2 = new RawDataFromMicUnitDTO();
        assertThat(rawDataFromMicUnitDTO1).isNotEqualTo(rawDataFromMicUnitDTO2);
        rawDataFromMicUnitDTO2.setId(rawDataFromMicUnitDTO1.getId());
        assertThat(rawDataFromMicUnitDTO1).isEqualTo(rawDataFromMicUnitDTO2);
        rawDataFromMicUnitDTO2.setId(2L);
        assertThat(rawDataFromMicUnitDTO1).isNotEqualTo(rawDataFromMicUnitDTO2);
        rawDataFromMicUnitDTO1.setId(null);
        assertThat(rawDataFromMicUnitDTO1).isNotEqualTo(rawDataFromMicUnitDTO2);
    }
}
