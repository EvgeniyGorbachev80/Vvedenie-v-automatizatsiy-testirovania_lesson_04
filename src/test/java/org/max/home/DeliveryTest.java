package org.max.home;

import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class DeliveryTest extends AbstractTest {
    @Test
    void getDeliveries_whenValid_shouldReturn() throws SQLException {
        //given
        String sql = "SELECT * FROM delivery";
        Statement stmt  = getConnection().createStatement();
        int countTableSize = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession().createSQLQuery(sql).addEntity(DeliveryEntity.class);
        //then
        Assertions.assertEquals(15, countTableSize);
        Assertions.assertEquals(15, query.list().size());
    }


    @ParameterizedTest
    @CsvSource({"1, 3", "2, 4", "3, 1", "4, 2", "5, 1", "6, 3", "7, 2", "8, 4", "9, 4", "10, 3", "11, 2", "12, 1", "13, 2", "14, 3", "15, 4"})
    void getDeliveryById_whenValid_shouldReturn(int id, Integer courier) throws SQLException {
        //given
        String sql = "SELECT * FROM delivery WHERE delivery_id=" + id;
        Statement stmt = getConnection().createStatement();
        Integer values = null;
        Integer number = null;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            values = rs.getInt("courier_id");
            number = rs.getInt("delivery_id");
        }
        //then
        Assertions.assertEquals(courier, values);
        Assertions.assertEquals(id, number);
    }
}

