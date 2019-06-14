package demo.wheel.kankan.ecommerce_heady.activity;

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
import butterknife.OnClick;
import demo.wheel.kankan.ecommerce_heady.R;
import demo.wheel.kankan.ecommerce_heady.dao.db.Variant;
import demo.wheel.kankan.ecommerce_heady.model.ProductListingModel;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private Context context;
    private ProductActionListener listener;
    private AdapterView.OnItemClickListener onItemClickListener;
    private List<ProductListingModel> mDataSet;

    interface ProductActionListener {
        void onSizeClick(int position);

    }

    ProductsAdapter(Context context, ProductActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    void doRefresh(List<ProductListingModel> dataSet) {
        mDataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ProductViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductListingModel mData = mDataSet.get(position);
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

        @BindView(R.id.tv_product_name)
        AppCompatTextView tvProductName;
        @BindView(R.id.tv_view_count)
        AppCompatTextView tvViewCount;
        @BindView(R.id.tv_order_count)
        AppCompatTextView tvOrderCount;
        @BindView(R.id.tv_shares)
        AppCompatTextView tvShares;
        @BindView(R.id.tv_color_and_size)
        AppCompatTextView tvColorAndSize;
        @BindView(R.id.tv_prize)
        AppCompatTextView tvPrize;

        private ProductsAdapter mAdapter;

        ProductViewHolder(View itemView, ProductsAdapter mAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mAdapter = mAdapter;
            itemView.setOnClickListener(this);
        }

        void setDataToView(ProductListingModel mData, int position) throws Exception {
            if (mData != null) {
                tvProductName.setText(mData.getProduct().getName());

                tvViewCount.setText(String.valueOf(mData.getProduct().getView_count()));
                tvOrderCount.setText(Html.fromHtml(context.getString(R.string.text_orders, String.valueOf(mData.getProduct().getOrder_count()))));
                tvShares.setText(String.valueOf(mData.getProduct().getShares()));

                /*if ((mData.getSizeVariantList() != null && !mData.getSizeVariantList().isEmpty()) &&
                        (mData.getSizeVariantList().get(0).getSize() != null &&
                                !mData.getSizeVariantList().get(0).getSize().isEmpty())) {
                    btnSize.setVisibility(View.VISIBLE);
                } else {
                    btnSize.setVisibility(View.GONE);
                }

                if (mData.getColorVariantList() != null && !mData.getColorVariantList().isEmpty()) {
                    btnColor.setVisibility(View.VISIBLE);
                    setColoAndPrize(mData.getColorVariantList().get(0));
                } else {
                    btnColor.setVisibility(View.GONE);
                }*/

                if (mData.getVariantList() != null && !mData.getVariantList().isEmpty()) {
                    setColoAndPrize(mData.getVariantList().get(0));
                }

            }
        }

        private void setColoAndPrize(Variant variant) {
            if (variant.getSize() != null && !variant.getSize().isEmpty()) {
                tvColorAndSize.setText(Html.fromHtml(context.getString(R.string.set_color_and_size, variant.getColor(), variant.getSize())));
            } else {
                tvColorAndSize.setText(Html.fromHtml(context.getString(R.string.set_color, variant.getColor())));
            }
            tvPrize.setText(Html.fromHtml(context.getString(R.string.text_prize, variant.getPrize())));
        }

        @Override
        public void onClick(View view) {
            mAdapter.onItemHolderClick(ProductViewHolder.this);
        }

        @OnClick(R.id.btn_variant_info)
        public void onViewClick() {
            if (listener != null) {
                listener.onSizeClick(getAdapterPosition());
            }
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
