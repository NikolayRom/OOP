package repository;
import java.util.HashMap;
import java.util.Optional;

public interface Repository<T> {
    void clear();
    Optional<T> deleteById(int id);
    Optional<T> findById(int id);
    Optional<HashMap<Integer, T>> getAll();
    void save(T entity);
}
