package org.max.home;

import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.max.seminar.EmployeeEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductTest extends AbstractTest {
    @Test
    void getProducts_whenValid_shouldReturn() throws SQLException {
        //given
        String sql = "SELECT * FROM products";
        Statement stmt  = getConnection().createStatement();
        int countTableSize = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession().createSQLQuery(sql).addEntity(ProductsEntity.class);
        //then
        Assertions.assertEquals(10, countTableSize);
        Assertions.assertEquals(10, query.list().size());
    }

    @ParameterizedTest
    @CsvSource({"1, GOJIRA ROLL", "2, VIVA LAS VEGAS ROLL", "3, FUTOMAKI", "4, TOOTSY MAKI", "5, ZONIE ROLL", "6, NUTTY GRILLED SALAD", "7, SASHIMI SALAD", "8, SUNNY TEA", "9, COFFEE", "10, MINERAL WATER"})
    void getProductsById_whenValid_shouldReturn(int id, String menuName) throws SQLException {
        //given
        String sql = "SELECT * FROM products WHERE product_id=" + id;
        Statement stmt  = getConnection().createStatement();
        String name = "";
        String name1 = "";
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            name = rs.getString("menu_name");
            name1 = rs.getString("menu_name");
        }
        //then
        Assertions.assertEquals(menuName, name);
        Assertions.assertEquals(menuName, name1);
    }

    @ParameterizedTest
    @CsvSource({"1, GOJIRA ROLL", "2, VIVA LAS VEGAS ROLL"})
    void getProductById_whenValid_shouldReturn(int id, String menuName) throws SQLException {
        //given
        //when
        final Query query = getSession().createQuery("from " + "ProductsEntity" + " where productId=" + id);
        ProductsEntity productsEntity = (ProductsEntity) query.uniqueResult();
        //then
        Assertions.assertEquals(menuName, productsEntity.getMenuName());
    }
}
