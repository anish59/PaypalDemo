package bbt.com.paypaldemo.viewModel;

import android.content.Context;
import android.util.Log;
import bbt.com.paypaldemo.client.ClientPayment;
import bbt.com.paypaldemo.model.PaymentRequest;

public class MainActivityViewModel {

    private Context context;
    private MainActivityViewModelCallBack callBack;

    public MainActivityViewModel(Context context, MainActivityViewModelCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public void getAccessToken() {
        new ClientPayment(context).getAccessToken(new ClientPayment.AccessTokenApiClientCallBack() {
            @Override
            public void onSuccess(String accessToken) {
                Log.e("Access Token", "onSuccess: " + accessToken);
                callBack.onSuccess(accessToken);
            }

            @Override
            public void onFailure(String err) {
                Log.e("Access Token", "onFailure: " + err);
                callBack.onSuccess(err);
            }
        });
    }

    public void doPayment(PaymentRequest paymentRequest) {
        new ClientPayment(context).doPayment(paymentRequest, new ClientPayment.PaymentCallBack() {
            @Override
            public void onPaymentSuccess(String result) {
                callBack.onPaymentSuccess(result);
            }

            @Override
            public void onFailure(String err) {
                callBack.onError(err);
            }
        });
    }

    public interface MainActivityViewModelCallBack {
        void onSuccess(String token);

        void onError(String err);

        void onPaymentSuccess(String result);
    }
}
