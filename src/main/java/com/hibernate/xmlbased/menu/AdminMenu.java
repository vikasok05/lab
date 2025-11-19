package com.hibernate.xmlbased.menu;

import java.util.List;
import java.util.Scanner;
import java.util.Set;
import com.hibernate.xmlbased.model.Department;
import com.hibernate.xmlbased.model.Developer;
import com.hibernate.xmlbased.model.User;

public class AdminMenu extends Menu {
    private final Scanner in = new Scanner(System.in);

    public void menu(com.hibernate.xmlbased.model.User authUser) {
        super.menu(authUser);

        while (true) {
            String options = null;
            System.out.println("0. Выход");
            System.out.println("1. Посмотреть все департаменты и их сотрудников");
            System.out.println("2. Найти департамент по ID (ЗАДАНИЕ)");
            System.out.println("3. Добавить департамент (ЗАДАНИЕ)");
            System.out.println("4. Перевести сотрудника в новый департамент (ЗАДАНИЕ)");
            System.out.println("5. Удалить департмент (Внимание! Удаляет также связанных сотрудников) (ЗАДАНИЕ)");
            System.out.println("6. Найти сотрудника по ID");
            System.out.println("7. Добавить сотрудника");
            System.out.println("8. Удалить сотрудника");
            System.out.println("9. Получить список юзеров");
            System.out.println("10. Найти юзера по никнейму (ЗАДАНИЕ)");
            System.out.println("11. Добавить аккаунт сотруднику");
            System.out.println("12. Удалить юзера");

            try {
                System.out.print("Ввод: ");
                options = in.nextLine();
                switch (options) {
                    case "0": return;
                    case "1": seeAllWorkers(); break;
                    case "2": findDepartmentByID(); break;
                    case "3": addDepartament(); break;
                    case "4": relocateDeveloper(); break;
                    case "5": deleteDepartment(); break;
                    case "6": findDeveloperByID(); break;
                    case "7": addDeveloper(); break;
                    case "8": deleteDeveloper(); break;
                    case "9": getUsers(); break;
                    case "10": getUserByUsername(); break;
                    case "11": addUser(); break;
                    case "12": deleteUser(); break;
                    default: System.out.println("\nНеверный ввод.");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void seeAllWorkers() {
        Set<Department> departments = departmentDAO.getDepartmentWithWorkers();

        System.out.println("\n=== Список департаментов и сотрудников ===");
        for (Department department : departments) {
            System.out.printf("\n📂 %s (%s)\n",
                    department.getDepartmentName(), department.getDepartmentId());
            System.out.printf("   Местоположение: %s\n", department.getLocation());
            System.out.println("   └── Сотрудники:");

            List<Developer> devs = department.getDevelopers();
            if (devs == null || devs.isEmpty()) {
                System.out.println("       (нет сотрудников)");
            } else {
                for (Developer dev : devs) {
                    System.out.printf("       👤 %-10s | %-15s | Опыт: %d лет\n",
                            dev.getName(), dev.getSpecialty(), dev.getExperience());
                }
            }
        }
        System.out.println("==========================================\n");
    }


    public void findDepartmentByID() {
        System.out.print("Введите ID департамента: ");
        String depID = in.nextLine();
        System.out.println(departmentDAO.findDepartmentByID(depID));
    }

    public void addDepartament() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Введите ID департамента: ");
        String depID = scan.nextLine();
        if (depID.length() != 3) throw new IllegalArgumentException("ID департамента должен быть длиной в три символа.");
        System.out.print("Введите название департамента: ");
        String departmentName = scan.nextLine();
        System.out.print("Введите адрес департамента: ");
        String location = scan.nextLine();
        Department dep = new Department(depID, departmentName, location);
        if (departmentDAO.addDepartament(dep) == dep) {
            System.out.println("Департамент успешно добавлен.");
        } else {
            System.out.println("Возникла ошибка добавления.");
        }
    }

    public void relocateDeveloper() {
        System.out.print("Введите ID сотрудника: ");
        Integer devID = Integer.valueOf(in.nextLine());
        Developer dev = developerDAO.getDeveloperById(devID);
        if (dev == null) throw new NullPointerException("Сотрудник не найден");
        System.out.print("Введите ID департамента: ");
        String depID = in.nextLine();
        Department department = departmentDAO.findDepartmentByID(depID);
        developerDAO.updateDevelopersDepartment(devID, department);
        System.out.println("Сотрудник переведен в новый департамент");
    }

    private void deleteDepartment() {
        System.out.print("Введите ID департамента: ");
        String depID = in.nextLine();
        departmentDAO.deleteDepartment(depID);
        System.out.println("Департамент удален.");
    }

    private void findDeveloperByID() {
        System.out.print("Введите ID работника: ");
        Integer devID = Integer.valueOf(in.nextLine());
        Developer developer = developerDAO.getDeveloperById(devID);
        if (developer == null) throw new NullPointerException("Сотрудник не найден");
        System.out.println(developer);
    }

    private void addDeveloper() {
        System.out.print("Введите имя работника: ");
        String name = in.nextLine();
        System.out.print("Введите специализацию работника: ");
        String specialty = in.nextLine();
        System.out.print("Введите опыт работника (в годах): ");
        double exp;
        try {
            exp = Double.parseDouble(in.nextLine().replace(',', '.'));
        } catch (NumberFormatException e) {
            System.out.println("❌ Ошибка: опыт должен быть числом!");
            return;
        }
        int experience = (int) exp;
        System.out.print("Введите ID департамента работника: ");
        String depID = in.nextLine();
        Department department = departmentDAO.findDepartmentByID(depID);
        Developer dev = new Developer(name, specialty, experience, department);
        developerDAO.addDeveloper(dev);
        System.out.println("Сотрудник успешно добавлен");
    }

    private void deleteDeveloper() {
        System.out.print("Введите ID сотрудника: ");
        Integer devID = Integer.valueOf(in.nextLine());
        Developer dev = developerDAO.getDeveloperById(devID);
        if (dev == null) throw new NullPointerException("Сотрудник не найден");
        developerDAO.removeDeveloper(devID);
        System.out.println("Разработчик успешно удален");
    }

    private void getUsers() {
        List<User> users = userDAO.getUsers();
        for (User user : users) System.out.println(user);
    }

    private void getUserByUsername() {
        System.out.print("Введите username: ");
        String username = in.nextLine();
        System.out.println(userDAO.getUserByUsername(username));
    }

    private void addUser() {
        seeAllWorkers();
        System.out.print("Введите ID сотрудника, для которого мы добавляем аккаунт: ");
        Integer devID = Integer.valueOf(in.nextLine());
        if (developerDAO.getDeveloperById(devID) == null) throw new NullPointerException("Сотрудник не найден");
        System.out.print("Введите username: ");
        String username = in.nextLine();
        System.out.print("Введите password: ");
        String password = in.nextLine();
        System.out.println("Юзер " + userDAO.addUser(new User(devID, username, password)) + " успешно добавлен");
    }

    private void deleteUser() {
        System.out.print("Введите ID аккаунта: ");
        Integer userID = Integer.valueOf(in.nextLine());
        if (userDAO.getUserByID(userID) == null) throw new NullPointerException("Юзер не найден");
        developerDAO.removeDeveloper(userID);
        System.out.println("Разработчик успешно удален");
    }
}
