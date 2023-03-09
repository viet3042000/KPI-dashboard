package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class EmailsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailsDTO.class);
        EmailsDTO emailsDTO1 = new EmailsDTO();
        emailsDTO1.setId(1L);
        EmailsDTO emailsDTO2 = new EmailsDTO();
        assertThat(emailsDTO1).isNotEqualTo(emailsDTO2);
        emailsDTO2.setId(emailsDTO1.getId());
        assertThat(emailsDTO1).isEqualTo(emailsDTO2);
        emailsDTO2.setId(2L);
        assertThat(emailsDTO1).isNotEqualTo(emailsDTO2);
        emailsDTO1.setId(null);
        assertThat(emailsDTO1).isNotEqualTo(emailsDTO2);
    }
}
