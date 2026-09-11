package Digital_Wallet_Management_System;

import java.text.NumberFormat;
import java.util.Locale;

public class AmountFormatter {

    private AmountFormatter() {
        // Prevent object creation
    }

    public static String format(double amount) {

        NumberFormat formatter =
                NumberFormat.getNumberInstance(
                        new Locale("en", "IN")
                );

        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);

        return formatter.format(amount);
    }

    public static String formatWithRupee(double amount) {
        return "₹" + format(amount);
    }
}