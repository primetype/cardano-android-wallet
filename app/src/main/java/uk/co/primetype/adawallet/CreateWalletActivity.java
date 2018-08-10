package uk.co.primetype.adawallet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import io.github.novacrypto.bip39.Words;

public class CreateWalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
    }

    /** Called when the user taps the New Wallet button */
    public void newWallet(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        EditText editText = (EditText) findViewById(R.id.walletName);
        String walletName = editText.getText().toString();

        EditText password1Text = (EditText) findViewById(R.id.walletPassword1);
        String password = password1Text.getText().toString();
        EditText password2Text = (EditText) findViewById(R.id.walletPassword2);
        String passwordRepeated = password2Text.getText().toString();

        // assert password == passwordRepeated;

        intent.putExtra(WALLET_NAME, walletName);
        intent.putExtra(WALLET_PASSWORD, password);

        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    public final static String WALLET_NAME;
    public final static String WALLET_PASSWORD;
    static {
        WALLET_NAME = "Wallet Name";
        WALLET_PASSWORD = "Wallet Password";
    }

}
