using System.Windows;
using Triathlon.Client.Controllers;
using Triathlon.Model; // Am schimbat din .Model.DTO în .Model

namespace Triathlon.Client.Views
{
    public partial class MainWindow : Window
    {
        private MainController controller;

        public MainWindow()
        {
            InitializeComponent();
        }

        public void SetController(MainController ctrl)
        {
            this.controller = ctrl;
    
            WelcomeLabel.Content = $"Arbitru: {ctrl.GetLoggedRefereeName()}"; 

            SaveButton.Click += (s, e) => {
                var selected = AllParticipantsTable.SelectedItem as Participant;
                
                if (selected != null) {
                    controller.AddResult(selected, PointsField.Text);
                    PointsField.Clear(); 
                } else {
                    MessageBox.Show("Vă rugăm selectați un participant din tabel!");
                }
            };

            LogoutButton.Click += (s, e) => {
                controller.Logout();
                this.Close(); 
            };
        }
    }
}