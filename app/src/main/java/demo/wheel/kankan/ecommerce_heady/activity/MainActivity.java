package demo.wheel.kankan.ecommerce_heady.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.wheel.kankan.ecommerce_heady.R;
import demo.wheel.kankan.ecommerce_heady.dao.db_manager.DatabaseManager;
import demo.wheel.kankan.ecommerce_heady.dao.db_manager.IDatabaseManager;
import demo.wheel.kankan.ecommerce_heady.model.DataModel;
import demo.wheel.kankan.ecommerce_heady.model.LandingPageModel;
import demo.wheel.kankan.ecommerce_heady.retrofit.GetRequest;

public class MainActivity extends AppCompatActivity implements MainCategoriesAdapter.categoryActionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_main_category)
    RecyclerView rvMainCategory;
    @BindView(R.id.tv_msg_no_data)
    AppCompatTextView tvMsgNoData;

    private IDatabaseManager databaseManager;
    MainCategoriesAdapter mAdapter;
    MainCategoriesAdapter.categoryActionListener listener;

    List<LandingPageModel> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setupToolbar();
        listener = this;
        databaseManager = DatabaseManager.getInstance(MainActivity.this);
        getECommentsDataFromServer();
    }

    private void setupToolbar() {
        toolbar.setTitle(getString(R.string.title_categories));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getECommentsDataFromServer() {
        new GetRequest(MainActivity.this,
                true, true,
                new GetRequest.ResponseListener() {

                    @Override
                    public void onSucceedToPostCall(DataModel response) {
                        if (response != null) {
                            // insert data into db
                            databaseManager.insertECommerceDataInDatabase(response);
                            getDataFromDB();
                        }
                    }

                    @Override
                    public void onFailedToPostCall(int statusCode, String msg) {

                    }

                    @Override
                    public void onNoInternet(String msg) {

                    }

                }).execute();
    }


    private void getDataFromDB() {
        categoryList = databaseManager.getCategoriesList();
        setProductsAdapter();
    }

    public void setProductsAdapter() {
        //handleEmptyList();

        if (mAdapter == null) {
            mAdapter = new MainCategoriesAdapter(MainActivity.this, listener, rvMainCategory);
            //mAdapter.setOnItemClickListener(this);
        }

        if (rvMainCategory.getAdapter() == null) {
            rvMainCategory.setHasFixedSize(false);
            rvMainCategory.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            rvMainCategory.setAdapter(mAdapter);
            rvMainCategory.setFocusable(false);
            rvMainCategory.setNestedScrollingEnabled(false);
        }

        mAdapter.doRefresh(categoryList);
    }

    /*public void handleEmptyList() {
        if (productList == null || productList.isEmpty()) {
            tvMsgNoData.setVisibility(View.VISIBLE);
        } else {
            tvMsgNoData.setVisibility(View.GONE);
        }
    }*/

    @Override
    public void onMainCategoryClick(LandingPageModel mData, int adapterPosition) {

    }

    @Override
    public void onSubCategoryClick(LandingPageModel mData, int mainCategoryAdapterPosition, int adapterPosition) {

    }

    @Override
    public void onChildCategoryClick(LandingPageModel mData, int mainCategoryAdapterPosition, int subCategoryAdapterPosition, int adapterPosition) {
        Intent intent = new Intent(MainActivity.this, ProductListingActivity.class);
        intent.putExtra("category", mData);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.refresh_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            getECommentsDataFromServer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
