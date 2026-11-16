import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Клас BasicDataOperationsUsingMap реалізує операції з колекціями типу Map для зберігання пар ключ-значення
 * з використанням лямбда-виразів і Stream API.
 *
 * <p>Ключ - record {@link Oak} з характеристиками дуба, значення - місце зростання дуба.</p>
 *
 * <p>Операції, які виконуються для кожного типу Map:</p>
 * <ul>
 *   <li>пошук за ключем;</li>
 *   <li>пошук за значенням (filter() + findFirst());</li>
 *   <li>виведення пар ключ-значення на консоль до і після сортування;</li>
 *   <li>сортування за ключами;</li>
 *   <li>визначення найменшого й найбільшого ключа;</li>
 *   <li>додавання пари ключ-значення;</li>
 *   <li>видалення за ключем;</li>
 *   <li>видалення за значенням.</li>
 * </ul>
 */
public class BasicDataOperationsUsingMap {
    private static final Oak KEY_TO_SEARCH_AND_DELETE = new Oak("Дуб скельний", 120);
    private static final Oak KEY_TO_ADD = new Oak("Дуб монгольський", 70);

    private static final String VALUE_TO_SEARCH_AND_DELETE = "Софіївка";
    private static final String VALUE_TO_ADD = "Тростянець";

    private HashMap<Oak, String> hashMap;
    private TreeMap<Oak, String> treeMap;

    /**
     * Record Oak зберігає характеристики дуба: вид (species) і вік (age).
     *
     * <p>Record автоматично генерує:</p>
     * <ul>
     *   <li>приватні final поля species і age та канонічний конструктор;</li>
     *   <li>методи доступу species() і age();</li>
     *   <li>equals() і hashCode(), які враховують усі компоненти record (species і age).</li>
     * </ul>
     *
     * <p>Вручну реалізовано:</p>
     * <ul>
     *   <li>compareTo() - природний порядок: вид за зростанням, потім вік за спаданням;</li>
     *   <li>toString() - формат Oak{species='...', age=...}, доповнений хеш-кодом.</li>
     * </ul>
     *
     * @param species вид дуба, наприклад "Дуб звичайний"
     * @param age вік дерева, років
     */
    public record Oak(String species, Integer age) implements Comparable<Oak> {

        /**
         * Компактний конструктор перевіряє, що характеристики задані.
         * Завдяки цьому в compareTo() не потрібно окремо обробляти значення null.
         */
        public Oak {
            Objects.requireNonNull(species, "species не може бути null");
            Objects.requireNonNull(age, "age не може бути null");
        }

        /**
         * Порівнює цей дуб з іншим для визначення природного порядку сортування.
         *
         * <p>Критерій порівняння: вид (species) за зростанням, а для однакових видів - вік (age) за спаданням.</p>
         *
         * <p>Метод використовується:</p>
         * <ul>
         *   <li>TreeMap - для автоматичного впорядкування ключів;</li>
         *   <li>sorted(Map.Entry.comparingByKey()) - для сортування потоку пар за ключами.</li>
         * </ul>
         *
         * <p>Метод узгоджений з equals(): compareTo() повертає 0 тоді й лише тоді,
         * коли species і age однакові, тобто коли equals() повертає true.</p>
         *
         * @param other дуб для порівняння
         * @return від'ємне число, якщо цей дуб іде раніше за other; 0, якщо вони рівні;
         *         додатне число, якщо цей дуб іде пізніше за other
         */
        @Override
        public int compareTo(Oak other) {
            // Спочатку порівнюємо за видом (за зростанням)
            int speciesComparison = this.species.compareTo(other.species);
            if (speciesComparison != 0) {
                return speciesComparison;
            }
            // Якщо види однакові, порівнюємо за віком (за спаданням - other порівнюється з this)
            return other.age.compareTo(this.age);
        }

        /**
         * Повертає рядкове представлення дуба.
         * Хеш-код додано, щоб у логах було видно, від чого залежить порядок елементів у HashMap.
         *
         * @return рядок у форматі Oak{species='...', age=..., hashCode=...}
         */
        @Override
        public String toString() {
            return "Oak{species='" + species + "', age=" + age + ", hashCode=" + hashCode() + "}";
        }
    }

    /**
     * Конструктор, який ініціалізує об'єкт готовими даними.
     *
     * @param hashMap HashMap з початковими даними (ключ: Oak, значення: місце зростання)
     * @param treeMap TreeMap з початковими даними (ключ: Oak, значення: місце зростання)
     */
    BasicDataOperationsUsingMap(HashMap<Oak, String> hashMap, TreeMap<Oak, String> treeMap) {
        this.hashMap = hashMap;
        this.treeMap = treeMap;
    }

