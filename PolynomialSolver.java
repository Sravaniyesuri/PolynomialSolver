import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;
import org.json.JSONObject;
import org.json.JSONTokener;

public class PolynomialSolver {

    public static void main(String[] args) throws Exception {
        // Choose which input file to test
        String fileName = "input1.json"; // change to "input2.json" for second test case

        // Read JSON file
        FileReader reader = new FileReader(fileName);
        JSONObject jsonObject = new JSONObject(new JSONTokener(reader));

        // Read keys (n, k)
        JSONObject keys = jsonObject.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        // Extract roots
        List<BigInteger> roots = new ArrayList<BigInteger>();
        for (String key : jsonObject.keySet()) {
            if (key.equals("keys")) continue;
            JSONObject obj = jsonObject.getJSONObject(key);
            int base = Integer.parseInt(obj.getString("base"));
            String value = obj.getString("value");
            roots.add(new BigInteger(value, base));
        }

        // Sort roots to be consistent
        Collections.sort(roots);

        // Take first k roots
        List<BigInteger> selectedRoots = roots.subList(0, k);

        // Build polynomial coefficients
        // Start with [1] representing polynomial "1"
        List<BigInteger> coeffs = new ArrayList<BigInteger>();
        coeffs.add(BigInteger.ONE);

        for (BigInteger root : selectedRoots) {
            List<BigInteger> newCoeffs = new ArrayList<BigInteger>();
            newCoeffs.add(coeffs.get(0).negate().multiply(root));

            for (int i = 1; i < coeffs.size(); i++) {
                BigInteger term = coeffs.get(i - 1).add(coeffs.get(i).negate().multiply(root));
                newCoeffs.add(term);
            }

            newCoeffs.add(coeffs.get(coeffs.size() - 1));
            coeffs = newCoeffs;
        }

        // Print coefficients (highest degree first)
        System.out.println("Polynomial Coefficients: ");
        for (int i = coeffs.size() - 1; i >= 0; i--) {
            System.out.print(coeffs.get(i) + " ");
        }
        System.out.println();
    }
}
