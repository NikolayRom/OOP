package repository;

import model.Identifiable;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

public abstract class MemoryManager<T extends Identifiable> implements Repository<T> {
    protected HashMap<Integer, T> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }
  
    @Override
    public Optional<T> deleteById(int id) {
        if(!storage.containsKey(id) || storage.get(id) == null) {
            return Optional.empty();
        }
        return Optional.of(storage.remove(id));
    }
    @Override
    public Optional<T> findById(int id) {
        if(!storage.containsKey(id) || storage.get(id) == null) {
            return Optional.empty();
        }
        return Optional.of(storage.get(id));
    }
    @Override
    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }
    @Override
    public void save(T entity) {
        storage.put(entity.getId(), entity);
    }
}