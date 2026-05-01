namespace Triathlon.Model
{
    public abstract class Entity
    {
        public int Id { get; set; }

        protected Entity(int id)
        {
            this.Id = id;
        }
    }
}