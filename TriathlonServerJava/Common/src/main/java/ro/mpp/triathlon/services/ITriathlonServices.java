package ro.mpp.triathlon.services;

import ro.mpp.triathlon.model.Event;
import ro.mpp.triathlon.model.Participant;
import ro.mpp.triathlon.model.Referee;
import java.util.List;

public interface ITriathlonServices {
    Referee login(String username, String password, ITriathlonObserver client) throws Exception;
    void logout(Referee referee, ITriathlonObserver client) throws Exception;
    List<Participant> getParticipantsByEvent(int idEvent) throws Exception;
    List<Event> getAllEvents() throws Exception;
    void addResult(int idReferee, int idParticipant, int points) throws Exception;
}