package com.example.restaurantsystem;

public class ItemOption {

    private int id;
    private int menuItemId;
    private String optionName;
    private String optionValues;
    private String controlType;
    private boolean required;

    // getter and setters
    public ItemOption(int id, int menuItemId, String optionName, String optionValues, String controlType, boolean required) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.optionName = optionName;
        this.optionValues = optionValues;
        this.controlType = controlType;
        this.required = required;
    }

    public int getId() {
        return id;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public String getOptionName() {
        return optionName;
    }

    public String getOptionValues() {
        return optionValues;
    }

    public String getControlType() {
        return controlType;
    }

    public boolean isRequired() {
        return required;
    }
}