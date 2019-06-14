package demo.wheel.kankan.ecommerce_heady.activity;

import android.content.Context;
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


public class MainCategoriesAdapter extends RecyclerView.Adapter<MainCategoriesAdapter.MainCategoryViewHolder> {

    private Context context;
    private categoryActionListener listener;
    private AdapterView.OnItemClickListener onItemClickListener;
    private List<LandingPageModel> mDataSet;

    private static final int UNSELECTED = -1;
    private int selectedItem = UNSELECTED;
    private RecyclerView rvMainCategory;

    interface categoryActionListener {
        void onMainCategoryClick(LandingPageModel mData, int adapterPosition);

        void onSubCategoryClick(LandingPageModel mData, int mainCategoryAdapterPosition, int adapterPosition);

        void onChildCategoryClick(LandingPageModel mData, int mainCategoryAdapterPosition,
                                  int subCategoryAdapterPosition, int adapterPosition);
    }

    MainCategoriesAdapter(Context context, categoryActionListener listener, RecyclerView rvMainCategory) {
        this.context = context;
        this.listener = listener;
        this.rvMainCategory = rvMainCategory;
    }

    void doRefresh(List<LandingPageModel> dataSet) {
        selectedItem = UNSELECTED;
        mDataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MainCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_category_item_layout, parent, false);
        return new MainCategoryViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MainCategoryViewHolder holder, int position) {
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

    public class MainCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_main_category_name)
        AppCompatTextView tvMainCategoryName;
        @BindView(R.id.img_next)
        AppCompatImageView imgNext;
        @BindView(R.id.rv_category)
        RecyclerView rvCategory;

        private MainCategoriesAdapter mAdapter;

        MainCategoryViewHolder(View itemView, MainCategoriesAdapter mAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mAdapter = mAdapter;
            itemView.setOnClickListener(this);
        }

        void setDataToView(LandingPageModel mData, int position) throws Exception {
            if (mData != null) {
                tvMainCategoryName.setText(mData.getCategory().getName());
                initChildLayoutManager(mData.getSubCategoryList());
                if (mData.isExpanded()) {
                    selectedItem = getAdapterPosition();
                    rvCategory.setVisibility(View.VISIBLE);
                } else {
                    rvCategory.setVisibility(View.GONE);
                }
            }
        }

        void initChildLayoutManager(List<LandingPageModel> pDetails) {
            if (rvCategory.getAdapter() == null) {
                rvCategory.setHasFixedSize(false);
                rvCategory.setLayoutManager(new LinearLayoutManager(context));

                rvCategory.setFocusable(false);
                rvCategory.setNestedScrollingEnabled(false);
            }

            SubCategoriesAdapter mAdapter = new SubCategoriesAdapter(context, listener, getAdapterPosition(), rvCategory);
            rvCategory.setAdapter(mAdapter);
            mAdapter.doRefresh(pDetails);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onMainCategoryClick(mDataSet.get(getAdapterPosition()), getAdapterPosition());
            }
            mAdapter.onItemHolderClick(MainCategoryViewHolder.this);

            MainCategoryViewHolder holder = (MainCategoryViewHolder) rvMainCategory.findViewHolderForAdapterPosition(selectedItem);
            //recyclerView.scrollToPosition(getAdapterPosition());
            if (holder != null) {
                holder.rvCategory.setVisibility(View.GONE);
                // holder.iv_arrow.setImageResource(R.drawable.ic_arrow_down);
                mDataSet.get(selectedItem).setExpanded(false);
            }

            final int position = getAdapterPosition();

            if (mDataSet.get(position).isExpanded() || position == selectedItem) {
                selectedItem = UNSELECTED;
                mDataSet.get(position).setExpanded(false);
                rvCategory.setVisibility(View.GONE);
                /*if (el_comment.isExpanded()) {
                    el_comment.collapse();
                    iv_arrow.setImageResource(R.drawable.ic_arrow_down);
                }*/
            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       /* el_comment.expand();
                        iv_arrow.setImageResource(R.drawable.ic_arrow_up);*/
                        rvCategory.setVisibility(View.VISIBLE);
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

    private void onItemHolderClick(MainCategoryViewHolder holder) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
    }
}
