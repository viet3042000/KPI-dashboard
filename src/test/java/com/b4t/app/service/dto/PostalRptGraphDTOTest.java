package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class PostalRptGraphDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostalRptGraphDTO.class);
        PostalRptGraphDTO postalRptGraphDTO1 = new PostalRptGraphDTO();
        postalRptGraphDTO1.setId(1L);
        PostalRptGraphDTO postalRptGraphDTO2 = new PostalRptGraphDTO();
        assertThat(postalRptGraphDTO1).isNotEqualTo(postalRptGraphDTO2);
        postalRptGraphDTO2.setId(postalRptGraphDTO1.getId());
        assertThat(postalRptGraphDTO1).isEqualTo(postalRptGraphDTO2);
        postalRptGraphDTO2.setId(2L);
        assertThat(postalRptGraphDTO1).isNotEqualTo(postalRptGraphDTO2);
        postalRptGraphDTO1.setId(null);
        assertThat(postalRptGraphDTO1).isNotEqualTo(postalRptGraphDTO2);
    }
}
