package demo.wheel.kankan.ecommerce_heady.dao.table_manager;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import demo.wheel.kankan.ecommerce_heady.dao.db.Variant;
import demo.wheel.kankan.ecommerce_heady.dao.db.VariantDao;
import demo.wheel.kankan.ecommerce_heady.dao.db_manager.DatabaseManager;
import demo.wheel.kankan.ecommerce_heady.model.ProductsModel;
import demo.wheel.kankan.ecommerce_heady.model.VariantsModel;
import java8.util.Optional;
import java8.util.stream.StreamSupport;

public class VariantsManager {


    private DatabaseManager databaseManager;
    private VariantsManager mInstance = null;
    private Gson gson;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     */
    public VariantsManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        gson = new Gson();
    }

    public synchronized VariantsManager getInstance(DatabaseManager databaseManager) {
        if (mInstance == null) {
            mInstance = new VariantsManager(databaseManager);
        }
        return mInstance;
    }

    // set db model using api model
    private Variant setVariantsFromApi(VariantsModel variantsModel, Long id, Long productID) {
        return new Variant(id,
                variantsModel.getId(),
                productID,
                variantsModel.getColor(),
                variantsModel.getSize(),
                variantsModel.getPrice()
        );
    }

    // get all Variant ByProductId
    public List<Variant> getVariantsByProductId(Long productID) {
        try {
            databaseManager.openReadableDb();
            //databaseManager.daoSession.clear();
            return databaseManager.daoSession.getVariantDao().queryBuilder()
                    .where(VariantDao.Properties.Product_id.eq(productID))
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public synchronized void bulkInsertOrUpdateCategories(ProductsModel response) {

        List<Variant> updateList = new ArrayList<>();
        List<Variant> insertList = new ArrayList<>();
        // List<Long> contactIds = new ArrayList<>();

        if (response.getVariants() != null && !response.getVariants().isEmpty()) {
            //agenciesList = removeDuplicateRecord(agenciesList);

            List<Variant> dbList = getVariantsByProductId(response.getId());
            Optional<Variant> variantOptional;
            for (VariantsModel variantsModel : response.getVariants()) {

                if (dbList != null && !dbList.isEmpty()) {
                    variantOptional = StreamSupport.stream(dbList)
                            .filter(p -> p.getVariant_id() == variantsModel.getId())
                            .findFirst();

                    if (variantOptional.isPresent()) {
                        updateList.add(setVariantsFromApi(variantsModel, variantOptional.get().getId(), variantOptional.get().getProduct_id()));
                        dbList.remove(variantOptional.get());
                    } else {
                        insertList.add(setVariantsFromApi(variantsModel, null, response.getId()));
                    }
                } else {
                    insertList.add(setVariantsFromApi(variantsModel, null, response.getId()));
                }
            }
        }
        /*if (isPerformDelete)
            databaseManager.bulkDeleteNotIn(Agencies.class, contactIds, AgenciesDao.Properties.Contact_id);*/

        if (!insertList.isEmpty())
            databaseManager.daoSession.getVariantDao().insertInTx(insertList);
        if (!updateList.isEmpty())
            databaseManager.daoSession.getVariantDao().updateInTx(updateList);
    }

    // get all Variant ByProductId
    public List<Variant> getSizeVariantsByProductId(List<Long> productID) {
        try {
            databaseManager.openReadableDb();
            //databaseManager.daoSession.clear();

            return databaseManager.daoSession.getVariantDao().queryRawCreate(" WHERE " +
                    VariantDao.Properties.Product_id.columnName +
                    " IN (" + TextUtils.join(",", productID) + ")" +
                    " GROUP BY " + VariantDao.Properties.Size.columnName +
                    " ORDER BY " + VariantDao.Properties.Size.columnName + " ASC ")
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Variant> getColorVariantsByProductId(List<Long> productID) {
        try {
            databaseManager.openReadableDb();
            //databaseManager.daoSession.clear();

            return databaseManager.daoSession.getVariantDao().queryRawCreate(" WHERE " +
                    VariantDao.Properties.Product_id.columnName +
                    " IN (" + TextUtils.join(",", productID) + ")" +
                    " GROUP BY " + VariantDao.Properties.Color.columnName +
                    " ORDER BY " + VariantDao.Properties.Color.columnName + " ASC ")
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
