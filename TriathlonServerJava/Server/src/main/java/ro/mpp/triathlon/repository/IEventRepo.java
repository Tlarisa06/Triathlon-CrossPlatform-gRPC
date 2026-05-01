package ro.mpp.triathlon.repository;



import ro.mpp.triathlon.model.Event;

import java.util.List;

public interface IEventRepo extends IRepository<Integer, Event> {
    // Returnează toate rezultatele introduse de un anumit arbitru
    List<Event> findByRefereeId(int idReferee);
}