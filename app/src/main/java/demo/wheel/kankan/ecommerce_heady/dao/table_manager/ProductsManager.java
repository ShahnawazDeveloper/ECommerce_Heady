package demo.wheel.kankan.ecommerce_heady.dao.table_manager;

import com.google.gson.Gson;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import demo.wheel.kankan.ecommerce_heady.dao.db.Product;
import demo.wheel.kankan.ecommerce_heady.dao.db.ProductDao;
import demo.wheel.kankan.ecommerce_heady.dao.db.Variant;
import demo.wheel.kankan.ecommerce_heady.dao.db.VariantDao;
import demo.wheel.kankan.ecommerce_heady.dao.db_manager.DatabaseManager;
import demo.wheel.kankan.ecommerce_heady.model.CategoriesModel;
import demo.wheel.kankan.ecommerce_heady.model.FilterModel;
import demo.wheel.kankan.ecommerce_heady.model.ProductListingModel;
import demo.wheel.kankan.ecommerce_heady.model.ProductsModel;
import demo.wheel.kankan.ecommerce_heady.model.ProductsViewModel;
import demo.wheel.kankan.ecommerce_heady.model.RankingsModel;
import demo.wheel.kankan.ecommerce_heady.utility.Constants;
import java8.util.Optional;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class ProductsManager {


    private DatabaseManager databaseManager;
    private ProductsManager mInstance = null;
    private Gson gson;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     */
    public ProductsManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        gson = new Gson();
    }

    public synchronized ProductsManager getInstance(DatabaseManager databaseManager) {
        if (mInstance == null) {
            mInstance = new ProductsManager(databaseManager);
        }
        return mInstance;
    }

    // set db model using api model
    private Product setProductsFromApi(ProductsModel categoriesModel, Long id, Long categoryID) {
        return new Product(id,
                categoriesModel.getId(),
                categoryID,
                categoriesModel.getName(),
                categoriesModel.getDate_added(),
                0L,
                0L,
                0L
        );
    }


    // get Products By CategoryId
    public List<Product> getProductsByCategoryId(Long categoryId) {
        try {
            databaseManager.openReadableDb();
            //databaseManager.daoSession.clear();
            return databaseManager.daoSession.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.Category_id.eq(categoryId))
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public synchronized void bulkInsertOrUpdateProducts(CategoriesModel categoriesModel) {

        List<Product> updateList = new ArrayList<>();
        List<Product> insertList = new ArrayList<>();
        // List<Long> contactIds = new ArrayList<>();

        if (categoriesModel.getProducts() != null && !categoriesModel.getProducts().isEmpty()) {
            //agenciesList = removeDuplicateRecord(agenciesList);

            List<Product> dbList = getProductsByCategoryId(categoriesModel.getId());
            Optional<Product> productOptional;
            for (ProductsModel productsModel : categoriesModel.getProducts()) {

                if (dbList != null && !dbList.isEmpty()) {
                    productOptional = StreamSupport.stream(dbList)
                            .filter(p -> p.getProduct_id() == productsModel.getId())
                            .findFirst();

                    if (productOptional.isPresent()) {
                        updateList.add(setProductsFromApi(productsModel, productOptional.get().getId(), productOptional.get().getCategory_id()));
                        dbList.remove(productOptional.get());
                    } else {
                        insertList.add(setProductsFromApi(productsModel, null, categoriesModel.getId()));
                    }
                } else {
                    insertList.add(setProductsFromApi(productsModel, null, categoriesModel.getId()));
                }
                new VariantsManager(databaseManager).bulkInsertOrUpdateCategories(productsModel);
            }
        }
        /*if (isPerformDelete)
            databaseManager.bulkDeleteNotIn(Agencies.class, contactIds, AgenciesDao.Properties.Contact_id);*/

        if (!insertList.isEmpty())
            databaseManager.daoSession.getProductDao().insertInTx(insertList);
        if (!updateList.isEmpty())
            databaseManager.daoSession.getProductDao().updateInTx(updateList);
    }


    // get Products By ProductId
    private Product getProductsByProductId(Long productId) {
        try {
            databaseManager.openReadableDb();
            //databaseManager.daoSession.clear();
            List<Product> productList = databaseManager.daoSession.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.Product_id.eq(productId))
                    .limit(1)
                    .list();

            if (productList != null && !productList.isEmpty()) {
                return productList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateRankingsOfProduct(List<RankingsModel> rankingsModel) {

        if (rankingsModel != null && !rankingsModel.isEmpty()) {
            List<Product> updateList = new ArrayList<>();

            for (RankingsModel model : rankingsModel) {

                if (model.getProducts() != null && !model.getProducts().isEmpty()) {

                    for (ProductsModel productsModel : model.getProducts()) {

                        Product product;
                        if (!updateList.isEmpty()) {
                            Optional<Product> productOptional = StreamSupport.stream(updateList)
                                    .filter(p -> p.getProduct_id() == productsModel.getId())
                                    .findFirst();
                            if (productOptional.isPresent()) {
                                product = productOptional.get();
                            } else {
                                product = getProductsByProductId(productsModel.getId());
                            }
                        } else {
                            product = getProductsByProductId(productsModel.getId());
                        }
                        if (product != null) {
                            if (model.getRanking().equalsIgnoreCase(Constants.rankingEnum.Viewed.getName())) {
                                product.setView_count(productsModel.getView_count());
                            } else if (model.getRanking().equalsIgnoreCase(Constants.rankingEnum.Ordered.getName())) {
                                product.setOrder_count(productsModel.getOrder_count());
                            } else if (model.getRanking().equalsIgnoreCase(Constants.rankingEnum.Shared.getName())) {
                                product.setShares(productsModel.getShares());
                            }
                            updateList.add(product);
                        }
                    }
                }
            }
            if (!updateList.isEmpty())
                databaseManager.daoSession.getProductDao().updateInTx(updateList);
        }

    }


    // get All Products for landing page
    public List<Product> getAllProducts() {
        try {
            databaseManager.openReadableDb();
            //databaseManager.daoSession.clear();
            return databaseManager.daoSession.getProductDao()
                    .queryBuilder()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // get Products By CategoryId
    public ProductsViewModel getProductsByCategoryIdAndFilters(Long categoryId,
                                                               Constants.rankingEnum rankingEnum,
                                                               ProductsViewModel productsViewModel) {
        try {
            databaseManager.openReadableDb();
            //databaseManager.daoSession.clear();

            QueryBuilder<Product> queryBuilder = databaseManager.daoSession.getProductDao().queryBuilder();

            queryBuilder.where(ProductDao.Properties.Category_id.eq(categoryId));

            if (productsViewModel.getColorFilterList() != null) {
                List<String> colorList = StreamSupport.stream(productsViewModel.getColorFilterList())
                        .filter(FilterModel::isSelected)
                        .map(e -> e.getVariant().getColor())
                        .collect(Collectors.toList());

                if (colorList != null && !colorList.isEmpty()) {
                    queryBuilder.join(ProductDao.Properties.Product_id, Variant.class, VariantDao.Properties.Product_id)
                            .where(VariantDao.Properties.Color.in(colorList));
                }
            }

            if (productsViewModel.getSizeFilterList() != null) {
                List<String> sizeList = StreamSupport.stream(productsViewModel.getSizeFilterList())
                        .filter(FilterModel::isSelected)
                        .map(e -> e.getVariant().getSize())
                        .collect(Collectors.toList());

                if (sizeList != null && !sizeList.isEmpty()) {
                    queryBuilder.join(ProductDao.Properties.Product_id, Variant.class, VariantDao.Properties.Product_id)
                            .where(VariantDao.Properties.Size.in(sizeList));
                }
            }

            if (rankingEnum != null) {
                switch (rankingEnum) {
                    case Ordered:
                        queryBuilder.orderDesc(ProductDao.Properties.Order_count);
                        break;
                    case Viewed:
                        queryBuilder.orderDesc(ProductDao.Properties.View_count);
                        break;
                    case Shared:
                        queryBuilder.orderDesc(ProductDao.Properties.Shares);
                        break;
                }
            }

            //queryBuilder.build()
            queryBuilder.distinct();
            List<Product> productList = queryBuilder.list();


            if (productList != null && !productList.isEmpty()) {
                List<ProductListingModel> productListingModelList = new ArrayList<>();

                for (Product product : productList) {
                    ProductListingModel productListingModel = new ProductListingModel();
                    productListingModel.setProduct(product);

                    productListingModel.setVariantList(new VariantsManager(databaseManager)
                            .getSizeVariantsByProductId(Collections.singletonList(product.getProduct_id())));

                    productListingModelList.add(productListingModel);
                }

                productsViewModel.setProductListingModelList(productListingModelList);
            }

            return productsViewModel;

            /*queryBuilder.where(ProductDao.Properties.Category_id.eq(categoryId));


            queryBuilder.join(ProductDao.Properties.Product_id, Variant.class, VariantDao.Properties.Product_id)
                    .where(VariantDao.Properties.Color.eq("Blue"));

            queryBuilder.join(ProductDao.Properties.Product_id, Variant.class, VariantDao.Properties.Product_id)
                    .where(VariantDao.Properties.Size.eq("32"));

            queryBuilder.distinct();
            queryBuilder.list();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // get Products data By CategoryId
    public ProductsViewModel getProductsDataByCategoryId(Long categoryId) {
        try {
            databaseManager.openReadableDb();
            //databaseManager.daoSession.clear();
            List<Product> productList = databaseManager.daoSession.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.Category_id.eq(categoryId))
                    .list();
            ProductsViewModel viewModel = new ProductsViewModel();

            List<Variant> colorList = new ArrayList<>();
            List<Variant> sizeList = new ArrayList<>();
            List<Long> productIDList = new ArrayList<>();
            List<ProductListingModel> productListingModelList = new ArrayList<>();

            if (productList != null && !productList.isEmpty()) {

                for (Product product : productList) {
                    ProductListingModel productListingModel = new ProductListingModel();
                    productListingModel.setProduct(product);

                    productListingModel.setVariantList(new VariantsManager(databaseManager)
                            .getSizeVariantsByProductId(Collections.singletonList(product.getProduct_id())));

                    productListingModelList.add(productListingModel);

                  /*  if (productListingModel.getColorVariantList() != null &&
                            !productListingModel.getColorVariantList().isEmpty()) {
                        colorList.addAll(productListingModel.getColorVariantList());
                    }

                    if (productListingModel.getSizeVariantList() != null &&
                            !productListingModel.getSizeVariantList().isEmpty()) {
                        sizeList.addAll(productListingModel.getSizeVariantList());
                    }*/
                    productIDList.add(product.getProduct_id());
                }

                viewModel.setProductListingModelList(productListingModelList);


                colorList = new VariantsManager(databaseManager)
                        .getColorVariantsByProductId(productIDList);
                if (!colorList.isEmpty()) {
                    List<FilterModel> colorFilterList = new ArrayList<>();
                    for (Variant variant : colorList) {
                        colorFilterList.add(new FilterModel(variant, false));
                    }
                    viewModel.setColorFilterList(colorFilterList);
                }

                sizeList = (new VariantsManager(databaseManager)
                        .getSizeVariantsByProductId(productIDList));
                if (!sizeList.isEmpty()) {
                    List<FilterModel> sizeFilterList = new ArrayList<>();
                    for (Variant variant : sizeList) {
                        sizeFilterList.add(new FilterModel(variant, false));
                    }
                    viewModel.setSizeFilterList(sizeFilterList);
                }
            }
            return viewModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