    /**
     * Виконує комплекс операцій з HashMap і TreeMap: пошук, виведення, сортування, додавання, видалення.
     */
    public void executeDataOperations() {
        // Спочатку працюємо з HashMap
        System.out.println("========= Операції з HashMap =========");
        System.out.println("Початковий розмір HashMap: " + hashMap.size());

        // Пошук до сортування
        findByKeyInHashMap();
        findByValueInHashMap();
        findMinMaxKeyInHashMap();

        printHashMap();
        sortHashMap();
        printHashMap();

        // Пошук після сортування
        findByKeyInHashMap();
        findByValueInHashMap();
        findMinMaxKeyInHashMap();

        addEntryToHashMap();

        removeByKeyFromHashMap();
        removeByValueFromHashMap();

        System.out.println("Кінцевий розмір HashMap: " + hashMap.size());

        // Потім обробляємо TreeMap
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

    // ===== Методи для HashMap =====

    /**
     * Виводить вміст HashMap.
     * HashMap не гарантує жодного порядку елементів: він залежить від hashCode() ключів.
     */
    private void printHashMap() {
        System.out.println("\n=== Пари ключ-значення в HashMap ===");
        long timeStart = System.nanoTime();

        hashMap.entrySet().forEach(entry ->
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue()));

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в HashMap");
    }

