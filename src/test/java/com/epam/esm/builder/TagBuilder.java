package com.epam.esm.builder;

import com.epam.esm.entity.Tag;

public class TagBuilder implements Cloneable {

    public final static int EXIST_TAG_ID = 1;
    public final static int NOT_EXIST_TAG_ID = 11;
    public final static String EXIST_TAG_NAME = "android";
    public final static String NOT_EXIST_TAG_NAME = "new tag";
    public final static int ALL_TAGS_COUNT = 10;
    public final static int CERTIFICATE_ID_WITH_TAG = 1;
    public final static int CERTIFICATE_ID_WITHOUT_TAG = 6;
    public final static int TAG_ID_WITH_CERTIFICATE = 1;
    public final static int TAG_ID_WITHOUT_CERTIFICATE = 10;

    private Integer id;
    private String name;

    public TagBuilder() {
        this.id = EXIST_TAG_ID;
        this.name = EXIST_TAG_NAME;
    }

    public TagBuilder withId(Integer id) {
        TagBuilder clone = getClone();
        clone.id = id;
        return clone;
    }

    public TagBuilder withName(String name) {
        TagBuilder clone = getClone();
        clone.name = name;
        return clone;
    }

    public Tag build() {
        return new Tag(id, name);
    }

    private TagBuilder getClone() {
        TagBuilder clone = null;
        try {
            clone = (TagBuilder) this.clone();
        } catch (CloneNotSupportedException ignored) {
        }
        return clone;
    }
}
