package academy.javapro;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * Demonstrates merging and zipping two collections using generics and BiFunction.
 */
public class Midterm2 {

    /**
     * Simple Person class used for demonstration.
     */
    static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        /**
         * Returns a simple string representation of this Person.
         */
        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }

    /**
     * Merges two collections of different types into a new list.
     *
     * @param <T>             type of elements in the first collection
     * @param <S>             type of elements in the second collection
     * @param <R>             type of elements in the result list
     * @param firstCollection first collection of type T
     * @param secondCollection second collection of type S
     * @param mergeFunction   how to merge one T and one S into an R
     * @return list of merged results
     * @throws IllegalArgumentException if the two collections differ in size
     */
    public static <T, S, R> List<R> mergeCollections(
            Collection<T> firstCollection,
            Collection<S> secondCollection,
            BiFunction<T, S, R> mergeFunction) {

        int size1 = firstCollection.size();
        int size2 = secondCollection.size();
        if (size1 != size2) {
            throw new IllegalArgumentException(
                "Collections must have the same size for element-wise merging. " +
                "First collection size: " + size1 +
                ", Second collection size: " + size2);
        }

        List<T> list1 = new ArrayList<>(firstCollection);
        List<S> list2 = new ArrayList<>(secondCollection);
        List<R> result = new ArrayList<>(size1);

        for (int i = 0; i < size1; i++) {
            R merged = mergeFunction.apply(list1.get(i), list2.get(i));
            result.add(merged);
        }

        return result;
    }

    /**
     * Zips two collections by merging elements until one runs out.
     *
     * @param <T>             type of elements in the first collection
     * @param <S>             type of elements in the second collection
     * @param <R>             type of elements in the result list
     * @param firstCollection first collection of type T
     * @param secondCollection second collection of type S
     * @param mergeFunction   how to merge one T and one S into an R
     * @return list of merged results up to the shorter collection’s length
     */
    public static <T, S, R> List<R> zipCollections(
            Collection<T> firstCollection,
            Collection<S> secondCollection,
            BiFunction<T, S, R> mergeFunction) {

        int limit = Math.min(firstCollection.size(), secondCollection.size());
        List<R> result = new ArrayList<>(limit);

        Iterator<T> firstIterator = firstCollection.iterator();
        Iterator<S> secondIterator = secondCollection.iterator();

        while (firstIterator.hasNext() && secondIterator.hasNext()) {
            T firstElem = firstIterator.next();
            S secondElem = secondIterator.next();
            R merged = mergeFunction.apply(firstElem, secondElem);
            result.add(merged);
        }

        return result;
    }

    public static void main(String[] args) {
        // Example 1: Merging numbers and strings
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<String> words = Arrays.asList("one", "two", "three", "four", "five");
        BiFunction<Integer, String, String> numberWordMerger =
            (num, word) -> num + " = " + word;
        List<String> combinedNumberWords = mergeCollections(numbers, words, numberWordMerger);
        System.out.println("Example 1: Merging numbers with their word representations");
        combinedNumberWords.forEach(System.out::println);

        // Example 2: Creating Person objects from names and ages
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        List<Integer> ages = Arrays.asList(25, 30, 22);
        BiFunction<String, Integer, Person> personCreator = Person::new;
        List<Person> people = mergeCollections(names, ages, personCreator);
        System.out.println("\nExample 2: Creating Person objects from names and ages");
        people.forEach(System.out::println);

        // Example 3: Merging using mathematical operations
        List<Double> firstNumbers = Arrays.asList(1.5, 2.5, 3.5);
        List<Double> secondNumbers = Arrays.asList(0.5, 1.0, 1.5);
        BiFunction<Double, Double, Double> sum = Double::sum;
        BiFunction<Double, Double, Double> product = (a, b) -> a * b;
        BiFunction<Double, Double, String> comparisonResult =
            (a, b) -> a + " vs " + b + ": " +
                      (a > b ? "First is larger" :
                      a < b ? "Second is larger" : "Both are equal");
        List<Double> sums = zipCollections(firstNumbers, secondNumbers, sum);
        List<Double> products = zipCollections(firstNumbers, secondNumbers, product);
        List<String> comparisons = zipCollections(firstNumbers, secondNumbers, comparisonResult);
        System.out.println("\nExample 3: Mathematical operations");
        System.out.println("Sums: " + sums);
        System.out.println("Products: " + products);
        System.out.println("Comparisons:");
        comparisons.forEach(System.out::println);

        // Example 4: Using different sized collections with zipCollections
        List<Character> letters = Arrays.asList('A', 'B', 'C', 'D', 'E');
        List<Integer> positions = Arrays.asList(1, 2, 3); // shorter list
        BiFunction<Character, Integer, String> positionedLetter =
            (letter, position) -> position + ". " + letter;
        List<String> lettersWithPositions = zipCollections(letters, positions, positionedLetter);
        System.out.println("\nExample 4: Using different sized collections");
        lettersWithPositions.forEach(System.out::println);
    }
}