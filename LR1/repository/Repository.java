package repository;
import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    void clear();
    void deleteById(int id);
    Optional<T> findById(int id);
    List<T> getAll();
    void save(T entity);
}
