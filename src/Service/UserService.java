package Service;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import DAO.UniqueConstraintException;
import Entity.User;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface UserService {
  User create(User user)
      throws SQLException, ApplicationErrorException, UniqueConstraintException;

  int count() throws ApplicationErrorException;

  List<User> list(HashMap<String, String> listattributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException;

  int edit(User user)
      throws SQLException, ApplicationErrorException, UniqueConstraintException;

  int delete(String username) throws ApplicationErrorException;
}
