package ait.citizens.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Person implements Comparable<Person> {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    public Person(int id, String firstName, String lastName, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Person person = (Person) object;

        return id == person.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(Person o) {
        return Integer.compare(id, o.id);
    }

    @Override
    public String toString() {
        return "Person " +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate + '\'' +
                ", age=" + getAge() +
                ' ';
    }
}
