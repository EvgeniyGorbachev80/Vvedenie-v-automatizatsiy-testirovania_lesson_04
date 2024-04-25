package org.max.home;

import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class OrdersProductsTest extends AbstractTest {

    @Test
    void getOrdersProducts_whenValid_shouldReturn() throws SQLException {
        //given
        String sql = "SELECT * FROM orders_products";
        Statement stmt  = getConnection().createStatement();
        int countTableSize = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession().createSQLQuery(sql).addEntity(OrdersProductsEntity.class);
        //then
        Assertions.assertEquals(23, countTableSize);
        Assertions.assertEquals(23, query.list().size());
    }
}
