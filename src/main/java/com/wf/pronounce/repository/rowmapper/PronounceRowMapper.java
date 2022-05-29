package com.wf.pronounce.repository.rowmapper;

import com.wf.pronounce.domain.Pronounce;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Pronounce}, with proper type conversions.
 */
@Service
public class PronounceRowMapper implements BiFunction<Row, String, Pronounce> {

    private final ColumnConverter converter;

    public PronounceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Pronounce} stored in the database.
     */
    @Override
    public Pronounce apply(Row row, String prefix) {
        Pronounce entity = new Pronounce();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPreferredName(converter.fromRow(row, prefix + "_preferred_name", String.class));
        entity.setPronunciationContentType(converter.fromRow(row, prefix + "_pronunciation_content_type", String.class));
        entity.setPronunciation(converter.fromRow(row, prefix + "_pronunciation", byte[].class));
        return entity;
    }
}
