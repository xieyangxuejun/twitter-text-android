package com.twitter.text;

public enum Type {
    URL, HASHTAG, MENTION, CASHTAG, NONE;

    public Extractor.Entity.Type convert() {
        for (Extractor.Entity.Type type : Extractor.Entity.Type.values()) {
            if (type.ordinal() == this.ordinal()) return type;
        }
        return Extractor.Entity.Type.NONE;
    }

    public static Type from(int ordinal) {
        for (Type type : Type.values()) {
            if (ordinal == type.ordinal()) return type;
        }
        return NONE;
    }
}