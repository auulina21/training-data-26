/** UTF-8, необмежена кількість рядків; порожні рядки пропускаються. */
public class DataFileHandler {
    public static Integer[] loadArrayFromFile(String filePath) throws java.io.IOException {
        var path = java.nio.file.Path.of(filePath);
        try (var reader = java.nio.file.Files.newBufferedReader(path, java.nio.charset.StandardCharsets.UTF_8)) {
            return reader.lines()
                .map(line -> line.trim().replaceFirst("^\\uFEFF", ""))
                .filter(line -> !line.isEmpty())
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
        } catch (RuntimeException exception) {
            throw new java.io.IOException("Некоректні дані у файлі " + filePath, exception);
        }
    }
    public static void writeArrayToFile(Integer[] values, String filePath) {
        try (var writer = java.nio.file.Files.newBufferedWriter(java.nio.file.Path.of(filePath), java.nio.charset.StandardCharsets.UTF_8)) {
            String content = java.util.Arrays.stream(values).map(String::valueOf)
                .collect(java.util.stream.Collectors.joining(System.lineSeparator()));
            writer.write(content);
            if (values.length > 0) writer.newLine();
        } catch (java.io.IOException exception) {
            throw new java.io.UncheckedIOException("Не вдалося записати " + filePath, exception);
        }
    }
}
