package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class EmailsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Emails.class);
        Emails emails1 = new Emails();
        emails1.setId(1L);
        Emails emails2 = new Emails();
        emails2.setId(emails1.getId());
        assertThat(emails1).isEqualTo(emails2);
        emails2.setId(2L);
        assertThat(emails1).isNotEqualTo(emails2);
        emails1.setId(null);
        assertThat(emails1).isNotEqualTo(emails2);
    }
}
