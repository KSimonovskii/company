package telran.employee.dao;

import telran.employee.model.Employee;
import telran.employee.model.SalesManager;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

public class CompanyImpl implements Company {

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private Lock rLock = rwLock.readLock();
    private Lock wLock = rwLock.writeLock();

    private Set<Employee> employees;
    private int capacity;

    public CompanyImpl(int capacity) {
        this.capacity = capacity;
        employees = new HashSet<>();
    }

    //O(1)
    @Override
    public boolean addEmployee(Employee employee) {
        wLock.lock();
        try {
            if (employee == null
                    || capacity == employees.size()) {
                return false;
            }
            return employees.add(employee);
        } finally {
            wLock.unlock();
        }
    }

    //O(n)
    @Override
    public Employee removeEmployee(int id) {
        wLock.lock();
        try {
            Employee victim = findEmployee(id);
            employees.remove(victim);
            return victim;
        } finally {
            wLock.unlock();
        }
    }

    //O(n)
    @Override
    public Employee findEmployee(int id) {

        rLock.lock();
        try {
            return employees.stream()
                    .filter(employee -> employee.getId() == id)
                    .findFirst()
                    .orElse(null);
        } finally {
            rLock.unlock();
        }
    }

    //O(1)
    @Override
    public int quantity() {
        rLock.lock();
        try {
            return employees.size();
        } finally {
            rLock.unlock();
        }
    }

    //O(n)
    @Override
    public double totalSalary() {

        rLock.lock();
        try {
            return employees.stream()
                    .mapToDouble(Employee::calcSalary)
                    .sum();
        } finally {
            rLock.unlock();
        }
    }

    @Override
    public double totalSales() {

        rLock.lock();
        try {
            return employees.stream()
                    .filter(employee -> employee instanceof SalesManager)
                    .map(employee -> (SalesManager) employee)
                    .mapToDouble(SalesManager::getSalesValue)
                    .sum();
        } finally {
            rLock.unlock();
        }

    }

    @Override
    public void printEmployees() {
        System.out.println("==== Company in " + COUNTRY + " ====");
        rLock.lock();
        try {
            employees.forEach(System.out::println);
        } finally {
            rLock.unlock();
        }
    }

    @Override
    public Employee[] findEmployeesHoursGreaterThan(int hours) {
        return findEmployeesPredicate(employee -> employee.getHours() > hours);
    }

    @Override
    public Employee[] findEmployeesSalaryBetween(int minSalary, int maxSalary) {
        return findEmployeesPredicate(e -> e.calcSalary() >= minSalary && e.calcSalary() < maxSalary);
    }

    private Employee[] findEmployeesPredicate(Predicate<Employee> predicate){
        rLock.lock();
        try {
            return employees.stream()
                    .filter(employee -> predicate.test(employee))
                    .toArray(Employee[]::new);
        } finally {
            rLock.unlock();
        }

    }
}
