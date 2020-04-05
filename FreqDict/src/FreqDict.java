import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FreqDict {
    private String sourcePath;
    private Map<Character, Integer> frequencies;

    public FreqDict(String sourcePath){
        this.sourcePath = sourcePath;
    }

    private void countSymbols(){
        try (BufferedReader reader = new BufferedReader(new FileReader(this.sourcePath))) {
            String line;
            this.frequencies = new HashMap<>();
            while ((line = reader.readLine()) != null){
                for (char symbol :
                        line.toCharArray()) {
                    if (((symbol >= 'a') && (symbol <= 'z')) || ((symbol >= 'A') && (symbol <= 'Z'))) {
                        this.frequencies.merge(symbol, 1, Integer::sum);
                    }
                }
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void makeOutput(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            this.frequencies.forEach((key, value) -> {
                try {
                    writer.write(key + " - " + value + '\n');
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void scan(){
        this.countSymbols();
        this.makeOutput();
    }
}
