package com.b4t.app.commons.export;

import java.util.Objects;

public class TagInfo {
    private String tagName;
    private String style;
    private int tagType;

    public TagInfo(String tagName, String style, int tagType) {
        this.tagName = tagName;
        this.style = style;
        this.tagType = tagType;
    }

    public TagInfo(String tagName, int tagType) {
        this.tagName = tagName;
        this.tagType = tagType;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagInfo tagInfo = (TagInfo) o;
        return tagType == tagInfo.tagType &&
            Objects.equals(tagName, tagInfo.tagName) &&
            Objects.equals(style, tagInfo.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, style, tagType);
    }
}
