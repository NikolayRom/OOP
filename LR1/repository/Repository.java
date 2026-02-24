package repository;
import java.util.Optional;
import java.util.List;

public interface Repository<T> {
    void clear();
    Optional<T> deleteById(int id);
    Optional<T> findById(int id);
    List<T> getAll();
    void save(T entity);
}
