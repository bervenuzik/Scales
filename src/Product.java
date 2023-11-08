
import java.io.Serializable;
import java.util.ArrayList;


public class Product implements Serializable {

    private static final ArrayList<String> productCategories = new ArrayList<>();

    public static enum PurchaseTypes {
        KILO("Kg"),
        PIECE("Pcs");

        public final String value;

        PurchaseTypes(String value) {
            this.value = value;
        }
    }

    private String name;
    private double price;
    private String category;
    private String description;
    private int discount;
    private double promotionAmount;
    private double promotionPrice;
    private PurchaseTypes purchaseType;

    public PurchaseTypes getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(PurchaseTypes purchaseType) {
        this.purchaseType = purchaseType;
    }


    static {
        productCategories.add("fruit");
        productCategories.add("vegetable");
        productCategories.add("other");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws IllegalArgumentException {
        if (descriptionValidating(description)) {
            this.description = description;
        }
    }

    public Product(String name, String price, String category, String description, PurchaseTypes type) throws IllegalArgumentException {
        nameValidating(name);
        priceValidation(price);
        typeValidation(category);
        descriptionValidating(description);

        price = formatPrice(price);

        this.name = name.trim().toLowerCase();
        this.price = Double.parseDouble((price.trim()));
        this.category = category;
        this.description = description.trim().toLowerCase();
        this.discount = 0;
        this.promotionAmount = 0.0;
        this.promotionPrice = 0.0;
        this.purchaseType = type;
    }

    public Product(String name, String price, String category) throws IllegalArgumentException {

        this(name, price, category, "", PurchaseTypes.PIECE);

    }

    public Product(String name, String price) throws IllegalArgumentException {
        this(name, price, "other", "", PurchaseTypes.PIECE);

    }

    public Product(String name) throws IllegalArgumentException {
        this(name, "0.00", "other", "", PurchaseTypes.KILO);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {
        if (nameValidating(name)) {
            this.name = name;
        }
    }

    public double getPrice() {
        return this.price;
    }

    public double calculatePrice(double customerAmount) {
        if (discount != 0 && promotionAmount == 0 && promotionPrice == 0) {
            return (this.price * ((double) (100 - discount) / 100)) * customerAmount;
        }
        if (discount == 0 && promotionAmount != 0 && promotionPrice != 0) {
            if (customerAmount % promotionAmount == 0)
                return (customerAmount / promotionAmount) * promotionPrice;  //if customer want to buy same amount like promotion or X much more
            if (customerAmount % promotionAmount == customerAmount)
                return customerAmount * this.price; //if customer want to buy less then promotion say
            if (customerAmount % promotionAmount > 0 && customerAmount % promotionAmount != customerAmount)
                return (int) (customerAmount / promotionAmount) * promotionPrice + ((customerAmount % promotionAmount) * this.price); //if customer want to buy nore then promotion say but not enough to next promotion
        }
        return this.price * customerAmount;
    }

    public void setPrice(String price) throws NullPointerException , NumberFormatException {
        priceValidation((price));
        price = formatPrice(price);
        this.price = Double.parseDouble(price);

    }

    public static boolean nameValidating(String name) throws IllegalArgumentException {
        String validatedName = name.toLowerCase().trim();
        if (validatedName.isEmpty()) throw new IllegalArgumentException("Name can't be empty");
        for (char letter : validatedName.toCharArray()) {
            if (name.isEmpty() || (letter < 97 && letter != 32) || letter > 122) {
                throw new IllegalArgumentException("Wrong input of name, must contain only letters and spaces");
            }
        }
        return true;
    }

    public static boolean descriptionValidating(String description) throws IllegalArgumentException {
        description = description.toLowerCase();
        char[] descriptionToCharArray = description.toCharArray();
        for (char symbol : descriptionToCharArray) {
            if ((symbol < 97 && symbol != 32) || symbol > 122)
                throw new IllegalArgumentException("Wrong input of description, must contain only letters and spaces");
        }
        return true;

    }

    public static boolean typeValidation(String type) throws IllegalArgumentException {
        for (String t : getProductCategories()) {
            if (t.equals(type.trim().toLowerCase())) {
                return true;
            }
        }
        throw new IllegalArgumentException("There is no such type");

    }

    public static void priceValidation(String price) throws IllegalArgumentException {
        try {
            if (price.isEmpty()) throw new IllegalArgumentException("Input must not be empty");
            double priceDouble;
            price = formatPrice(price);
            priceDouble = Double.parseDouble(price);
            if ((priceDouble <= 0)) throw new IllegalArgumentException("Price must be more then 0");
        } catch (NumberFormatException exp) {
            throw new IllegalArgumentException("Wrong format. Price can't contain letters");
        }
    }

    public static void discountValidation(String discount) throws IllegalArgumentException {
        int integerCheck;
        try {
            if (discount.isEmpty()) throw new IllegalArgumentException("Input must not be empty");
            integerCheck = Integer.parseInt(discount);
            if (integerCheck < 0 || integerCheck >= 100)
                throw new IllegalArgumentException("Discount must be more or equal 0 and less then 100");

        } catch (NumberFormatException exp) {
            throw new IllegalArgumentException("Wrong format. Discount can't contain letters and must be an integer");
        }

    }

    public static void promotionValidation(String promotionalAmount, String promotionalPrice, double currentPrice) throws IllegalArgumentException {
        double quantityDouble;
        double promotionalpriceDouble;

        try {

            if (promotionalAmount.isEmpty()) throw new IllegalArgumentException("Quantity must not be empty");
            if (promotionalPrice.isEmpty()) throw new IllegalArgumentException("Promotional price must not be empty");

            quantityDouble = Double.parseDouble(promotionalAmount);
            if ((quantityDouble < 0)) throw new IllegalArgumentException("Quantity must be more then 0 or equal");

            promotionalpriceDouble = Double.parseDouble(promotionalPrice);
            if ((promotionalpriceDouble < 0))
                throw new IllegalArgumentException("Promotional price  must be more or equal 0");
            if (promotionalpriceDouble > (currentPrice * quantityDouble))
                throw new IllegalArgumentException("Promotional price must be less then standard price for the same quantity");


        } catch (NumberFormatException exp) {
            throw new IllegalArgumentException("Wrong format. Discount can contain only integer digits");
        }

    }

    public int getDiscount() {
        return this.discount;
    }

    public double getPromotionPrice() {
        return this.promotionPrice;
    }

    public double getPromotionAmount() {
        return this.promotionAmount;
    }

    public void setDiscount(String discount) throws IllegalArgumentException {
        int discountInteger;
        discountValidation(discount);
        discountInteger = Integer.parseInt(discount);
        this.discount = discountInteger;
        if (this.promotionAmount > 0 || this.promotionPrice > 0) this.clearPromotion();

    }

    public void setPromotion(String newPromotionAmount, String newPromotionPrice, double currentPrice) throws IllegalArgumentException {

        promotionValidation(newPromotionAmount, newPromotionPrice, currentPrice);

        this.promotionPrice = Double.parseDouble(newPromotionPrice);
        this.promotionAmount = Double.parseDouble(newPromotionAmount);
        if (this.discount > 0) this.clearDiscount();

    }

    private void clearPromotion() {
        this.promotionPrice = 0.0;
        this.promotionAmount = 0.0;
    }

    private void clearDiscount() {
        this.discount = 0;
    }

    public static String formatPrice(String price) {
        return price.trim().replace(',', '.');
    }

    public static ArrayList<String> getProductCategories() {
        return productCategories;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) throws IllegalArgumentException {
        if (typeValidation(category)) this.category = category;
    }

    @Override
    public String toString() {
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_RESET = "\u001B[0m";
        if (discount != 0 && promotionAmount == 0 && promotionPrice == 0)
            return String.format(ANSI_YELLOW + " Category: %-12s\t Name: %-20s\t Price: %7.2f/%-5s Discount: %-3d%%" + ANSI_RESET, category, name, price, purchaseType.value, discount);
        if (discount == 0 && promotionAmount != 0 && promotionPrice != 0)
            return String.format(ANSI_YELLOW + " Category: %-12s\t Name: %-20s\t Price: %7.2f/%-5s Promotion: %3.2f/%-4s for %-10.2f" + ANSI_RESET, category, name, price, purchaseType.value, promotionAmount, purchaseType.value, promotionPrice);
        return String.format(ANSI_YELLOW + " Category: %-12s\t Name: %-20s\t Price: %7.2f/%-5s" + ANSI_RESET, category, name, price, purchaseType.value);
    }


    public void printAdmin() {
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_RESET = "\u001B[0m";

        String format = ANSI_YELLOW + " Category: %-12s\t Name: %-20s\t Price: %7.2f/%5s\tDescription: %-20s\t Discount: %3d%% \t  Promotion(pieces): %-3.2f \t (Price): %-10.2f %n" + ANSI_RESET;
        System.out.printf(format, category, name, price, purchaseType.value, description, discount, promotionAmount, promotionPrice);
    }

    public String writeReceptFormat(double amount) {
        double totalPrice = calculatePrice(amount);
        if (discount != 0 && promotionAmount == 0 && promotionPrice == 0) {
            return String.format("Name: %-20s\t Price: %-7.2f\t Discount: %3d%%  Total price: %-10.2f for %7.2f %-7s %n", name, this.price, discount, totalPrice, amount, purchaseType.value);
        }
        if (discount == 0 && promotionAmount != 0 && promotionPrice != 0) {
            return String.format("Name: %-20s\t Price: %-7.2f\t Promotion(pieces): %-3.2f for %-10.2f  Total price: %-10.2f for %7.2f %-7s %n", name, this.price, promotionAmount, promotionPrice, totalPrice, amount, purchaseType.value);
        }
        return String.format("Name: %-20s\t Price: %-7.2f  Total price: %-10.2f for %7.2f %-7s %n", name, this.price, totalPrice, amount, purchaseType.value);
    }

    public void setProductType(PurchaseTypes newType) {
        this.purchaseType = newType;
    }
}



