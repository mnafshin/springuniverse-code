package info.mnafshin.springuniverse.di.spring;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

@Component
public class InMemoryUserRepository implements UserRepository {

    private final List<String> users = new CopyOnWriteArrayList<>();

    @Override
    public void save(String name) {
        users.add(name);
    }

    @Override
    public List<String> findAll() {
        return List.copyOf(users);
    }
}
