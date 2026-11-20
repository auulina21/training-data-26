# Практична робота №4, варіант 19

Соловйов Ілля Сергійович, КІ-26. Програмування мовою Java.

Навчальне джерело: https://github.com/chdbc-samples/training-data-26
Адаптацію підготовлено з допомогою Codex; результати перевірено компіляцією та запуском.

Integer; Vector, LinkedHashSet, ConcurrentLinkedQueue. Точний пошук -6986, вибірка значень < 0.

```sh
javac -encoding UTF-8 *.java
java BasicDataOperations -6986 0
```

UTF-8, порожні рядки та BOM; optional третій аргумент задає вхідний файл.
Результати: list/Integer.sorted.list.data, .set.data, .queue.data.

CherryTree(String variety, Integer frostResistance); сорт та морозостійкість за спаданням.
HashMap і TreeMap: 10 початкових пар, пошук Любська/-28 → Костянтин;
додавання Встріча/-29 → Захар, видалення ключа Любська/-28 та значення Лідія.
Кінцевий розмір кожної Map: 8.

```sh
javac -encoding UTF-8 *.java
java BasicDataOperationsUsingMap
```

Робота №4: обидві програми переведені на Stream API. Гілка створена з lesson-3
та перенесена на lesson-2 через `git rebase lesson-2 -X theirs`.
Реалізації бінарного пошуку після сортування та firstEntry/lastEntry збережено.

Реальні логи Codespaces з часом операцій у наносекундах розміщено в logs/.
Один запуск на малих даних відображає вплив JIT, завантаження класів і шуму;
вимірювання не є універсальним рейтингом швидкодії колекцій.
