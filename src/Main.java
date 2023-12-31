import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {
    final static String OS_NAME = System.getProperty("os.name");
    final static String USERNAME = System.getProperty("user.name");
    final static String PRODUCTS_FILE_PATH = OS_NAME.startsWith("Windows") ? "C:\\Scales\\" : "/Users/" + USERNAME + "/Documents/Scales/";
    final static String PRODUCTS_FILE = OS_NAME.startsWith("Windows") ? "C:\\Scales\\Products.txt" : "/Users/" + USERNAME + "/Documents/Scales/Products.txt";

    final static ShoppingBasket basket = ShoppingBasket.getInstance();

    static Scanner input = new Scanner(System.in);

    static ArrayList<Product> products = new ArrayList<>();
    static final String PASSWORD = "111";

    public static void main(String[] args) {


        try {
            createProductsFile();
            readProductsFromFile();
        } catch (Exception exp) {
            System.err.println(exp.getMessage());
        }

        boolean exit = false;

        showGreetings();

        while (!exit) {
            showMainMenu();
            try {
                int customerInput = input.nextInt();
                input.nextLine();
                switch (customerInput) {
                    case 1 -> showAllProducts();
                    case 2 -> showProductsOfCategory();
                    case 3 -> findProducts(true);
                    case 4 -> showBasket();
                    case 5 -> addToBasket();
                    case 6 -> doPurchase();
                    case 7 -> administrationMode();
                    case 9 -> exit = true;
                    default -> System.err.println("Value not from list");
                }

            } catch (InputMismatchException exp) {
                System.err.println("Wrong input , write a number");
                System.out.println();
                input.nextLine();
                continue;
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
                continue;
            }
        }

        System.out.println("THANK YOU FOR USING OUR PROGRAM");
    }

    public static void showGreetings() {
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";
        System.out.println(ANSI_GREEN + "Hi and welcome to the Scales program!");
        System.out.println("Now you will get some alternativs for actions :" + ANSI_RESET);
    }

    private static int findProductIndex() {
        boolean isfound = false;
        int index = -1;
        String searchable;
        if (products.isEmpty()) {
            System.err.println("Product list is empty");
            return -1;
        }
        try {
            System.out.println("What product ? Enter a name or 'BACK' to cancel: ");
            searchable = input.nextLine().trim().toLowerCase();

            //if customer want to cancel
            if (isCancel(searchable)) return -1;
            //check if input is empty
            Product.nameValidating(searchable);

            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getName().equals(searchable)) {
                    index = i;
                    isfound = true;
                    break;
                }
            }
            if (!isfound) {
                System.err.println("Such product is not existing");
                return -1;
            }

        } catch (Exception exp) {
            System.err.println(exp.getMessage());
            ;

        }
        return index;
    }

    private static int findProductIndex(Product product) {
        boolean isfound = false;
        int index = -1;
        if (products.isEmpty()) {
            System.err.println("Product list is empty");
            return -1;
        }
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(product.getName())) {
                index = i;
                isfound = true;
                break;
            }
        }
        if (!isfound) {
            System.err.println("Such product is not existing");
            return -1;
        }
        return index;
    }

    public static void showMainMenu() {
        System.out.println();
        System.out.println("Press: ");
        System.out.println("1. To show all products ");
        System.out.println("2. To Show certain type of products");
        System.out.println("3. To search a product");
        System.out.println("4. Show basket");
        System.out.println("5. Add product to basket");
        System.out.println("6. To do purchase");
        System.out.println("7. Activate ADMINISTRATION_MODE");
        System.out.println("\n9. To Exit the program");
    }

    public static void showAllProducts() {
        //print out all products
        printProducts();
    }

    public static void showBasket() {
        try {
            basket.printBasket();
            String checkStatement = "yes";
            System.out.println("If you want to delete any product from basket write '" + checkStatement + "'");
            System.out.println("If you want to go back write something else");
            String customerInput = input.nextLine().trim().toLowerCase();
            if (customerInput.equals(checkStatement)) {
                int index = findProductIndex();
                if (index == -1) return;
                basket.deleteProductFromBasket(products.get(index));
            } else return;

        } catch (Exception exp) {
            System.err.println(exp.getMessage());
            return;
        }

    }

    public static void administrationMode() {
        //initial values
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";
        boolean isAdministrator = false;
        String customerInput;
        int tryAmount = 3;


        System.out.println("Write in a PASSWORD, you have 3 tries: ");


        //checking if customer is not an administrator
        while (!isAdministrator) {
            //take customers input and formatting it
            customerInput = input.nextLine().trim();
            //if password is OK make ADMIN_MODE available
            if (PASSWORD.equals(customerInput)) {
                System.out.println("ADMINISTRATION MODE ON");
                isAdministrator = true;
            } else { //otherwise show warning and
                tryAmount--;
                //if tries more then 3 back to the menu
                if (tryAmount == 0) break;
                System.err.println("WRONG PASSWORD, TRY AGAIN :");
            }
        }

        // checkin if customer is administrator
        while (isAdministrator) {
            int choise;
            System.out.print(ANSI_GREEN);
            try {
                showInstructions();
                //show a menu
                System.out.println("Choose an action , press: ");
                System.out.println("1. To add a product");
                System.out.println("2. To delete a product");
                System.out.println("3. To change information in product");
                System.out.println("4. To show all products");
                System.out.println("9. To exit from ADMINISTRATION MODE");
                //take customer's input
                choise = input.nextInt();
                input.nextLine();
                switch (choise) {
                    case 1 -> addProduct();
                    case 2 -> deleteProduct();
                    case 3 -> changeInfo();
                    case 4 -> printProductsAdmin();
                    case 9 -> {
                        System.out.println(ANSI_RESET + "ADMINISTRATION MODE OFF");
                        return;
                    }
                    default -> System.err.println("Wrong input, try again, choose from list");
                }
            } catch (InputMismatchException exp) {
                System.err.println("Wrong format , write a number");
                System.out.println();
                input.nextLine();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        }


    }

    public static void doPurchase() throws Exception {
        if (basket.isEmpty()) {
            throw new Exception("Basket is empty , add products first");
        }
        try {
            basket.doPurchase();
            System.out.println("Purchase is Successful. Recept is saved");
        } catch (IOException exp) {
            System.err.println("File wasn't found");
        }

    }

    public static void addToBasket() throws Exception {
        //initial values
        String customerInput;
        double amount;
        double result;
        int index;
        Product searchableProduct;
        if (products.isEmpty()) {
            System.err.println("Product list is empty");
            return;
        }

        while (true) {
            try {
                ArrayList<Product> foundProducts;
                foundProducts = findProducts(false);
                if (foundProducts.isEmpty()) {
                    return;
                }
                System.out.println("What product do you want to add , or write back to cancel");
                for (int i = 0; i < foundProducts.size(); i++) {
                    System.out.println(i + 1 + " " + foundProducts.get(i));
                }
                customerInput = input.nextLine().trim().toLowerCase();
                if (isCancel(customerInput)) return;
                index = Integer.parseInt(customerInput) - 1;
                if (!controlMenuNumberInput(customerInput)) continue;
                if (index >= foundProducts.size() || index < 0) {
                    System.err.println("Wrong input");
                    continue;
                }
                searchableProduct = foundProducts.get(index);
                break;
            } catch (IndexOutOfBoundsException exp) {
                System.err.println(exp.getMessage());
                return;
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        }
        while (true) {
            try {
                //ask how much want customer buy
                System.out.println("How many " + searchableProduct.getPurchaseType() + " do you want to buy? write 'BACK' to cancel");
                customerInput = input.nextLine().trim().toLowerCase();
                //check if customer want cancel and go back to previous menu
                if (isCancel(customerInput)) return;
                //check if customers input is correct( price validation is OK for that)

                Product.priceValidation(customerInput);
                amount = Double.parseDouble(Product.formatPrice(customerInput));
                //calculate price of purchase
                result = searchableProduct.calculatePrice(amount);
                System.out.printf("This product will cost you %10.2f SEK for %10.2f %-5s %n", result, amount, searchableProduct.getPurchaseType().value);


                System.out.println("Do tou want to add a product to basket? (yes/no):");
                customerInput = input.nextLine().trim().toLowerCase();
                if (customerInput.equals("yes")) {
                    basket.addProductToBasket(searchableProduct, amount);
                    System.out.println("product is successfully added");
                    return;
                }
                if (customerInput.equals("no")) return;

            } catch (Exception exp) {
                System.err.println(exp.getMessage());
                continue;
            }
        }

    }


    public static void changeInfo() {
        int customerInput;
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";
        int index;
        while (true) {
                //geting a product's index that customer want to work with
                index = findAndChooseProduct("What product do you want to change?");
                // if customer want to exit
                if (index == -1) {
                    return;
                }
                break;
        }

        while (true) {
            System.out.println("Here is your product:");
            products.get(index).printAdmin();
            System.out.println();
            try {
                System.out.print(ANSI_GREEN);
                System.out.println("What kind of info you want to change?");
                System.out.println("1. Category");
                System.out.println("2. Name");
                System.out.println("3. Price");
                System.out.println("4. Description");
                System.out.println("5. Discount");
                System.out.println("6. Promotion");
                System.out.println("7. Type of purchase");
                System.out.println("0. Exit");
                System.out.println("->");

                customerInput = input.nextInt();
                input.nextLine();
                switch (customerInput) {
                    case 1 -> changeCategory(index);
                    case 2 -> changeName(index);
                    case 3 -> changePrice(index);
                    case 4 -> changeDescription(index);
                    case 5 -> changeDiscount(index);
                    case 6 -> changePromotion(index);
                    case 7 -> changePurchaseType(index);
                    case 0 -> {
                        saveProductsToFile();
                        System.out.print(ANSI_RESET);
                        return;
                    }
                    default -> System.err.println("Not a value of list , try again");
                }
                saveProductsToFile();
                System.out.println("Changes are saved");
            } catch (InputMismatchException exp) {
                System.err.println("Wrong format , write a number");
                System.out.println();
                input.nextLine();
            } catch (FileNotFoundException exp){
                System.out.println("Product wasn't updated. File with products is not found");
            }
        }
    }


    private static void changePurchaseType(int index) {
        String userInput;
        int userInputInteger;
        ArrayList<Product.PurchaseTypes> types = new ArrayList<>(Arrays.asList(Product.PurchaseTypes.values()));
        while (true) {
            try {
                System.out.println("Choose a new type or write 'BACK' to cancel");
                for (int i = 0; i < types.size(); i++) {
                    System.out.println(i + 1+". " + types.get(i).value);
                }
                userInput = input.nextLine().trim().toLowerCase();
                if (isCancel(userInput)) return;
                if (!controlMenuNumberInput(userInput, types)) continue;
                userInputInteger = Integer.parseInt(userInput) - 1;
                products.get(index).setProductType(types.get(userInputInteger));
                return;

            } catch (NumberFormatException exp) {
                System.err.println(exp.getMessage());
            }
        }
    }

    private static void changePromotion(int index) {
        String newPromotionAmount;
        String newPromotionPrica;
        double currentPrice = products.get(index).getPrice();
        while (true) {
            try {
                System.out.println("Write a new promotion or or write 'BACK' to cancel");
                System.out.println("How many pieces want you sell in promotion");
                newPromotionAmount = input.nextLine();
                if (isCancel(newPromotionAmount)) return;
                System.out.println("How How much you want them to cost? The price should be less than before promotion");
                newPromotionPrica = input.nextLine();
                if (isCancel(newPromotionPrica)) return;
                products.get(index).setPromotion(newPromotionAmount, newPromotionPrica, currentPrice);
                return;
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
                continue;
            }
        }

    }

    private static void changeDiscount(int index) {

        String newDiscount;
        while (true) {
            try {
                System.out.println("Write a new discount or or write 'BACK' to cancel");
                newDiscount = input.nextLine();
                if (isCancel(newDiscount)) return;
                products.get(index).setDiscount(newDiscount);
                return;
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
                continue;
            }
        }

    }

    private static void changeDescription(int index) {
        String newDescription;

        while (true) {
            try {
                System.out.println("Choose a new Description or or write 'BACK' to cancel");
                newDescription = input.nextLine();

                //check if customer want to cancel
                if (isCancel(newDescription)) return;

                //set new description
                products.get(index).setDescription(newDescription);
                return;
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
                continue;
            }
        }

    }

    public static void changeCategory(int index) {
        String customerInput;
        String newCategory;
        int newCategoryIndex;
        while (true) {

            System.out.println("Choose a new type or or write 'BACK' to cancel");
            printTypes();
            customerInput = input.nextLine().trim().toLowerCase();
            try {
                //check if customer want to cancel
                if (isCancel(customerInput)) return;
                if (!controlMenuNumberInput(customerInput, Product.getProductCategories())) continue;

                //parse customer choice to integer
                newCategoryIndex = Integer.parseInt(customerInput) - 1;
                //get a type with
                newCategory = Product.getProductCategories().get(newCategoryIndex);
                //set new type to product
                products.get(index).setCategory(newCategory);
                return;
            } catch (NumberFormatException exp) {
                System.err.println("Not a value of list , try again");
                continue;
            } catch (Exception exc) {
                System.err.println(exc.getMessage());
                continue;
            }
        }

    }

    public static void changeName(int index) {
        String newName;

        while (true) {
            try {
                System.out.println("Choose a new name or or write 'BACK' to cancel");
                newName = input.nextLine();
                if (isCancel(newName)) return;

                products.get(index).setName(newName);
                return;
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
                continue;
            }
        }
    }

    public static void changePrice(int index) {
        String newPrice;
        while (true) {
            try {
                System.out.println("Write a new price or or write 'BACK' to cancel");
                newPrice = input.nextLine();
                if (isCancel(newPrice)) return;
                products.get(index).setPrice(newPrice);
                return;
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
                continue;
            }
        }
    }

    private static void deleteProduct() {
        int index;
        String massege = "Choose product you want to delete ,or write 'BACK' to cancel ";
        if (products.isEmpty()) {
            System.err.println("List of products is empty is empty");
            return;
        }
        while (true) {
            try {
                index = findAndChooseProduct(massege);
                if (index == -1) return;
                products.remove(index);
                System.out.println("A product is removed , SUCCESS");
                saveProductsToFile();
                return;
            } catch (InputMismatchException exp) {
                System.err.println("Wrong format , write a number");
            } catch (FileNotFoundException exp) {
                System.err.println(exp.getMessage());
            }
        }
    }

    public static void printTypes() {
        ArrayList<String> productsTypes = Product.getProductCategories();
        int size = productsTypes.size();
        for (int i = 0; i < size; i++) {
            System.out.println(i + 1 + " " + Product.getProductCategories().get(i));
        }
    }

    public static void addProduct() {

        String category;
        String name;
        String price;
        String description = "";
        boolean wantAddDescription = false;
        String customerInput;
        Product result;
        int choise;
        Product.PurchaseTypes type;

        while (true) {
            while (true) {
                try {
                    System.out.println("What category of product you want to add, choose from: ");

                    printTypes();
                    System.out.println("'BACK' to cancel");

                    //scan a category of product
                    customerInput = input.nextLine().trim().toLowerCase();
                    // check if customer want to cancel
                    if (isCancel(customerInput)) return;
                    // control input
                    if (!controlMenuNumberInput(customerInput)) continue;
                    //parse string input to integer
                    choise = Integer.parseInt(customerInput);
                    if (choise < 1 || choise > Product.getProductCategories().size())
                        throw new Exception("Value not from list");
                    //get chosen category
                    category = Product.getProductCategories().get(choise - 1);

                } catch (Exception exp) {
                    System.err.println(exp.getMessage());
                    continue;
                }

                break;

            }


            while (true) {
                try {
                    System.out.println("Enter a name of product or BACK to cancel");
                    System.out.println("Name can contain only letters");


                    customerInput = input.nextLine().trim().toLowerCase();
                    if (isCancel(customerInput)) return;
                    //check if input is correct

                    Product.nameValidating(customerInput);
                    //check if such product is already existing and name is correct
                    if (isProductAlreadyExist(customerInput)) continue;


                    name = customerInput;
                } catch (Exception exp) {
                    System.err.println(exp.getMessage());
                    continue;
                }
                break;
            }


            //input of price
            while (true) {
                try {
                    System.out.println("Enter a price (#.##) of product or BACK to cancel");
                    customerInput = input.nextLine().trim().toLowerCase();
                    //check if customer want to cancel
                    if (isCancel(customerInput)) return;

                    Product.priceValidation(customerInput);
                } catch (Exception exp) {
                    System.err.println(exp.getMessage());
                    continue;
                }
                price = Product.formatPrice(customerInput);
                break;

            }


            //if customer want to add a description
            System.out.println("Do you want to add description to product for easier search? Write 'YES' if you want");
            customerInput = input.nextLine().trim().toLowerCase();
            if (customerInput.equals("yes")) wantAddDescription = true;
            //add description
            if (wantAddDescription) {
                while (true) {
                    try {
                        System.out.println("Enter a description to product or write 'BACK' to cancel");
                        customerInput = input.nextLine().trim().toLowerCase();
                        //check if customer want to cancel
                        if (isCancel(customerInput)) return;
                        //check if input is not empty or is not wrong format
                        Product.descriptionValidating(customerInput);

                    } catch (Exception exp) {
                        System.err.println(exp.getMessage());
                        continue;
                    }

                    description = customerInput;
                    //stop the loop
                    break;
                }
            }


            System.out.println("Choose what type of product it is or write 'BACK' if you want go to the main meny");
            while (true) {

                Product.PurchaseTypes[] types = Product.PurchaseTypes.values();
                try {
                    System.out.println("Press:");
                    for (int i = 0; i < types.length; i++) {
                        System.out.println(i + 1 + ". To count product by " + types[i].value.toUpperCase());
                    }
                    customerInput = input.nextLine().trim().toLowerCase();
                    if (isCancel(customerInput)) return;
                    if (!controlMenuNumberInput(customerInput)) continue;
                    if (Integer.parseInt(customerInput) > types.length || Integer.parseInt(customerInput) <= 0) {
                        System.err.println("Value not from list");
                        continue;
                    }
                    type = types[Integer.parseInt(customerInput) - 1];
                    break;
                } catch (Exception exp) {
                    System.err.println(exp.getMessage());
                }

            }


            //try to create a product
            try {
                if (!wantAddDescription) {
                    result = new Product(name, price, category, "", type);
                } else {
                    result = new Product(name, price, category, description, type);
                }
                products.add(result);
                System.out.println("You added a new Product , SUCCESS ");
                saveProductsToFile();
                return;
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        }
    }

    public static void showProductsOfCategory() {
        ArrayList<String> categories = Product.getProductCategories();
        while (true) {
            try {
                System.out.println("Press : ");
                for (int i = 0; i < categories.size(); i++) {
                    System.out.println(i + 1 + ". If you want to print " + categories.get(i));
                }
                System.out.println("Write 'BACK' if you want go to the main meny");

                String customerInput = input.nextLine().trim().toLowerCase();
                if (isCancel(customerInput)) break;
                if (!controlMenuNumberInput(customerInput , categories)) continue;
                printProducts(categories.get((Integer.parseInt(customerInput) - 1)));

            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        }

    }

    public static void printProducts() {
        if (products.isEmpty()) {
            System.err.println("product-list is empty");
            return;
        }

        for (Product product : products) {
            System.out.println(product.toString());
        }

    }
    public static void printProductsAdmin() {
        if (products.isEmpty()) {
            System.err.println("product-list is empty");
            return;
        }

        for (Product product : products) {
            product.printAdmin();
            System.out.println();
        }

    }

    public static void printProducts(String type) {
        boolean isEmpty = true;
        if (products.isEmpty()) {
            System.err.println("product-list is empty");
            return;
        }

        for (Product product : products) {
            if (product.getCategory().equals(type)) {
                System.out.println(product);
                isEmpty = false;
            }

        }
        if (isEmpty) System.err.println("There are no products of such type yet");
    }

    public static boolean isProductAlreadyExist(String name) {
        int index;
        index = getIndexOfProductByName(name);
        if (index > -1) {
            System.err.println("Such element is already existing");
            return true;
        }
        return false;
    }


    public static int getIndexOfProductByName(String name) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(name)) return i;
        }
        return -1; //if product is not found
    }

    public static boolean isCancel(String input) {
        final String submit = "back";
        return input.equals(submit);
    }

    public static boolean controlMenuNumberInput(String customerInput) {
        try {
            Integer.parseInt(customerInput);
        } catch (NumberFormatException exp) {
            System.err.println("Wrong format");
            return false;
        }
        if (customerInput.isEmpty()) {
            System.err.println("Input can't be empty ,try again");
            return false;
        }
        return true;
    }

    public static <T> boolean controlMenuNumberInput(String customerInput, ArrayList<T> arr) {
        int inputInt;
        try {
            inputInt = Integer.parseInt(customerInput);
        } catch (NumberFormatException exp) {
            System.err.println("Wrong format");
            return false;
        }
        if (customerInput.isEmpty()) {
            System.err.println("Input can't be empty ,try again");
            return false;
        }
        if (inputInt > arr.size() || inputInt <= 0) {
            System.err.println("Value not from list");
            return false;
        }
        return true;
    }

    public static void showInstructions() {

        String[] instractions = {
                "Any input can't be empty",
                "Price can contain oly digits and one point",
                "Name must contain only letters and spaces",
                "Description must contain only letters and spaces",
        };
        System.out.println();
        System.out.println("Check an follow instructions to use ADMINISTRATION_MODE");
        for (int i = 0; i < instractions.length; i++) {
            System.out.println((i + 1) + ". " + instractions[i]);
        }
        System.out.println();
    }


    public static ArrayList<Product> findProducts(boolean print) {
        boolean isfound = false;
        String searchable;
        ArrayList<Product> foundProducts = new ArrayList<>();
        if (products.isEmpty()) {
            System.err.println("Product list is empty");
            return foundProducts;
        }
        try {
            System.out.println("What product(s) are you searching? Enter a name or 'BACK' to cancel: ");
            searchable = input.nextLine().trim().toLowerCase();

            if (isCancel(searchable)) return foundProducts;
            Product.nameValidating(searchable);
            for (Product product : products) {
                if (product.getName().contains(searchable) || product.getDescription().contains(searchable)) {
                    foundProducts.add(product);
                    if (print) System.out.println(product);
                    isfound = true;
                }
            }
            if (!isfound) System.err.println("Such product is not existing");
            return foundProducts;
        } catch (Exception exp) {
            System.err.println(exp.getMessage());
            return foundProducts;
        }
    }

    public static void saveProductsToFile() throws FileNotFoundException {
        createProductsFile();
        try (FileOutputStream fileOutputStream = new FileOutputStream(PRODUCTS_FILE)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(products);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File with products is not found");
        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }

    }

    public static boolean readProductsFromFile() {
        try (FileInputStream fileInputStream = new FileInputStream(PRODUCTS_FILE)) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            setProducts((ArrayList) objectInputStream.readObject());
            objectInputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File with products is not found");
            return false;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    public static void createProductsFile() {
        Path path = Path.of(PRODUCTS_FILE_PATH);
        Path file = Path.of(PRODUCTS_FILE);
        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
            if (!Files.exists(file)) {
                Files.createFile(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setProducts(ArrayList<Product> arr) {
        products = new ArrayList<>(arr);
    }

    public static int findAndChooseProduct(String massage) {
        int index;
        String customerInput;
        ArrayList<Product> foundProducts = findProducts(false);
        if (foundProducts.isEmpty()) {
            return -1;
        }
        while (true) {
            for (int i = 0; i < foundProducts.size(); i++) {
                System.out.println(i + 1 + ". " + foundProducts.get(i));
            }
            try {
                System.out.println(massage);
                customerInput = input.nextLine().trim().toLowerCase();
                if (isCancel(customerInput)) return -1;
                index = Integer.parseInt(customerInput) - 1;
            } catch (NumberFormatException exp) {
                System.err.println("Wrong format, write in a number or 'BACK' to cancel");
                continue;
            }
            if (index < 0 || index >= foundProducts.size()) {
                System.err.println("wrong input");
                continue;
            }
            return findProductIndex(foundProducts.get(index));

        }
    }
}

