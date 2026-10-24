package info.mnafshin.springuniverse.di.plain;

import java.util.List;

public interface UserRepository {
    void save(String name);

    List<String> findAll();
}
