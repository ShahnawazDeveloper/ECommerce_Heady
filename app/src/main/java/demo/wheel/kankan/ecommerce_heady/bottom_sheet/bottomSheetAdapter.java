package demo.wheel.kankan.ecommerce_heady.bottom_sheet;

import android.content.Context;
import android.support.annotation.NonNull;
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
import demo.wheel.kankan.ecommerce_heady.utility.Constants;


public class bottomSheetAdapter extends RecyclerView.Adapter<bottomSheetAdapter.ProductViewHolder> {

    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener;
    private List<Constants.rankingEnum> mDataSet;


    bottomSheetAdapter(Context context) {
        this.context = context;
    }

    void doRefresh(List<Constants.rankingEnum> dataSet) {
        mDataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_item_layout, parent, false);
        return new ProductViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Constants.rankingEnum mData = mDataSet.get(position);
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

        private bottomSheetAdapter mAdapter;

        ProductViewHolder(View itemView, bottomSheetAdapter mAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mAdapter = mAdapter;
            itemView.setOnClickListener(this);
        }

        void setDataToView(Constants.rankingEnum mData, int position) throws Exception {
            if (mData != null) {
                tvTitle.setText(mData.getName());
            }
        }

        @Override
        public void onClick(View view) {
            mAdapter.onItemHolderClick(ProductViewHolder.this);
        }

    }

    void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(ProductViewHolder holder) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
    }
}
