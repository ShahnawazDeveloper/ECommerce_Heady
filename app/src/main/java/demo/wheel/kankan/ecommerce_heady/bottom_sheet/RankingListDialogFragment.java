package demo.wheel.kankan.ecommerce_heady.bottom_sheet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import demo.wheel.kankan.ecommerce_heady.R;
import demo.wheel.kankan.ecommerce_heady.utility.Constants;


public class RankingListDialogFragment extends BottomSheetDialogFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.rv_ranking)
    RecyclerView rvRanking;

    bottomSheetAdapter mAdapter;
    List<Constants.rankingEnum> rankingEnumList;
    Unbinder unbinder;
    private CustomInterface customInterface;

    public static RankingListDialogFragment newInstance() {
        return new RankingListDialogFragment();
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
        rankingEnumList = Constants.rankingEnum.getClientList();
        setProductsAdapter();
    }

    public void setProductsAdapter() {
        if (mAdapter == null) {
            mAdapter = new bottomSheetAdapter(getActivity());
            mAdapter.setOnItemClickListener(this);
        }
        mAdapter.doRefresh(rankingEnumList);
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
            customInterface.callbackMethod(rankingEnumList.get(position));
        }
        dismiss();
    }

    public interface CustomInterface {

        void callbackMethod(Constants.rankingEnum rankingEnum);
    }
}
