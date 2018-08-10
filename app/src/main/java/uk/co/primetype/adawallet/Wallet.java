package uk.co.primetype.adawallet;

import java.security.SecureRandom;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.SeedCalculator;
import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.wordlists.English;

final class Wallet {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private class MnemonicBuilder implements MnemonicGenerator.Target {
        public String output;
        public MnemonicBuilder() {
            this.output = new String();
        }
        public void append(final CharSequence string) {
            this.output = this.output + string;
        }
    }

    /**
     * Create a new wallet, generating a new seed.
     *
     * @param name the alias to give to the wallet
     * @param passphrase the wallet passphrase
     * @param wordSize the mnemonic phrase length (in words)
     */
    public Wallet(String name, String passphrase, Words wordSize) {
        SecureRandom random = new SecureRandom();

        this.walletName = name;

        this.entropy = new byte[wordSize.byteLength()];
        random.nextBytes(this.entropy);

        MnemonicBuilder sb = new MnemonicBuilder();
        new MnemonicGenerator(English.INSTANCE)
                .createMnemonic(this.entropy, sb);

        byte[] seed = new SeedCalculator().calculateSeed(sb.output, passphrase);
        assert seed.length == 64;

        this.walletPtr = createWalletFromSeed(seed, protocolMagic);

        this.accounts = new Account[10];
    }

    /**
     * Free internal native allocated memory, do not use object following this call.
     */
    public void drop() {
        if (this.walletPtr != 0) {
            deleteWallet(this.walletPtr);
            this.walletPtr = 0;
        }
    }

    /**
     * retrieve the mnemonics associated to this given Wallet
     *
     * @return Mnemonics String (in English)
     */
    public String mnemonics() {
        MnemonicBuilder sb = new MnemonicBuilder();
        new MnemonicGenerator(English.INSTANCE)
                .createMnemonic(this.entropy, sb);
        return sb.output;
    }

    /**
     * Create a new account with the given alias
     *
     * This function stores a collection of the created accounts.
     * The account indices are automatically generated using sequential indexing starting from 0.
     *
     * TODO: if account alias already set, do not create it again, returns the existing instance
     *
     * @param accountAlias the alias to give to this account
     * @return the newly create account
     */
    public Account newAccount(String accountAlias) {
        long accountPtr = createAccount(this.walletPtr, accountAlias, this.accounts.length);
        Account account = new Account(this, accountPtr, accountAlias);
        this.accounts[this.accounts.length - 1] = account;

        return account;
    }

    private native long createWalletFromSeed(byte[] seed, int protocolMagic);
    private native void deleteWallet(long wallet);
    private native long createAccount(long wallet, String alias, int index);

    private String walletName = "Wallet";
    private Account[] accounts;
    private long walletPtr = 0;
    private byte[] entropy;

    private final static int protocolMagic;
    static {
        protocolMagic = 0x2D964A09;
    }
}
