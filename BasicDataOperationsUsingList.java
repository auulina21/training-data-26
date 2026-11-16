import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Клас BasicDataOperationsUsingList реалізує операції з колекціями типу ArrayList для даних LocalDateTime
 * з використанням лямбда-виразів і Stream API.
 * 
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataOperations()} - Виконує комплекс операцій з даними.</li>
 *   <li>{@link #performArraySorting()} - Упорядковує масив елементів LocalDateTime.</li>
 *   <li>{@link #findInArray()} - Здійснює пошук елемента в масиві LocalDateTime.</li>
 *   <li>{@link #locateMinMaxInArray()} - Визначає найменше і найбільше значення в масиві.</li>
 *   <li>{@link #sortList()} - Сортує колекцію List з LocalDateTime.</li>
 *   <li>{@link #findInList()} - Пошук конкретного значення в списку.</li>
 *   <li>{@link #locateMinMaxInList()} - Пошук мінімального і максимального значення в списку.</li>
 * </ul>
 */
public class BasicDataOperationsUsingList {
    private boolean listSorted = false;
    private boolean arraySorted = false;
    private LocalDateTime dateTimeValueToSearch;
    private LocalDateTime[] dateTimeArray;
    private List<LocalDateTime> dateTimeList;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
     * @param dateTimeValueToSearch Значення для пошуку
     * @param dateTimeArray Масив LocalDateTime
     */
    BasicDataOperationsUsingList(LocalDateTime dateTimeValueToSearch, LocalDateTime[] dateTimeArray) {
        this.dateTimeValueToSearch = dateTimeValueToSearch;
        this.dateTimeArray = dateTimeArray.clone();
        this.dateTimeList = new ArrayList<>(Arrays.asList(dateTimeArray));
    }
    
    /**
     * Виконує комплексні операції з структурами даних.
     * 
     * Метод завантажує масив і список об'єктів LocalDateTime, 
     * здійснює сортування та пошукові операції.
     */
    public void executeDataOperations(LocalDateTime lower, LocalDateTime upper) {
        // спочатку працюємо з колекцією List
        System.out.println("List: до сортування");
        analyzeCondition(dateTimeList, listSorted, lower, upper);
        findInList();
        locateMinMaxInList();
        
        sortList();
        System.out.println("List: після сортування");
        analyzeCondition(dateTimeList, listSorted, lower, upper);
        
        findInList();
        locateMinMaxInList();

        // потім обробляємо масив дати та часу
        System.out.println("Масив: до сортування");
        analyzeCondition(dateTimeArray, arraySorted, lower, upper);
        findInArray();
        locateMinMaxInArray();

        performArraySorting();
        System.out.println("Масив: після сортування");
        analyzeCondition(dateTimeArray, arraySorted, lower, upper);
        
        findInArray();
        locateMinMaxInArray();

        // зберігаємо до окремого файлу відсортований вміст ArrayList
        DataFileHandler.writeArrayToFile(dateTimeList.toArray(new LocalDateTime[0]), BasicDataOperations.SORTED_FILE_PREFIX + ".list.data");
    }

    /**
     * Упорядковує масив об'єктів LocalDateTime за зростанням.
     * Фіксує та виводить тривалість операції сортування в наносекундах.
     */
    void performArraySorting() {
        long timeStart = System.nanoTime();

        // sorted() - проміжна операція, toArray() збирає елементи потоку в новий масив
        dateTimeArray = Arrays.stream(dateTimeArray)
                .sorted()
                .toArray(LocalDateTime[]::new);
        arraySorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування масиву LocalDateTime[]");
    }

    /**
     * Здійснює пошук конкретного значення в масиві дати та часу.
     */
    void findInArray() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + dateTimeValueToSearch + "' в масивi LocalDateTime[], " + (arraySorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        int position;
        if (arraySorted) {
            position = Arrays.binarySearch(dateTimeArray, dateTimeValueToSearch);
        } else {
            // потік індексів масиву: findFirst() зупиняє перебір на першому збігу
            position = IntStream.range(0, dateTimeArray.length)
                    .filter(index -> dateTimeArray[index].compareTo(dateTimeValueToSearch) == 0)
                    .findFirst()
                    .orElse(-1);
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (position >= 0) {
            System.out.println("Елемент '" + dateTimeValueToSearch + "' знайдено в масивi за позицією: " + position);
        } else {
            System.out.println("Елемент '" + dateTimeValueToSearch + "' відсутній в масиві.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в масиві дати та часу.
     */
    void locateMinMaxInArray() {
        if (dateTimeArray == null || dateTimeArray.length == 0) {
            System.out.println("Масив є пустим або не ініціалізованим.");
            return;
        }

        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "визначення мiнiмальної i максимальної дати в масивi LocalDateTime[], " + (arraySorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        LocalDateTime minValue;
        LocalDateTime maxValue;

        if (arraySorted) {
            // у відсортованому масиві граничні значення лежать на його кінцях
            minValue = dateTimeArray[0];
            maxValue = dateTimeArray[dateTimeArray.length - 1];
        } else {
            // min() і max() повертають Optional<LocalDateTime>; масив не порожній, тому значення є
            minValue = Arrays.stream(dateTimeArray)
                    .min(LocalDateTime::compareTo)
                    .orElseThrow();
            maxValue = Arrays.stream(dateTimeArray)
                    .max(LocalDateTime::compareTo)
                    .orElseThrow();
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        System.out.println("Найменше значення в масивi: " + minValue);
        System.out.println("Найбільше значення в масивi: " + maxValue);
    }

    /**
     * Шукає конкретне значення дати та часу в колекції ArrayList.
     */
    void findInList() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + dateTimeValueToSearch + "' в ArrayList<LocalDateTime>, " + (listSorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        int position;
        if (listSorted) {
            position = Collections.binarySearch(dateTimeList, dateTimeValueToSearch);
        } else {
            // takeWhile() пропускає елементи до першого збігу, count() рахує їх:
            // кількість елементів перед шуканим і є його позицією
            long elementsBeforeMatch = dateTimeList.stream()
                    .takeWhile(dateTime -> dateTime.compareTo(dateTimeValueToSearch) != 0)
                    .count();
            position = elementsBeforeMatch < dateTimeList.size() ? (int) elementsBeforeMatch : -1;
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (position >= 0) {
            System.out.println("Елемент '" + dateTimeValueToSearch + "' знайдено в ArrayList за позицією: " + position);
        } else {
            System.out.println("Елемент '" + dateTimeValueToSearch + "' відсутній в ArrayList.");
        }
    }

    /**
     * Визначає найменше і найбільше значення в колекції ArrayList з датами.
     */
    void locateMinMaxInList() {
        if (dateTimeList == null || dateTimeList.isEmpty()) {
            System.out.println("Колекція ArrayList є пустою або не ініціалізованою.");
            return;
        }

        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "визначення мiнiмальної i максимальної дати в ArrayList<LocalDateTime>, " + (listSorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        LocalDateTime minValue;
        LocalDateTime maxValue;

        if (listSorted) {
            // у відсортованому списку граничні значення лежать на його кінцях
            minValue = dateTimeList.get(0);
            maxValue = dateTimeList.get(dateTimeList.size() - 1);
        } else {
            // min() і max() повертають Optional<LocalDateTime>; список не порожній, тому значення є
            minValue = dateTimeList.stream()
                    .min(LocalDateTime::compareTo)
                    .orElseThrow();
            maxValue = dateTimeList.stream()
                    .max(LocalDateTime::compareTo)
                    .orElseThrow();
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        System.out.println("Найменше значення в List: " + minValue);
        System.out.println("Найбільше значення в List: " + maxValue);
    }

    /**
     * Упорядковує колекцію List з об'єктами LocalDateTime за зростанням.
     * Відстежує та виводить час виконання операції сортування.
     */
    void sortList() {
        long timeStart = System.nanoTime();

        // Collectors.toCollection(ArrayList::new) збирає відсортований потік у новий ArrayList
        dateTimeList = dateTimeList.stream()
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
        listSorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування ArrayList<LocalDateTime>");
    }

    // BEGIN CONDITION
    /** вибірка даних; sorted=true лише для даних, упорядкованих за зростанням. */
    public int analyzeCondition(LocalDateTime[] values, boolean sorted, LocalDateTime lower, LocalDateTime upper) {
        if (lower.isAfter(upper)) throw new IllegalArgumentException("Нижня межа перевищує верхню");
        String conditionDescription = "значення в інтервалі [" + lower + "; " + upper + "] включно";
        String phase = sorted ? "після сортування" : "до сортування";
        System.out.println("Масив: вибірка даних " + phase + ". Умова: " + conditionDescription);
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "вибірка даних: масив LocalDateTime[], " + phase + ", умова: " + conditionDescription;
        long timeStart = System.nanoTime();
        LocalDateTime[] matches;
        if (!sorted) {
            // Невпорядковані дані: filter() перевіряє кожне входження.
            matches = Arrays.stream(values)
                    .filter(value -> !value.isBefore(lower) && !value.isAfter(upper))
                    .toArray(LocalDateTime[]::new);
        } else {
            // Впорядковані дані: dropWhile() відкидає початкові значення, менші за lower,
            // а takeWhile() зупиняє потік на першому значенні, більшому за upper,
            // тож решта масиву не переглядається.
            matches = Arrays.stream(values)
                    .dropWhile(value -> value.isBefore(lower))
                    .takeWhile(value -> !value.isAfter(upper))
                    .toArray(LocalDateTime[]::new);
        }
        PerformanceTracker.displayOperationTime(timeStart, operationName);
        // Друк результатів не входить у виміряний час пошуку.
        Arrays.stream(matches).forEach(System.out::println);
        int count = matches.length;
        System.out.println("У масиві знайдено " + count + " входжень (повтори враховано). Умова: " + conditionDescription + ".");
        return count;
    }

    /** вибірка даних; sorted=true лише для даних, упорядкованих за зростанням. */
    public int analyzeCondition(List<LocalDateTime> values, boolean sorted, LocalDateTime lower, LocalDateTime upper) {
        if (lower.isAfter(upper)) throw new IllegalArgumentException("Нижня межа перевищує верхню");
        String conditionDescription = "значення в інтервалі [" + lower + "; " + upper + "] включно";
        String phase = sorted ? "після сортування" : "до сортування";
        System.out.println("List: вибірка даних " + phase + ". Умова: " + conditionDescription);
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "вибірка даних: ArrayList<LocalDateTime>, " + phase + ", умова: " + conditionDescription;
        long timeStart = System.nanoTime();
        List<LocalDateTime> matches;
        if (!sorted) {
            // Невпорядковані дані: filter() перевіряє кожне входження.
            matches = values.stream()
                    .filter(value -> !value.isBefore(lower) && !value.isAfter(upper))
                    .toList();
        } else {
            // Впорядковані дані: dropWhile() відкидає початкові значення, менші за lower,
            // а takeWhile() зупиняє потік на першому значенні, більшому за upper,
            // тож решта списку не переглядається.
            matches = values.stream()
                    .dropWhile(value -> value.isBefore(lower))
                    .takeWhile(value -> !value.isAfter(upper))
                    .toList();
        }
        PerformanceTracker.displayOperationTime(timeStart, operationName);
        // Друк результатів не входить у виміряний час пошуку.
        matches.forEach(System.out::println);
        int count = matches.size();
        System.out.println("У List знайдено " + count + " входжень (повтори враховано). Умова: " + conditionDescription + ".");
        return count;
    }
    // END CONDITION
}
