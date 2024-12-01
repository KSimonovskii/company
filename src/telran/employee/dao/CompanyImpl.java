package telran.employee.dao;

import telran.employee.model.Employee;
import telran.employee.model.SalesManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CompanyImpl implements Company {

    private List<Employee> employees;
    private int capacity;


    public CompanyImpl(int capacity) {
        this.capacity = capacity;
        employees = new ArrayList<>();
    }

    @Override
    public boolean addEmployee(Employee employee) {
        if (employee == null
                || capacity == employees.size()
                || findEmployee(employee.getId()) != null) {
            return false;
        }

        return employees.add(employee);
    }

    @Override
    public Employee removeEmployee(int id) {

        Employee victim = findEmployee(id);
        employees.remove(victim);
        return victim;
    }

    @Override
    public Employee findEmployee(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    @Override
    public int quantity() {
        return employees.size();
    }

    @Override
    public double totalSalary() {
        double sum = 0;
        for (Employee employee : employees) {
            sum += employee.calcSalary();
        }
        return sum;
    }

    @Override
    public double totalSales() {
        double res = 0;
        for (Employee employee : employees) {
            if (employee instanceof SalesManager manager) {
                res += manager.getSalesValue();
            }
        }
        return res;
    }

    @Override
    public void printEmployees() {
        System.out.println("==== Company in " + COUNTRY + " ====");
        for (Employee employee : employees) {
            System.out.println(employee);
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
        List<Employee> res = new ArrayList<>();
        for (Employee employee: employees
             ) {
            if (predicate.test(employee)){
                res.add(employee);
            }

        }

        return res.toArray(new Employee[0]);
    }
}
