using System.Collections.Generic;
using Triathlon.Model;

namespace Triathlon.Services
{
    public interface ITriathlonServices
    {
        Referee Login(string username, string password, ITriathlonObserver client);
        void Logout(Referee referee, ITriathlonObserver client);
        List<Participant> GetParticipantsByEvent(int idEvent);
        void AddResult(int idReferee, int idParticipant, int points);
        List<Event> GetAllEvents();
    }
}