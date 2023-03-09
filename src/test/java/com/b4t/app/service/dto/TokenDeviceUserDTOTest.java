package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class TokenDeviceUserDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TokenDeviceUserDTO.class);
        TokenDeviceUserDTO tokenDeviceUserDTO1 = new TokenDeviceUserDTO();
        tokenDeviceUserDTO1.setId(1L);
        TokenDeviceUserDTO tokenDeviceUserDTO2 = new TokenDeviceUserDTO();
        assertThat(tokenDeviceUserDTO1).isNotEqualTo(tokenDeviceUserDTO2);
        tokenDeviceUserDTO2.setId(tokenDeviceUserDTO1.getId());
        assertThat(tokenDeviceUserDTO1).isEqualTo(tokenDeviceUserDTO2);
        tokenDeviceUserDTO2.setId(2L);
        assertThat(tokenDeviceUserDTO1).isNotEqualTo(tokenDeviceUserDTO2);
        tokenDeviceUserDTO1.setId(null);
        assertThat(tokenDeviceUserDTO1).isNotEqualTo(tokenDeviceUserDTO2);
    }
}
