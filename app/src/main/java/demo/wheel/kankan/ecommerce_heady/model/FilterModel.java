package demo.wheel.kankan.ecommerce_heady.model;

import java.io.Serializable;

import demo.wheel.kankan.ecommerce_heady.dao.db.Variant;

public class FilterModel implements Serializable {
    private Variant variant;
    private boolean isSelected;

    public FilterModel(Variant variant, boolean isSelected) {
        this.variant = variant;
        this.isSelected = isSelected;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
