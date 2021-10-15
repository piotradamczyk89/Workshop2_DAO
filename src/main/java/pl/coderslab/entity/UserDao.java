package pl.coderslab.entity;

import BCrypt.*;
import pl.coderslab.DbUtil;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class UserDao {

    private final String insert = "INSERT INTO Workshop2.users (email, username, u_password) VALUES (?, ?, ?)";
    private final String update = "UPDATE users SET email = ?, username = ?, u_password=? WHERE id = ?";
    private final String selectId = "Select * FROM users Where id=?";
    private final String delete = "DELETE FROM users WHERE id = ?";
    private final String select = "Select * FROM users";

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            statement.setString(2, user.getUserName());
            statement.setString(1, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int userId) {
        try (Connection conn = DbUtil.getConnection()) {


            PreparedStatement statement = conn.prepareStatement(selectId);


            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            String userName = "";
            String email = "";
            String password = "";
            int id = 0;
            if (resultSet.next()) {
                userName = resultSet.getString("username");
                email = resultSet.getString("email");
                password = resultSet.getString("u_password");
                id = resultSet.getInt("id");

            }
            if (userId == id) {
                User user = new User(userName, email, password);
                user.setId(id);
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(User user) {
        try (Connection connect = DbUtil.getConnection()) {

            PreparedStatement preparedStatement = connect.prepareStatement(update);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getUserName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setLong(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void delete(int userId) {
        try (Connection connect = DbUtil.getConnection()) {
            PreparedStatement preparedStatement=connect.prepareStatement(select);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean checkIfIdExist=false;
            while (resultSet.next()) {
                if(resultSet.getInt("id")==userId) {
                    checkIfIdExist = true;
                }
                }
            if (checkIfIdExist) {
                PreparedStatement preparedStatement1 = connect.prepareStatement(delete);
                preparedStatement1.setLong(1, userId);
                preparedStatement1.executeUpdate();
                System.out.println("Rekord został poprawnie usuniety");
            } else {
                System.out.println("nie ma takeigo rekordu");
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    private User[] addToArray(User[] user, User user1) {
        user = Arrays.copyOf(user, user.length + 1);
        user[user.length - 1] = user1;
        return user;
    }

    public User[] findAll() {
        try (Connection connect = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connect.prepareStatement(select);

            ResultSet resultSet = preparedStatement.executeQuery();
            User[] users = new User[0];
            while (resultSet.next()) {
                User user = new User(resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("u_password"));
                user.setId(resultSet.getInt("id"));
                users = addToArray(users, user);
            }
            return users;
        } catch (SQLException error) {
            error.printStackTrace();
            return null;
        }
    }

}
