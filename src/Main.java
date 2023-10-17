import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    static Scanner input = new Scanner(System.in);

    static ArrayList<Product> products = new ArrayList<>();
    static final String PASSWORD = "111";

    public static void main(String[] args) {

        try {
            products.add(new Product("bana", "34,0", "fruit", "africa yellow"));
            products.add(new Product("banana", "23,6", "fruit", "africa "));
            products.add(new Product("mango", "456,3", "vegetable", "red "));
            products.add(new Product("tomato", "23,78", "vegetable", "red chili"));
            products.add(new Product("blueberry", "75,1", "other", "blue berry"));
            products.add(new Product("lingonberry", "34,5", "other", "red Sweden"));
        } catch (Exception exp) {
            System.err.println(exp.getMessage());
        }

        boolean exit = false;

        showGreetings();

        while (!exit){
            showMainMenu();
            try {
                String customerInput = input.nextLine().trim().toLowerCase();
                controlMenuInput(customerInput);
                switch (customerInput) {
                    case "1" -> showAllProducts();
                    case "2" -> showTypeProducts();
                    case "3" -> searchProducts();
                    case "4" -> calculatePurchase();
                    case "5" -> administrationMode();
                    case "9" -> exit = true;
                    default -> System.err.println("Wrong input try again");
                }
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

    private static int findProduct() throws Exception, IndexOutOfBoundsException {
        boolean isfound = false;
        int index = -1;
        String searchable;
        if (products.isEmpty()) throw new IndexOutOfBoundsException("Product list is empty");
        try {
            System.out.println("What product are you searching? Enter a name or 'BACK' to cancel: ");
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
            if (!isfound) throw new Exception("Such product is not existing");
            return index;
        } catch (Exception exp) {
            throw new Exception(exp.getMessage());

        }

    }

    public static void showMainMenu() {
        System.out.println();
        System.out.println("Press: ");
        System.out.println("1. To show all products ");
        System.out.println("2. To Show certain type of products");
        System.out.println("3. To search a product");
        System.out.println("4. To calculate price of purchase");
        System.out.println("5. Activate ADMINISTRATION_MODE");
        System.out.println("\n9. To Exit the program");
    }

    public static void showAllProducts() throws Exception {
        //print out all products
        printProducts();
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

            System.out.print(ANSI_GREEN);
            try {
                showInstructions();
                //show a menu
                System.out.println("Choose an action, press: ");
                System.out.println("1. To add a product");
                System.out.println("2. To delete a product");
                System.out.println("3. To change information in product");
                System.out.println("Write 'BACK' to exit ADMINISTRATION MODE");
                //take customer's input
                customerInput = input.nextLine().trim().toLowerCase();
                //if customer wrote back , close administration mode. Go back to previous menu
                if (isCancel(customerInput)) {System.out.print(ANSI_RESET);break;}
                controlMenuInput(customerInput);
                switch (customerInput) {
                    case "1" -> addProduct();
                    case "2" -> deleteProduct();
                    case "3" -> changeInfo();
                    default -> System.err.println("Wrong input, try again, choose from list");
                }
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        }


    }

    public static void calculatePurchase() throws Exception {
        //initial values
        String customerInput;
        double weight;
        double price;
        double result;
        int index;
        if (products.isEmpty()) throw new Exception("Product list is empty");

        while (true) {
            try {
                //geting a product's index that customer want to work with
                index = findProduct();
                //if customer want to cancel
                if (index == -1) {
                    return;
                }
                //if element is found print element
                System.out.println("Here is your product:");
                products.get(index).print();
                System.out.println();
                //get product's price
                price = products.get(index).getPrice();
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
                System.out.println("How many kilogram do you want to buy? write 'BACK' to cancel");
                customerInput = input.nextLine().trim().toLowerCase();
                //check if customer want cancel and go back to previous menu
                if (isCancel(customerInput)) return;
                //check if customers input is correct( price validation is OK for that)

                Product.priceValidation(customerInput);
                weight = Double.parseDouble(Product.formatPrice(customerInput));
                //calculate price of purchase
                result = price * weight;
                System.out.printf("This product will cost you %10.2f SEK\n", result);
                return;
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
                continue;
            }
        }

    }


    public static void changeInfo() {
        String customerInput;

        int index = 0;
        while (true) {
            try {
                //geting a product's index that customer want to work with
                index = findProduct();

                // if customer want to exit
                if (index == -1) {
                    return;
                }
                System.out.println("Here is your product:");
                break;
            } catch (IndexOutOfBoundsException exp) {
                System.err.println(exp.getMessage());
                return;
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
                continue;
            }
        }


        while (true){
            products.get(index).printAdmin();
            System.out.println();
            try {
                System.out.println("What kind of info you want to change?");
                System.out.println("Wright: ");
                System.out.println("1. Change type");
                System.out.println("2. Change name");
                System.out.println("3. Change price");
                System.out.println("4. Change description");
                System.out.println("Write 'BACK' to cancel");

                customerInput = input.nextLine().trim().toLowerCase();
                //check if customer want to cancel
                if (isCancel(customerInput)) return;
                //control customers input
                controlMenuInput(customerInput);

                switch (customerInput) {
                    case "1" -> {
                        changeType(index);
                        return;
                    }
                    case "2" -> {
                        changeName(index);
                        return;
                    }
                    case "3" -> {
                        changePrice(index);
                        return;
                    }
                    case "4" -> {
                        changeDescription(index);
                        return;
                    }
                    default -> System.err.println("Not a value of list , try again");
                }
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
                continue;
            }
        }
    }

    private static void changeDescription(int index) {
        String newDescription;

        while (true){
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

    public static void changeType(int index) {
        String newType;
        int choise;
        while (true){

            System.out.println("Choose a new type or or write 'BACK' to cancel");
            printTypes();
            newType = input.nextLine();
            try {
                //check if customer want to cancel
                if (isCancel(newType)) return;

                controlMenuInput(newType);

                //parse customer choice to integer
                choise = Integer.parseInt(newType);
                //get a type with
                newType = Product.getProductTypes().get(choise - 1);


                //set new type to product
                products.get(index).setType(newType);
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
        boolean pending = true;
        do {
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
        } while (pending);
    }

    public static void changePrice(int index) {
        String newPrice;
        while (true){
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

    private static void deleteProduct() throws Exception {
        int index;
        //if (products.isEmpty()) throw new Exception("List of products is empty is empty");
        while(true){
            try {
                index = findProduct();
                //if customer want to cancel
                if (index == -1) {
                    return;
                }
                products.remove(index);
                System.out.println("A product is removed , SUCCESS");
                return;
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        }
    }

    public static void printTypes() {
        ArrayList<String> productsTypes = Product.getProductTypes();
        int size = productsTypes.size();
        for (int i = 0; i < size; i++) {
            System.out.println(i + 1 + " " + Product.getProductTypes().get(i));
        }
    }

    public static void addProduct() {

        String type;
        String name;
        String price;
        String description = "";
        boolean wantAddDescription = false;
        String customerInput;
        Product result;
        int choise;

        while (true){
            while (true){
                try {
                    System.out.println("What type of product you want to add, choose from: ");

                    printTypes();
                    System.out.println("'BACK' to cancel");

                    //scan a type of product
                    customerInput = input.nextLine().trim().toLowerCase();
                    // check if customer want to cancel
                    if (isCancel(customerInput)) return;
                    // control input
                    controlMenuInput(customerInput);


                    //parse string input to integer
                    choise = Integer.parseInt(customerInput);
                    if (choise < 1 || choise > Product.getProductTypes().size())
                        throw new Exception("Value not from list");
                    //get chosen type
                    type = Product.getProductTypes().get(choise - 1);

                } catch (Exception exp) {
                    System.err.println(exp.getMessage());
                    continue;
                }

                break;

            }


            while (true){
                try {
                    System.out.println("Enter a name of product or BACK to cancel");
                    System.out.println("Name can contain only letters");


                    customerInput = input.nextLine().trim().toLowerCase();
                    if (isCancel(customerInput)) return;
                    //check if input is correct

                    Product.nameValidating(customerInput);
                    //check if such product is already existing and name is correct
                    isAlreadyExist(customerInput);


                    name = customerInput;
                } catch (Exception exp) {
                    System.err.println(exp.getMessage());
                    continue;
                }
                break;
            }


            //input of price
            while (true){
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
                while (true){
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
            //try to create a product
            try {
                if (!wantAddDescription) {
                    result = new Product(name, price, type);
                } else {
                    result = new Product(name, price, type, description);
                }
                products.add(result);
                System.out.println("You added a new Product , SUCCESS ");
                return;
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        }
    }

    public static void showTypeProducts() {
        while (true){
            try {
                System.out.println("Press : ");
                System.out.println("1. If you want to print Fruits");
                System.out.println("2. If you want to print Vegetables");
                System.out.println("3. If you want to print Other products");
                System.out.println("Write 'BACK' if you want go to the main meny");

                String customerInput = input.nextLine().trim().toLowerCase();
                if (isCancel(customerInput)) break;
                controlMenuInput(customerInput);

                switch (customerInput) {
                    case "1" -> printProducts("fruit");
                    case "2" -> printProducts("vegetable");
                    case "3" -> printProducts("other");
                    default -> System.err.println("Value not from list, try again");
                }
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
                continue;
            }
        }

    }

    public static void printProducts() throws Exception {
        //check if customer didn't add products at all yet
        if (products.isEmpty()) throw new Exception("Productlist is empty");
        //print all products
        for (Product product : products) {
            product.print();
        }
        return;
    }

    public static void printProducts(String type) throws Exception {
        boolean isEmpty = true;
        //check if customer didn't add products at all yet
        if (products.isEmpty()) throw new Exception("There are no products yet");

        //print products of type
        for (Product product : products) {
            if (product.getType().equals(type)) {
                product.print();
                isEmpty = false;
            }

        }
        //check if don't have products of such type
        if (isEmpty) throw new Exception("There are no products of such type yet");
    }

    public static int isAlreadyExist(String name) throws Exception {
        int index;
        index = getIndexOfProduct(products, name);

        if (index > -1) throw new Exception("Such element is already existing");
        return index;
    }


    public static int getIndexOfProduct(ArrayList<Product> array, String name) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getName().equals(name)) return i;
        }
        return -1;
    }

    public static boolean isCancel(String input) {
        final String submit = "back";
        return input.equals(submit);
    }

    public static void controlMenuInput(String customerInput) throws Exception {


        if (customerInput.isEmpty()) throw new Exception("Input can't be empty ,try again");

        if (customerInput.length() > 1) {
            throw new Exception("Input should be only 1 number or 'BACK' , try again");
        }
    }

    public static void showInstructions() {

        String[] instractions = {
                "Any input can't be empty",
                "Price can contain oly digits and one point",
                "Name must contain only letters and spaces",
                "Description must contain only letters and spaces",
        };
        System.out.println();
        System.out.println("Check an follow instructions to use ADMINISTRATION_MODE" );
        for (int i = 0; i < instractions.length; i++) {
            System.out.println((i + 1) + ". " + instractions[i]);
        }
        System.out.println();
    }


    public static void searchProducts() throws Exception {
        boolean isfound = false;
        String searchable = "";
        if (products.isEmpty()) throw new Exception("Product list is empty");
        try {
            System.out.println("What product(s) are you searching? Enter a name or 'BACK' to cancel: ");
            searchable = input.nextLine().trim().toLowerCase();

            //if customer want to cancel
            if (isCancel(searchable)) return;
            //check if input is empty
            Product.nameValidating(searchable);

            for (Product product : products) {
                if (product.getName().contains(searchable) || product.getDescription().contains(searchable)) {
                    product.print();
                    isfound = true;
                }
            }
            if (!isfound) throw new Exception("Such product is not existing");
        } catch (Exception exp) {
            throw new Exception(exp.getMessage());

        }
    }


}

