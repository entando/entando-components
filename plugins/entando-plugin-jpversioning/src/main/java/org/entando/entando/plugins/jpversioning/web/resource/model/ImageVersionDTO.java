package org.entando.entando.plugins.jpversioning.web.resource.model;

public class ImageVersionDTO {

    private String dimensions;
    private String path;
    private String size;

    public ImageVersionDTO(String dimensions, String path, String size) {
        this.dimensions = dimensions;
        this.path = path;
        this.size = size;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
