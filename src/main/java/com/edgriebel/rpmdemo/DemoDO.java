package com.edgriebel.rpmdemo;

import io.swagger.annotations.ApiModelProperty;

public class DemoDO {
    @ApiModelProperty(notes="Name of item")
    public String  name;
    @ApiModelProperty(notes="Value of item")
    public String  value;
    @ApiModelProperty(notes="Is this item enabled")
    public Boolean enabled;
    
    public DemoDO() {
        // do nothing
    }
    
    public DemoDO name(String name) {
        this.name = name;
        return this;
    }
    
    public DemoDO value(String value) {
        this.value = value;
        return this;
    }
    
    public DemoDO enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    
    @Override
    public String toString() {
        return "DemoDO [" +
                " name=" + name + 
                " value=" + value + 
                " enabled=" + enabled +
                "]";
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }


}