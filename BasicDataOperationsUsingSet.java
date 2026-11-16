import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Клас BasicDataOperationsUsingSet реалізує операції з множиною HashSet для LocalDateTime
 * з використанням лямбда-виразів і Stream API.
 *
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataAnalysis()} - Запускає аналіз даних.</li>
 *   <li>{@link #findInSet()} - Пошук значення в множині LocalDateTime.</li>
 *   <li>{@link #locateMinMaxInSet()} - Знаходить мінімальне і максимальне значення в множині.</li>
 *   <li>{@link #analyzeDuplicates()} - Порівнює кількість вхідних значень і елементів множини.</li>
 * </ul>
 */
public class BasicDataOperationsUsingSet {
    LocalDateTime dateTimeValueToSearch;
    int sourceValueCount;
    Set<LocalDateTime> dateTimeSet = new HashSet<>();

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     *
     * @param dateTimeValueToSearch Значення для пошуку
     * @param dateTimeArray Масив LocalDateTime
     */
    BasicDataOperationsUsingSet(LocalDateTime dateTimeValueToSearch, LocalDateTime[] dateTimeArray) {
        this.dateTimeValueToSearch = dateTimeValueToSearch;
        this.sourceValueCount = dateTimeArray.length;
        this.dateTimeSet = new HashSet<>(Arrays.asList(dateTimeArray));
    }

    /**
     * Запускає комплексний аналіз даних з використанням множини HashSet.
     *
     * Метод виконує пошук, визначення граничних значень і аналіз повторів у множині LocalDateTime.
     */
    public void executeDataAnalysis() {
        findInSet();
        locateMinMaxInSet();
        analyzeDuplicates();

        // зберігаємо до файлу відсортовані унікальні значення з HashSet (без повторів)
        LocalDateTime[] sortedSetValues = dateTimeSet.stream()
                .sorted()
                .toArray(LocalDateTime[]::new);
        DataFileHandler.writeArrayToFile(sortedSetValues, BasicDataOperations.SORTED_FILE_PREFIX + ".set.data");
    }

    /**
     * Здійснює пошук конкретного значення в множині дати та часу.
     */
    private void findInSet() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + dateTimeValueToSearch + "' в HashSet<LocalDateTime>";
        long timeStart = System.nanoTime();

        // anyMatch() перебирає елементи потоку, доки предикат не поверне true: це лінійний
        // пошук, O(n). Метод contains() знайшов би елемент HashSet без перебору, за O(1).
        boolean elementExists = dateTimeSet.stream()
                .anyMatch(dateTime -> dateTime.equals(dateTimeValueToSearch));

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (elementExists) {
            System.out.println("Елемент '" + dateTimeValueToSearch + "' знайдено в HashSet");
        } else {
            System.out.println("Елемент '" + dateTimeValueToSearch + "' відсутній в HashSet.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в множині LocalDateTime.
     */
    private void locateMinMaxInSet() {
        if (dateTimeSet == null || dateTimeSet.isEmpty()) {
            System.out.println("HashSet є пустим або не ініціалізованим.");
            return;
        }

        long timeStart = System.nanoTime();

        // HashSet розкладає елементи за хеш-кодами, а не за порядком дат, тому найменша
        // і найбільша дати можуть лежати будь-де. min() і max() - термінальні операції:
        // кожна обходить увесь потік і порівнює елементи через compareTo(), O(n).
        // Результат - Optional<LocalDateTime>; множина не порожня, тому значення є.
        LocalDateTime minValue = dateTimeSet.stream()
                .min(LocalDateTime::compareTo)
                .orElseThrow();
        LocalDateTime maxValue = dateTimeSet.stream()
                .max(LocalDateTime::compareTo)
                .orElseThrow();

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмальної i максимальної дати в HashSet<LocalDateTime>");

        System.out.println("Найменше значення в HashSet: " + minValue);
        System.out.println("Найбільше значення в HashSet: " + maxValue);
    }

    /**
     * Порівнює кількість вхідних значень і елементів множини: різниця — це відкинуті повтори.
     */
    private void analyzeDuplicates() {
        System.out.println("Кiлькiсть вхiдних значень: " + sourceValueCount);
        System.out.println("Кiлькiсть елементiв в HashSet: " + dateTimeSet.size());
        System.out.println("Повторних входжень, вiдкинутих HashSet: " + (sourceValueCount - dateTimeSet.size()));
    }
}
