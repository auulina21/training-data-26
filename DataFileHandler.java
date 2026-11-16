import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Клас DataFileHandler управляє роботою з файлами даних LocalDateTime
 * з використанням лямбда-виразів і Stream API.
 */
public class DataFileHandler {
    /**
     * Завантажує масив об'єктів LocalDateTime з файлу.
     * 
     * @param filePath Шлях до файлу з даними.
     * @return Масив об'єктів LocalDateTime.
     */
    public static LocalDateTime[] loadArrayFromFile(String filePath) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            return fileReader.lines()
                    // видаляємо можливі невидимі символи та BOM
                    .map(currentLine -> currentLine.trim().replaceAll("^\\uFEFF", ""))
                    // пропускаємо порожні рядки
                    .filter(currentLine -> !currentLine.isEmpty())
                    // перетворюємо рядок на значення LocalDateTime
                    .map(currentLine -> LocalDateTime.parse(currentLine, timeFormatter))
                    // збираємо елементи потоку в масив
                    .toArray(LocalDateTime[]::new);
        } catch (IOException ioException) {
            throw new java.io.UncheckedIOException("Не вдалося прочитати " + filePath, ioException);
        }
    }

    /**
     * Зберігає масив об'єктів LocalDateTime у файл.
     * 
     * @param dateTimeArray Масив об'єктів LocalDateTime.
     * @param filePath Шлях до файлу для збереження.
     */
    public static void writeArrayToFile(LocalDateTime[] dateTimeArray, String filePath) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
            // перетворюємо кожне значення на рядок і з'єднуємо рядки розділювачем
            String content = Arrays.stream(dateTimeArray)
                    .map(LocalDateTime::toString)
                    .collect(Collectors.joining(System.lineSeparator()));

            fileWriter.write(content);
            // для порожнього масиву файл лишається порожнім
            if (dateTimeArray.length > 0) {
                fileWriter.newLine();
            }
        } catch (IOException ioException) {
            throw new java.io.UncheckedIOException("Не вдалося записати " + filePath, ioException);
        }
    }
}
