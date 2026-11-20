/** Варіант 19. Сорт і морозостійкість за спаданням.
 * Адаптовано з chdbc-samples/training-data-26; допомога Codex, перевірено запуском. */
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class BasicDataOperationsUsingMap {
    private static final CherryTree KEY_TO_SEARCH_AND_DELETE = new CherryTree("Любська", -28);
    private static final CherryTree KEY_TO_ADD = new CherryTree("Встріча", -29);

    private static final String VALUE_TO_SEARCH_AND_DELETE = "Лідія";
    private static final String VALUE_TO_ADD = "Захар";

    private static final Comparator<Map.Entry<CherryTree, String>> CUSTOM_COMPARATOR = Map.Entry.comparingByValue();

    private HashMap<CherryTree, String> hashMap;
    private TreeMap<CherryTree, String> treeMap;

    public record CherryTree(String variety, Integer frostResistance) implements Comparable<CherryTree> {

        public CherryTree {
            Objects.requireNonNull(variety, "variety не може бути null");
            Objects.requireNonNull(frostResistance, "frostResistance не може бути null");
        }

        @Override
        public int compareTo(CherryTree other) {

            int varietyComparison = other.variety.compareTo(this.variety);
            if (varietyComparison != 0) {
                return varietyComparison;
            }

            return other.frostResistance.compareTo(this.frostResistance);
        }

        @Override
        public String toString() {
            return "CherryTree{variety='" + variety + "', frostResistance=" + frostResistance + ", hashCode=" + hashCode() + "}";
        }
    }

    BasicDataOperationsUsingMap(HashMap<CherryTree, String> hashMap, TreeMap<CherryTree, String> treeMap) {
        this.hashMap = hashMap;
        this.treeMap = treeMap;
    }

    public void executeDataOperations() {

        System.out.println("========= Операції з HashMap =========");
        System.out.println("Початковий розмір HashMap: " + hashMap.size());

        findByKeyInHashMap();
        findByValueInHashMap();
        findMinMaxKeyInHashMap();

        printHashMap();
        sortHashMap();
        printHashMap();

        findByKeyInHashMap();
        findByValueInHashMap();
        findMinMaxKeyInHashMap();

        addEntryToHashMap();

        removeByKeyFromHashMap();
        removeByValueFromHashMap();

        System.out.println("Кінцевий розмір HashMap: " + hashMap.size());

        System.out.println("\n\n========= Операції з TreeMap =========");
        System.out.println("Початковий розмір TreeMap: " + treeMap.size());

        findByKeyInTreeMap();
        findByValueInTreeMap();
        findMinMaxKeyInTreeMap();

        printTreeMap();

        addEntryToTreeMap();

        removeByKeyFromTreeMap();
        removeByValueFromTreeMap();

        System.out.println("Кінцевий розмір TreeMap: " + treeMap.size());
    }

    
    private void printHashMap() {
        System.out.println("\n=== Пари ключ-значення в HashMap ===");
        long timeStart = System.nanoTime();

        for (Map.Entry<CherryTree, String> entry : hashMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в HashMap");
    }

    private void sortHashMap() {
        long timeStart = System.nanoTime();

        List<CherryTree> sortedKeys = new ArrayList<>(hashMap.keySet());
        Collections.sort(sortedKeys);

        HashMap<CherryTree, String> sortedHashMap = new HashMap<>();
        for (CherryTree key : sortedKeys) {
            sortedHashMap.put(key, hashMap.get(key));
        }

        hashMap = sortedHashMap;

        PerformanceTracker.displayOperationTime(timeStart, "сортування HashMap за ключами");
    }

    void findByKeyInHashMap() {
        long timeStart = System.nanoTime();

        boolean found = hashMap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в HashMap");

        if (found) {
            String value = hashMap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Садівник (садівниця): " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в HashMap.");
        }
    }

    void findByValueInHashMap() {
        long timeStart = System.nanoTime();

        List<Map.Entry<CherryTree, String>> entries = new ArrayList<>(hashMap.entrySet());
        Collections.sort(entries, CUSTOM_COMPARATOR);

        Map.Entry<CherryTree, String> searchEntry = new AbstractMap.SimpleEntry<>(null, VALUE_TO_SEARCH_AND_DELETE);

        int position = Collections.binarySearch(entries, searchEntry, CUSTOM_COMPARATOR);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в HashMap");

        if (position >= 0) {
            Map.Entry<CherryTree, String> foundEntry = entries.get(position);
            System.out.println("Садівник (садівниця) '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Вишня: " + foundEntry.getKey());
        } else {
            System.out.println("Садівник (садівниця) '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутнє в HashMap.");
        }
    }

    void findMinMaxKeyInHashMap() {
        if (hashMap.isEmpty()) {
            System.out.println("HashMap порожня: найменшого й найбільшого ключа немає.");
            return;
        }

        long timeStart = System.nanoTime();

        CherryTree minKey = Collections.min(hashMap.keySet());
        CherryTree maxKey = Collections.max(hashMap.keySet());

        PerformanceTracker.displayOperationTime(timeStart, "визначення найменшого й найбільшого ключа в HashMap");

        System.out.println("Найменший ключ: " + minKey + ", найбільший ключ: " + maxKey);
    }

    void addEntryToHashMap() {
        long timeStart = System.nanoTime();

        hashMap.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до HashMap");

        System.out.println("Додано новий запис: вишня='" + KEY_TO_ADD + "', садівник (садівниця)='" + VALUE_TO_ADD + "'");
    }

    void removeByKeyFromHashMap() {
        long timeStart = System.nanoTime();

        String removedValue = hashMap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з HashMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Садівник (садівниця) було: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    void removeByValueFromHashMap() {
        long timeStart = System.nanoTime();

        List<CherryTree> keysToRemove = new ArrayList<>();
        for (Map.Entry<CherryTree, String> entry : hashMap.entrySet()) {
            if (VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue())) {
                keysToRemove.add(entry.getKey());
            }
        }

        for (CherryTree key : keysToRemove) {
            hashMap.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з HashMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з садівником (садівницею) '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    
    private void printTreeMap() {
        System.out.println("\n=== Пари ключ-значення в TreeMap ===");
        long timeStart = System.nanoTime();

        for (Map.Entry<CherryTree, String> entry : treeMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в TreeMap");
    }

    void findByKeyInTreeMap() {
        long timeStart = System.nanoTime();

        boolean found = treeMap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в TreeMap");

        if (found) {
            String value = treeMap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Садівник (садівниця): " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в TreeMap.");
        }
    }

    void findByValueInTreeMap() {
        long timeStart = System.nanoTime();

        List<Map.Entry<CherryTree, String>> entries = new ArrayList<>(treeMap.entrySet());
        Collections.sort(entries, CUSTOM_COMPARATOR);

        Map.Entry<CherryTree, String> searchEntry = new AbstractMap.SimpleEntry<>(null, VALUE_TO_SEARCH_AND_DELETE);

        int position = Collections.binarySearch(entries, searchEntry, CUSTOM_COMPARATOR);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в TreeMap");

        if (position >= 0) {
            Map.Entry<CherryTree, String> foundEntry = entries.get(position);
            System.out.println("Садівник (садівниця) '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Вишня: " + foundEntry.getKey());
        } else {
            System.out.println("Садівник (садівниця) '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутнє в TreeMap.");
        }
    }

    void findMinMaxKeyInTreeMap() {
        if (treeMap.isEmpty()) {
            System.out.println("TreeMap порожня: найменшого й найбільшого ключа немає.");
            return;
        }

        long timeStart = System.nanoTime();

        CherryTree minKey = treeMap.firstEntry().getKey();
        CherryTree maxKey = treeMap.lastEntry().getKey();

        PerformanceTracker.displayOperationTime(timeStart, "визначення найменшого й найбільшого ключа в TreeMap");

        System.out.println("Найменший ключ: " + minKey + ", найбільший ключ: " + maxKey);
    }

    void addEntryToTreeMap() {
        long timeStart = System.nanoTime();

        treeMap.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до TreeMap");

        System.out.println("Додано новий запис: вишня='" + KEY_TO_ADD + "', садівник (садівниця)='" + VALUE_TO_ADD + "'");
    }

    void removeByKeyFromTreeMap() {
        long timeStart = System.nanoTime();

        String removedValue = treeMap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з TreeMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Садівник (садівниця) було: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    void removeByValueFromTreeMap() {
        long timeStart = System.nanoTime();

        List<CherryTree> keysToRemove = new ArrayList<>();
        for (Map.Entry<CherryTree, String> entry : treeMap.entrySet()) {
            if (VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue())) {
                keysToRemove.add(entry.getKey());
            }
        }

        for (CherryTree key : keysToRemove) {
            treeMap.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з TreeMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з садівником (садівницею) '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    public static void main(String[] args) {

        Map<CherryTree, String> cherries = Map.of(
            new CherryTree("Шпанка", -30), "Лідія",
            new CherryTree("Любська", -28), "Костянтин",
            new CherryTree("Тургенівка", -35), "Емілія",
            new CherryTree("Шпанка", -32), "Арсен",
            new CherryTree("Чудо-вишня", -25), "Лідія",
            new CherryTree("Норд Стар", -40), "Марина",
            new CherryTree("Любська", -26), "Ігнат",
            new CherryTree("Молодіжна", -33), "Світлана",
            new CherryTree("Мелітопольська десертна", -24), "Данило",
            new CherryTree("Тургенівка", -31), "Інна"
        );

        HashMap<CherryTree, String> hashMap = new HashMap<>(cherries);
        TreeMap<CherryTree, String> treeMap = new TreeMap<>(cherries);

        BasicDataOperationsUsingMap operations = new BasicDataOperationsUsingMap(hashMap, treeMap);
        operations.executeDataOperations();
    }
}
