package ro.mpp.triathlon.network.grpc;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import ro.mpp.triathlon.model.Participant;
import ro.mpp.triathlon.model.Referee;
import ro.mpp.triathlon.services.ITriathlonObserver;
import ro.mpp.triathlon.services.ITriathlonServices;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TriathlonGrpcImpl extends TriathlonGrpcServiceGrpc.TriathlonGrpcServiceImplBase {
    private final ITriathlonServices server;
    private final Map<Integer, StreamObserver<UpdateResponse>> observers = new ConcurrentHashMap<>();

    public TriathlonGrpcImpl(ITriathlonServices server) {
        this.server = server;
    }

    @Override
    public void login(UserRequest request, StreamObserver<RefereeResponse> responseObserver) {
        try {
            ITriathlonObserver observer = this::notifyUpdate;

            Referee referee = server.login(request.getUsername(), request.getPassword(), observer);

            RefereeResponse res = RefereeResponse.newBuilder()
                    .setId(referee.getId())
                    .setName(referee.getName())
                    .setIdEvent(referee.getIdEvent())
                    .build();

            responseObserver.onNext(res);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getParticipantsByEvent(EventIdRequest request, StreamObserver<ParticipantDTO> responseObserver) {
        try {
            List<Participant> participants = server.getParticipantsByEvent(request.getIdEvent());

            for (Participant p : participants) {
                ParticipantDTO grpcDto = ParticipantDTO.newBuilder()
                        .setIdParticipant(p.getId())
                        .setName(p.getName())
                        .setPoints(p.getTotalPoints())
                        .build();

                responseObserver.onNext(grpcDto);
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void addResult(AddResultRequest request, StreamObserver<Empty> responseObserver) {
        try {
            server.addResult(request.getIdReferee(), request.getIdParticipant(), request.getPoints());
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void subscribe(RefereeResponse request, StreamObserver<UpdateResponse> responseObserver) {
        observers.put(request.getId(), responseObserver);
        System.out.println("Arbitru abonat: " + request.getName());
    }

    @Override
    public void logout(RefereeResponse request, StreamObserver<Empty> responseObserver) {
        try {
            observers.remove(request.getId());
            server.logout(new Referee(request.getId(), request.getName(), "", 0), null);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    private void notifyUpdate() {
        UpdateResponse update = UpdateResponse.newBuilder().setMessage("UPDATE").build();
        observers.forEach((id, obs) -> {
            try {
                obs.onNext(update);
            } catch (Exception e) {
                observers.remove(id);
            }
        });
    }
}