import java.util.Vector;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BasicDataOperationsUsingList {
    private boolean listSorted = false;
    private boolean arraySorted = false;
    private Integer valueToSearch;
    private Integer[] valueArray;
    private List<Integer> valueList;

    BasicDataOperationsUsingList(Integer valueToSearch, Integer[] valueArray) {
        this.valueToSearch = valueToSearch;
        this.valueArray = valueArray.clone();
        this.valueList = new Vector<>(Arrays.asList(valueArray.clone()));
    }

    public void executeDataOperations(Integer threshold) {

        System.out.println("List: до сортування");
        analyzeCondition(valueList, listSorted, threshold);
        findInList();
        locateMinMaxInList();
        
        sortList();
        System.out.println("List: після сортування");
        analyzeCondition(valueList, listSorted, threshold);
        
        findInList();
        locateMinMaxInList();

        System.out.println("Масив: до сортування");
        analyzeCondition(valueArray, arraySorted, threshold);
        findInArray();
        locateMinMaxInArray();

        performArraySorting();
        System.out.println("Масив: після сортування");
        analyzeCondition(valueArray, arraySorted, threshold);
        
        findInArray();
        locateMinMaxInArray();

        DataFileHandler.writeArrayToFile(valueList.toArray(new Integer[0]), BasicDataOperations.SORTED_FILE_PREFIX + ".list.data");
    }

    void performArraySorting() {
        long timeStart = System.nanoTime();

        valueArray = Arrays.stream(valueArray)
                .sorted()
                .toArray(Integer[]::new);
        arraySorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування масиву Integer[]");
    }

    void findInArray() {

        String operationName = "пошук елемента '" + valueToSearch + "' в масивi Integer[], " + (arraySorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        int position;
        if (arraySorted) {
            position = Arrays.binarySearch(valueArray, valueToSearch);
        } else {

            position = IntStream.range(0, valueArray.length)
                    .filter(index -> valueArray[index].compareTo(valueToSearch) == 0)
                    .findFirst()
                    .orElse(-1);
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (position >= 0) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в масивi за позицією: " + position);
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в масиві.");
        }
    }

    void locateMinMaxInArray() {
        if (valueArray == null || valueArray.length == 0) {
            System.out.println("Масив є пустим або не ініціалізованим.");
            return;
        }

        String operationName = "визначення мінімального і максимального значення в масивi Integer[], " + (arraySorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        Integer minValue;
        Integer maxValue;

        if (arraySorted) {

            minValue = valueArray[0];
            maxValue = valueArray[valueArray.length - 1];
        } else {

            minValue = Arrays.stream(valueArray)
                    .min(Integer::compareTo)
                    .orElseThrow();
            maxValue = Arrays.stream(valueArray)
                    .max(Integer::compareTo)
                    .orElseThrow();
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        System.out.println("Найменше значення в масивi: " + minValue);
        System.out.println("Найбільше значення в масивi: " + maxValue);
    }

    void findInList() {

        String operationName = "пошук елемента '" + valueToSearch + "' в Vector<Integer>, " + (listSorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        int position;
        if (listSorted) {
            position = Collections.binarySearch(valueList, valueToSearch);
        } else {

            long elementsBeforeMatch = valueList.stream()
                    .takeWhile(dateTime -> dateTime.compareTo(valueToSearch) != 0)
                    .count();
            position = elementsBeforeMatch < valueList.size() ? (int) elementsBeforeMatch : -1;
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (position >= 0) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в Vector за позицією: " + position);
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в Vector.");
        }
    }

    void locateMinMaxInList() {
        if (valueList == null || valueList.isEmpty()) {
            System.out.println("Колекція Vector є пустою або не ініціалізованою.");
            return;
        }

        String operationName = "визначення мінімального і максимального значення в Vector<Integer>, " + (listSorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        Integer minValue;
        Integer maxValue;

        if (listSorted) {

            minValue = valueList.get(0);
            maxValue = valueList.get(valueList.size() - 1);
        } else {

            minValue = valueList.stream()
                    .min(Integer::compareTo)
                    .orElseThrow();
            maxValue = valueList.stream()
                    .max(Integer::compareTo)
                    .orElseThrow();
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        System.out.println("Найменше значення в List: " + minValue);
        System.out.println("Найбільше значення в List: " + maxValue);
    }

    void sortList() {
        long timeStart = System.nanoTime();

        valueList = valueList.stream()
                .sorted()
                .collect(Collectors.toCollection(Vector::new));
        listSorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування Vector<Integer>");
    }

    public int analyzeCondition(Integer[] values, boolean sorted, Integer threshold) {
        String conditionDescription = "значення, менші за " + threshold;
        String phase = sorted ? "після сортування" : "до сортування";
        System.out.println("масив Integer[]: вибірка даних " + phase + ". Умова: " + conditionDescription);
        String operationName = "вибірка даних: масив Integer[], " + phase + ", умова: " + conditionDescription;
        long timeStart = System.nanoTime();
        Integer[] matches;
        if (!sorted) {
            matches = Arrays.stream(values).filter(value -> value < threshold).toArray(Integer[]::new);
        } else {
            matches = Arrays.stream(values).takeWhile(value -> value < threshold).toArray(Integer[]::new);
        }
        PerformanceTracker.displayOperationTime(timeStart, operationName);
        Arrays.stream(matches).forEach(System.out::println);
        int count = matches.length;
        System.out.println("У масиві знайдено " + count + " входжень (повтори враховано). Умова: " + conditionDescription + ".");
        return count;
    }

    public int analyzeCondition(List<Integer> values, boolean sorted, Integer threshold) {
        String conditionDescription = "значення, менші за " + threshold;
        String phase = sorted ? "після сортування" : "до сортування";
        System.out.println("Vector<Integer>: вибірка даних " + phase + ". Умова: " + conditionDescription);
        String operationName = "вибірка даних: Vector<Integer>, " + phase + ", умова: " + conditionDescription;
        long timeStart = System.nanoTime();
        List<Integer> matches;
        if (!sorted) {
            matches = values.stream().filter(value -> value < threshold).toList();
        } else {
            matches = values.stream().takeWhile(value -> value < threshold).toList();
        }
        PerformanceTracker.displayOperationTime(timeStart, operationName);
        matches.forEach(System.out::println);
        int count = matches.size();
        System.out.println("У List знайдено " + count + " входжень (повтори враховано). Умова: " + conditionDescription + ".");
        return count;
    }
}
