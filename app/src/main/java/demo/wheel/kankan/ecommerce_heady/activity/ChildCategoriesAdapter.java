package demo.wheel.kankan.ecommerce_heady.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
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


public class ChildCategoriesAdapter extends RecyclerView.Adapter<ChildCategoriesAdapter.MainCategoryViewHolder> {

    private Context context;
    private MainCategoriesAdapter.categoryActionListener listener;
    private AdapterView.OnItemClickListener onItemClickListener;
    private List<LandingPageModel> mDataSet;
    private int mainCategoryAdapterPosition;
    private int subCategoryAdapterPosition;


    ChildCategoriesAdapter(Context context, MainCategoriesAdapter.categoryActionListener listener, int mainCategoryAdapterPosition,
                           int subCategoryAdapterPosition) {
        this.context = context;
        this.listener = listener;
        this.mainCategoryAdapterPosition = mainCategoryAdapterPosition;
        this.subCategoryAdapterPosition = subCategoryAdapterPosition;
    }

    void doRefresh(List<LandingPageModel> dataSet) {
        mDataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MainCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_category_item_layout, parent, false);
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


        private ChildCategoriesAdapter mAdapter;

        @BindView(R.id.tv_child_category_name)
        AppCompatTextView tvChildCategoryName;
        @BindView(R.id.img_next)
        AppCompatImageView imgNext;

        MainCategoryViewHolder(View itemView, ChildCategoriesAdapter mAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mAdapter = mAdapter;
            itemView.setOnClickListener(this);
        }

        void setDataToView(LandingPageModel mData, int position) throws Exception {
            if (mData != null) {
                tvChildCategoryName.setText(mData.getCategory().getName());

            }
        }


        @Override
        public void onClick(View view) {
            mAdapter.onItemHolderClick(MainCategoryViewHolder.this);

            if (listener != null) {

                listener.onChildCategoryClick(mDataSet.get(getAdapterPosition()),
                        mainCategoryAdapterPosition,
                        subCategoryAdapterPosition,
                        getAdapterPosition());

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
