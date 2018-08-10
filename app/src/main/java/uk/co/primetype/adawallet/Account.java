package uk.co.primetype.adawallet;

final class Account {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * Create an account and set the appropriate value internally. This
     * function should not be used except by `Wallet::createAccount`.
     *
     * @param wallet the wallet where this account belongs (handy for direct access
     *               to the wallet)
     * @param accountPtr the native pointer to the account
     * @param alias the alias to give the account
     */
    public Account(Wallet wallet, long accountPtr, String alias) {
        this.wallet = wallet;
        this.alias = alias;
        this.accountPtr = accountPtr;
    }

    /**
     * get the alias of this account
     *
     * @return the alias of this account
     */
    public String alias() { return this.alias; }

    /**
     * Get the wallet where this account belongs
     *
     * @return the Wallet where this account belongs
     */
    public Wallet wallet() { return this.wallet; }

    /**
     * Generate a set of addresses.
     *
     * @param external BIP44 concept for internal or external addresses
     *                 i.e. an address is internal if it is used to transfer
     *                 assets between addresses of the same account.
     * @param from the index to start to generate the addresses from
     * @param count the number of addresses to generate
     * @return a collection of ready to use addresses
     */
    public String[] generateAddresses(boolean external, int from, int count) {
        return this.generateAddresses(this.accountPtr, external, from, count);
    }

    /**
     * Free internal native allocated memory, do not use object following this call.
     */
    public void drop() {
        if (this.accountPtr != 0) {
            deleteAccount(this.accountPtr);
            this.accountPtr = 0;
        }
    }

    private native void deleteAccount(long account);
    private native String[] generateAddresses(long account, boolean external, int from, int count);

    private Wallet wallet = null;
    private long accountPtr = 0;
    private String alias;
}
