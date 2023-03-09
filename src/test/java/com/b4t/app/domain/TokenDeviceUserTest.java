package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class TokenDeviceUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TokenDeviceUser.class);
        TokenDeviceUser tokenDeviceUser1 = new TokenDeviceUser();
        tokenDeviceUser1.setId(1L);
        TokenDeviceUser tokenDeviceUser2 = new TokenDeviceUser();
        tokenDeviceUser2.setId(tokenDeviceUser1.getId());
        assertThat(tokenDeviceUser1).isEqualTo(tokenDeviceUser2);
        tokenDeviceUser2.setId(2L);
        assertThat(tokenDeviceUser1).isNotEqualTo(tokenDeviceUser2);
        tokenDeviceUser1.setId(null);
        assertThat(tokenDeviceUser1).isNotEqualTo(tokenDeviceUser2);
    }
}
