namespace Triathlon.Model
{
    public class Referee : Entity
    {
        public new int Id { get => base.Id; set => base.Id = value; }

        public string Name { get; set; }
        public string Password { get; set; }
        public int IdEvent { get; set; }

        public Referee(int id, string name, string password, int idEvent) : base(id)
        {
            this.Name = name;
            this.Password = password;
            this.IdEvent = idEvent;
        }
    }
}