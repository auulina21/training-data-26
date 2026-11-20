/** UTF-8, необмежена кількість рядків; порожні рядки пропускаються. */
public class DataFileHandler {
    public static Integer[] loadArrayFromFile(String filePath) throws java.io.IOException {
        var path = java.nio.file.Path.of(filePath);
        java.util.List<Integer> values = new java.util.ArrayList<>();
        try (var reader = java.nio.file.Files.newBufferedReader(path, java.nio.charset.StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (lineNumber == 1 && line.startsWith("\uFEFF")) line = line.substring(1);
                if (line.isEmpty()) continue;
                try {
                    values.add(Integer.valueOf(line));
                } catch (NumberFormatException exception) {
                    throw new java.io.IOException("Некоректне Integer у рядку " + lineNumber + " файлу " + filePath, exception);
                }
            }
        }
        return values.toArray(new Integer[0]);
    }
    public static void writeArrayToFile(Integer[] values, String filePath) {
        try (var writer = java.nio.file.Files.newBufferedWriter(java.nio.file.Path.of(filePath), java.nio.charset.StandardCharsets.UTF_8)) {
            for (Integer value : values) {
                writer.write(value.toString());
                writer.newLine();
            }
        } catch (java.io.IOException exception) {
            throw new java.io.UncheckedIOException("Не вдалося записати " + filePath, exception);
        }
    }
}
