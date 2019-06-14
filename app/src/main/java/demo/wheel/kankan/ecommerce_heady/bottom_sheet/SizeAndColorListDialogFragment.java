package demo.wheel.kankan.ecommerce_heady.bottom_sheet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import demo.wheel.kankan.ecommerce_heady.R;
import demo.wheel.kankan.ecommerce_heady.dao.db.Variant;


public class SizeAndColorListDialogFragment extends BottomSheetDialogFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.rv_ranking)
    RecyclerView rvRanking;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;

    SizeAndColorBottomSheetAdapter mAdapter;
    List<Variant> variantsModelList;
    Unbinder unbinder;
    private CustomInterface customInterface;

    public static SizeAndColorListDialogFragment newInstance(List<Variant> variantList) {
        //return new SizeAndColorListDialogFragment();

        SizeAndColorListDialogFragment fragment = new SizeAndColorListDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("variantList", (Serializable) variantList);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(CustomInterface callback) {
        this.customInterface = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ranking_bottom_sheet_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (getArguments() != null) {
            variantsModelList = (List<Variant>) getArguments().getSerializable("variantList");
            tvTitle.setText(getText(R.string.text_avail_variants));
        }
        setProductsAdapter();
    }

    public void setProductsAdapter() {
        if (mAdapter == null) {
            mAdapter = new SizeAndColorBottomSheetAdapter(getActivity());
            mAdapter.setOnItemClickListener(this);
        }
        mAdapter.doRefresh(variantsModelList);
        if (rvRanking.getAdapter() == null) {
            rvRanking.setHasFixedSize(false);
            rvRanking.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvRanking.setAdapter(mAdapter);
            rvRanking.setFocusable(false);
            rvRanking.setNestedScrollingEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (customInterface != null) {
            customInterface.callbackMethod(variantsModelList.get(position));
        }
        dismiss();
    }

    public interface CustomInterface {

        void callbackMethod(Variant variant);
    }
}
