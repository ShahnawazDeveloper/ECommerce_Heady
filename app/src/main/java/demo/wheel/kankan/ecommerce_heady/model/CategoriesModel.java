package demo.wheel.kankan.ecommerce_heady.model;

import java.util.List;

public class CategoriesModel {

    private String name;
    private long id;
    private Long[] child_categories;
    private List<ProductsModel> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long[] getChild_categories() {
        return child_categories;
    }

    public void setChild_categories(Long[] child_categories) {
        this.child_categories = child_categories;
    }

    public List<ProductsModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsModel> products) {
        this.products = products;
    }

}
