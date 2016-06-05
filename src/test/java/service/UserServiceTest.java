package service;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import entities.Address;
import entities.Permission;
import entities.Person;
import entities.Role;
import entities.User;

public class UserServiceTest {
	
	@Test
    public void test() {
        ArrayList<User> users = new ArrayList<User>();
        List<Address> addresses1 = new ArrayList<Address>();
        List<Address> addresses2 = new ArrayList<Address>();
        List<Permission> permissions1 = new ArrayList<Permission>();
        
        Address address1 = new Address().setStreetName("stree1");
        Address address2 = new Address().setStreetName("stree2");
        Address address3 = new Address().setStreetName("stree3");
        addresses1.add(address1);
        addresses1.add(address2);
        addresses2.add(address3);
        
        Permission permission1 = new Permission().setName("permission1");
        Permission permission2 = new Permission().setName("permission2");
        permissions1.add(permission1);
        permissions1.add(permission2);
        
        Role role1 = new Role().setName("role1").setPermissions(permissions1);
        Role role2 = new Role().setName("role2").setPermissions(permissions1);
        
        Person person1 = new Person().setAge(16).setAddresses(addresses1).setName("Zenon").setSurname("Kowalski").setRole(role1);
        Person person2 = new Person().setAge(24).setAddresses(addresses2).setName("Ryszard").setSurname("Nowak").setRole(role1);
        Person person3 = new Person().setAge(67).setAddresses(addresses2).setName("Ignacy").setSurname("Bezpracy").setRole(role2);
        
        User user1 = new User().setName("user1").setPersonDetails(person1);
        User user2 = new User().setName("user2").setPersonDetails(person2);
        User user3 = new User().setName("user3").setPersonDetails(person3);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        
        UserService userService = new UserService(users);
        
        
        // Test 1 - find users with addresses count more than...
        int numberOfAddresses = 1, result = 0;
        List<User> usersWithAddressesCountMoreThan = userService.findUsersWithAddressesCountMoreThan(numberOfAddresses);
        for (User u : users) {
            int count = u.getPersonDetails().getAddresses().size();
            if (count > numberOfAddresses) result++;
        }
        assertEquals(usersWithAddressesCountMoreThan.size(), result);
        
        
        // Test 2 - find oldest person
        Person oldestPerson1 = userService.findOldestPerson();
        Person oldestPerson2 = new Person().setAge(0);
        for (User u : users) {
        	if (u.getPersonDetails().getAge() > oldestPerson2.getAge()) oldestPerson2 = u.getPersonDetails();
        }
        assertEquals(oldestPerson1, oldestPerson2);
        
        // Test 3 - find user with longest username
        User longestUsername1 = userService.findUserWithLongestUsername();
        User longestUsername2 = new User().setName("");
        for (User u : users) {
        	if (u.getName().length() > longestUsername2.getName().length()) longestUsername2 = u;
        }
        assertEquals(longestUsername1, longestUsername2);
	
        
        // Test 4 - get names and surnames comma-separated of all users above 18
        String adultUsers1 = userService.getNamesAndSurnamesCommaSeparatedOfAllUsersAbove18();
        StringBuilder adultUsers2 = new StringBuilder();
        int i = 0;
        for (User u : users) {
        	if (u.getPersonDetails().getAge() >= 18) adultUsers2.append(u.getPersonDetails().getName() + " " + u.getPersonDetails().getSurname());
        	if (i < users.size()-1 && adultUsers2.length() > 0) adultUsers2.append(", ");
        	i++;
        }
        assertEquals(adultUsers1, adultUsers2.toString());
        
        
        // Test 5 - get sorted permissions of users with name starting with...
        List<String> sortedPermissions1 = userService.getSortedPermissionsOfUsersWithNameStartingWith("Zen");
        List<String> sortedPermissions2 = Arrays.asList("permission1", "permission2");
        assertEquals(sortedPermissions1, sortedPermissions2);
        
        
        // Test 6 - get users average age
        double averageAge1 = userService.getUsersAverageAge();
        double averageAge2 = 0.0;
        for (User u : users) {
        	averageAge2 += u.getPersonDetails().getAge();
        }
        assertEquals(averageAge1, (averageAge2 / users.size()), 0);
        
        
        // Test 7 - print capitalized permission names of users with surname ending with
        System.out.println("Test 7\nExpected:\nPERMISSION1\nPERMISSION2\nResult:");
        userService.printCapitalizedPermissionNamesOfUsersWithSurnameStartingWith("ski");
        
        
        // Test 8 - group users by role
        System.out.print("\nTest 8");
        Map<Role, List<User>> usersByRole = userService.groupUsersByRole();
        for (Map.Entry<Role, List<User>> entry : usersByRole.entrySet()) {
            System.out.print("\nRole: " + entry.getKey().getName() + "\nUsers: ");
            for (User u : entry.getValue()) System.out.print(u.getName() + " ");
        }
        
        
        // Test 9 - partition user by under and over 18
        System.out.print("\n\nTest 9");
        Map<Boolean, List<User>> usersByAge = userService.partitionUserByUnderAndOver18();
        for (Map.Entry<Boolean, List<User>> entry : usersByAge.entrySet()) {
            System.out.print("\nAbove 18?: " + entry.getKey() + "\nUsers: ");
            for (User u : entry.getValue()) System.out.print(u.getName() + " ");
        }
	}
	
}
