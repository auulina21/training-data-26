import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class BasicDataOperationsUsingSet {
    Integer valueToSearch;
    int sourceValueCount;
    Set<Integer> valueSet = new LinkedHashSet<>();

    BasicDataOperationsUsingSet(Integer valueToSearch, Integer[] valueArray) {
        this.valueToSearch = valueToSearch;
        this.sourceValueCount = valueArray.length;
        this.valueSet = new LinkedHashSet<>(Arrays.asList(valueArray.clone()));
    }

    public void executeDataAnalysis() {
        findInSet();
        locateMinMaxInSet();
        analyzeDuplicates();

        Integer[] sortedSetValues = valueSet.stream()
                .sorted()
                .toArray(Integer[]::new);
        DataFileHandler.writeArrayToFile(sortedSetValues, BasicDataOperations.SORTED_FILE_PREFIX + ".set.data");
    }

    private void findInSet() {

        String operationName = "пошук елемента '" + valueToSearch + "' в LinkedHashSet<Integer>";
        long timeStart = System.nanoTime();

        boolean elementExists = valueSet.stream()
                .anyMatch(dateTime -> dateTime.equals(valueToSearch));

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (elementExists) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в LinkedHashSet");
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в LinkedHashSet.");
        }
    }

    private void locateMinMaxInSet() {
        if (valueSet == null || valueSet.isEmpty()) {
            System.out.println("LinkedHashSet є пустим або не ініціалізованим.");
            return;
        }

        long timeStart = System.nanoTime();



        Integer minValue = valueSet.stream()
                .min(Integer::compareTo)
                .orElseThrow();
        Integer maxValue = valueSet.stream()
                .max(Integer::compareTo)
                .orElseThrow();

        PerformanceTracker.displayOperationTime(timeStart, "визначення мінімального і максимального значення в LinkedHashSet<Integer>");

        System.out.println("Найменше значення в LinkedHashSet: " + minValue);
        System.out.println("Найбільше значення в LinkedHashSet: " + maxValue);
    }

    private void analyzeDuplicates() {
        System.out.println("Кiлькiсть вхiдних значень: " + sourceValueCount);
        System.out.println("Кiлькiсть елементiв в LinkedHashSet: " + valueSet.size());
        System.out.println("Повторних входжень, вiдкинутих LinkedHashSet: " + (sourceValueCount - valueSet.size()));
    }
}
