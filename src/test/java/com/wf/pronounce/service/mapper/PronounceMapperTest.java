package com.wf.pronounce.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PronounceMapperTest {

    private PronounceMapper pronounceMapper;

    @BeforeEach
    public void setUp() {
        pronounceMapper = new PronounceMapperImpl();
    }
}
