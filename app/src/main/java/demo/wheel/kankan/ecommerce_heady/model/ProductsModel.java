package demo.wheel.kankan.ecommerce_heady.model;

import java.util.List;

public class ProductsModel {

    private String date_added;
    private String name;
    private TaxModel tax;
    private long id;
    private List<VariantsModel> variants;

    private long view_count;
    private long order_count;
    private long shares;

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaxModel getTax() {
        return tax;
    }

    public void setTax(TaxModel tax) {
        this.tax = tax;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<VariantsModel> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantsModel> variants) {
        this.variants = variants;
    }

    public long getView_count() {
        return view_count;
    }

    public void setView_count(long view_count) {
        this.view_count = view_count;
    }

    public long getOrder_count() {
        return order_count;
    }

    public void setOrder_count(long order_count) {
        this.order_count = order_count;
    }

    public long getShares() {
        return shares;
    }

    public void setShares(long shares) {
        this.shares = shares;
    }

}
