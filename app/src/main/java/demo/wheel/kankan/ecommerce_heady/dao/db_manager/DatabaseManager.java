package demo.wheel.kankan.ecommerce_heady.dao.db_manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Pair;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import demo.wheel.kankan.ecommerce_heady.dao.db.Category;
import demo.wheel.kankan.ecommerce_heady.dao.db.DaoMaster;
import demo.wheel.kankan.ecommerce_heady.dao.db.DaoSession;
import demo.wheel.kankan.ecommerce_heady.dao.db.Product;
import demo.wheel.kankan.ecommerce_heady.dao.table_manager.CategoryManager;
import demo.wheel.kankan.ecommerce_heady.dao.table_manager.ProductsManager;
import demo.wheel.kankan.ecommerce_heady.model.DataModel;
import demo.wheel.kankan.ecommerce_heady.model.LandingPageModel;
import demo.wheel.kankan.ecommerce_heady.model.ProductsViewModel;
import demo.wheel.kankan.ecommerce_heady.utility.Constants;

public class DatabaseManager implements AsyncOperationListener, IDatabaseManager {

    private static DatabaseManager mInstance = null;
    private DatabaseUpgradeHelper mHelper;//DaoMaster.DevOpenHelper

    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    public DaoSession daoSession;
    private AsyncSession asyncSession;
    private List<AsyncOperation> completedOperations;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     *
     * @param context The Android {@link Context}.
     */
    public DatabaseManager(final Context context) {
        mHelper = new DatabaseUpgradeHelper(context, "ECommerceHeady");
        // open writable db testing
        database = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
        // open writable db testing
        completedOperations = new CopyOnWriteArrayList<>();
    }

    /**
     * @param context The Android {@link Context}.
     * @return this.instance
     */
    public static DatabaseManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseManager(context);
            // prints sql queries
            QueryBuilder.LOG_SQL = true;
            //QueryBuilder.LOG_VALUES = true;
        }
        return mInstance;
    }

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        completedOperations.add(operation);
    }

    private void assertWaitForCompletion1Sec() {
        asyncSession.waitForCompletion(1000);
        asyncSession.isCompleted();
    }

    /**
     * Query for readable DB
     */
    public void openReadableDb() throws SQLiteException {
        /*database = mHelper.getReadableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);*/
    }

    /**
     * Query for writable DB
     */
    public void openWritableDb() throws SQLiteException {
       /* database = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);*/
    }

    @Override
    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        if (mInstance != null) {
            mInstance = null;
        }
    }

    @Override
    public void dropDatabase() {
        openWritableDb();
        DaoMaster.dropAllTables(daoSession.getDatabase(), true);
        mHelper.onCreate(database);              // creates the tables
        //asyncSession.deleteAll(DBUser.class);    // clear all elements from a table
    }

    @Override
    public void insertECommerceDataInDatabase(DataModel response) {
        new CategoryManager(mInstance).bulkInsertOrUpdateCategories(response);

        new ProductsManager(mInstance).updateRankingsOfProduct(response.getRankings());
    }

    @Override
    public List<Category> getAllCategories() {
        return new CategoryManager(mInstance).getAllCategories();
    }

    @Override
    public Pair<List<Category>, List<Product>> getLandingPageData() {
        return new Pair<>(new CategoryManager(mInstance).getMainCategories(),
                new ProductsManager(mInstance).getAllProducts());
    }

    @Override
    public List<LandingPageModel> getCategoriesList() {
        return new CategoryManager(mInstance).getCategoriesList();
    }

    @Override
    public List<Product> getProductByCategory(Category category) {
        return new ProductsManager(mInstance).getProductsByCategoryId(category.getCategory_id());
    }

    @Override
    public ProductsViewModel getProductByCategoryWithFilter(Category category, String color, String size, Constants.rankingEnum rankingEnum, ProductsViewModel productsViewModel) {
        return new ProductsManager(mInstance).getProductsByCategoryIdAndFilters(category.getCategory_id(), rankingEnum, productsViewModel);
    }

    @Override
    public ProductsViewModel getProductsDataByCategoryId(Category category) {
        return new ProductsManager(mInstance).getProductsDataByCategoryId(category.getCategory_id());
    }

}