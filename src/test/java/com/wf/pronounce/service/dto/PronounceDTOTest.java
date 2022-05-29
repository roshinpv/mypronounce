package com.wf.pronounce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wf.pronounce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PronounceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PronounceDTO.class);
        PronounceDTO pronounceDTO1 = new PronounceDTO();
        pronounceDTO1.setId(1L);
        PronounceDTO pronounceDTO2 = new PronounceDTO();
        assertThat(pronounceDTO1).isNotEqualTo(pronounceDTO2);
        pronounceDTO2.setId(pronounceDTO1.getId());
        assertThat(pronounceDTO1).isEqualTo(pronounceDTO2);
        pronounceDTO2.setId(2L);
        assertThat(pronounceDTO1).isNotEqualTo(pronounceDTO2);
        pronounceDTO1.setId(null);
        assertThat(pronounceDTO1).isNotEqualTo(pronounceDTO2);
    }
}
