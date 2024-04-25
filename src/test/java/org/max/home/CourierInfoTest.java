package org.max.home;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import javax.persistence.PersistenceException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class CourierInfoTest extends AbstractTest {

    @Test
    @Order(1)
    void getCouriers_whenValid_shouldReturn() throws SQLException {
        //given
        String sql = "SELECT * FROM courier_info";
        Statement stmt  = getConnection().createStatement();
        int countTableSize = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession().createSQLQuery(sql).addEntity(CourierInfoEntity.class);
        //then
        Assertions.assertEquals(4, countTableSize);
        Assertions.assertEquals(4, query.list().size());
    }

    @Order(2)
    @ParameterizedTest
    @CsvSource({"1, Rython", "2, Looran", "3, Kolaris", "4, Frontal"})
    void getClientById_whenValid_shouldReturn(int id, String lastName) throws SQLException {
        //given
        String sql = "SELECT * FROM courier_info WHERE courier_id=" + id;
        Statement stmt  = getConnection().createStatement();
        String name = "";
        String lastname = "";
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            name = rs.getString("last_name");
            lastname = rs.getString("last_name");
        }
        //then
        Assertions.assertEquals(lastName, name);
        Assertions.assertEquals(lastName, lastname);
    }

    @Test
    @Order(3)
    void addCouriers_whenValid_shouldSave() {
        //given
        CourierInfoEntity entity = new CourierInfoEntity();
        entity.setCourierId((short) 5);
        entity.setFirstName("John");
        entity.setLastName("Wick");
        entity.setPhoneNumber("+ 7 960 655 0955");
        entity.setDeliveryType("foot");

        //when
        Session session = getSession();
        session.beginTransaction();
        session.persist(entity);
        session.getTransaction().commit();

        final Query query = getSession()
                .createSQLQuery("SELECT * FROM courier_info WHERE courier_id="+5).addEntity(CourierInfoEntity.class);
        CourierInfoEntity courierInfoEntity = (CourierInfoEntity) query.uniqueResult();
        //then
        Assertions.assertNotNull(courierInfoEntity);
        Assertions.assertEquals("+ 7 960 655 0955", courierInfoEntity.getPhoneNumber());
    }

    @Test
    @Order(4)
    void deleteCouriers_whenValid_shouldDelete() {
        //given
        final Query query = getSession()
                .createSQLQuery("SELECT * FROM courier_info WHERE courier_id=" + 5).addEntity(CourierInfoEntity.class);
        Optional<CourierInfoEntity> courierInfoEntity = (Optional<CourierInfoEntity>) query.uniqueResultOptional();
        Assumptions.assumeTrue(courierInfoEntity.isPresent());
        //when
        Session session = getSession();
        session.beginTransaction();
        session.delete(courierInfoEntity.get());
        session.getTransaction().commit();
        //then
        final Query queryAfterDelete = getSession()
                .createSQLQuery("SELECT * FROM courier_info WHERE courier_id=" + 5).addEntity(CourierInfoEntity.class);
        Optional<CourierInfoEntity> courierInfoAfterDelete = (Optional<CourierInfoEntity>) queryAfterDelete.uniqueResultOptional();
        Assertions.assertFalse(courierInfoAfterDelete.isPresent());
    }

    @Test
    @Order(5)
    void addCouriers_whenNotValid_shouldThrow() {
        //given
        CourierInfoEntity entity = new CourierInfoEntity();
        //when
        Session session = getSession();
        session.beginTransaction();
        session.persist(entity);
        //then
        Assertions.assertThrows(PersistenceException.class, () -> session.getTransaction().commit());
    }
}
