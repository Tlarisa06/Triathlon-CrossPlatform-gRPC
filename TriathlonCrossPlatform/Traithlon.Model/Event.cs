namespace Triathlon.Model
{
    public class Event : Entity
    {
        public new int Id { get => base.Id; set => base.Id = value; }

        public int IdRef { get; set; }
        public int IdPart { get; set; }
        public int Points { get; set; }

        public Event(int id, int idRef, int idPart, int points) : base(id)
        {
            this.Id = id;
            this.IdRef = idRef;
            this.IdPart = idPart;
            this.Points = points;
        }
    }
}