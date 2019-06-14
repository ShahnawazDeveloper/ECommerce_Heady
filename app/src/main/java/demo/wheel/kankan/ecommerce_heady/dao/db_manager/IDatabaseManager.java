package demo.wheel.kankan.ecommerce_heady.dao.db_manager;

import android.util.Pair;

import java.util.List;

import demo.wheel.kankan.ecommerce_heady.dao.db.Category;
import demo.wheel.kankan.ecommerce_heady.dao.db.Product;
import demo.wheel.kankan.ecommerce_heady.model.DataModel;
import demo.wheel.kankan.ecommerce_heady.model.LandingPageModel;
import demo.wheel.kankan.ecommerce_heady.model.ProductsViewModel;
import demo.wheel.kankan.ecommerce_heady.utility.Constants;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author Octa
 */
public interface IDatabaseManager {

    /**
     * Closing available connections
     */
    void closeDbConnections();

    /**
     * Delete all tables and content from our database
     */
    void dropDatabase();


    void insertECommerceDataInDatabase(DataModel response);

    Pair<List<Category>, List<Product>> getLandingPageData();

    List<Category> getAllCategories();

    List<LandingPageModel> getCategoriesList();

    List<Product> getProductByCategory(Category category);

    ProductsViewModel getProductByCategoryWithFilter(Category category, String color, String size, Constants.rankingEnum rankingEnum,ProductsViewModel productsViewModel);

    ProductsViewModel getProductsDataByCategoryId(Category category);

}
