import java.util.Vector;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

        Arrays.sort(valueArray);
        arraySorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування масиву Integer[]");
    }

    void findInArray() {

        String operationName = "пошук елемента '" + valueToSearch + "' в масивi Integer[], " + (arraySorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        int position = -1;
        if (arraySorted) {
            position = Arrays.binarySearch(valueArray, valueToSearch);
        } else {
            for (int i = 0; i < valueArray.length; i++) {
                if (valueArray[i].compareTo(valueToSearch) == 0) {
                    position = i;
                    break;
                }
            }
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

        Integer minValue = valueArray[0];
        Integer maxValue = valueArray[0];

        if (arraySorted) {

            maxValue = valueArray[valueArray.length - 1];
        } else {
            for (Integer currentValue : valueArray) {
                if (currentValue.compareTo(minValue) < 0) {
                    minValue = currentValue;
                }
                if (currentValue.compareTo(maxValue) > 0) {
                    maxValue = currentValue;
                }
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        System.out.println("Найменше значення в масивi: " + minValue);
        System.out.println("Найбільше значення в масивi: " + maxValue);
    }

    void findInList() {

        String operationName = "пошук елемента '" + valueToSearch + "' в Vector<Integer>, " + (listSorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        int position = -1;
        if (listSorted) {
            position = Collections.binarySearch(valueList, valueToSearch);
        } else {
            int index = 0;
            for (Integer value : valueList) {
                if (value.compareTo(valueToSearch) == 0) {
                    position = index;
                    break;
                }
                index++;
            }
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
            minValue = Collections.min(valueList);
            maxValue = Collections.max(valueList);
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        System.out.println("Найменше значення в List: " + minValue);
        System.out.println("Найбільше значення в List: " + maxValue);
    }

    void sortList() {
        long timeStart = System.nanoTime();

        Collections.sort(valueList);
        listSorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування Vector<Integer>");
    }

    public int analyzeCondition(Integer[] values, boolean sorted, Integer threshold) {
        String conditionDescription = "значення, менші за " + threshold;
        String phase = sorted ? "після сортування" : "до сортування";
        System.out.println("масив Integer[]: вибірка даних " + phase + ". Умова: " + conditionDescription);
        String operationName = "вибірка даних: масив Integer[], " + phase + ", умова: " + conditionDescription;
        long timeStart = System.nanoTime();
        Integer[] matches = new Integer[values.length];
        int count = 0;
        int inspected = 0;
        for (Integer value : values) {
            inspected++;
            if (sorted && value >= threshold) break;
            if (value < threshold) matches[count++] = value;
        }
        PerformanceTracker.displayOperationTime(timeStart, operationName);
        System.out.println("Переглянуто елементів: " + inspected);
        for (int i = 0; i < count; i++) System.out.println(matches[i]);
        System.out.println("У масиві знайдено " + count + " входжень (повтори враховано). Умова: " + conditionDescription + ".");
        return count;
    }

    public int analyzeCondition(List<Integer> values, boolean sorted, Integer threshold) {
        String conditionDescription = "значення, менші за " + threshold;
        String phase = sorted ? "після сортування" : "до сортування";
        System.out.println("Vector<Integer>: вибірка даних " + phase + ". Умова: " + conditionDescription);
        String operationName = "вибірка даних: Vector<Integer>, " + phase + ", умова: " + conditionDescription;
        long timeStart = System.nanoTime();
        Integer[] matches = new Integer[values.size()];
        int count = 0;
        int inspected = 0;
        java.util.ListIterator<Integer> cursor = values.listIterator();
        while (cursor.hasNext()) {
            Integer value = cursor.next();
            inspected++;
            if (sorted && value >= threshold) break;
            if (value < threshold) matches[count++] = value;
        }
        PerformanceTracker.displayOperationTime(timeStart, operationName);
        System.out.println("Переглянуто елементів: " + inspected);
        for (int i = 0; i < count; i++) System.out.println(matches[i]);
        System.out.println("У List знайдено " + count + " входжень (повтори враховано). Умова: " + conditionDescription + ".");
        return count;
    }
}
