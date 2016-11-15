package eu.falcon.semantic.api.repository;


import java.util.List;
import java.util.Optional;
;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @param <U> User object
 * @param <ID>
 */


@Service
public interface ISemanticService<U, ID> {

    public int MIN_CHARS_FOR_SPECIAL_FIELDS = 6;

    /**
     * Fetch a user from database given a username.
     *
     * @param username Find a user based on a username
     * @return An instance of User object wrapped in an Optional object
     */
    public Optional<U> findByUsername(String username);

    /**
     * Fetch a user from database given an id.
     *
     * @param id The id of the user to fetch
     * @return An instance of User object wrapped in an Optional object
     * @throws UserDoesNotExistException User does not exist exception
     */
    public Optional<U> findOne(ID id);

    /**
     * Delete a user from database.
     *
     * @param id The id of the user to be deleted
     * @throws UserDoesNotExistException User does not exist exception
     */
    public void delete(ID id);

    /**
     * Fetch all users from database.
     *
     * @return A list of User objects
     */
    public List<U> findAll();

    public Page<U> findAll(Pageable page);

    /**
     * Creates a new user to the database.
     *
     * @param u A User object

     */
    public void create(U u) ;

    /**
     * Update details of a user
     *
     * @param u User object

     * exception
     */
    public void edit(U u)  ;

    /**
     * Update an already existing user
     *
     * @param u
     */
    public void update(U u);
}
