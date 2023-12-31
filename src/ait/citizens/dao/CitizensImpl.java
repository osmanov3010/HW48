package ait.citizens.dao;

import ait.citizens.model.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CitizensImpl implements Citizens {
    private List<Person> idList;
    private List<Person> lastNameList;
    private List<Person> ageList;
    private static Comparator<Person> lastNameComparator = (p1, p2) -> {
        int res = p1.getLastName().compareTo(p2.getLastName());
        return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
    };
    private static Comparator<Person> ageComparator = (p1, p2) -> {
        int res = Integer.compare(p1.getAge(), p2.getAge());
        return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
    };

    public CitizensImpl() {
        idList = new ArrayList<>();
        lastNameList = new ArrayList<>();
        ageList = new ArrayList<>();
    }

    public CitizensImpl(List<Person> citizens) {
        this();
        citizens.forEach(p -> add(p));
    }

    // Complexity = O(log n)
    @Override
    public boolean add(Person person) {
        if (person == null) {
            return false;
        }
        // Так быстрее!!! чем contains
        int index = Collections.binarySearch(idList, person);
        if (index >= 0) {
            return false;
        }

        index = -index - 1;
        idList.add(index, person);
        index = Collections.binarySearch(ageList, person, ageComparator);
        index = index >= 0 ? index : -index - 1;
        ageList.add(index, person);
        index = Collections.binarySearch(lastNameList, person, lastNameComparator);
        index = index >= 0 ? index : -index - 1;
        lastNameList.add(index, person);

        return true;
    }

    // Complexity = O(log n)
    @Override
    public boolean remove(int id) {
        Person person = find(id);
        if (person != null) {
            int index = Collections.binarySearch(idList, person);
            idList.remove(index);
            index = Collections.binarySearch(ageList, person, ageComparator);
            ageList.remove(index);
            index = Collections.binarySearch(lastNameList, person, lastNameComparator);
            lastNameList.remove(index);
            return true;
        }

        return false;
    }

    // Complexity = O(log n)
    @Override
    public Person find(int id) {
        Person pattern = new Person(id, null, null, null);
        int index = Collections.binarySearch(idList, pattern);
        return index >= 0 ? idList.get(index) : null;
    }

    // Complexity = O(log n)
    @Override
    public Iterable<Person> find(int minAge, int maxAge) {
        LocalDate now = LocalDate.now();
        Person patternFrom = new Person(Integer.MIN_VALUE, null, null, now.minusYears(minAge));
        int indexFrom = -Collections.binarySearch(ageList, patternFrom, ageComparator) - 1;
        Person patternTo = new Person(Integer.MAX_VALUE, null, null, now.minusYears(maxAge));
        int indexTo = -Collections.binarySearch(ageList, patternTo, ageComparator) - 1;
        return ageList.subList(indexFrom, indexTo);
    }

    // Complexity = O(log n)
    @Override
    public Iterable<Person> find(String lastName) {
        Person pattern = new Person(Integer.MIN_VALUE, null, lastName, LocalDate.MIN);
        int startIndex = -Collections.binarySearch(lastNameList, pattern, lastNameComparator) - 1;
        pattern = new Person(Integer.MAX_VALUE, null, lastName, LocalDate.MAX);
        int endIndex = -Collections.binarySearch(lastNameList, pattern, lastNameComparator) - 1;
        return lastNameList.subList(startIndex, endIndex);
    }

    // Complexity = O(1)
    @Override
    public Iterable<Person> getAllPersonsSortedById() {
        return idList;
    }

    // Complexity = O(1)
    @Override
    public Iterable<Person> getAllPersonsSortedByAge() {
        return ageList;
    }

    // Complexity = O(1)
    @Override
    public Iterable<Person> getAllPersonsSortedByLastName() {
        return lastNameList;
    }

    // Complexity = O(1)
    @Override
    public int size() {
        return idList.size();
    }
}
