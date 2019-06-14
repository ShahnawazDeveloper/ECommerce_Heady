package demo.wheel.kankan.ecommerce_heady.model;

import java.io.Serializable;
import java.util.List;

import demo.wheel.kankan.ecommerce_heady.dao.db.Product;
import demo.wheel.kankan.ecommerce_heady.dao.db.Variant;

public class ProductListingModel implements Serializable {

    private Product product;

    private List<Variant> variantList;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Variant> getVariantList() {
        return variantList;
    }

    public void setVariantList(List<Variant> variantList) {
        this.variantList = variantList;
    }
}
