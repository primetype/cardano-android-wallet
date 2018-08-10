package uk.co.primetype.adawallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

import io.github.novacrypto.bip39.Words;

public class MainActivity extends AppCompatActivity {

    private Wallet wallet;
    private Account account;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.wallet = null;
        this.account = null;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Generate Wallet button */
    public void generateWallet(View view) {
        String password = this.getWalletPassword();
        Words size = this.getWalletSize();


        this.wallet = new Wallet("Default Wallet", password, size);
        this.account = wallet.newAccount("Main Account");
        Button button = (Button) findViewById(R.id.addAddress);
        button.setVisibility(Button.VISIBLE);

        this.setWalletMnemonics();
    }

    public void generateAddress(View view) {
        LinearLayout addressesView = (LinearLayout) findViewById(R.id.addressesLayout);

        String[] addresses = this.account.generateAddresses(true, this.index, 1);
        String address = addresses[0];

        this.index += 1;

        TextView tv = new TextView(this);
        tv.setText(address);
        tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        addressesView.addView(tv, 0);
    }

    private void setWalletMnemonics() {
        TextView tv = (TextView) findViewById(R.id.walletMnemonics);
        String mnemonics = this.wallet.mnemonics();
        tv.setText(mnemonics);
    }

    private String getWalletPassword() {
        EditText password1Text = (EditText) findViewById(R.id.walletPassword);
        String password = password1Text.getText().toString();
        return password;
    }

    private Words getWalletSize() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.walletMnemonicSize);
        Words size = Words.FIFTEEN;

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.walletMnemonicSize12:
                size = Words.TWELVE;
                break;
            case R.id.walletMnemonicSize15:
                size = Words.FIFTEEN;
                break;
            case R.id.walletMnemonicSize18:
                size = Words.EIGHTEEN;
                break;
            case R.id.walletMnemonicSize21:
                size = Words.TWENTY_ONE;
                break;
            case R.id.walletMnemonicSize24:
                size = Words.TWENTY_FOUR;
                break;
            default: break;
        }

        return size;
    }
}
