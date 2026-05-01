namespace Triathlon.Model
{
    public class Participant : Entity
    {
        public new int Id { get => base.Id; set => base.Id = value; }

        public string Name { get; set; }
        public int TotalPoints { get; set; }

        public Participant(int id, string name, int totalPoints) : base(id)
        {
            this.Id = id;
            this.Name = name;
            this.TotalPoints = totalPoints;
        }
    }
}