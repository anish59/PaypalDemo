package bbt.com.paypaldemo.client;

import android.content.Context;
import bbt.com.paypaldemo.Apis;
import bbt.com.paypaldemo.AppApplication;
import bbt.com.paypaldemo.helper.Functions;
import bbt.com.paypaldemo.helper.ProgressUtils;
import bbt.com.paypaldemo.model.AccessTokenResponse;
import bbt.com.paypaldemo.model.PaymentRequest;
import bbt.com.paypaldemo.model.PaymentResponse;
import bbt.com.paypaldemo.services.PaymentService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientPayment {
    private Context context;
    private PaymentService paymentService;

    public ClientPayment(Context context) {
        this.context = context;
        paymentService = AppApplication.getRetrofit().create(PaymentService.class);
    }

    public void getAccessToken(final AccessTokenApiClientCallBack callBack) {
        if (!Functions.isConnected(context)) {
            //callBack.onError(context.getString(R.string.no_internet));
            callBack.onFailure("No Internet");
            return;
        }
        ProgressUtils.showProgress(context);
        paymentService.getAccessToken().enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                ProgressUtils.hideProgress();
                if (response.code() == Apis.OK_RESPONSE) {
                    if (response.body().getClient_token() != null) {
                        callBack.onSuccess(response.body().getClient_token());
                    } else {
                        callBack.onFailure("token null");
                    }

                } else {
                    callBack.onFailure("Some Error");
                }
            }

            @Override
            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                ProgressUtils.hideProgress();
                callBack.onFailure(t.toString());
            }
        });
    }

    public void doPayment(PaymentRequest paymentRequest, final PaymentCallBack callBack) {
        if (!Functions.isConnected(context)) {
            //callBack.onError(context.getString(R.string.no_internet));
            callBack.onFailure("No Internet");
            return;
        }
        ProgressUtils.showProgress(context);

        paymentService.doPayment(paymentRequest).enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                ProgressUtils.hideProgress();

                if (response.code() == Apis.OK_RESPONSE) {
                    if (response.body() != null) {
                        callBack.onPaymentSuccess(response.body().getResult());
                    } else {
                        callBack.onFailure("Some issue");
                    }

                } else {
                    callBack.onFailure("Some Error");
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                ProgressUtils.hideProgress();
                callBack.onFailure(t.toString());

            }
        });
    }


    public interface AccessTokenApiClientCallBack {
        void onSuccess(String accessToken);

        void onFailure(String err);
    }

    public interface PaymentCallBack {
        void onPaymentSuccess(String result);

        void onFailure(String err);
    }
}
