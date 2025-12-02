package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(" Application need a proper username and password");
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter actor last name");
        String actorLastName = scanner.nextLine();

        // connecting with sakila database, put username and password

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        actorLastName(dataSource, actorLastName);
        actorFirstNameAndLastName(username, password);

    }
    // Create a actor last name method

    private static void actorLastName(BasicDataSource dataSource, String actorLastName) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(" SELECT First_name, Last_Name From Actor Where Last_Name Like ? ")) {

            statement.setString(1, actorLastName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Matching you name\n");

                    do {
                        String firstName = resultSet.getString("first_name");
                        String lastNameofActor = resultSet.getString("last_name");
                        System.out.println(firstName + " " + lastNameofActor);

                    } while (resultSet.next());
                } else {
                    System.out.println(" not matching name");

                }
            }
           // actorFirstNameAndLastName(dataSource, );

        } catch (SQLException e) {
            System.out.println("Show me run time error");
            e.printStackTrace();
        }
    }

    private static void actorFirstNameAndLastName( String username, String password) {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        try (Connection connection = basicDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("""
                        SELECT title FROM film f
                        JOIN film_actor fa ON f.film_id = fa.film_id
                        JOIN actor a ON fa.actor_id = a.actor_id
                        WHERE a.first_name = ? AND a.last_name = ?;
                """)) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("\nEnter first name of actor");
            String firstName = scanner.nextLine();
            System.out.println("Enter last name of actor");
            String lastName = scanner.nextLine();

            statement.setString(1, firstName);
            statement.setString(2, lastName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\nactor name hase been match: \n");
                    do {
                        String title = resultSet.getString("title");
                        System.out.println(title);

                    } while (resultSet.next());


                } else {
                    System.out.println("\n film found for the actor");
                }
            }


        } catch (SQLException e) {
            System.out.println("Show me run time error");
            e.printStackTrace();
        }
    }

}
