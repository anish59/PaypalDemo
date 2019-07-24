package bbt.com.paypaldemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import bbt.com.paypaldemo.helper.PrefUtils;
import bbt.com.paypaldemo.model.PaymentRequest;
import bbt.com.paypaldemo.viewModel.MainActivityViewModel;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener, MainActivityViewModel.MainActivityViewModelCallBack {

    private EditText edtAmount;
    private Button btnPay;
    private MainActivityViewModel viewModel;
    private String nonce;
    private LinearLayout container;
    private int Drop_IN_REQ = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
    }

    private void init() {
        registerListener();
        viewModel = new MainActivityViewModel(this, this);
        viewModel.getAccessToken();
    }

    private void registerListener() {
        btnPay.setOnClickListener(this);
    }

    private void initView() {
        edtAmount = (EditText) findViewById(R.id.edtAmount);
        btnPay = (Button) findViewById(R.id.btnPay);
//        container = (LinearLayout) findViewById(R.id.layoutContainer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPay:
                if (TextUtils.isEmpty(edtAmount.getText().toString())) {
                    Toast.makeText(this, "please enter amount", Toast.LENGTH_SHORT).show();
                    break;
                }

                DropInRequest dropInRequest = new DropInRequest()
                        .clientToken(PrefUtils.getPayPalAccessToken(Main2Activity.this))
                        .amount(edtAmount.getText().toString().trim());
                     /*   .requestThreeDSecureVerification(Settings.isThreeDSecureEnabled(this))
                        .collectDeviceData(Settings.shouldCollectDeviceData(this))
                        .googlePaymentRequest(getGooglePaymentRequest())
                        .androidPayCart(getAndroidPayCart())
                        .androidPayShippingAddressRequired(Settings.isAndroidPayShippingAddressRequired(this))
                        .androidPayPhoneNumberRequired(Settings.isAndroidPayPhoneNumberRequired(this))*/
                startActivityForResult(dropInRequest.getIntent(this), Drop_IN_REQ);
                break;
        }
    }

    @Override
    public void onSuccess(String token) {
        Toast.makeText(this, "Success see log", Toast.LENGTH_SHORT).show();
        PrefUtils.setPayPalAccessToken(this, token);
    }

    @Override
    public void onError(String err) {
        Log.e("onError: ", err );
        Toast.makeText(this, "Error see log", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentSuccess(String result) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        Log.e("onPaymentSuccess:", result);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == Drop_IN_REQ) {
            DropInResult dropInResult = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
            PaymentMethodNonce paymentMethodNonce = dropInResult.getPaymentMethodNonce();
            nonce = paymentMethodNonce.getNonce();
            viewModel.doPayment(new PaymentRequest(nonce, Double.valueOf(edtAmount.getText().toString().trim())));
        }

    }
}

/*

//                todo ==> refer for getting nonce==> https://developers.braintreepayments.com/guides/client-sdk/setup/android/v3
                try {

//                    FragmentTransaction fragmentTransaction = (Main2Activity.this).getSupportFragmentManager().beginTransaction();


                        BraintreeFragment braintreeFragment = BraintreeFragment.newInstance(this, PrefUtils.getPayPalAccessToken(this));

                    */
/*if (braintreeFragment != null) {
                        fragmentTransaction.replace(R.id.layoutContainer, braintreeFragment);
                        fragmentTransaction.commit();
                    }*//*

//                    getSupportFragmentManager().beginTransaction().add(R.id.layoutContainer,braintreeFragment);

                        braintreeFragment.addListener(new PaymentMethodNonceCreatedListener() {
@Override
public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        nonce = paymentMethodNonce.getNonce();
        viewModel.doPayment(new PaymentRequest(nonce, Double.valueOf(edtAmount.getText().toString().trim())));

        }
        });
        } catch (InvalidArgumentException e) {
        e.printStackTrace();
        Log.e("Braintree Exception", e.toString());
        }
*/
