package com.ecomm.np.genevaecommerce.model;



public class BestCollection {
    private String image;
    private String name;
    private String description;
    private int id;

    public BestCollection() {
    }

    public BestCollection(String image, String name, String description,int id) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BestCollection{" +
                "image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }
}
