
import java.util.ArrayList;



public class Product{

    private static ArrayList<String> productTypes = new ArrayList<>();

    static {
        productTypes.add("fruit");
        productTypes.add("vegetable");
        productTypes.add("other");
    }

    private String name;
    private double price;
    private String type;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws IllegalArgumentException {
        if (descriptionValidating(description)) {
            this.description = description;
        }
    }

    public Product(String name, String price, String type, String description) throws Exception {
        nameValidating(name);
        priceValidation(price);
        typeValidation(type);
        descriptionValidating(description);

        price = formatPrice(price);

        this.name = name.trim().toLowerCase();
        this.price = Double.parseDouble((price.trim()));
        this.type = type;
        this.description = description.trim().toLowerCase();


    }

    public Product(String name, String price, String type) throws Exception {

        this(name, price, type, "");


    }

    public Product(String name, String prise) throws Exception {
        this(name, prise, "other");

    }

    public Product(String name) throws Exception {
        this(name, "0.00", "other");

    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if (nameValidating(name)) {
            this.name = name;
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(String price) throws Exception {
        priceValidation((price));
        price = formatPrice(price);
        this.price = Double.parseDouble(price);

    }

    public static boolean nameValidating(String name) throws Exception {
        String validatedName = name.toLowerCase().trim();
        if (validatedName.isEmpty()) throw new Exception("name can't be empty");
        //checking if any of chars in name is not a letter
        for (char letter : validatedName.toCharArray()) {
            if (name.isEmpty() || (letter < 97 && letter != 32) || letter > 122) {
                throw new Exception("wrong input of name, must contain only letters and spaces");
            }
        }
        return true;
    }

    public static boolean descriptionValidating(String description) throws IllegalArgumentException {
        description = description.toLowerCase();
        char[] descriptionToCharArray = description.toCharArray();
        for (char symbol : descriptionToCharArray) {
            if ((symbol < 97 && symbol != 32) || symbol > 122)
                throw new IllegalArgumentException("wrong input of description, must contain only letters and spaces");
        }
        return true;

    }

    public static boolean typeValidation(String type) throws IllegalArgumentException {
        for (String t : getProductTypes()) {
            if (t.equals(type.trim().toLowerCase())) {
                return true;
            }
        }
        throw new IllegalArgumentException("there is no such type");

    }

    public static void priceValidation(String price) throws Exception {
        try {
            if (price.isEmpty()) throw new IllegalArgumentException("Input must not be empty");
            double priceDouble;
            price = formatPrice(price);
            priceDouble = Double.parseDouble(price);
            if ((priceDouble <= 0)) throw new IllegalArgumentException("Price must be more then 0");
        } catch (NumberFormatException exp) {
            throw new Exception("Wrong format. Price can't contain letters");
        }

    }

   //kollar om priset innehåller ',' symbol och ersätter den mot '.'
    public static String formatPrice(String price) {
        price = price.trim();
        char[] priceDecimalSymbol = price.toCharArray();
        for (int i = 0; i < priceDecimalSymbol.length; i++) {
            if (priceDecimalSymbol[i] == ',') {
                priceDecimalSymbol[i] = '.';
            }
        }
        price = "";

        for (char symbol : priceDecimalSymbol) {
            price += symbol;
        }

        return price;
    }

    public static ArrayList<String> getProductTypes() {
        return productTypes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) throws IllegalArgumentException {
        if (typeValidation(type)) this.type = type;
    }

    @Override
    public String toString() {
        return "Product " +
                "name='" + name + '\'' +
                ", price=" + price +
                ", type=" + type
                ;
    }

    public void print() {
        //console color settings
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_RESET = "\u001B[0m";

        String format = ANSI_YELLOW + " type: %-12s\t Name: %-25s\t Price: %-7.2f%n" + ANSI_RESET;
        System.out.printf(format, type, name, price, description);
    }

    public void printAdmin() {
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_RESET = "\u001B[0m";

        String format = ANSI_YELLOW + " type: %-12s\t Name: %-25s\t Price: %-7.2f Description: %-20s\t%n" + ANSI_RESET;
        System.out.printf(format, type, name, price, description);
    }
}
