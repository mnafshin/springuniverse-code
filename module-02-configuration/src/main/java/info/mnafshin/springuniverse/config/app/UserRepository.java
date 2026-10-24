package info.mnafshin.springuniverse.config.app;

import java.util.List;

public interface UserRepository {
    void save(String name);

    List<String> findAll();
}
