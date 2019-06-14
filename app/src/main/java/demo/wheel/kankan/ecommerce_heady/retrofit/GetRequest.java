package demo.wheel.kankan.ecommerce_heady.retrofit;

import android.content.Context;
import android.support.annotation.NonNull;

import com.kaopiz.kprogresshud.KProgressHUD;

import demo.wheel.kankan.ecommerce_heady.R;
import demo.wheel.kankan.ecommerce_heady.model.DataModel;
import demo.wheel.kankan.ecommerce_heady.utility.ConnectionDetector;
import demo.wheel.kankan.ecommerce_heady.utility.ProgressUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetRequest {

    private APIService mApiService;
    private Context context;
    private boolean isLoaderRequired, isNoInternetDialog;

    private ResponseListener listener;
    private ProgressUtil progressUtil;
    private KProgressHUD dialog = null;

    public GetRequest(Context context, boolean isLoaderRequired,
                      boolean isNoInternetDialog, ResponseListener listener) {
        this.context = context;
        this.isLoaderRequired = isLoaderRequired;
        this.isNoInternetDialog = isNoInternetDialog;
        this.listener = listener;
        progressUtil = ProgressUtil.getInstance();

        // get retrofit client for making api call
        mApiService = RetrofitClient.getClient().create(APIService.class);
    }

    public void execute() {
        if (!ConnectionDetector.getInstance().internetCheck(context, isNoInternetDialog) && listener != null) {
            listener.onNoInternet(context.getString(R.string.msg_server_error));
            return;
        }

        if (isLoaderRequired) {
            dialog = progressUtil.initProgressBar(context);
        }

        if (mApiService != null) {
            progressUtil.showDialog(dialog);

            Call<DataModel> bodyCall = mApiService.GetRequestWithHeader();
            bodyCall.enqueue(new Callback<DataModel>() {
                @Override
                public void onResponse(@NonNull Call<DataModel> call, @NonNull Response<DataModel> response) {

                    progressUtil.hideDialog(dialog);
                    //new AsyncResponseManager(response, listener).execute();
                    if (listener != null) {
                        if (response.isSuccessful() && response.body() != null) {
                            listener.onSucceedToPostCall(response.body());
                        } else {
                            listener.onFailedToPostCall(response.code(), context.getString(R.string.msg_server_error));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DataModel> call, @NonNull Throwable t) {
                    progressUtil.hideDialog(dialog);
                    if (listener != null)
                        listener.onFailedToPostCall(400, context.getString(R.string.error_msg_connection_timeout));
                }
            });
        }
    }

    public interface ResponseListener {
        void onSucceedToPostCall(DataModel response);

        void onFailedToPostCall(int statusCode, String msg);

        void onNoInternet(String msg);

    }

}
