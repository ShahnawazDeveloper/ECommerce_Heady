package demo.wheel.kankan.ecommerce_heady.retrofit;

import demo.wheel.kankan.ecommerce_heady.model.DataModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface APIService {

    @Headers("Content-Type:application/json")//"Cache-Control: no-cache"
    @GET("json")
    Call<DataModel> GetRequestWithHeader();

}
