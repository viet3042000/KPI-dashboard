package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class SecurRptGraphDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecurRptGraphDTO.class);
        SecurRptGraphDTO securRptGraphDTO1 = new SecurRptGraphDTO();
        securRptGraphDTO1.setId(1L);
        SecurRptGraphDTO securRptGraphDTO2 = new SecurRptGraphDTO();
        assertThat(securRptGraphDTO1).isNotEqualTo(securRptGraphDTO2);
        securRptGraphDTO2.setId(securRptGraphDTO1.getId());
        assertThat(securRptGraphDTO1).isEqualTo(securRptGraphDTO2);
        securRptGraphDTO2.setId(2L);
        assertThat(securRptGraphDTO1).isNotEqualTo(securRptGraphDTO2);
        securRptGraphDTO1.setId(null);
        assertThat(securRptGraphDTO1).isNotEqualTo(securRptGraphDTO2);
    }
}
