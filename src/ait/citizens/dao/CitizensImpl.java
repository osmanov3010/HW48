package ait.citizens.dao;

import ait.citizens.model.Person;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    private static Comparator<Person> idComparator = (p1, p2) -> {
        return Integer.compare(p1.getId(), p2.getId());
    };

    public CitizensImpl() {
        idList = new ArrayList<>();
        lastNameList = new ArrayList<>();
        ageList = new ArrayList<>();
    }

    public CitizensImpl(List<Person> citizens) {
        this();
        for (Person person : citizens) {
            add(person);
        }

    }

    @Override
    public boolean add(Person person) {
        if (person == null || idList.contains(person)) {
            return false;
        }

        addToNeededList(idList, idComparator, person);
        addToNeededList(lastNameList, lastNameComparator, person);
        addToNeededList(ageList, ageComparator, person);

        return true;
    }

    private void addToNeededList(List<Person> list, Comparator<Person> comparator, Person person) {

        int index = -Collections.binarySearch(list, person, comparator) - 1;
        if (index == list.size()) {
            list.add(person);
        } else {
            list.add(index, person);
        }
    }

    @Override
    public boolean remove(int id) {
        Person person = find(id);

        if (person != null) {
            idList.remove(person);
            lastNameList.remove(person);
            ageList.remove(person);
            return true;
        }

        return false;
    }

    @Override
    public Person find(int id) {
        Person pattern = new Person(id, null, null, null);
        int index = Collections.binarySearch(idList, pattern);
        return index >= 0 ? idList.get(index) : null;
    }

    @Override
    public Iterable<Person> find(int minAge, int maxAge) {
        Person patternFrom = new Person(-1, null, null, LocalDate.now().minusYears(minAge));
        int indexFrom = -Collections.binarySearch(ageList, patternFrom, ageComparator) - 1;
        Person patternTo = new Person(-1, null, null, LocalDate.now().minusYears(maxAge));
        int indexTo = -Collections.binarySearch(ageList, patternTo, ageComparator) - 1;

        return ageList.subList(indexFrom, indexTo);
    }

    @Override
    public Iterable<Person> find(String lastName) {
        List<Person> result = new ArrayList<>();

        Person pattern = new Person(-1, null, lastName, LocalDate.MIN);
        int startIndex = -Collections.binarySearch(lastNameList, pattern, lastNameComparator) - 1;
        pattern = new Person(Integer.MAX_VALUE, null, lastName, LocalDate.MIN);
        int endIndex = -Collections.binarySearch(lastNameList, pattern, lastNameComparator) - 1;
        if (startIndex == endIndex) {
            result.add(lastNameList.get(startIndex));
        } else {
            result.addAll(lastNameList.subList(startIndex, endIndex));
        }

        return result;
    }

    @Override
    public Iterable<Person> getAllPersonsSortedById() {
        return idList;
    }

    @Override
    public Iterable<Person> getAllPersonsSortedByAge() {
        return ageList;
    }

    @Override
    public Iterable<Person> getAllPersonsSortedByLastName() {
        return lastNameList;
    }

    @Override
    public int size() {
        return idList.size();
    }
}
