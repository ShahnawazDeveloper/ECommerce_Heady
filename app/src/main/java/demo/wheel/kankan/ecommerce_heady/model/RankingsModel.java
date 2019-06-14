package demo.wheel.kankan.ecommerce_heady.model;

import java.util.List;

public class RankingsModel {

    private String ranking;
    private List<ProductsModel> products;

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public List<ProductsModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsModel> products) {
        this.products = products;
    }
}
