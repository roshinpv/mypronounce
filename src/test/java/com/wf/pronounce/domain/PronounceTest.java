package com.wf.pronounce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wf.pronounce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PronounceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pronounce.class);
        Pronounce pronounce1 = new Pronounce();
        pronounce1.setId(1L);
        Pronounce pronounce2 = new Pronounce();
        pronounce2.setId(pronounce1.getId());
        assertThat(pronounce1).isEqualTo(pronounce2);
        pronounce2.setId(2L);
        assertThat(pronounce1).isNotEqualTo(pronounce2);
        pronounce1.setId(null);
        assertThat(pronounce1).isNotEqualTo(pronounce2);
    }
}
