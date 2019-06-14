package demo.wheel.kankan.ecommerce_heady.model;

import java.io.Serializable;
import java.util.List;

public class ProductsViewModel implements Serializable {

    private List<ProductListingModel> productListingModelList;

    private List<FilterModel> colorFilterList;
    private List<FilterModel> sizeFilterList;

    public List<ProductListingModel> getProductListingModelList() {
        return productListingModelList;
    }

    public void setProductListingModelList(List<ProductListingModel> productListingModelList) {
        this.productListingModelList = productListingModelList;
    }

    public List<FilterModel> getColorFilterList() {
        return colorFilterList;
    }

    public void setColorFilterList(List<FilterModel> colorFilterList) {
        this.colorFilterList = colorFilterList;
    }

    public List<FilterModel> getSizeFilterList() {
        return sizeFilterList;
    }

    public void setSizeFilterList(List<FilterModel> sizeFilterList) {
        this.sizeFilterList = sizeFilterList;
    }
}
