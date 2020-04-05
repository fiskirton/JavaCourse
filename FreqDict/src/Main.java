public class Main {
    public static void main(String[] args) {
        String filepath = "text.txt";

        FreqDict dict = new FreqDict(filepath);
        dict.scan();
    }
}
