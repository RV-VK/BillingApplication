package Service;

import DAO.ApplicationErrorException;
import DAO.UniqueConstraintException;
import DAO.UserDAO;
import DAO.UserDAOImplementation;
import Entity.User;

import java.sql.SQLException;

public class LoginServiceImplementation implements  LoginService{
    private UserDAO userDAO=new UserDAOImplementation();

    /**
     * This method invokes the DAO of the Product entity and serves the Initial setup function.
     *
     * @return status - Boolean.
     * @throws SQLException Exception thrown based on SQL syntax.
     */
    @Override
    public boolean checkIfInitialSetup() throws SQLException {
        return userDAO.checkIfInitialSetup();
    }

    /**
     * This method invokes the DAO of the User entity and serves the create user function.
     *
     * @param user User to be created.
     * @return User - Created user.
     * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table
     * @throws SQLException Exception thrown based on SQL syntax.
     * @throws ApplicationErrorException Exception thrown due to Persistence problems.
     */
    @Override
    public User createUser(User user) throws UniqueConstraintException, SQLException, ApplicationErrorException {
        UserService userService=new UserServiceImplementation();
        return userService.create(user);
    }


    /**
     * This method invokes the DAO of the User entity and serves the Login function.
     *
     * @param userName Username to be Logged in as.
     * @param passWord Corresponding password.
     * @return usertype - String.
     * @throws SQLException Exception thrown based on SQL syntax.
     * @throws ApplicationErrorException Exception thrown due to Persistence problems.
     */
  @Override
  public String login(String userName, String passWord)
      throws SQLException, ApplicationErrorException {
        return userDAO.login(userName,passWord);
    }


}
