package demo.wheel.kankan.ecommerce_heady.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.wheel.kankan.ecommerce_heady.R;
import demo.wheel.kankan.ecommerce_heady.model.LandingPageModel;


public class SubCategoriesAdapter extends RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder> {
    private Context context;
    private MainCategoriesAdapter.categoryActionListener listener;
    private AdapterView.OnItemClickListener onItemClickListener;
    private List<LandingPageModel> mDataSet;

    private static final int UNSELECTED = -1;
    private int selectedItem = UNSELECTED;
    private RecyclerView rvCategory;
    private int mainCategoryAdapterPosition;

    SubCategoriesAdapter(Context context, MainCategoriesAdapter.categoryActionListener listener, int adapterPosition, RecyclerView rvCategory) {
        this.context = context;
        this.listener = listener;
        this.mainCategoryAdapterPosition = adapterPosition;
        this.rvCategory = rvCategory;
    }

    void doRefresh(List<LandingPageModel> dataSet) {
        mDataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_item_layout, parent, false);
        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LandingPageModel mData = mDataSet.get(position);
        try {
            holder.setDataToView(mData, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.tv_category_name)
        AppCompatTextView tvCategoryName;
        @BindView(R.id.img_next)
        AppCompatImageView imgNext;
        @BindView(R.id.rv_sub_category)
        RecyclerView rvSubCategory;


        private SubCategoriesAdapter mAdapter;

        ViewHolder(View itemView, SubCategoriesAdapter mAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mAdapter = mAdapter;
            itemView.setOnClickListener(this);
        }

        void setDataToView(LandingPageModel mData, int position) throws Exception {
            if (mData != null) {
                tvCategoryName.setText(mData.getCategory().getName());
                initChildLayoutManager(mData.getSubCategoryList());
                if (mData.isExpanded()) {
                    selectedItem = getAdapterPosition();
                    rvSubCategory.setVisibility(View.VISIBLE);
                    tvCategoryName.setTypeface(null, Typeface.BOLD);
                } else {
                    rvSubCategory.setVisibility(View.GONE);
                    tvCategoryName.setTypeface(null, Typeface.NORMAL);
                }
            }
        }

        void initChildLayoutManager(List<LandingPageModel> pDetails) {
            if (rvSubCategory.getAdapter() == null) {
                rvSubCategory.setHasFixedSize(false);
                rvSubCategory.setLayoutManager(new LinearLayoutManager(context));

                rvSubCategory.setFocusable(false);
                rvSubCategory.setNestedScrollingEnabled(false);
            }

            ChildCategoriesAdapter mAdapter = new ChildCategoriesAdapter(context, listener, mainCategoryAdapterPosition, getAdapterPosition());
            rvSubCategory.setAdapter(mAdapter);
            mAdapter.doRefresh(pDetails);
        }

        @Override
        public void onClick(View view) {
            mAdapter.onItemHolderClick(ViewHolder.this);

            if (listener != null) {
                listener.onSubCategoryClick(mDataSet.get(getAdapterPosition()), mainCategoryAdapterPosition, getAdapterPosition());
            }

            ViewHolder holder = (ViewHolder) rvCategory.findViewHolderForAdapterPosition(selectedItem);
            //recyclerView.scrollToPosition(getAdapterPosition());
            if (holder != null) {
                holder.rvSubCategory.setVisibility(View.GONE);
                holder.tvCategoryName.setTypeface(null, Typeface.NORMAL);
                // holder.iv_arrow.setImageResource(R.drawable.ic_arrow_down);
                mDataSet.get(selectedItem).setExpanded(false);
            }

            final int position = getAdapterPosition();

            if (mDataSet.get(position).isExpanded() || position == selectedItem) {
                selectedItem = UNSELECTED;
                mDataSet.get(position).setExpanded(false);
                rvSubCategory.setVisibility(View.GONE);
                tvCategoryName.setTypeface(null, Typeface.NORMAL);
            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvSubCategory.setVisibility(View.VISIBLE);
                        tvCategoryName.setTypeface(null, Typeface.BOLD);
                        selectedItem = position;
                        mDataSet.get(position).setExpanded(true);
                    }
                }, 300);
            }
        }

    }

    void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(ViewHolder holder) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
    }
}
