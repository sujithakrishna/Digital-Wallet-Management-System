package Digital_Wallet_Management_System;

import java.util.HashMap;
import java.util.Map;

public class WalletStore {

    private static Map<String, Wallet> wallets = new HashMap<>();

    public static void addWallet(Wallet wallet)
    {
        wallets.put(wallet.getUserId(), wallet);
    }

    public static Wallet getWalletByUserId(String userId)
    {
        return wallets.get(userId);
    }

    public static boolean walletExists(String userId)
    {
        return wallets.containsKey(userId);
    }

    public static int getWalletCount()
    {
        return wallets.size();
    }
}