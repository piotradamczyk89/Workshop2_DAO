package pl.coderslab;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

public class MainTest {
    public static void main(String[] args) {
       // User user = new User("Jan", "Kowaliski_3@gmail.com", "dupa");
        UserDao userDao = new UserDao();
      //  user=userDao.create(user);
       // System.out.println(user);
//        User user1=userDao.read();
//        user1.setEmail("michal.dyrkacz@gmail.com");
//        userDao.update(user1);



        userDao.delete(1);

        User łukasz_kaczmarek = new User("Łukasz Kaczmarek", "lukaszkacz@gmail.com", "254488arka");
        userDao.create(łukasz_kaczmarek);
        User[] users = new User[0];
        users = userDao.findAll();
        for (User user1 : users) {
            System.out.println(user1);
        }

    }
}
