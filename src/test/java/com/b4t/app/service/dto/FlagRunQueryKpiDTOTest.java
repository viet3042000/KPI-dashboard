package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class FlagRunQueryKpiDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlagRunQueryKpiDTO.class);
        FlagRunQueryKpiDTO flagRunQueryKpiDTO1 = new FlagRunQueryKpiDTO();
        flagRunQueryKpiDTO1.setId(1L);
        FlagRunQueryKpiDTO flagRunQueryKpiDTO2 = new FlagRunQueryKpiDTO();
        assertThat(flagRunQueryKpiDTO1).isNotEqualTo(flagRunQueryKpiDTO2);
        flagRunQueryKpiDTO2.setId(flagRunQueryKpiDTO1.getId());
        assertThat(flagRunQueryKpiDTO1).isEqualTo(flagRunQueryKpiDTO2);
        flagRunQueryKpiDTO2.setId(2L);
        assertThat(flagRunQueryKpiDTO1).isNotEqualTo(flagRunQueryKpiDTO2);
        flagRunQueryKpiDTO1.setId(null);
        assertThat(flagRunQueryKpiDTO1).isNotEqualTo(flagRunQueryKpiDTO2);
    }
}
