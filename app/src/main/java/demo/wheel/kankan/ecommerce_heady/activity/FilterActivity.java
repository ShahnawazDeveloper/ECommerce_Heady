package demo.wheel.kankan.ecommerce_heady.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.wheel.kankan.ecommerce_heady.R;
import demo.wheel.kankan.ecommerce_heady.model.FilterModel;
import demo.wheel.kankan.ecommerce_heady.utility.Constants;

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_size)
    AppCompatTextView tvSize;
    @BindView(R.id.tv_color)
    AppCompatTextView tvColor;
    @BindView(R.id.rv_filter)
    RecyclerView rvFilter;
    @BindView(R.id.btn_filter)
    AppCompatButton btnFilter;

    FilterAdapter mAdapter;
    List<Constants.rankingEnum> rankingEnumList;


    private List<FilterModel> colorFilterList;
    private List<FilterModel> sizeFilterList;

    public static FilterActivity newInstance() {
        return new FilterActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        rankingEnumList = Constants.rankingEnum.getClientList();
        getDataFromIntent();
    }


    private void getDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("colorFilter")) {
                colorFilterList = (List<FilterModel>) extras.getSerializable("colorFilter");
                if (colorFilterList != null && !colorFilterList.isEmpty()) {
                    tvColor.setVisibility(View.VISIBLE);
                } else {
                    tvColor.setVisibility(View.GONE);
                }
            }

            if (extras.containsKey("sizeFilter")) {
                sizeFilterList = (List<FilterModel>) extras.getSerializable("sizeFilter");
                if ((sizeFilterList != null && !sizeFilterList.isEmpty()) &&
                        (sizeFilterList.get(0).getVariant().getSize() != null &&
                                !sizeFilterList.get(0).getVariant().getSize().isEmpty())) {
                    tvSize.setVisibility(View.VISIBLE);
                } else {
                    tvSize.setVisibility(View.GONE);
                }
            }

            if (colorFilterList != null && !colorFilterList.isEmpty()) {
                tvColor.performClick();
            } else {
                tvSize.performClick();
            }

        }
    }

    public void setProductsAdapter(List<FilterModel> filterList, boolean isSize) {
        //if (mAdapter == null) {
        mAdapter = new FilterAdapter(FilterActivity.this, filterList, isSize);
        //mAdapter.setOnItemClickListener(this);
        // }

        // (rvFilter.getAdapter() == null) {
        rvFilter.setHasFixedSize(false);
        rvFilter.setLayoutManager(new LinearLayoutManager(FilterActivity.this));
        rvFilter.setAdapter(mAdapter);
        rvFilter.setFocusable(false);
        rvFilter.setNestedScrollingEnabled(false);
        //}

        // mAdapter.doRefresh(colorFilterList, sizeFilterList, isSize);
    }


    @OnClick({R.id.tv_size, R.id.tv_color, R.id.btn_filter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_size:
                setSizeTextViewBold();
                setProductsAdapter(sizeFilterList, true);
                break;
            case R.id.tv_color:
                setColorTextViewBold();
                setProductsAdapter(colorFilterList, false);
                break;
            case R.id.btn_filter:
                Intent intent = new Intent();
                intent.putExtra("colorFilter", (Serializable) colorFilterList);
                intent.putExtra("sizeFilter", (Serializable) sizeFilterList);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void setSizeTextViewBold() {
        tvSize.setTypeface(null, Typeface.BOLD);
        tvColor.setTypeface(null, Typeface.NORMAL);
    }


    private void setColorTextViewBold() {
        tvColor.setTypeface(null, Typeface.BOLD);
        tvSize.setTypeface(null, Typeface.NORMAL);
    }
}
