
import java.util.ArrayList;



public class Product{

    private static ArrayList<String> productTypes = new ArrayList<>();

    private static enum PurchaseTypes {
        KILO,
        PIECE,
    }

    private String name;
    private double price;
    private String type;
    private String description;
    private int discount;
    private double[] promotion;

    public PurchaseTypes getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(PurchaseTypes purchaseType) {
        this.purchaseType = purchaseType;
    }

    private PurchaseTypes purchaseType;
    static {
        productTypes.add("fruit");
        productTypes.add("vegetable");
        productTypes.add("other");
    }

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
        this.discount = 0;
        this.promotion = new double[]{0.0 , 0.0};
        this.purchaseType = PurchaseTypes.KILO;
    }

    public Product(String name, String price, String type) throws Exception {

        this(name, price, type, "");

    }

    public Product(String name, String prise) throws Exception {
        this(name, prise, "other","");

    }

    public Product(String name) throws Exception {
        this(name, "0.00", "other","");

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

        return this.price;



    }
    public double calculatePrice ( double customerAmount ){



        if (discount != 0 && promotion[0] == 0 && promotion[1] == 0){
            return this.price * (double)discount/100;
        }
        if(discount == 0 && promotion[0] != 0 && promotion[1] != 0){
            if (customerAmount == promotion[0]) return promotion[1];
            if (customerAmount % promotion[0] == 0) return promotion[1] * customerAmount;
            if (customerAmount % promotion[0] > 0) return  Math.floor(customerAmount / promotion[0] + (this.price * customerAmount % promotion[0]));
            if (customerAmount % promotion[0] == promotion[0]) return customerAmount * this.price;
        }

        return this.price * customerAmount;
    }

    public void setPrice(String price) throws Exception {
        priceValidation((price));
        price = formatPrice(price);
        this.price = Double.parseDouble(price);

    }

    public static boolean nameValidating(String name) throws IllegalArgumentException {
        String validatedName = name.toLowerCase().trim();
        if (validatedName.isEmpty()) throw new IllegalArgumentException("name can't be empty");
        //checking if any of chars in name is not a letter
        for (char letter : validatedName.toCharArray()) {
            if (name.isEmpty() || (letter < 97 && letter != 32) || letter > 122) {
                throw new IllegalArgumentException("wrong input of name, must contain only letters and spaces");
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
            if ((integerCheck < 0)) throw new IllegalArgumentException("Discount must be more or equal 0");
        } catch (NumberFormatException exp) {
            throw new IllegalArgumentException("Wrong format. Discount can't contain letters and must be an integer");
        }

    }

    public static void promotionValidation(String[] promotion , double currentPrice) throws IllegalArgumentException {
        String quantity  = promotion[0];
        String promotionalPrice = promotion[1];
        double quantityDouble;
        double promotionalpriceDouble;

        try {

                if (quantity.isEmpty()) throw new IllegalArgumentException("Quantity must not be empty");
                if (promotionalPrice.isEmpty()) throw new IllegalArgumentException("Promotional price must not be empty");

                quantityDouble = Double.parseDouble(quantity);
                if ((quantityDouble <= 0)) throw new IllegalArgumentException("Quantity must be more then 0");

                promotionalpriceDouble = Double.parseDouble(promotionalPrice);
                if ((promotionalpriceDouble < 0)) throw new IllegalArgumentException("Promotional price  must be more or equal 0");
                if(promotionalpriceDouble < (currentPrice * quantityDouble)) throw new IllegalArgumentException("Promotional price must be less then standard price for the same quantity");


        } catch (NumberFormatException exp) {
            throw new IllegalArgumentException("Wrong format. Discount can contain only integer digits");
        }

    }
    public  int getDiscount (){
        return this.discount;
    }
    public  double[] getPromotion (){
        return this.promotion;
    }
    public void setDiscount(String discount) throws  IllegalArgumentException{
        int discountInteger;
        discountValidation(discount);
        discountInteger = Integer.parseInt(discount);
        this.discount = discountInteger;
        if(this.promotion[0] > 0 || this.promotion[1] > 0 ) this.clearPromotion();

    }

    public void setPromotion(String[] newPromotion, double currentPrice) throws IllegalArgumentException{

        promotionValidation(newPromotion , currentPrice);

        this.promotion[0] = Double.parseDouble(newPromotion[0]);
        this.promotion[1] = Double.parseDouble(newPromotion[1]);
        if(this.discount > 0) this.clearDiscount();

    }
    private void clearPromotion (){
        this.promotion[0] = 0.0;
        this.promotion[1] = 0.0;
    }
    private void clearDiscount(){
        this.discount = 0;
    }

    public static String formatPrice(String price) { //kollar om priset innehåller ',' symbol och ersätter den mot '.'
        price = price.trim();
        char[] priceDecimalSymbol = price.toCharArray();
        for (int i = 0; i < priceDecimalSymbol.length; i++) {
            if (priceDecimalSymbol[i] == ',') priceDecimalSymbol[i] = '.';
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
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_RESET = "\u001B[0m";

        return  String.format(ANSI_YELLOW + " type: %-12s\t Name: %-25s\t Price: %-7.2f%n" + ANSI_RESET,type, name, price);
    }

    public void print() {
        System.out.printf(toString());
    }


    public void printAdmin() {
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_RESET = "\u001B[0m";

        String format = ANSI_YELLOW + " type: %-12s\t Name: %-25s\t Price: %-7.2f Description: %-20s\t%n" + ANSI_RESET;
        System.out.printf(format, type, name, price, description);
    }


}
