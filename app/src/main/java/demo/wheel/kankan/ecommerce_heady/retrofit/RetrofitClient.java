package demo.wheel.kankan.ecommerce_heady.retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mac on 10/16/17.
 */

public class RetrofitClient {

    public static final String BASE_URL = "https://stark-spire-93433.herokuapp.com/";
    private static Retrofit retrofit = null;

    static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(SelfSigningClientBuilder.getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void cancelAllRequest() {
        //client.dispatcher().cancelAll();
        SelfSigningClientBuilder.getUnsafeOkHttpClient().dispatcher().cancelAll();
    }

}
