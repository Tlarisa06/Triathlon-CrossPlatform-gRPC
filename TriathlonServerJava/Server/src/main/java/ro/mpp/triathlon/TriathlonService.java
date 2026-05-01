package ro.mpp.triathlon;

import ro.mpp.triathlon.model.*;
import ro.mpp.triathlon.repository.*;
import ro.mpp.triathlon.services.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TriathlonService implements ITriathlonServices {
    private IParticipantRepo participantRepo;
    private IRefereeRepo refereeRepo;
    private IEventRepo eventRepo;
    private Map<Integer, ITriathlonObserver> loggedClients;

    public TriathlonService(IParticipantRepo pRepo, IRefereeRepo rRepo, IEventRepo eRepo) {
        this.participantRepo = pRepo;
        this.refereeRepo = rRepo;
        this.eventRepo = eRepo;
        this.loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized Referee login(String username, String password, ITriathlonObserver client) throws Exception {
        Referee referee = refereeRepo.findByUsernameAndPassword(username, password);
        if (referee != null) {
            if (loggedClients.containsKey(referee.getId()))
                throw new Exception("Utilizator deja logat.");
            loggedClients.put(referee.getId(), client);
            return referee;
        }
        throw new Exception("Autentificare eșuată!");
    }

    @Override
    public synchronized void logout(Referee referee, ITriathlonObserver client) throws Exception {
        loggedClients.remove(referee.getId());
    }

    @Override
    public synchronized void addResult(int idReferee, int idParticipant, int points) throws Exception {
        eventRepo.add(new Event(0, idReferee, idParticipant, points));

        Participant p = participantRepo.findById(idParticipant);
        if (p != null) {
            p.setTotalPoints(p.getTotalPoints() + points);
            participantRepo.update(p);
        }
        notifyClients();
    }

    @Override
    public List<Participant> getParticipantsByEvent(int idEvent) {
        List<Participant> participants = (List<Participant>) participantRepo.getAll();
        if (idEvent == -1) {
            participants.sort(Comparator.comparing(Participant::getName, String.CASE_INSENSITIVE_ORDER));
        } else {
            participants.sort((p1, p2) -> Integer.compare(p2.getTotalPoints(), p1.getTotalPoints()));
        }
        return participants;
    }

    @Override
    public List<Event> getAllEvents() {
        return (List<Event>) eventRepo.getAll();
    }

    private void notifyClients() {
        loggedClients.values().forEach(client -> {
            try { client.updateReceived(); }
            catch (Exception e) { System.err.println("Notificare esuata: " + e.getMessage()); }
        });
    }
}