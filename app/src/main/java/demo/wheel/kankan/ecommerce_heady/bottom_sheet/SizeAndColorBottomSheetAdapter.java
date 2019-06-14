package demo.wheel.kankan.ecommerce_heady.bottom_sheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.wheel.kankan.ecommerce_heady.R;
import demo.wheel.kankan.ecommerce_heady.dao.db.Variant;


public class SizeAndColorBottomSheetAdapter extends RecyclerView.Adapter<SizeAndColorBottomSheetAdapter.ProductViewHolder> {

    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener;
    private List<Variant> mDataSet;

    SizeAndColorBottomSheetAdapter(Context context) {
        this.context = context;
    }

    void doRefresh(List<Variant> dataSet) {
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
        Variant mData = mDataSet.get(position);
        try {
            holder.setDataToView(mData);
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
        @BindView(R.id.tv_prize)
        AppCompatTextView tvPrize;


        private SizeAndColorBottomSheetAdapter mAdapter;

        ProductViewHolder(View itemView, SizeAndColorBottomSheetAdapter mAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mAdapter = mAdapter;
            itemView.setOnClickListener(this);
            tvPrize.setVisibility(View.VISIBLE);
        }

        void setDataToView(Variant mData) throws Exception {
            if (mData != null) {
                if (mData.getSize() != null && !mData.getSize().isEmpty()) {
                    tvTitle.setText(Html.fromHtml(context.getString(R.string.set_color_and_size, mData.getColor(), mData.getSize())));
                } else {
                    tvTitle.setText(Html.fromHtml(context.getString(R.string.set_color, mData.getColor())));
                }
                tvPrize.setText(Html.fromHtml(context.getString(R.string.text_prize, mData.getPrize())));
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
