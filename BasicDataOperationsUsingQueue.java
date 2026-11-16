import java.time.LocalDateTime;
import java.util.Queue;
import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Клас BasicDataOperationsUsingQueue реалізує роботу з колекціями типу Queue для LocalDateTime
 * з використанням лямбда-виразів і Stream API.
 *
 * <p>Основні функції класу:</p>
 * <ul>
 *   <li>{@link #runDataProcessing()} - Запускає комплекс операцій з даними.</li>
 *   <li>{@link #findInQueue()} - Пошук значення в черзі LocalDateTime.</li>
 *   <li>{@link #locateMinMaxInQueue()} - Знаходить граничні значення в черзі.</li>
 *   <li>{@link #performQueueOperations()} - Виконує операції peek і poll з чергою.</li>
 * </ul>
 *
 */
public class BasicDataOperationsUsingQueue {
    private LocalDateTime dateTimeValueToSearch;
    private Queue<LocalDateTime> dateTimeQueue;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     *
     * @param dateTimeValueToSearch Значення для пошуку
     * @param dateTimeArray Масив LocalDateTime
     */
    BasicDataOperationsUsingQueue(LocalDateTime dateTimeValueToSearch, LocalDateTime[] dateTimeArray) {
        this.dateTimeValueToSearch = dateTimeValueToSearch;
        this.dateTimeQueue = new PriorityQueue<>(Arrays.asList(dateTimeArray));
    }

    /**
     * Запускає комплексну обробку даних з використанням черги.
     *
     * Метод виконує пошук і визначення граничних значень у черзі LocalDateTime,
     * а потім операції peek і poll.
     */
    public void runDataProcessing() {
        findInQueue();
        locateMinMaxInQueue();

        // зберігаємо до файлу відсортовані значення з PriorityQueue (з повторами);
        // порядок обходу черги не є відсортованим, тому сортуємо копію її вмісту
        LocalDateTime[] sortedQueueValues = dateTimeQueue.stream()
                .sorted()
                .toArray(LocalDateTime[]::new);
        DataFileHandler.writeArrayToFile(sortedQueueValues, BasicDataOperations.SORTED_FILE_PREFIX + ".queue.data");

        // Вилучення виконуємо після пошуку і запису файлу, щоб вони працювали з повним складом черги.
        performQueueOperations();
    }

    /**
     * Здійснює пошук конкретного значення в черзі дати та часу.
     */
    private void findInQueue() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + dateTimeValueToSearch + "' в PriorityQueue<LocalDateTime>";
        // вимірюємо час пошуку в черзі
        long timeStart = System.nanoTime();

        // PriorityQueue зберігає елементи як бінарну купу в масиві, тому бінарний пошук
        // неможливий. anyMatch() перебирає елементи потоку, доки предикат не поверне true:
        // це лінійний пошук, O(n), як і в методі contains().
        boolean elementExists = dateTimeQueue.stream()
                .anyMatch(dateTime -> dateTime.equals(dateTimeValueToSearch));

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (elementExists) {
            System.out.println("Елемент '" + dateTimeValueToSearch + "' знайдено в Queue");
        } else {
            System.out.println("Елемент '" + dateTimeValueToSearch + "' відсутній в Queue.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в черзі LocalDateTime.
     */
    private void locateMinMaxInQueue() {
        if (dateTimeQueue == null || dateTimeQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        // відстежуємо час пошуку граничних значень
        long timeStart = System.nanoTime();

        // min() і max() - термінальні операції: кожна обходить увесь потік і порівнює
        // елементи через compareTo(), O(n). Результат - Optional<LocalDateTime>;
        // черга не порожня, тому значення є.
        LocalDateTime minValue = dateTimeQueue.stream()
                .min(LocalDateTime::compareTo)
                .orElseThrow();
        LocalDateTime maxValue = dateTimeQueue.stream()
                .max(LocalDateTime::compareTo)
                .orElseThrow();

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмальної i максимальної дати в PriorityQueue<LocalDateTime>");

        System.out.println("Найменше значення в Queue: " + minValue);
        System.out.println("Найбільше значення в Queue: " + maxValue);
    }

    /**
     * Виконує операції peek і poll з чергою LocalDateTime.
     */
    private void performQueueOperations() {
        if (dateTimeQueue == null || dateTimeQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        // peek() повертає корінь купи, тобто найменшу дату, не видаляючи її; O(1).
        LocalDateTime headElement = dateTimeQueue.peek();
        System.out.println("Головний елемент черги (peek): " + headElement);

        // poll() видаляє корінь: на його місце стає останній елемент купи і «просідає» вниз,
        // доки не відновиться порядок купи; O(log n). Новим коренем стає наступна найменша дата.
        headElement = dateTimeQueue.poll();
        System.out.println("Видалений елемент черги (poll): " + headElement);

        headElement = dateTimeQueue.peek();
        System.out.println("Новий головний елемент черги: " + headElement);
    }
}
