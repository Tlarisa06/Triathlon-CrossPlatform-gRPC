package ro.mpp.triathlon.server.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ro.mpp.triathlon.TriathlonService;
import ro.mpp.triathlon.network.grpc.TriathlonGrpcImpl;
import ro.mpp.triathlon.repository.db.EventDbRepo;
import ro.mpp.triathlon.repository.db.ParticipantDbRepo;
import ro.mpp.triathlon.repository.db.RefereeDbRepo;

import java.io.IOException;
import java.util.Properties;

public class StartGrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartGrpcServer.class.getResourceAsStream("/bd.properties"));
        } catch (IOException e) {
            System.err.println("Nu s-a putut încărca bd.properties " + e);
            return;
        }

        RefereeDbRepo refRepo = new RefereeDbRepo(serverProps);
        ParticipantDbRepo partRepo = new ParticipantDbRepo(serverProps);
        EventDbRepo eventRepo = new EventDbRepo(serverProps);

        TriathlonService service = new TriathlonService(partRepo, refRepo, eventRepo);

        int port = 55555;
        Server server = ServerBuilder.forPort(port)
                .addService(new TriathlonGrpcImpl(service))
                .build();

        System.out.println("Server gRPC pornit pe portul " + port + "...");
        server.start();
        server.awaitTermination();
    }
}