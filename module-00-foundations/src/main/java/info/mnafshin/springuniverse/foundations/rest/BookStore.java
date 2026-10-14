package info.mnafshin.springuniverse.foundations.rest;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** In-memory store for book resources (no database). */
public final class BookStore {

    private final ConcurrentHashMap<Long, Book> books = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public Collection<Book> list() {
        return books.values();
    }

    public Optional<Book> get(long id) {
        return Optional.ofNullable(books.get(id));
    }

    public Book create(String title) {
        long id = nextId.getAndIncrement();
        Book book = new Book(id, title);
        books.put(id, book);
        return book;
    }

    public Optional<Book> put(long id, String title) {
        if (!books.containsKey(id)) {
            return Optional.empty();
        }
        Book book = new Book(id, title);
        books.put(id, book);
        return Optional.of(book);
    }

    public boolean delete(long id) {
        return books.remove(id) != null;
    }
}
