package info.mnafshin.springuniverse.container.checkout;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The shop's two tables, orders and order_lines, in the app's in-memory H2 database.
 * Not a bean on its own: each example declares it with {@code @Import(OrderTables.class)}.
 */
public class OrderTables {

    private final JdbcTemplate jdbc;

    public OrderTables(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        jdbc.execute("create table if not exists orders (id varchar(20) primary key)");
        jdbc.execute("create table if not exists order_lines (order_id varchar(20), item varchar(40), price int)");
    }

    public void addOrder(String id) {
        jdbc.update("insert into orders (id) values (?)", id);
    }

    public void addLine(String id, String item, int price) {
        jdbc.update("insert into order_lines (order_id, item, price) values (?, ?, ?)", id, item, price);
    }

    /** what is really in the database right now */
    public String counts() {
        Integer orders = jdbc.queryForObject("select count(*) from orders", Integer.class);
        Integer lines = jdbc.queryForObject("select count(*) from order_lines", Integer.class);
        return "in the database: orders " + orders + ", order lines " + lines;
    }
}
