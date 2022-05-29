package com.wf.pronounce.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.wf.pronounce.domain.Pronounce} entity.
 */
public class PronounceDTO implements Serializable {

    private Long id;

    private String preferredName;

    @Lob
    private byte[] pronunciation;

    private String pronunciationContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public byte[] getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(byte[] pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getPronunciationContentType() {
        return pronunciationContentType;
    }

    public void setPronunciationContentType(String pronunciationContentType) {
        this.pronunciationContentType = pronunciationContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PronounceDTO)) {
            return false;
        }

        PronounceDTO pronounceDTO = (PronounceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pronounceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PronounceDTO{" +
            "id=" + getId() +
            ", preferredName='" + getPreferredName() + "'" +
            ", pronunciation='" + getPronunciation() + "'" +
            "}";
    }
}
