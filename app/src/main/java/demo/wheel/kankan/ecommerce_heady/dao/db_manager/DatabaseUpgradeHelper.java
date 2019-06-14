package demo.wheel.kankan.ecommerce_heady.dao.db_manager;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import demo.wheel.kankan.ecommerce_heady.dao.db.DaoMaster;

/**
 * Used for migrating data from one schema version to another.
 */

public class DatabaseUpgradeHelper extends DaoMaster.OpenHelper {

    public DatabaseUpgradeHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("Db Upgrade => ","Update DB-Schema to version: " + Integer.toString(oldVersion) + "->" + Integer.toString(newVersion));
    }

}
