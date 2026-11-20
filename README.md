# Практична робота №2, варіант 19

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

Реальні логи Codespaces з часом операцій у наносекундах розміщено в logs/.
Один запуск на малих даних відображає вплив JIT, завантаження класів і шуму;
вимірювання не є універсальним рейтингом швидкодії колекцій.
