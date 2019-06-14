package demo.wheel.kankan.ecommerce_heady.dao;

import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class DaoGenerator {
    public static void main(String[] args) {
        Schema schema = new Schema(1, "demo.wheel.kankan.ecommerce_heady.dao.db");
        schema.enableKeepSectionsByDefault();

        createCategoryTable(schema);
        createProductTable(schema);
        createVariantTable(schema);
        createTaxTable(schema);

        try {
            new org.greenrobot.greendao.generator.DaoGenerator().generateAll(schema, "./app/src/main/java"); //PROJECT_DIR + "\\app\\src\\main\\java"
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createCategoryTable(Schema schema) {
        Entity mainMenu = schema.addEntity("Category");

        mainMenu.addIdProperty().primaryKey().autoincrement();
        mainMenu.addLongProperty("category_id");
        mainMenu.addStringProperty("name");
        mainMenu.addLongProperty("parent_id");
    }

    private static void createProductTable(Schema schema) {
        Entity mainMenu = schema.addEntity("Product");

        mainMenu.addIdProperty().primaryKey().autoincrement();
        mainMenu.addLongProperty("product_id");
        mainMenu.addLongProperty("category_id");
        mainMenu.addStringProperty("name");
        mainMenu.addStringProperty("date_added");
        mainMenu.addLongProperty("view_count");
        mainMenu.addLongProperty("order_count");
        mainMenu.addLongProperty("shares");

    }

    private static void createVariantTable(Schema schema) {
        Entity mainMenu = schema.addEntity("Variant");

        mainMenu.addIdProperty().primaryKey().autoincrement();
        mainMenu.addLongProperty("variant_id");
        mainMenu.addLongProperty("product_id");
        mainMenu.addStringProperty("color");
        mainMenu.addStringProperty("size");
        mainMenu.addStringProperty("prize");
    }

    private static void createTaxTable(Schema schema) {
        Entity mainMenu = schema.addEntity("Tax");

        mainMenu.addIdProperty().primaryKey().autoincrement();
        mainMenu.addStringProperty("name");
        mainMenu.addStringProperty("value");
        mainMenu.addLongProperty("product_id");
    }

}
