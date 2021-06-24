package com.epam.esm.domain.tag;

import com.epam.esm.domain.giftcertificate.GiftCertificate;

import java.util.Objects;
import java.util.Set;

public class Tag {
    private int id;
    private String name;
    private Set<GiftCertificate> certificates;

    public Tag(int id, String name, Set<GiftCertificate> certificates) {
        this.id = id;
        this.name = name;
        this.certificates = certificates;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<GiftCertificate> getCertificates() {
        return certificates;
    }

    public void addCertificate(GiftCertificate certificate) {
        this.certificates.add(certificate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id == tag.id && name.equals(tag.name) && Objects.equals(certificates, tag.certificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, certificates);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", certificates=" + certificates +
                '}';
    }
}
