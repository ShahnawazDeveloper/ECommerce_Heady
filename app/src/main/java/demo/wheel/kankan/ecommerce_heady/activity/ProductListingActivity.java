package demo.wheel.kankan.ecommerce_heady.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.wheel.kankan.ecommerce_heady.R;
import demo.wheel.kankan.ecommerce_heady.bottom_sheet.RankingListDialogFragment;
import demo.wheel.kankan.ecommerce_heady.bottom_sheet.SizeAndColorListDialogFragment;
import demo.wheel.kankan.ecommerce_heady.dao.db.Category;
import demo.wheel.kankan.ecommerce_heady.dao.db_manager.DatabaseManager;
import demo.wheel.kankan.ecommerce_heady.dao.db_manager.IDatabaseManager;
import demo.wheel.kankan.ecommerce_heady.model.FilterModel;
import demo.wheel.kankan.ecommerce_heady.model.LandingPageModel;
import demo.wheel.kankan.ecommerce_heady.model.ProductListingModel;
import demo.wheel.kankan.ecommerce_heady.model.ProductsViewModel;
import demo.wheel.kankan.ecommerce_heady.utility.Constants;

public class ProductListingActivity extends AppCompatActivity implements ProductsAdapter.ProductActionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_products)
    RecyclerView rvProducts;
    @BindView(R.id.tv_msg_no_data)
    AppCompatTextView tvMsgNoData;

    LandingPageModel mData;
    Category category;
    private IDatabaseManager databaseManager;
    private List<ProductListingModel> productList;
    ProductsAdapter mAdapter;
    ProductsAdapter.ProductActionListener listener;
    String color, size;

    ProductsViewModel productsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        productList = new ArrayList<>();
        databaseManager = DatabaseManager.getInstance(ProductListingActivity.this);
        listener = this;
        getDataFromIntent();
        setupToolbar();
    }

    private void getDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("category")) {
            mData = (LandingPageModel) extras.getSerializable("category");
            if (mData != null && mData.getCategory() != null) {
                category = mData.getCategory();
                getProductByCategory();
                toolbar.setTitle(category.getName());
            }
        }
    }

    private void setupToolbar() {
        if (mData != null && mData.getCategory() != null) {
            toolbar.setTitle(category.getName());
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private void getProductByCategory() {

        productsViewModel = databaseManager.getProductsDataByCategoryId(category);

        if (productsViewModel != null) {
            productList = productsViewModel.getProductListingModelList();
        }

        //productList = databaseManager.getProductByCategory(category);
        setProductsAdapter();
    }

    public void setProductsAdapter() {
        handleEmptyList();

        // if (mAdapter == null) {
        mAdapter = new ProductsAdapter(ProductListingActivity.this, listener);
        //mAdapter.setOnItemClickListener(this);
        // }

        // if (rvProducts.getAdapter() == null) {
        rvProducts.setHasFixedSize(false);
        rvProducts.setLayoutManager(new LinearLayoutManager(ProductListingActivity.this));
        rvProducts.setAdapter(mAdapter);
        rvProducts.setFocusable(false);
        rvProducts.setNestedScrollingEnabled(false);
        // }

        mAdapter.doRefresh(productList);
    }

    public void handleEmptyList() {
        if (productList == null || productList.isEmpty()) {
            tvMsgNoData.setVisibility(View.VISIBLE);
        } else {
            tvMsgNoData.setVisibility(View.GONE);
        }
    }

    Constants.rankingEnum ranking;

    @OnClick(R.id.btn_ranking)
    public void onRankingClicked() {
        RankingListDialogFragment addPhotoBottomDialogFragment = RankingListDialogFragment.newInstance();

        addPhotoBottomDialogFragment.setListener(new RankingListDialogFragment.CustomInterface() {
            @Override
            public void callbackMethod(Constants.rankingEnum rankingEnum) {
                Toast.makeText(ProductListingActivity.this, rankingEnum.getName(), Toast.LENGTH_SHORT).show();
                ranking = rankingEnum;
                productsViewModel = databaseManager.getProductByCategoryWithFilter(category, color, size, rankingEnum, productsViewModel);
                productList = productsViewModel.getProductListingModelList();
                setProductsAdapter();
            }
        });

        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), "ranking_dialog_fragment");
    }

    @OnClick(R.id.btn_filter)
    public void onFilterClicked() {
        Intent intent = new Intent(ProductListingActivity.this, FilterActivity.class);
        intent.putExtra("colorFilter", (Serializable) productsViewModel.getColorFilterList());
        intent.putExtra("sizeFilter", (Serializable) productsViewModel.getSizeFilterList());
        startActivityForResult(intent, Constants.HI_REQUEST_CODE_FILTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == Constants.HI_REQUEST_CODE_FILTER) {
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    if (extras.containsKey("colorFilter")) {
                        productsViewModel.setColorFilterList((List<FilterModel>) extras.getSerializable("colorFilter"));
                    }
                    if (extras.containsKey("sizeFilter")) {
                        productsViewModel.setSizeFilterList((List<FilterModel>) extras.getSerializable("sizeFilter"));
                    }
                    productsViewModel = databaseManager.getProductByCategoryWithFilter(category, color, size, ranking, productsViewModel);
                    productList = productsViewModel.getProductListingModelList();
                    setProductsAdapter();
                }
            }
        }
    }

    @Override
    public void onSizeClick(int position) {
        SizeAndColorListDialogFragment sizeAndColorListDialogFragment = SizeAndColorListDialogFragment
                .newInstance(productList.get(position).getVariantList());
        // sizeAndColorListDialogFragment.variantsModelList = productList.get(position).getSizeVariantList();
        // sizeAndColorListDialogFragment.isSize = true;
        sizeAndColorListDialogFragment.show(getSupportFragmentManager(), "Size_Color_dialog_fragment");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
