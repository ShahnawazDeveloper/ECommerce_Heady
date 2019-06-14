package demo.wheel.kankan.ecommerce_heady.model;

import java.util.List;

public class DataModel {

    private List<RankingsModel> rankings;

    private List<CategoriesModel> categories;

    public List<RankingsModel> getRankings() {
        return rankings;
    }

    public void setRankings(List<RankingsModel> rankings) {
        this.rankings = rankings;
    }

    public List<CategoriesModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoriesModel> categories) {
        this.categories = categories;
    }

}
