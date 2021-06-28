package com.epam.esm.entity;

import com.epam.esm.builder.TagBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.epam.esm.builder.TagBuilder.*;

public class TagTest {

    TagBuilder tagBuilder = new TagBuilder();

    @Test
    void testNullFieldsWithDefaultConstructor() {
        Tag tag = new Tag();

        assertNull(tag.getId());
        assertNull(tag.getName());
    }

    @Test
    void testEqualFieldsWithDefaultConstructor() {
        Tag originalTag = tagBuilder.build();
        Tag createdTag = new Tag();

        createdTag.setId(originalTag.getId());
        createdTag.setName(originalTag.getName());

        assertTags(originalTag, createdTag);
    }

    @Test
    void testNullNameConstructorWithId() {
        Tag originalTag = tagBuilder.build();
        Tag createdTag = new Tag(originalTag.getId());

        assertEquals(originalTag.getId(), createdTag.getId());
        assertNull(createdTag.getName());
    }

    @Test
    void testEqualsSameLinks() {
        Tag originalTag = new Tag();

        assertEquals(originalTag, originalTag);
    }

    @Test
    void testNotEqualsWithNull() {
        Tag originalTag = new Tag();
        Tag copy = null;

        assertNotEquals(originalTag, copy);
    }

    @Test
    void testNotEqualsWithOtherClass() {
        Tag originalTag = new Tag();
        Object copy = new Object();

        assertNotEquals(originalTag, copy);
    }

    @Test
    void testNotEqualsWithOtherIds() {
        Tag firstTag = new Tag(EXIST_TAG_ID, EXIST_TAG_NAME);
        Tag secondTag = new Tag(NOT_EXIST_TAG_ID, EXIST_TAG_NAME);

        assertNotEquals(firstTag, secondTag);
    }

    @Test
    void testNotEqualsWithOtherNames() {
        Tag firstTag = new Tag(EXIST_TAG_ID, EXIST_TAG_NAME);
        Tag secondTag = new Tag(EXIST_TAG_ID, NOT_EXIST_TAG_NAME);

        assertNotEquals(firstTag, secondTag);
    }

    private void assertTags(Tag originalTag, Tag createdTag) {
        assertEquals(originalTag.hashCode(), createdTag.hashCode());
        assertEquals(originalTag, createdTag);
        assertEquals(originalTag.toString(), createdTag.toString());
        assertEquals(originalTag.getId(), createdTag.getId());
        assertEquals(originalTag.getName(), createdTag.getName());
    }
}
