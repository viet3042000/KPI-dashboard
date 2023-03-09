package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class NotificationUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationUser.class);
        NotificationUser notificationUser1 = new NotificationUser();
        notificationUser1.setId(1L);
        NotificationUser notificationUser2 = new NotificationUser();
        notificationUser2.setId(notificationUser1.getId());
        assertThat(notificationUser1).isEqualTo(notificationUser2);
        notificationUser2.setId(2L);
        assertThat(notificationUser1).isNotEqualTo(notificationUser2);
        notificationUser1.setId(null);
        assertThat(notificationUser1).isNotEqualTo(notificationUser2);
    }
}
