package telran.employee.dao;

import telran.employee.model.Employee;
import telran.employee.model.SalesManager;

import java.util.function.Predicate;

public class CompanyImpl implements Company{

    private Employee[] employees;
    private int size;


    public CompanyImpl(int capacity){
        employees = new Employee[capacity];
    }

    @Override
    public boolean addEmployee(Employee employee) {
        if (employee == null
                || size == employees.length
                || findEmployee(employee.getId()) != null) {
            return false;
        }

        employees[size++] = employee;
        return true;
    }

    @Override
    public Employee removeEmployee(int id) {

        for (int i = 0; i < size; i++) {
            if (employees[i] == null || employees[i].getId() != id) {
                continue;
            }
            Employee victim = employees[i];
            employees[i] = employees[--size];
            employees[size] = null;
            return victim;
        }

        return null;
    }

    @Override
    public Employee findEmployee(int id) {
        for (int i = 0; i < size; i++) {
            if (employees[i] != null && employees[i].getId() == id) {
                return employees[i];
            }
        }
        return null;
    }

    @Override
    public int quantity() {
        return size;
    }

    @Override
    public double totalSalary() {
        double sum = 0;
        for (int i = 0; i < employees.length; i++) {
            if (employees[i] != null) {
                sum += employees[i].calcSalary();
            }
        }
        return sum;
    }

    @Override
    public double totalSales() {
        double res = 0;
        for (int i = 0; i < employees.length; i++) {
            if (employees[i] == null) {
                continue;
            }
            if (employees[i] instanceof SalesManager manager) {
                res += manager.getSalesValue();
            }
        }
        return res;
    }

    @Override
    public void printEmployees() {
        System.out.println("==== Company in " + COUNTRY + " ====");
        for (int i = 0; i < size; i++) {
            System.out.println(employees[i]);
        }
    }

    @Override
    public Employee[] findEmployeesHoursGreaterThan(int hours) {
        Predicate<Employee> predicate = new Predicate<Employee>() {
            @Override
            public boolean test(Employee employee) {
                return employee.getHours() > hours;
            }
        };
        return findEmployeesPredicate(predicate);
    }

    @Override
    public Employee[] findEmployeesSalaryBetween(int minSalary, int maxSalary) {
        Predicate<Employee> predicate = e -> e.calcSalary() >= minSalary && e.calcSalary() < maxSalary;
        return findEmployeesPredicate(predicate);
    }

    private Employee[] findEmployeesPredicate(Predicate<Employee> predicate){
        int count = 0;
        for (int i = 0; i < size; i++){
            if (predicate.test(employees[i])) {
                count++;
            }
        }
        Employee[] resEmployees = new Employee[count];
        for (int i = 0, j = 0; j < resEmployees.length; i++){
            if (predicate.test(employees[i])) {
                resEmployees[j++] = employees[i];
            }
        }
        return resEmployees;


    }
}
