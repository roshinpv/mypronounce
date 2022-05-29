package com.wf.pronounce.service.mapper;

import com.wf.pronounce.domain.Pronounce;
import com.wf.pronounce.service.dto.PronounceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pronounce} and its DTO {@link PronounceDTO}.
 */
@Mapper(componentModel = "spring")
public interface PronounceMapper extends EntityMapper<PronounceDTO, Pronounce> {}
