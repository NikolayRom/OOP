package repository;

import java.util.Optional;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import model.Request;

public class RequestsRepository {
    private static RequestsRepository instance;
    private Queue<Request> storage = new LinkedList<>();
    private RequestsRepository() {
        this.storage = new LinkedList<>();
    }
    public static RequestsRepository getInstance() {
        if(instance == null) instance = new RequestsRepository();
        return instance;
    }

    public void push(Request request) {
        storage.add(request);
    }

    public Optional<Request> pop() {
        if(storage.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(storage.poll());
    }

    public Optional<Request> peek() {
        if(storage.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(storage.peek());
    }

    public List<Request> getAll() {
        return new ArrayList<>(storage);
    }
}