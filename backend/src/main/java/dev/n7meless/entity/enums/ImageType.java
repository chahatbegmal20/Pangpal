package dev.n7meless.entity.enums;

public enum ImageType {
    MESSAGE("message"), ALBUM("album"), AVATAR("avatar"), POST("post");
    private final String type;

    ImageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
