package com.ecomm.np.genevaecommerce.DTO;

public class NewCollectionDTO {
    private String itemName;
    private String description;
    private String imageName;

    public NewCollectionDTO() {
    }

    public NewCollectionDTO(String itemName, String description, String imageName) {
        this.itemName = itemName;
        this.description = description;
        this.imageName = imageName;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
