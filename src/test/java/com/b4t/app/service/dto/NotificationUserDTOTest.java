package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class NotificationUserDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationUserDTO.class);
        NotificationUserDTO notificationUserDTO1 = new NotificationUserDTO();
        notificationUserDTO1.setId(1L);
        NotificationUserDTO notificationUserDTO2 = new NotificationUserDTO();
        assertThat(notificationUserDTO1).isNotEqualTo(notificationUserDTO2);
        notificationUserDTO2.setId(notificationUserDTO1.getId());
        assertThat(notificationUserDTO1).isEqualTo(notificationUserDTO2);
        notificationUserDTO2.setId(2L);
        assertThat(notificationUserDTO1).isNotEqualTo(notificationUserDTO2);
        notificationUserDTO1.setId(null);
        assertThat(notificationUserDTO1).isNotEqualTo(notificationUserDTO2);
    }
}
