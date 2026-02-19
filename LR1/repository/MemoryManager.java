package repository;

import model.Identifiable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class MemoryManager<T extends Identifiable> implements Repository<T> {
    protected List<T> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }
    @Override
    public boolean isEmpty() {
        return storage.isEmpty();
    }
    @Override
    public void deleteById(int id) {
        storage.removeIf(ent -> ent.getId() == id);
    }
    @Override
    public Optional<T> findById(int id) {
        return storage.stream().filter(ent -> ent.getId() == id).findFirst();
    }
    @Override
    public List<T> getAll() {
        return new ArrayList<>(storage);
    }
    @Override
    public void save(T entity) {
        storage.add(entity);
    }
}
