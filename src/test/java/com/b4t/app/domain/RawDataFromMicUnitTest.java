package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class RawDataFromMicUnitTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RawDataFromMicUnit.class);
        RawDataFromMicUnit rawDataFromMicUnit1 = new RawDataFromMicUnit();
        rawDataFromMicUnit1.setId(1L);
        RawDataFromMicUnit rawDataFromMicUnit2 = new RawDataFromMicUnit();
        rawDataFromMicUnit2.setId(rawDataFromMicUnit1.getId());
        assertThat(rawDataFromMicUnit1).isEqualTo(rawDataFromMicUnit2);
        rawDataFromMicUnit2.setId(2L);
        assertThat(rawDataFromMicUnit1).isNotEqualTo(rawDataFromMicUnit2);
        rawDataFromMicUnit1.setId(null);
        assertThat(rawDataFromMicUnit1).isNotEqualTo(rawDataFromMicUnit2);
    }
}
