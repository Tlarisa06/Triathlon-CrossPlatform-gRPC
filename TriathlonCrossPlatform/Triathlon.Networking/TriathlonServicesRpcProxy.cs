using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Grpc.Core;
using Grpc.Net.Client;
using Triathlon.Model;
using Triathlon.Services;

namespace Triathlon.Networking
{
    public class TriathlonServicesRpcProxy : ITriathlonServices
    {
        private readonly string _host;
        private readonly int _port;
        private ITriathlonObserver? _clientObserver;
        private TriathlonGrpcService.TriathlonGrpcServiceClient _grpcClient;
        private GrpcChannel _channel;
        private volatile bool _finished;

        public TriathlonServicesRpcProxy(string host, int port)
        {
            _host = host;
            _port = port;
            _channel = GrpcChannel.ForAddress($"http://{_host}:{_port}");
            _grpcClient = new TriathlonGrpcService.TriathlonGrpcServiceClient(_channel);
        }

        public virtual Referee Login(string username, string password, ITriathlonObserver observer)
        {
            var request = new UserRequest { Username = username, Password = password };
            try
            {
                var response = _grpcClient.Login(request);
                _clientObserver = observer;
                
                StartSubscription(response);

                return new Referee(response.Id, response.Name, username, response.IdEvent);
            }
            catch (RpcException e)
            {
                throw new Exception("Login failed: " + e.Status.Detail);
            }
        }

        public virtual List<Participant> GetParticipantsByEvent(int idEvent)
        {
            var request = new EventIdRequest { IdEvent = idEvent };
            List<Participant> participants = new List<Participant>();
            
            using var call = _grpcClient.GetParticipantsByEvent(request);
            
            var responseStream = call.ResponseStream;
            while (responseStream.MoveNext(System.Threading.CancellationToken.None).Result)
            {
                var p = responseStream.Current;
                participants.Add(new Participant(p.IdParticipant, p.Name, p.Points));
            }
            
            return participants;
        }

        public virtual void AddResult(int idReferee, int idParticipant, int points)
        {
            var request = new AddResultRequest 
            { 
                IdReferee = idReferee, 
                IdParticipant = idParticipant, 
                Points = points 
            };
            
            try
            {
                _grpcClient.AddResult(request);
            }
            catch (RpcException e)
            {
                throw new Exception("Could not add result: " + e.Status.Detail);
            }
        }

        private async void StartSubscription(RefereeResponse loggedUser)
        {
            using var call = _grpcClient.Subscribe(loggedUser);
            _finished = false;

            try
            {
                while (await call.ResponseStream.MoveNext() && !_finished)
                {
                    var notification = call.ResponseStream.Current;
                    _clientObserver?.UpdateReceived();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error in subscription: " + ex.Message);
            }
        }

        public virtual void Logout(Referee referee, ITriathlonObserver observer)
        {
            try
            {
                _finished = true;
                var req = new RefereeResponse { Id = referee.Id, Name = referee.Name };
                _grpcClient.Logout(req);
                _channel.ShutdownAsync().Wait();
            }
            catch (Exception e) { Console.WriteLine(e.Message); }
        }

        public List<Event> GetAllEvents() => new List<Event>();
    }
}