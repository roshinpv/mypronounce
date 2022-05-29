package com.wf.pronounce.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.wf.pronounce.domain.Pronounce} entity.
 */
public class PronounceDTO implements Serializable {

    private Long id;

    private String login;

    private String firstName;

    private String lastName;

    private String preferredName;

    private String country;

    private String language;

    @Lob
    private byte[] pronunciation;

    private String pronunciationContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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
            ", login='" + getLogin() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", preferredName='" + getPreferredName() + "'" +
            ", country='" + getCountry() + "'" +
            ", language='" + getLanguage() + "'" +
            ", pronunciation='" + getPronunciation() + "'" +
            "}";
    }
}
