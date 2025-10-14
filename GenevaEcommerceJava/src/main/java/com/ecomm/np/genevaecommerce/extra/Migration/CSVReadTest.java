package com.ecomm.np.genevaecommerce.extra.Migration;
import java.io.*;
import java.util.*;

/**
 * @author : Asnit Bakhati
 */

public class CSVReadTest {

    public List<OrderItemAuditMapper> mapCSV() throws Exception {
        String filePath = "output.csv"; // update your path
        List<OrderItemAuditMapper> items = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");

                if (values.length < 9) continue; // skip incomplete lines
                OrderItemAuditMapper item = new OrderItemAuditMapper(
                        parseIntSafe(values[0]),        // orderId (order_tracer_code)
                        parseBooleanSafe(values[1]),    // isActive
                        parseBooleanSafe(values[3]),    // isPacked
                        parseBooleanSafe(values[2]),    // isDelivered
                        parseIntSafe(values[5]),        // quantity
                        values[8].equalsIgnoreCase("NULL") ? "" : values[8].trim(), // size
                        parseFloatSafe(values[4]),      // itemPrice
                        parseNullableInt(values[6]),    // orderItemId (NULL -> 0)
                        parseNullableInt(values[7])     // itemCode (NULL -> 0)
                );
                System.out.println(item.toString());
                items.add(item);
            }
            return items;
        } catch (IOException e) {
           throw new Exception();
        }
    }

    private static int parseIntSafe(String s) {
        s = s.trim();
        return (s.equalsIgnoreCase("NULL") || s.isEmpty()) ? 0 : Integer.parseInt(s);
    }

    private static float parseFloatSafe(String s) {
        s = s.trim();
        return (s.equalsIgnoreCase("NULL") || s.isEmpty()) ? 0.0f : Float.parseFloat(s);
    }

    private static int parseNullableInt(String s) {
        s = s.trim();
        return (s.equalsIgnoreCase("NULL") || s.isEmpty()) ? 0 : Integer.parseInt(s);
    }

    private static boolean parseBooleanSafe(String s) {
        s = s.trim();
        if (s.equals("\u0001")) return true;       // SOH = true
        if (s.equals("\0") || s.equals("0") || s.isEmpty()) return false; // false
        return false;
    }
}