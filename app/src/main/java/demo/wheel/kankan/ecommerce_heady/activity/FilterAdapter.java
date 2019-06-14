package demo.wheel.kankan.ecommerce_heady.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.wheel.kankan.ecommerce_heady.R;
import demo.wheel.kankan.ecommerce_heady.model.FilterModel;


public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ProductViewHolder> {

    private Context context;
    private List<FilterModel> mDataSet;
    private boolean isSize;

    FilterAdapter(Context context, List<FilterModel> dataSet, boolean isSize) {
        this.context = context;
        mDataSet = dataSet;
        this.isSize = isSize;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item_layout, parent, false);
        return new ProductViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        FilterModel mData = mDataSet.get(position);
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

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_title)
        AppCompatTextView tvTitle;
        @BindView(R.id.iv_check)
        AppCompatImageView ivCheck;

        private FilterAdapter mAdapter;

        ProductViewHolder(View itemView, FilterAdapter mAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mAdapter = mAdapter;
            itemView.setOnClickListener(this);
        }

        void setDataToView(FilterModel mData, int position) throws Exception {
            if (mData != null) {
                if (isSize) {
                    tvTitle.setText(mData.getVariant().getSize());
                } else {
                    tvTitle.setText(mData.getVariant().getColor());
                }

                if (mData.isSelected()) {
                    ivCheck.setVisibility(View.VISIBLE);
                } else {
                    ivCheck.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View view) {
            if (mDataSet.get(getAdapterPosition()).isSelected()) {
                mDataSet.get(getAdapterPosition()).setSelected(false);
                ivCheck.setVisibility(View.GONE);
            } else {
                mDataSet.get(getAdapterPosition()).setSelected(true);
                ivCheck.setVisibility(View.VISIBLE);
            }
        }
    }

}
