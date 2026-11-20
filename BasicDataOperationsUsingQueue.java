import java.util.Queue;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BasicDataOperationsUsingQueue {
    private Integer valueToSearch;
    private Queue<Integer> valueQueue;

    BasicDataOperationsUsingQueue(Integer valueToSearch, Integer[] valueArray) {
        this.valueToSearch = valueToSearch;
        this.valueQueue = new ConcurrentLinkedQueue<>(Arrays.asList(valueArray.clone()));
    }

    public void runDataProcessing() {
        findInQueue();
        locateMinMaxInQueue();

        Integer[] sortedQueueValues = valueQueue.toArray(new Integer[0]);
        Arrays.sort(sortedQueueValues);
        DataFileHandler.writeArrayToFile(sortedQueueValues, BasicDataOperations.SORTED_FILE_PREFIX + ".queue.data");

        performQueueOperations();
    }

    private void findInQueue() {

        String operationName = "пошук елемента '" + valueToSearch + "' в ConcurrentLinkedQueue<Integer>";

        long timeStart = System.nanoTime();



        boolean elementExists = this.valueQueue.contains(valueToSearch);

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (elementExists) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в Queue");
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в Queue.");
        }
    }

    private void locateMinMaxInQueue() {
        if (valueQueue == null || valueQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        long timeStart = System.nanoTime();



        Integer minValue = Collections.min(valueQueue);
        Integer maxValue = Collections.max(valueQueue);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мінімального і максимального значення в ConcurrentLinkedQueue<Integer>");

        System.out.println("Найменше значення в Queue: " + minValue);
        System.out.println("Найбільше значення в Queue: " + maxValue);
    }

    private void performQueueOperations() {
        if (valueQueue == null || valueQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        Integer headElement = valueQueue.peek();
        System.out.println("Головний елемент черги (peek): " + headElement);

        headElement = valueQueue.poll();
        System.out.println("Видалений елемент черги (poll): " + headElement);

        headElement = valueQueue.peek();
        System.out.println("Новий головний елемент черги: " + headElement);
    }
}
