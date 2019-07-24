package bbt.com.paypaldemo.services;

import bbt.com.paypaldemo.Apis;
import bbt.com.paypaldemo.model.AccessTokenResponse;
import bbt.com.paypaldemo.model.PaymentRequest;
import bbt.com.paypaldemo.model.PaymentResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PaymentService {

    @Headers("Cache-Control: no-cache")
    @GET(Apis.GET_ACCESS_TOKEN)
    Call<AccessTokenResponse> getAccessToken();

    @POST(Apis.CHECK_OUT)
    Call<PaymentResponse> doPayment(@Body PaymentRequest paymentRequest);
}
