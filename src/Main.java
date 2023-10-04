import java.util.ArrayList;
import java.util.Scanner;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    static Scanner input = new Scanner(System.in);

    static ArrayList<Product> products = new ArrayList<>();
    static final String PASSWORD = "111";
    static final String PRICE_WEIGHT_ALERT = "Wrong input of weight\nIt cant be less or equal 0\nYou should write only digits\nTry again";

    public static void main(String[] args) {
        //Test values
        products.add(new Product("bana","0,45", "fruit"));

        boolean exit = false;


        //Greeting
        {
            System.out.println("Hi and welcome to the Scales program!");
            System.out.println("Now you will get some alternativs for actions :");
            System.out.println();
        }

        do {
            showMainMenu();
            String customerInput = input.nextLine().trim().toLowerCase();
            if (!controlMenuInput(customerInput)) continue;
            switch (customerInput) {
                case "1" -> showAllProducts();
                case "2" -> showTypeProducts();
                case "3" -> searchProduct();
                case "4" -> calculatePurchase();
                case "5" -> administrationMode();
                case "9" -> exit = true;
                default -> System.err.println("Wrong input try again");
            }
        } while (!exit);

        System.out.println("THANK YOU FOR USING OUR PROGRAM");
    }


    private static void searchProduct() {//TODO ta bort duplicerat code , just functionen findProduct index
        boolean pending = true;
        boolean isfound = false;
        do {
            System.out.println("What product are you searching? Enter a name : ");
            String searchable = input.nextLine().trim().toLowerCase();
            //check if input is empty
            if (searchable.isEmpty()) {
                System.err.println("Input can not be empty");
                continue;
            }
            //check if input is correct
            if (!Product.nameValidating(searchable)) {
                System.err.println("Wrong input. Name can contain only letters");
                continue;
            }

            for (Product product : products) {
                if (product.getName().contains(searchable) || (product.getDescription().contains(searchable))) {
                    product.print();
                    System.out.println();
                    isfound = true;
                }
            }
            if (!isfound) System.err.println("Such element is not found");
            break;
        } while (pending);

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

    public static boolean showAllProducts() {
        //checking if array is not empty
        if (products.isEmpty()) {
            System.err.println("You didn't add any products yet");
            return false;
        }
        //print out all products
        printProducts();
        return true;
    }

    public static void administrationMode() {
        //initial values
        boolean isAdministrator = false;
        String customerInput;
        int tryAmount = 0;

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
                tryAmount++;
                //if tries more then 3 back to the menu
                if (tryAmount == 3) break;
                System.err.println("WRONG PASSWORD, TRY AGAIN :");
            }
        }

        // checkin if customer is administrator
        while (isAdministrator) {
            showInstractions();
            //show a menu
            System.out.println("Choose an action, press: ");
            System.out.println("1. To add a product");
            System.out.println("2. To delete a product");
            System.out.println("3. To change information in product");
            System.out.println("Write 'BACK' to exit ADMINISTRATION MODE");
            //take customer's input
            customerInput = input.nextLine().trim().toLowerCase();
            //if customer wrote back , close administration mode. Go back to previous menu
            if (isCancel(customerInput)) break;
            if (!controlMenuInput(customerInput)) continue;
            switch (customerInput) {
                case "1" -> addProduct();
                case "2" -> deleteProduct();
                case "3" -> changeInfo();
                default -> System.err.println("Wrong input, try again, choose from list");
            }
        }


    }

    public static double calculatePurchase() {
        //initial values
        String customerInput;
        boolean pending = true;
        double weight = 0;
        double price = 0;
        double result = 0;

        do {
            //geting a product's index that customer want to work with
            int index = findProductIndex();
            //input is wrong
            if (index == -3) {
                System.err.println("input is wrong try again");
                continue;
            }
            //customer whant to cancel
            if(index == -2) break;
            //if element is not found
            if (index == -1) {
                System.err.println("there is no such product");
                continue;
            }
            //if element is found print element
            System.out.println("Here is your product:");
            products.get(index).print();
            System.out.println();
            //get product's price
            price = products.get(index).getPrice();

            //ask how much want customer buy
            System.out.println("How many kilogram do you want to buy? write 'BACK' to cancel");
            customerInput = input.nextLine().trim().toLowerCase();
            //check if customer want cancel and go back to previous menu
            if (isCancel(customerInput)) return -1;
            //check if customers input is correct( price validation is OK for that)
            if (!Product.priceValidation(customerInput)) {
                System.out.println(PRICE_WEIGHT_ALERT);
                continue;
            }
            weight = Double.parseDouble(Product.formatPrice(customerInput));
            //calculate price of purchase
            result = price * weight;
            System.out.println("This product will cost you " + result + " SEK");
            //stop loop
            pending = false;
        } while (pending);
        return price * weight;
    }

    public static boolean changeInfo() {
        String customerInput;
        boolean pending = true;
        int index;
        do {
            //geting a product's index that customer want to work with
            index = findProductIndex();
            //input is wrong
            if (index == -3) {
                System.err.println("input is wrong try again");
                continue;
            }
            //customer whant to cancel
            if(index == -2) break;
            //if element is not found
            if (index == -1) {
                System.err.println("there is no such product");
                continue;
            }
            pending = false;
        }while(pending);
        System.out.println("Here is your product:");

        products.get(index).printAdmin();
        System.out.println();

        pending = true;
        do {

            System.out.println("What kind of info you want to change?");
            System.out.println("Wright: ");
            System.out.println("1. Change type");
            System.out.println("2. Change name");
            System.out.println("3. Change price");
            System.out.println("4. Change description");
            System.out.println("Write 'BACK' to cancel");

            customerInput = input.nextLine().trim().toLowerCase();
            //chek if customer want to cancel
            if (isCancel(customerInput)) break;
            //control customers input
            if (!controlMenuInput(customerInput)) continue;

            switch (customerInput) {
                case "1" -> {
                    changeType(index);
                    return true;
                }
                case "2" -> {
                    changeName(index);
                    return true;
                }
                case "3" -> {
                    changePrice(index);
                    return true;
                }
                case "4" -> {
                    changeDescription(index);
                    return true;
                }
                default -> System.err.println("Not a value of list , try again");
            }

        } while (pending);
        return true;
    }

    private static boolean changeDescription(int index) {
        String newDescription;
        boolean pending = true;
        do {
            System.out.println("Choose a new Description or or write 'BACK' to cancel");
            newDescription = input.nextLine();
            //check if input is empty
            if (newDescription.isEmpty()) {
                System.err.println("Input can't be empty");
                continue;
            }
            //check if customer want to cancel
            if(isCancel(newDescription)) return false;
            try {
                //set new description
                products.get(index).setDescription(newDescription);
                pending = false;
                return true;
            } catch (IllegalArgumentException exp) {
                System.err.println("Wrong input\nDescription can't be empty\nDescription must contain only letters and spaces\nTry again\n");
                continue;
            }
        } while (pending);

        return true;
    }

    public static boolean changeType(int index) {
        String newType;
        boolean pending = true;
        int choise;
        do {

            System.out.println("Choose a new type or or write 'BACK' to cancel");
            printTypes();
            newType = input.nextLine();
            //check if input is not empty
            if (newType.isEmpty()) {
                System.err.println("Input can't be empty");
                continue;
            }
            //check if customer want to cancel
            if (isCancel(newType)) return false;

            try {
                //parse customer choice to integer
                choise = Integer.parseInt(newType);
                //get a type with
                newType = Product.getProductTypes().get(choise-1);

            } catch (Exception exp) {
                System.err.println("Not a value of list , try again");
                continue;
            }

            try {
                //set new type to product
                products.get(index).setType(newType);
                pending = false;
            } catch (IllegalArgumentException exp) {
                System.err.println("Wrong input,try again");
                continue;
            }
        } while (pending);

        return true;

    }

    public static boolean changeName(int index) {
        String newName;
        boolean pending = true;
        do {
            System.out.println("Choose a new name or or write 'BACK' to cancel");
            newName = input.nextLine();
            if (newName.isEmpty()) {
                System.err.println("Name can't be empty");
                continue;
            }
            isCancel(newName);

            try {
                products.get(index).setName(newName);
                pending = false;
                return true;
            } catch (IllegalArgumentException exp) {
                System.err.println("Wrong input\nName can't be empty\nName must contain only letters and spaces\nTry again");
                continue;
            }
        } while (pending);

        return true;
    }

    public static boolean changePrice(int index) {
        String newPrice;
        boolean pending = true;
        do {

            System.out.println("Write a new price or or write 'BACK' to cancel");
            newPrice = input.nextLine();
            if (newPrice.isEmpty()){
                System.err.println("Price can't be empty, try again");
                continue;
            }
            if(isCancel(newPrice)) return false;

            try {

                products.get(index).setPrice(newPrice);
                return true;
            } catch (IllegalArgumentException exp) {
                System.err.println("Wrong input\nPrice can't be empty\nPrice can contain oly digits and one point\nTry again");
                continue;
            }
        } while (pending);

        return true;
    }

    private static boolean deleteProduct() {
        int index;
        do {
            try {
                index = findProductIndex();
                //input is wrong
                if (index == -3) {
                    System.err.println("input is wrong try again");
                    continue;
                }
                //customer whant to cancel
                if (index == -2) break;
                //if element is not found
                if (index == -1) {
                    System.err.println("there is no such product");
                    continue;
                }
                products.remove(index);
                break;
            } catch (IndexOutOfBoundsException exp) {
                System.err.println("Something went wrong , try again");
                continue;
            }
        }while (true);
        System.out.println("A product is removed , SUCCESS");
        return true;
    }

    public static void printTypes() {
        ArrayList<String> productsTypes = Product.getProductTypes();
        int size = productsTypes.size();
        for (int i = 0; i < size; i++) {
            System.out.println(i + 1 + " " + Product.getProductTypes().get(i));
        }
    }

    public static boolean addProduct() {

        String type = "";
        String name = "";
        String price = "";
        String description = "";
        boolean pending = true;
        boolean success = false;
        boolean wantAddDescription = false;
        String customerInput;
        Product result;
        int choise;

        do {
            System.out.println("What type of product you want to add, choose from: ");

            printTypes();
            System.out.println("'BACK' to cancel");
            //scan a type of product
            do {
                customerInput = input.nextLine().trim().toLowerCase();
                // check if customer want to cancel
                if (isCancel(customerInput)) return false;
                // control input
                if (!controlMenuInput(customerInput)) continue;

                try {
                    //parse string input to integer
                    choise = Integer.parseInt(customerInput);
                    //get chosen type
                    type = Product.getProductTypes().get(choise - 1);

                } catch (Exception exp) {
                    System.err.println("Not a value of list , try again");
                    continue;
                }

                pending = false;

            } while (pending);

            pending = true;


            do {
                System.out.println("Enter a name of product or BACK to cancel");
                System.out.println("Name can contain only letters");
                customerInput = input.nextLine().trim().toLowerCase();

                if (isCancel(customerInput)) return false;
                //check if input is correct

                if (customerInput.isBlank() || !Product.nameValidating(customerInput)) {
                    System.err.println("Wrong name\nName can only contain a letters\nName can't be empty\nTry again");
                    continue;
                }
                //check if such product is already existing and name is correct
                if (isExist(customerInput)) {
                    System.err.println("Such product is already existing, try again");
                    continue;
                }

                name = customerInput;
                pending = false;

            } while (pending);

            pending = true;


            //input of price
            do {
                System.out.println("Enter a price (#.##) of product or BACK to cancel");
                customerInput = input.nextLine().trim().toLowerCase();
                //check if customer want to cancel
                if (isCancel(customerInput)) return false;

                if (customerInput.isEmpty() || !Product.priceValidation(customerInput)) {
                    System.err.println("Wrong format of price\nPrice must contain only digits\nPrice can't be empty\nTry again");
                    continue;
                }

                price = Product.formatPrice(customerInput);
                pending = false;

            } while (pending);

            pending = true;
            //if customer want to add a description
            System.out.println("Do you want to add description to product for easier search? Write 'YES' if you want");
            customerInput = input.nextLine().trim().toLowerCase();
            if (customerInput.equals("yes")) wantAddDescription = true;
            //add description
            if (wantAddDescription) {
                do {
                    System.out.println("Enter a description to product or write 'BACK' to cancel");
                    customerInput = input.nextLine().trim().toLowerCase();
                    //check if customer want to cancel
                    if (isCancel(customerInput)) return false;
                    //check if input is not empty or is not wrong format
                    if (customerInput.isEmpty() || !Product.descriptionValidating(customerInput)) {
                        System.err.println("Wrong format of description.\nMust contain only letters and can't be empty, try again");
                        continue;
                    }

                    description = customerInput;
                    //stop the loop
                    pending = false;

                } while (pending);
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
                success = true;
                return true;
            } catch (IllegalArgumentException e) {
                System.err.println("Something went wrong, please try again");
            }
        } while (!success);
        return true;
    }

    public static void showTypeProducts() {
        boolean exit = false;
        do {
            System.out.println("Press : ");
            System.out.println("1. If you want to print Fruits");
            System.out.println("2. If you want to print Vegetables");
            System.out.println("3. If you want to print Other products");
            System.out.println("Write 'BACK' if you want go to the main meny");

            String customerInput = input.nextLine().trim().toLowerCase();
            if (isCancel(customerInput)) break;
            if (!controlMenuInput(customerInput)) continue;

            switch (customerInput) {
                case "1" -> printProducts("fruit");
                case "2" -> printProducts("vegetable");
                case "3" -> printProducts("other");
                default -> System.err.println("Value not from list, try again");
            }
        } while (true);

    }

    public static boolean printProducts() {
        //check if customer didn't add products at all yet
        if (products.isEmpty()) {
            System.err.println("There are no products yet");
            return false;
        }
        //print all products
        for (Product product : products) {
            product.print();
        }
        return true;
    }

    public static boolean printProducts(String type) {
        boolean isEmpty = true;
        //check if customer didn't add products at all yet
        if (products.isEmpty()) {
            System.err.println("There are no products yet");
            return false;
        }
        //print products of type
        for (Product product : products) {
            if (product.getType().equals(type)) {
                product.print();
                isEmpty = false;
            }

        }
        //check if don't have products of such type
        if (isEmpty) {
            System.err.println("There are no products of such type yet");
            return false;
        }
        return true;
    }

    public static boolean isExist(String name) {
        int index;
        index = getIndexOfProduct(products, name);
        return index > -1;
    }

    public static int findProductIndex() {
        String name = "";
        int index;
        boolean pending = true;
        String customerInput="";

        //function return -1 if such element is not existing
        //         return -2 if customer want to cancel
        //         return -3 if input is wrong

            System.out.println("Enter a name of product you want to work with or BACK to cancel");
            //take customers input
            customerInput = input.nextLine().trim().toLowerCase();
            if (customerInput.isEmpty()){
                return -3;
            }
            //check if customer want to cancel and go to previous menu
            if (isCancel(customerInput)) return -2;
            //check if input is correct
            if (!Product.nameValidating(customerInput)) {
                return -3;
            }
            //check if such product is already existing and name is correct
            if (!isExist(customerInput)) {
                return -1;
            }

            name = customerInput;
            //stop loop

        //search product in products
        index = getIndexOfProduct(products, name);
        //return index of product
        return index;
    }

    public static int getIndexOfProduct(ArrayList<Product> array, String name) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getName().equals(name)) return i;
        }
        return -1;
    }

    public static boolean isCancel(String input) {
        String submit = "back";
        return input.equals(submit);
    }

    public static boolean controlMenuInput(String customerInput) {

        if (customerInput.isEmpty()) {
            System.err.println("Input can't be empty ,try again");
            return false;
        }
        if (customerInput.length() > 1) {
            System.err.println("Input should be only 1 number or 'BACK' , try again");
            return false;
        }
        return true;
    }
    public static void showInstractions(){
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";
        String[] instractions ={
                "Any input can't be empty",
                "Price can contain oly digits and one point",
                "Name must contain only letters and spaces",
                "Description must contain only letters and spaces",
        };
        System.out.println();
        System.out.println(ANSI_GREEN + "Check an follow instructions to use ADMINISTRATION_MODE" +ANSI_RESET);
        for (int i = 0; i < instractions.length; i++) {
            System.out.println(ANSI_GREEN+ (i+1)+". "+ instractions[i] +ANSI_RESET);
        }
        System.out.println();
    }


}
