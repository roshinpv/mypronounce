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

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("preferred_name")
    private String preferredName;

    @Column("country")
    private String country;

    @Column("language")
    private String language;

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

    public String getFirstName() {
        return this.firstName;
    }

    public Pronounce firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Pronounce lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getCountry() {
        return this.country;
    }

    public Pronounce country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return this.language;
    }

    public Pronounce language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
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
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", preferredName='" + getPreferredName() + "'" +
            ", country='" + getCountry() + "'" +
            ", language='" + getLanguage() + "'" +
            ", pronunciation='" + getPronunciation() + "'" +
            ", pronunciationContentType='" + getPronunciationContentType() + "'" +
            "}";
    }
}
