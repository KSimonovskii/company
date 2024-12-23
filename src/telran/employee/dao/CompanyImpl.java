package telran.employee.dao;

import telran.employee.model.Employee;
import telran.employee.model.SalesManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class CompanyImpl implements Company {

    private Set<Employee> employees;
    private int capacity;


    public CompanyImpl(int capacity) {
        this.capacity = capacity;
        employees = new HashSet<>();
    }

    //O(1)
    @Override
    public boolean addEmployee(Employee employee) {
        if (employee == null
                || capacity == employees.size()) {
            return false;
        }

        return employees.add(employee);
    }

    //O(n)
    @Override
    public Employee removeEmployee(int id) {

        Employee victim = findEmployee(id);
        employees.remove(victim);
        return victim;
    }

    //O(n)
    @Override
    public Employee findEmployee(int id) {

        return employees.stream()
                .filter(employee -> employee.getId() == id)
                .findFirst()
                .orElse(null);
    }

    //O(1)
    @Override
    public int quantity() {
        return employees.size();
    }

    //O(n)
    @Override
    public double totalSalary() {
        return employees.stream()
                .mapToDouble(Employee::calcSalary)
                .sum();
    }

    @Override
    public double totalSales() {
        return employees.stream()
                .filter(employee -> employee instanceof SalesManager)
                .map(employee -> (SalesManager) employee)
                .mapToDouble(SalesManager::getSalesValue)
                .sum();
    }

    @Override
    public void printEmployees() {
        System.out.println("==== Company in " + COUNTRY + " ====");
        employees.forEach(System.out::println);
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
        return employees.stream()
                .filter(employee -> predicate.test(employee))
                .toArray(Employee[]::new);
    }
}
