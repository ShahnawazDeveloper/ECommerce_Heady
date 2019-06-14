package demo.wheel.kankan.ecommerce_heady.dao.db;

import org.greenrobot.greendao.annotation.*;

import java.io.Serializable;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "CATEGORY".
 */
@Entity
public class Category implements Serializable {

    @Id(autoincrement = true)
    private Long id;
    private Long category_id;
    private String name;
    private Long parent_id;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Category() {
    }

    public Category(Long id) {
        this.id = id;
    }

    @Generated
    public Category(Long id, Long category_id, String name, Long parent_id) {
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.parent_id = parent_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