    /**
     * Сортує HashMap за ключами.
     * Використовує потік пар, відсортований за природним порядком Oak (Oak.compareTo()),
     * і перезаписує hashMap новою HashMap, заповненою у відсортованому порядку.
     *
     * <p>Увага: HashMap не зберігає порядок додавання, тому після сортування порядок
     * виведення не зміниться. Зберегти відсортований порядок може LinkedHashMap.</p>
     */
    private void sortHashMap() {
        long timeStart = System.nanoTime();

        // 1) entrySet().stream() - перетворюємо пари ключ-значення в потік
        // 2) sorted(Map.Entry.comparingByKey()) - сортуємо за ключами: Oak.compareTo()
        // 3) collect(Collectors.toMap(...)) - збираємо відсортовані пари в нову HashMap:
        //    - Map.Entry::getKey - екстрактор ключа
        //    - Map.Entry::getValue - екстрактор значення
        //    - (e1, e2) -> e1 - функція злиття для однакових ключів (береться перше значення)
        //    - HashMap::new - конструктор Map, у яку збирається результат
        hashMap = hashMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        HashMap::new
                ));

        PerformanceTracker.displayOperationTime(timeStart, "сортування HashMap за ключами");
    }

    /**
     * Здійснює пошук елемента за ключем в HashMap.
     * Використовує Oak.hashCode() для вибору кошика та Oak.equals() для порівняння ключів.
     */
    void findByKeyInHashMap() {
        long timeStart = System.nanoTime();

        boolean found = hashMap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в HashMap");

        if (found) {
            String value = hashMap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Місце зростання: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в HashMap.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в HashMap.
     * Перебирає потік пар Map.Entry і повертає першу пару із заданим значенням.
     */
    void findByValueInHashMap() {
        long timeStart = System.nanoTime();

        // filter() залишає пари із заданим значенням, findFirst() повертає першу з них
        Optional<Map.Entry<Oak, String>> foundEntry = hashMap.entrySet().stream()
                .filter(entry -> VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue()))
                .findFirst();

        PerformanceTracker.displayOperationTime(timeStart, "пошук за значенням в HashMap");

        foundEntry.ifPresentOrElse(
                entry -> System.out.println("Місце зростання '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Дуб: " + entry.getKey()),
                () -> System.out.println("Місце зростання '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутнє в HashMap."));
    }

    /**
     * Визначає найменший і найбільший ключі в HashMap.
     * HashMap не зберігає порядок ключів, тому потрібен перебір усіх ключів потоком - O(n),
     * і сортування HashMap цього не змінює.
     */
    void findMinMaxKeyInHashMap() {
        if (hashMap.isEmpty()) {
            System.out.println("HashMap порожня: найменшого й найбільшого ключа немає.");
            return;
        }

        long timeStart = System.nanoTime();

        Oak minKey = hashMap.keySet().stream().min(Oak::compareTo).orElseThrow();
        Oak maxKey = hashMap.keySet().stream().max(Oak::compareTo).orElseThrow();

        PerformanceTracker.displayOperationTime(timeStart, "визначення найменшого й найбільшого ключа в HashMap");

        System.out.println("Найменший ключ: " + minKey + ", найбільший ключ: " + maxKey);
    }

    /**
     * Додає новий запис до HashMap.
     */
    void addEntryToHashMap() {
        long timeStart = System.nanoTime();

        hashMap.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до HashMap");

        System.out.println("Додано новий запис: дуб='" + KEY_TO_ADD + "', місце зростання='" + VALUE_TO_ADD + "'");
    }

    /**
     * Видаляє запис з HashMap за ключем.
     */
    void removeByKeyFromHashMap() {
        long timeStart = System.nanoTime();

        String removedValue = hashMap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з HashMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Місце зростання було: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    /**
     * Видаляє записи з HashMap за значенням.
     */
    void removeByValueFromHashMap() {
        long timeStart = System.nanoTime();

        // спочатку збираємо ключі пар із заданим значенням у список,
        // потім видаляємо їх: змінювати Map під час обходу її потоку не можна
        List<Oak> keysToRemove = hashMap.entrySet().stream()
                .filter(entry -> VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        keysToRemove.forEach(hashMap::remove);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з HashMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з місцем зростання '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    // ===== Методи для TreeMap =====

    /**
     * Виводить вміст TreeMap.
     * TreeMap завжди впорядкована за ключами (вид за зростанням, вік за спаданням).
     */
    private void printTreeMap() {
        System.out.println("\n=== Пари ключ-значення в TreeMap ===");
        long timeStart = System.nanoTime();

        treeMap.entrySet().forEach(entry ->
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue()));

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в TreeMap");
    }

    /**
     * Здійснює пошук елемента за ключем в TreeMap.
     * Використовує Oak.compareTo() для навігації по червоно-чорному дереву.
     */
    void findByKeyInTreeMap() {
        long timeStart = System.nanoTime();

        boolean found = treeMap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в TreeMap");

        if (found) {
            String value = treeMap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Місце зростання: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в TreeMap.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в TreeMap.
     * Перебирає потік пар Map.Entry і повертає першу пару із заданим значенням.
     */
    void findByValueInTreeMap() {
        long timeStart = System.nanoTime();

        // filter() залишає пари із заданим значенням, findFirst() повертає першу з них
        Optional<Map.Entry<Oak, String>> foundEntry = treeMap.entrySet().stream()
                .filter(entry -> VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue()))
                .findFirst();

        PerformanceTracker.displayOperationTime(timeStart, "пошук за значенням в TreeMap");

        foundEntry.ifPresentOrElse(
                entry -> System.out.println("Місце зростання '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Дуб: " + entry.getKey()),
                () -> System.out.println("Місце зростання '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутнє в TreeMap."));
    }

    /**
     * Визначає найменший і найбільший ключі в TreeMap.
     * TreeMap реалізує SequencedMap (Java 21) і впорядкована за Oak.compareTo(),
     * тому перший і останній записи - це найменший і найбільший ключі; перебір не потрібен.
     */
    void findMinMaxKeyInTreeMap() {
        if (treeMap.isEmpty()) {
            System.out.println("TreeMap порожня: найменшого й найбільшого ключа немає.");
            return;
        }

        long timeStart = System.nanoTime();

        Oak minKey = treeMap.firstEntry().getKey();
        Oak maxKey = treeMap.lastEntry().getKey();

        PerformanceTracker.displayOperationTime(timeStart, "визначення найменшого й найбільшого ключа в TreeMap");

        System.out.println("Найменший ключ: " + minKey + ", найбільший ключ: " + maxKey);
    }

    /**
     * Додає новий запис до TreeMap.
     */
    void addEntryToTreeMap() {
        long timeStart = System.nanoTime();

        treeMap.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до TreeMap");

        System.out.println("Додано новий запис: дуб='" + KEY_TO_ADD + "', місце зростання='" + VALUE_TO_ADD + "'");
    }

    /**
     * Видаляє запис з TreeMap за ключем.
     */
    void removeByKeyFromTreeMap() {
        long timeStart = System.nanoTime();

        String removedValue = treeMap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з TreeMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Місце зростання було: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    /**
     * Видаляє записи з TreeMap за значенням.
     */
    void removeByValueFromTreeMap() {
        long timeStart = System.nanoTime();

        // спочатку збираємо ключі пар із заданим значенням у список,
        // потім видаляємо їх: змінювати Map під час обходу її потоку не можна
        List<Oak> keysToRemove = treeMap.entrySet().stream()
                .filter(entry -> VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        keysToRemove.forEach(treeMap::remove);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з TreeMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з місцем зростання '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    /**
     * Головний метод для запуску програми.
     */
    public static void main(String[] args) {
        // Початкові дані (ключ: Oak, значення: місце зростання).
        // Map.of() створює незмінну Map, тому з неї створюються копії, які можна змінювати.
        Map<Oak, String> oaks = Map.of(
            new Oak("Дуб звичайний", 250), "Софіївка",
            new Oak("Дуб червоний", 80), "Голосіївський парк",
            new Oak("Дуб скельний", 120), "Холодний Яр",
            new Oak("Дуб звичайний", 1000), "Холодний Яр",
            new Oak("Дуб пухнастий", 60), "Ботанічний сад імені Фоміна",
            new Oak("Дуб болотяний", 45), "Софіївка",
            new Oak("Дуб червоний", 35), "Ботанічний сад імені Фоміна",
            new Oak("Дуб австрійський", 90), "Олександрія",
            new Oak("Дуб звичайний", 500), "Олександрія",
            new Oak("Дуб скельний", 300), "Тростянець"
        );

        HashMap<Oak, String> hashMap = new HashMap<>(oaks);
        TreeMap<Oak, String> treeMap = new TreeMap<>(oaks);

        // Створюємо об'єкт і виконуємо операції
        BasicDataOperationsUsingMap operations = new BasicDataOperationsUsingMap(hashMap, treeMap);
        operations.executeDataOperations();
    }
}
