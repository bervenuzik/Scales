import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

public class ShoppingBasket {
    final static String OS_NAME = System.getProperty("os.name");
    final static String USERNAME = System.getProperty("user.name");
    final static String RECEPTS_FILE_PATH = OS_NAME.startsWith("Windows")? "C:\\Scales\\Recepts\\" : "/Users/"+USERNAME+"/Documents/Scales/Recepts/";

    private static  ShoppingBasket Basket;
    private final LinkedHashMap<Product , Double> basketMap = new LinkedHashMap<>();

    private  ShoppingBasket(){
    }

    public static ShoppingBasket getInstance (){
        if (Basket == null) return new ShoppingBasket();
        return Basket;
    }

    public void addProductToBasket(Product product , double amount){
        if(basketMap.containsKey(product)) {
            double previousValue = basketMap.get(product);
            basketMap.replace(product,  previousValue  , previousValue +amount);
        }else {
            basketMap.put(product, amount);
        }
    }
    public boolean deleteProductFromBasket(Product product) throws Exception{
        if(basketMap.containsKey(product)) {
            basketMap.remove(product);
            System.out.println("Product is deleted from basket");
            return true;
        }else{
            throw new Exception("There is no such product in basket yet");
        }
    }
    private void clearBasket(){
        this.basketMap.clear();
    }

    public void doPurchase( ) throws Exception{
        try {
            createRecept();
            clearBasket();
        }catch (Exception exp){
            throw new Exception(exp.getMessage());
        }

    }
    public boolean isEmpty(){
        return basketMap.isEmpty();
    }

    private void createRecept() throws IOException {
        try {
            createReceptsDirectory();
            double totalToPay = 0;
            LocalDateTime myDateObj = LocalDateTime.now();
            //creating a file for a recept
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mmss-n");
            String formattedDate = myDateObj.format(myFormatObj);
            FileOutputStream fileOutputStream = new FileOutputStream(RECEPTS_FILE_PATH + formattedDate + ".txt");
            //write in a date in recept
            myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-n");
            formattedDate = myDateObj.format(myFormatObj) + "\n";
            fileOutputStream.write(formattedDate.getBytes());
            //write in products from basket to recept
            for (Product product : basketMap.keySet()) {
                fileOutputStream.write(product.writeReceptFormat(basketMap.get(product)).getBytes());
                totalToPay += product.calculatePrice(basketMap.get(product));
            }
            String totalString = String.format("Total prise to pay: %-7.2f %n" , totalToPay);
            fileOutputStream.write(totalString.getBytes());
            fileOutputStream.close();
            System.out.println("Receipt is created.");
        }catch (FileNotFoundException exp){
            throw new FileNotFoundException("File for recepts wasn't found");
        }
    }

    private static void createReceptsDirectory(){
        Path path = Path.of(RECEPTS_FILE_PATH);
        try {
            if(!Files.exists(path)) {
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String toString() {
        return basketMap.toString();
    }

    public void printBasket() throws Exception{
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_RESET = "\u001B[0m";
        if(isEmpty()) throw new Exception("Basket is empty ");
        double totalPriseToPay = 0;
        System.out.print(ANSI_YELLOW);
        for (Product prod: basketMap.keySet()) {

            double prodPrice = prod.calculatePrice(basketMap.get(prod));
            double prodAmount = basketMap.get(prod);
            //String basketProductPrintFormat = String.format( ANSI_YELLOW + "%-20s Amount: %-7.2f Total prise: %-7.2f" + ANSI_RESET,prod.toString() , prodAmount , prodPrice );
            System.out.println(prod.writeReceptFormat(prodAmount));
            totalPriseToPay += prod.calculatePrice(basketMap.get(prod));
        }
        System.out.printf("Total prise to pay : %-7.2f %n", totalPriseToPay);
        System.out.print(ANSI_RESET);
    }
}



