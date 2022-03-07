package eth.project.iss.bottled.repositories;

import static eth.project.iss.bottled.repositories.SQLQueries.CREATE_USER_QUERY;
import static eth.project.iss.bottled.repositories.SQLQueries.GET_USER_QUERY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    // Logger logger = LoggerFactory.getLogger("UserRepoLogger");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean createUser(String publicAddress) {
        Integer rowsUpdated = jdbcTemplate.update(CREATE_USER_QUERY, publicAddress);
        if (rowsUpdated == 1) {
            return true;
        }
        return false;
    }

    public boolean findUser(String publicAddress) {
        String walletAddress = "%" + publicAddress + "%";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_USER_QUERY, walletAddress);
        if (rs.next()) {
            return true;
        }
        return false;
    }
}