package core;

public interface DatabaseCRUD {

    /**
     * Persisted object to database
     * @param object    object to persist
     * @return id of persisted object
     */
    int create(Object object);

    /**
     * Retrieves persisted object from database
     * @param id    of object to retrieve
     * @return object retrieved
     */
    Object retrieve(int id);

    /**
     * Updates persisted object based on new object data
     * @param object to be persisted
     */
    void update(Object object);

    /**
     * Deletes persisted object from database
     * @param id of object to delete
     */
    void delete(int id);

}
