package edu.upc.dsa.domain.repository;

import edu.upc.dsa.domain.models.Drone;
import edu.upc.dsa.domain.models.FlightPlan;
import edu.upc.dsa.domain.models.Pilot;
import java.util.List;

public interface DroneManager {
    void addDrone(String id, String name, String manufacturer, String model);
    void addPilot(String id, String name, String surname);

    List<Drone> getDronesByFlightHours();
    List<Pilot> getPilotsByFlightHours();

    void storeDroneInWarehouse(String droneId);

    Drone repairDrone();

    void addFlightPlan(FlightPlan plan);

    List<FlightPlan> getFlightPlansByPilot(String pilotId);
    List<FlightPlan> getFlightPlansByDrone(String droneId);

    int numDrones();
    int numPilots();
    int numDronesInWarehouse();
    void clear();
}