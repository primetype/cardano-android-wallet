package uk.co.primetype.adawallet;

final class Wallets {
    private Wallet[] wallets;
    private int offset;
    private int length;

    /**
     * The maximum number of wallets supported
     */
    public final static int MAX_LENGTH;
    static {
        MAX_LENGTH = 12;
    }

    Wallets() {
        this.offset = 0;
        this.length = MAX_LENGTH;

        this.wallets = new Wallet[this.length];
    }

    /**
     * Insert a new wallet in the collection
     * @param wallet the wallet to insert in
     */
    public void push(Wallet wallet) {
        if (this.offset == this.length) {
            // TODO: error
        } else {
            this.wallets[this.offset] = wallet;
            this.offset += 1;
        }
    }

    /**
     * Get the array of wallets
     *
     * @return the array of wallets
     */
    public Wallet[] getWallets() { return this.wallets; }
}
