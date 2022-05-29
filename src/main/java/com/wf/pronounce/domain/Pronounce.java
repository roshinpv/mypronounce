package com.wf.pronounce.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Pronounce.
 */
@Table("pronounce")
public class Pronounce implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("preferred_name")
    private String preferredName;

    @Column("pronunciation")
    private byte[] pronunciation;

    @Column("pronunciation_content_type")
    private String pronunciationContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pronounce id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreferredName() {
        return this.preferredName;
    }

    public Pronounce preferredName(String preferredName) {
        this.setPreferredName(preferredName);
        return this;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public byte[] getPronunciation() {
        return this.pronunciation;
    }

    public Pronounce pronunciation(byte[] pronunciation) {
        this.setPronunciation(pronunciation);
        return this;
    }

    public void setPronunciation(byte[] pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getPronunciationContentType() {
        return this.pronunciationContentType;
    }

    public Pronounce pronunciationContentType(String pronunciationContentType) {
        this.pronunciationContentType = pronunciationContentType;
        return this;
    }

    public void setPronunciationContentType(String pronunciationContentType) {
        this.pronunciationContentType = pronunciationContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pronounce)) {
            return false;
        }
        return id != null && id.equals(((Pronounce) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pronounce{" +
            "id=" + getId() +
            ", preferredName='" + getPreferredName() + "'" +
            ", pronunciation='" + getPronunciation() + "'" +
            ", pronunciationContentType='" + getPronunciationContentType() + "'" +
            "}";
    }
}
