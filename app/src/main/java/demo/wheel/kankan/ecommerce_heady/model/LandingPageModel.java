package demo.wheel.kankan.ecommerce_heady.model;

import java.io.Serializable;
import java.util.List;

import demo.wheel.kankan.ecommerce_heady.dao.db.Category;

public class LandingPageModel implements Serializable {

    private Category category;
    private boolean isExpanded;
    private boolean hasChildCategory;
    private List<LandingPageModel> subCategoryList;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isHasChildCategory() {
        return hasChildCategory;
    }

    public void setHasChildCategory(boolean hasChildCategory) {
        this.hasChildCategory = hasChildCategory;
    }

    public List<LandingPageModel> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<LandingPageModel> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }
}
