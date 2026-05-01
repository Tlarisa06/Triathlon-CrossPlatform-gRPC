package ro.mpp.triathlon.repository.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.triathlon.model.Event;
import ro.mpp.triathlon.repository.IEventRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EventDbRepo implements IEventRepo {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public EventDbRepo(Properties props) {
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public List<Event> findByRefereeId(int idReferee) {
        List<Event> events = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM events WHERE idRef=?")) {
            preStmt.setInt(1, idReferee);
            try (ResultSet rs = preStmt.executeQuery()) {
                while (rs.next()) {
                    events.add(new Event(rs.getInt("id"), rs.getInt("idRef"), rs.getInt("idPart"), rs.getInt("points")));
                }
            }
        } catch (SQLException e) { logger.error(e); }
        return events;
    }

    @Override
    public void add(Event entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("INSERT INTO events (idRef, idPart, points) VALUES (?, ?, ?)")) {
            preStmt.setInt(1, entity.getIdRef());
            preStmt.setInt(2, entity.getIdPart());
            preStmt.setInt(3, entity.getPoints());
            preStmt.executeUpdate();
        } catch (SQLException e) { logger.error(e); }
    }

    // Implementează restul metodelor CRUD...
    @Override public void remove(Integer id) { }
    @Override public void update(Event entity) { }
    @Override public Event findById(Integer id) { return null; }
    @Override public List<Event> getAll() { return new ArrayList<>(); }
    @Override public void clear() { }
}