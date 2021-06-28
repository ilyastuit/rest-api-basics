package com.epam.esm.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.esm.builder.TagBuilder;
import com.epam.esm.TestEnvironment;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.exceptions.TagNameAlreadyExistException;
import com.epam.esm.repository.tag.TagRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TagRepositoryTest {

    private final TagBuilder tagBuilder = new TagBuilder();

    private static TagRepository tagRepository;
    private static Flyway flyway;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        tagRepository = TestEnvironment.getTagRepository();
        flyway = TestEnvironment.getFlyway();
    }

    @BeforeEach
    public void flyWayMigrations() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void successFindById() {
        Tag originalTag = tagBuilder.build();
        assertEquals(1, tagRepository.findById(1).size());
        Tag fetchedTag = tagRepository.findById(1).get(0);
        assertTags(originalTag, fetchedTag);
    }

    @Test
    void emptyFindOne() {
        assertEquals(0, tagRepository.findById(TagBuilder.NOT_EXIST_TAG_ID).size());
    }

    @Test
    void successFindAll() {
        assertEquals(TagBuilder.ALL_TAGS_COUNT, tagRepository.findAll().size());
    }

    @Test
    void successSave() throws TagNameAlreadyExistException {
        Tag originalTag = tagBuilder.withId(TagBuilder.NOT_EXIST_TAG_ID).withName(TagBuilder.NOT_EXIST_TAG_NAME).build();
        assertEquals(TagBuilder.ALL_TAGS_COUNT + 1, tagRepository.save(originalTag));

        assertEquals(TagBuilder.ALL_TAGS_COUNT + 1, tagRepository.findAll().size());
        Tag fetchedTag = tagRepository.findById(TagBuilder.ALL_TAGS_COUNT + 1).get(0);
        assertTags(originalTag, fetchedTag);
    }

    @Test
    void failSave() {
        assertThrows(TagNameAlreadyExistException.class, () -> {
            tagRepository.save(tagBuilder.withName(TagBuilder.EXIST_TAG_NAME).build());
        });
    }

    @Test
    void successFindByName() {
        Tag originalTag = tagBuilder.build();
        final String tagName = originalTag.getName();
        assertEquals(1, tagRepository.findByName(tagName).size());
        Tag fetchedTag = tagRepository.findByName(tagName).get(0);
        assertTags(originalTag, fetchedTag);
    }

    @Test
    void emptyFindByName() {
        assertEquals(0, tagRepository.findByName(TagBuilder.NOT_EXIST_TAG_NAME).size());
    }

    @Test
    void successFindAssignedTagToCertificate() {
        assertEquals(TagBuilder.TAG_ID_WITH_CERTIFICATE, tagRepository.findAssignedTagToCertificate(TagBuilder.CERTIFICATE_ID_WITH_TAG, TagBuilder.TAG_ID_WITH_CERTIFICATE).get(0).getId());
    }

    @Test
    void emptyFindAssignedTagToCertificate() {
        assertEquals(0, tagRepository.findAssignedTagToCertificate(TagBuilder.CERTIFICATE_ID_WITHOUT_TAG, TagBuilder.TAG_ID_WITHOUT_CERTIFICATE).size());
    }

    @Test
    void successAssignTagToGiftCertificate() {
        tagRepository.assignTagToGiftCertificate(TagBuilder.CERTIFICATE_ID_WITHOUT_TAG, TagBuilder.TAG_ID_WITHOUT_CERTIFICATE);
        assertEquals(TagBuilder.TAG_ID_WITHOUT_CERTIFICATE, tagRepository.findByGiftCertificateId(TagBuilder.CERTIFICATE_ID_WITHOUT_TAG).get(0).getId());
    }

    @Test
    void successFindByGiftCertificateId() {
        assertEquals(TagBuilder.CERTIFICATE_ID_WITH_TAG, tagRepository.findByGiftCertificateId(TagBuilder.CERTIFICATE_ID_WITH_TAG).get(0).getId());
    }

    @Test
    void emptyFindByGiftCertificateId() {
        assertEquals(0, tagRepository.findByGiftCertificateId(TagBuilder.CERTIFICATE_ID_WITHOUT_TAG).size());
    }

    @Test
    void successDeleteById() {
        assertEquals(1, tagRepository.findAssignedTagToCertificate(TagBuilder.CERTIFICATE_ID_WITH_TAG, TagBuilder.TAG_ID_WITH_CERTIFICATE).size());
        assertEquals(1, tagRepository.findById(TagBuilder.TAG_ID_WITH_CERTIFICATE).size());
        tagRepository.deleteById(1);
        assertEquals(0, tagRepository.findAssignedTagToCertificate(TagBuilder.CERTIFICATE_ID_WITH_TAG, TagBuilder.TAG_ID_WITH_CERTIFICATE).size());
        assertEquals(0, tagRepository.findById(TagBuilder.TAG_ID_WITH_CERTIFICATE).size());
    }

    private void assertTags(Tag originalTag, Tag fetchedTag) {
        assertEquals(originalTag.getId(), fetchedTag.getId());
        assertEquals(originalTag.getName(), fetchedTag.getName());
    }
}
