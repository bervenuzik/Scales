
import java.util.ArrayList;


public class Product {

    private static ArrayList<String> productTypes = new ArrayList<>();
    static{
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

    public boolean setDescription(String description) throws IllegalArgumentException{
        if(descriptionValidating(description)) {this.description = description;return true;}
        throw new IllegalArgumentException("wrong input of description, must contain only letters");

    }
    public Product(String name , String price , String type, String description)throws IllegalArgumentException{
        price = formatPrice(price);

        if(nameValidating(name) && priceValidation(price) && typeValidation(type) && descriptionValidating(description)){
            this.name = name.trim().toLowerCase();
            this.price = Double.parseDouble((price.trim()));
            this.type = type;
            this.description = description.trim().toLowerCase();
        }else {
            System.err.println("Try again");
            throw  new IllegalArgumentException("Name and description can contains only letters, price should be double, type should be chosen from list");
        }

    }
    public Product (String name , String price , String type) throws IllegalArgumentException{

        this(name, price,type,"");



    }
    public Product (String name , String prise  )throws IllegalArgumentException{
        this(name,prise,"other");

    }
    public Product (String name)throws IllegalArgumentException{
        this(name,"0.00", "other");

    }
    public String getName() {
        return name;
    }

    public boolean setName(String name) throws IllegalArgumentException {
        if(nameValidating(name)) {this.name = name;return true;}
        throw new IllegalArgumentException("wrong input of name, must contain only letters");
    }

    public double getPrice() {
        return price;
    }

    public boolean setPrice(String price) {
        if(priceValidation((price))) {
            price = formatPrice(price);
            this.price = Double.parseDouble(price);
            return true;
        }
        throw new IllegalArgumentException("wrong input of price");
    }
    public static boolean nameValidating(String name){
        String validatedName = name.toLowerCase().trim();
            //checking if any of chars in name is not a letter
                for (char letter:validatedName.toCharArray()) {
                    if (name.isEmpty() || (letter < 97 && letter !=32) || letter > 122) {
                        return false;
                    }
                }
        return true;

    }

    public static boolean descriptionValidating(String description){
        description = description.toLowerCase();
        char[] descriptionToCharArray = description.toCharArray();
        for (char symbol:descriptionToCharArray) {
            if( (symbol < 97 && symbol !=32) || symbol > 122) return false;
        }
        return true;

    }
    public static boolean typeValidation(String type){
        for (String t:getProductTypes()) {
            if(t.equals(type.trim().toLowerCase())){
                return true;
            }
        }
        return false;
    }
    public static boolean priceValidation(String price){
        try{
            double priceDouble;
            price = formatPrice(price);
            priceDouble = Double.parseDouble(price);
            return !(priceDouble <= 0);
        }catch(NumberFormatException e){
            return false;
        }

    }
    public static String formatPrice(String price){
        price = price.trim();
           char[] priceDecimalSymbol = price.toCharArray();
            for (int i = 0; i < priceDecimalSymbol.length; i++) {
                if (priceDecimalSymbol[i] == ','){
                    priceDecimalSymbol[i] = '.';
                }
            }
            price ="";

            for (char symbol:priceDecimalSymbol) {
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

    public boolean setType(String type) throws IllegalArgumentException {
        if(typeValidation(type)) {this.type = type;return true;}
        throw new IllegalArgumentException("wrong input of type");
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
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_RESET = "\u001B[0m";

        String format = ANSI_YELLOW +" type: %-12s\t Name: %-25s\t Price: %-7.2f%n"+ANSI_RESET;
        System.out.printf(format,type ,name , price, description);
    }
    public void printAdmin(){
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_RESET = "\u001B[0m";

        String format = ANSI_YELLOW +" type: %-12s\t Name: %-25s\t Price: %-7.2f Description: %-20s\t%n"+ANSI_RESET;
        System.out.printf(format,type ,name , price, description);
    }
}
