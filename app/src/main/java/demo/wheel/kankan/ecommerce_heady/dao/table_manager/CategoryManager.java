package demo.wheel.kankan.ecommerce_heady.dao.table_manager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import demo.wheel.kankan.ecommerce_heady.dao.db.Category;
import demo.wheel.kankan.ecommerce_heady.dao.db.CategoryDao;
import demo.wheel.kankan.ecommerce_heady.dao.db_manager.DatabaseManager;
import demo.wheel.kankan.ecommerce_heady.model.CategoriesModel;
import demo.wheel.kankan.ecommerce_heady.model.DataModel;
import demo.wheel.kankan.ecommerce_heady.model.LandingPageModel;
import java8.util.Optional;
import java8.util.stream.StreamSupport;

public class CategoryManager {

    private DatabaseManager databaseManager;
    private CategoryManager mInstance = null;
    private Gson gson;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     */
    public CategoryManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        gson = new Gson();
    }

    public synchronized CategoryManager getInstance(DatabaseManager databaseManager) {
        if (mInstance == null) {
            mInstance = new CategoryManager(databaseManager);
        }
        return mInstance;
    }

    // set db model using api model
    private Category setCategoriesFromApi(CategoriesModel categoriesModel, Long id, Long parentID) {
        return new Category(id,
                categoriesModel.getId(),
                categoriesModel.getName(),
                parentID
        );
    }


    // get all Categories list
    public List<Category> getAllCategories() {
        try {
            databaseManager.openReadableDb();
            //databaseManager.daoSession.clear();
            return databaseManager.daoSession.getCategoryDao().queryBuilder().list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public synchronized void bulkInsertOrUpdateCategories(DataModel response) {

        List<Category> updateList = new ArrayList<>();
        List<Category> insertList = new ArrayList<>();
        // List<Long> contactIds = new ArrayList<>();

        if (response.getCategories() != null && !response.getCategories().isEmpty()) {
            //agenciesList = removeDuplicateRecord(agenciesList);

            List<Category> dbList = getAllCategories();
            Optional<Category> commentMaster;
            for (CategoriesModel category : response.getCategories()) {

                if (dbList != null && !dbList.isEmpty()) {
                    commentMaster = StreamSupport.stream(dbList)
                            .filter(p -> p.getCategory_id() == category.getId())
                            .findFirst();

                    if (commentMaster.isPresent()) {
                        updateList.add(setCategoriesFromApi(category, commentMaster.get().getId(), commentMaster.get().getParent_id()));
                        dbList.remove(commentMaster.get());
                    } else {
                        insertList.add(setCategoriesFromApi(category, null, 0L));
                    }
                } else {
                    insertList.add(setCategoriesFromApi(category, null, 0L));
                }

                new ProductsManager(databaseManager).bulkInsertOrUpdateProducts(category);
            }

            for (CategoriesModel category : response.getCategories()) {

                if (category.getChild_categories() != null && category.getChild_categories().length > 0) {
                    for (Long i : category.getChild_categories()) {

                        if (!insertList.isEmpty()) {
                            StreamSupport.stream(insertList)
                                    .filter(e -> e.getCategory_id().equals(i))
                                    //.filter(e -> Arrays.asList(category.getChild_categories()).contains(e.getCategory_id()) )
                                    .findFirst().ifPresent(producer -> {
                                producer.setParent_id(category.getId());
                            });
                        }

                        if (!updateList.isEmpty()) {
                            StreamSupport.stream(updateList)
                                    .filter(e -> e.getCategory_id().equals(i))
                                    //.filter(e -> Arrays.asList(category.getChild_categories()).contains(e.getCategory_id()) )
                                    .findFirst().ifPresent(producer -> {
                                producer.setParent_id(category.getId());
                            });
                        }
                    }
                }
            }

        }
        /*if (isPerformDelete)
            databaseManager.bulkDeleteNotIn(Agencies.class, contactIds, AgenciesDao.Properties.Contact_id);*/

        if (!insertList.isEmpty())
            databaseManager.daoSession.getCategoryDao().insertInTx(insertList);
        if (!updateList.isEmpty())
            databaseManager.daoSession.getCategoryDao().updateInTx(updateList);
    }


    // get main Categories list
    public List<Category> getMainCategories() {
        try {
            databaseManager.openReadableDb();
            //databaseManager.daoSession.clear();
            return databaseManager.daoSession.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.Parent_id.eq(0))
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Category> getCategoriesParentId(long parentId) {
        try {
            databaseManager.openReadableDb();
            //databaseManager.daoSession.clear();
            return databaseManager.daoSession.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.Parent_id.eq(parentId))
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<LandingPageModel> getCategoriesList() {
        List<LandingPageModel> categoriesList = new ArrayList<>();
        List<Category> mainCategoryList = getCategoriesParentId(0);

        if (mainCategoryList != null && !mainCategoryList.isEmpty()) {

            for (Category category : mainCategoryList) {
                LandingPageModel model = new LandingPageModel();
                model.setCategory(category);
                model.setExpanded(false);

                List<Category> subCategoryList = getCategoriesParentId(category.getCategory_id());

                if (subCategoryList != null && !subCategoryList.isEmpty()) {
                    model.setHasChildCategory(true);
                    List<LandingPageModel> subCategoriesList = new ArrayList<>();
                    for (Category subCategory : subCategoryList) {
                        LandingPageModel subCategoryModel = new LandingPageModel();

                        subCategoryModel.setCategory(subCategory);
                        subCategoryModel.setExpanded(false);

                        List<Category> subCategoryList1 = getCategoriesParentId(subCategory.getCategory_id());

                        if (subCategoryList1 != null && !subCategoryList1.isEmpty()) {
                            model.setHasChildCategory(true);

                            List<LandingPageModel> subCategoriesList1 = new ArrayList<>();
                            for (Category subCategory1 : subCategoryList1) {

                                LandingPageModel subCategoryModel1 = new LandingPageModel();

                                subCategoryModel1.setCategory(subCategory1);
                                subCategoryModel1.setExpanded(false);

                                List<Category> subCategoryList2 = getCategoriesParentId(subCategory1.getCategory_id());

                                if (subCategoryList2 != null && !subCategoryList2.isEmpty()) {
                                    model.setHasChildCategory(true);
                                } else {
                                    model.setHasChildCategory(false);
                                }
                                subCategoriesList1.add(subCategoryModel1);
                            }
                            subCategoryModel.setSubCategoryList(subCategoriesList1);
                        } else {
                            model.setHasChildCategory(false);
                        }
                        subCategoriesList.add(subCategoryModel);
                    }
                    model.setSubCategoryList(subCategoriesList);
                } else {
                    model.setHasChildCategory(false);
                }
                categoriesList.add(model);
            }
        }

        return categoriesList;
    }

}
