package info.mnafshin.springuniverse.di.plain;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserRepository implements UserRepository {

    private final List<String> users = new ArrayList<>();

    @Override
    public void save(String name) {
        users.add(name);
    }

    @Override
    public List<String> findAll() {
        return List.copyOf(users);
    }
}
