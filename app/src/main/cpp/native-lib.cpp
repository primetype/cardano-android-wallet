#include <jni.h>
#include <string>

typedef void const *WalletPtr;
typedef void const *AccountPtr;

extern "C" {
WalletPtr wallet_new_from_seed(uint8_t const *seed_ptr, const uint32_t protocol_magic);
void wallet_delete(WalletPtr wallet_ptr);

AccountPtr account_create(WalletPtr wallet, char const *account_alias, uint32_t account_index);
size_t account_generate_addresses(AccountPtr account, bool internal, uint32_t from_index,
                                             size_t num_indices, char **addresses);
void account_delete(AccountPtr account);
}

extern "C" JNIEXPORT jlong
JNICALL
Java_uk_co_primetype_adawallet_Wallet_createWalletFromSeed(
        JNIEnv *env,
        jobject /* this */,
        jbyteArray seed,
        jint protocol_magic) {
    // we get the bytes, we don't need to know if it is a copy that has been made.
    jbyte* seed_buffer = env->GetByteArrayElements(seed, NULL);

    WalletPtr wallet = wallet_new_from_seed((uint8_t*)seed_buffer, protocol_magic);

    // release the allocated seed_buffer, we use JNI_ABORT because we don't have data to
    // copy back to the JAVA array
    env->ReleaseByteArrayElements(seed, seed_buffer, 0);

    return (jlong)wallet;
}

extern "C" JNIEXPORT void
JNICALL
Java_uk_co_primetype_adawallet_Wallet_deleteWallet(
        JNIEnv *env,
        jobject /* this */,
        jlong wallet) {
    wallet_delete((WalletPtr)wallet);
}

extern "C" JNIEXPORT jlong
JNICALL
Java_uk_co_primetype_adawallet_Wallet_createAccount(
        JNIEnv *env,
        jobject /* this */,
        jlong wallet,
        jstring alias,
        jint index) {
    char const* account_alias = env->GetStringUTFChars(alias, NULL);

    AccountPtr account_ptr = account_create((WalletPtr)wallet, account_alias, (uint32_t)index);

    env->ReleaseStringUTFChars(alias, account_alias);

    return (jlong)account_ptr;
}
extern "C" JNIEXPORT void
JNICALL
Java_uk_co_primetype_adawallet_Account_deleteAccount(
        JNIEnv *env,
        jobject /* this */,
        jlong account) {
    account_delete((AccountPtr)account);
}
extern "C" JNIEXPORT jobjectArray
JNICALL
Java_uk_co_primetype_adawallet_Account_generateAddresses(
        JNIEnv *env,
        jobject /* this */,
        jlong account,
        jboolean external,
        jint from,
        jint count) {
    AccountPtr account_ptr = (AccountPtr)account;

    char** addresses = (char**)malloc(sizeof(char*) * count);
    jobjectArray ret = (jobjectArray)env->NewObjectArray(
            count,
            env->FindClass("java/lang/String"),
            env->NewStringUTF("<invalid address>"));

    size_t gens = account_generate_addresses(account_ptr, external, from, count, addresses);

    for(jsize i = 0; i < count; i++) {
        char* address = addresses[i];
        jstring addr = env->NewStringUTF(address);
        env->SetObjectArrayElement(ret, i, addr);
        free(address);
    }

    free(addresses);
    return ret;
}