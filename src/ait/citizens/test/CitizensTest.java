package ait.citizens.test;

import ait.citizens.dao.Citizens;
import ait.citizens.dao.CitizensImpl;
import ait.citizens.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CitizensTest {

    Citizens citizens;
    Person[] persons;
    Person personForTest = new Person(12, "Mary", "MacDonald", LocalDate.of(1990, 11, 28));

    static Comparator<Person> ageComparator = (p1, p2) -> {
        int res = Integer.compare(p1.getAge(), p2.getAge());
        return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
    };

    static Comparator<Person> lastNameComparator = (p1, p2) -> {
        int res = p1.getLastName().compareTo(p2.getLastName());
        return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
    };


    @BeforeEach
    void setUp() {
        citizens = new CitizensImpl();

        persons = new Person[]{
                new Person(1, "Ivan", "MacDonald", LocalDate.of(1992, 8, 19)),
                new Person(2, "James", "Johnson", LocalDate.of(1993, 1, 8)),
                new Person(3, "Robert", "Tremblay", LocalDate.of(1991, 2, 1)),
                new Person(4, "John", "Martin", LocalDate.of(1989, 4, 7)),
                new Person(5, "Michael", "Smith", LocalDate.of(1978, 5, 6)),
                new Person(6, "David", "Brown", LocalDate.of(1966, 6, 1)),
                new Person(7, "William", "Olegov", LocalDate.of(2003, 7, 5)),
                new Person(8, "Richard", "Wilson", LocalDate.of(2010, 9, 12)),
                new Person(9, "Joseph", "Johnson", LocalDate.of(1992, 12, 22)),
                new Person(10, "Thomas", "MacDonald", LocalDate.of(1990, 11, 28)),
                new Person(11, "Christopher", "Johnson", LocalDate.of(1981, 10, 26)),
        };

        for (int i = 0; i < persons.length; i++) {
            citizens.add(persons[i]);
        }
    }


    @Test
    void testConstructor() {

        List<Person> personList = new ArrayList<>();
        List<Person> expected = new ArrayList<>();


        //Adding all persons
        for (int i = 0; i < persons.length; i++) {
            personList.add(persons[i]);
        }

        //Saving expected list
        personList.forEach(e -> expected.add(e));

        //Adding duplicate persons to list
        personList.add(persons[0]);
        personList.add(persons[1]);
        personList.add(persons[2]);

        citizens = new CitizensImpl(personList);

        List<Person> actual = (List<Person>) citizens.getAllPersonsSortedById();

        assertIterableEquals(expected, actual);

    }


    @Test
    void add() {
        assertFalse(citizens.add(null));
        assertFalse(citizens.add(persons[0]));

        assertTrue(citizens.add(personForTest));
        assertEquals(12, citizens.size());
        assertEquals(personForTest, citizens.find(12));
    }

    @Test
    void remove() {
        assertFalse(citizens.remove(12));
        assertTrue(citizens.remove(6));
        assertNull(citizens.find(6));
        assertEquals(10, citizens.size());
    }

    @Test
    void findPersonById() {
        assertNull(citizens.find(12));
        citizens.add(personForTest);
        assertEquals(personForTest, citizens.find(12));
        assertEquals(persons[7], citizens.find(8));
    }

    @Test
    void findPersonsByLastName() {
        List<Person> expected = new ArrayList<>();
        List<Person> actual = new ArrayList<>();
        expected.add(persons[0]);
        expected.add(persons[9]);
        expected.sort(lastNameComparator);
        Iterable<Person> res = citizens.find("MacDonald");
        res.forEach(p -> actual.add(p));
        assertIterableEquals(expected, actual);
    }

    @Test
    void findPersonsInRangeOfAge() {
        List<Person> expected = new ArrayList<>();
        List<Person> actual = new ArrayList<>();
        expected.add(persons[6]);
        expected.add(persons[7]);
        expected.sort(ageComparator);

        Iterable<Person> res = citizens.find(10, 25);
        res.forEach(p -> actual.add(p));
        assertIterableEquals(expected, actual);
    }

    @Test
    void getAllPersonsSortedById() {
        Iterable<Person> res = citizens.getAllPersonsSortedById();
        List<Person> actual = new ArrayList<>();
        res.forEach(p -> actual.add(p));
        assertArrayEquals(persons, actual.toArray());
    }

    @Test
    void getAllPersonsSortedByAge() {

        Iterable<Person> res = citizens.getAllPersonsSortedByAge();
        int age = -1;
        for (Person person : res) {
            assertTrue(Integer.compare(person.getAge(), age) >= 0);
            age = person.getAge();
        }
    }

    @Test
    void getAllPersonsSortedByLastName() {
        Iterable<Person> res = citizens.getAllPersonsSortedByLastName();
        String name = "";
        for (Person person : res) {
            assertTrue(person.getLastName().compareTo(name) >= 0);
            name = person.getLastName();
        }
    }

    @Test
    void size() {
        assertEquals(11, citizens.size());
    }
}