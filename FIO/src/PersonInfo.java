import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class PersonInfo {
    private String surname;
    private String name;
    private String patronymic;
    private String birthday;

    public PersonInfo(final String input){
        String[] parsed = input.split(" ");
        this.surname = PersonInfo.capitalize(parsed[0]);
        this.name = PersonInfo.capitalize(parsed[1]);
        this.patronymic = PersonInfo.capitalize(parsed[2]);
        this.birthday = parsed[3];
    }

    private String getInitials(){
        return this.surname + " " + this.name.charAt(0) + "." + this.patronymic.charAt(0);
    }

    private String getSex(){
        if (this.patronymic.toLowerCase().charAt(this.patronymic.length() - 1) == 'ч'){
            return "male";
        } else { return "female"; }
    }

    private int getAge() throws DateTimeParseException {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthdayDateObj = null;
        try {
            birthdayDateObj = LocalDate.parse(this.birthday, format);
        } catch (DateTimeParseException ex){
            throw new DateTimeParseException("Date format should be 'dd-MM-yyyy'", this.birthday, 0);
        }
        LocalDate now = LocalDate.now();
        assert birthdayDateObj != null;
        return Period.between(birthdayDateObj, now).getYears();
    }

    public void printInfo() throws DateTimeParseException {
        String info = this.getInitials() + " | " + this.getSex() + " | " + this.getAge();
        System.out.println(info);
    }

    private static String capitalize(String string){
        return string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
    }

}
