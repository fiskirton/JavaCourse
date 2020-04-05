import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String prompt = "Please, enter your full name and birthday in format 'dd-MM-yyyy': ";
        System.out.println(prompt);

        Scanner in = new Scanner(System.in);
        String userInput = in.nextLine();

        PersonInfo info = new PersonInfo(userInput);
        try {
            info.printInfo();
        } catch (DateTimeParseException ex){
            System.out.println(ex.getMessage());
        }

    }
}
