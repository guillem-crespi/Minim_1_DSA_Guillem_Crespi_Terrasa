package edu.upc.dsa.domain.repository;

import edu.upc.dsa.domain.models.Drone;
import edu.upc.dsa.domain.models.FlightPlan;
import edu.upc.dsa.domain.models.Pilot;
import org.apache.log4j.Logger;

import java.util.*;

public class DroneManagerImpl implements DroneManager {

    private static final Logger logger = Logger.getLogger(DroneManagerImpl.class);
    private static DroneManagerImpl instance;

    private Map<String, Drone> drons;
    private Map<String, Pilot> pilots;
    private Stack<Drone> warehouse;
    private Map<String, List<FlightPlan>> flightPlansByPilot;
    private Map<String, List<FlightPlan>> flightPlansByDron;

    private DroneManagerImpl() {
        this.drons = new HashMap<>();
        this.pilots = new HashMap<>();
        this.warehouse = new Stack<>();
        this.flightPlansByPilot = new HashMap<>();
        this.flightPlansByDron = new HashMap<>();
    }

    public static DroneManagerImpl getInstance() {
        if (instance == null) {
            instance = new DroneManagerImpl();
        }
        return instance;
    }

    @Override
    public void addDrone(String id, String name, String manufacturer, String model) {
        logger.info("Inici addDrone - id: " + id + ", name: " + name);
        Drone d = new Drone(id, name, manufacturer, model);
        this.drons.put(id, d);
        this.flightPlansByDron.put(id, new LinkedList<>());
        logger.info("Final addDrone - Dron afegit correctament.");
    }

    @Override
    public void addPilot(String id, String name, String surname) {
        logger.info("Inici addPilot - id: " + id + ", name: " + name);
        Pilot p = new Pilot(id, name, surname);
        this.pilots.put(id, p);
        this.flightPlansByPilot.put(id, new LinkedList<>());
        logger.info("Final addPilot - Pilot afegit correctament.");
    }

    @Override
    public List<Drone> getDronesByFlightHours() {
        logger.info("Inici getDronesByFlightHours");
        List<Drone> list = new ArrayList<>(this.drons.values());
        list.sort((d1, d2) -> Double.compare(d2.getFlightHours(), d1.getFlightHours()));
        logger.info("Final getDronesByFlightHours - retornant " + list.size() + " drons.");
        return list;
    }

    @Override
    public List<Pilot> getPilotsByFlightHours() {
        logger.info("Inici getPilotsByFlightHours");
        List<Pilot> list = new ArrayList<>(this.pilots.values());
        list.sort((p1, p2) -> Double.compare(p2.getFlightHours(), p1.getFlightHours()));
        logger.info("Final getPilotsByFlightHours - retornant " + list.size() + " pilots.");
        return list;
    }

    @Override
    public void storeDroneInWarehouse(String droneId) {
        logger.info("Inici storeDroneInWarehouse - droneId: " + droneId);
        Drone d = this.drons.get(droneId);

        if (d == null) {
            logger.error("Error: Dron no trobat.");
            throw new IllegalArgumentException("El dron no existeix");
        }
        if (d.isInMaintenance()) {
            logger.error("Error: El dron ja està al magatzem.");
            throw new IllegalStateException("El dron ja està en manteniment");
        }

        d.setInMaintenance(true);
        this.warehouse.push(d);
        logger.info("Final storeDroneInWarehouse - Dron guardat. Mida magatzem: " + this.warehouse.size());
    }

    @Override
    public Drone repairDrone() {
        logger.info("Inici repairDrone");
        if (this.warehouse.isEmpty()) {
            logger.error("Error: Magatzem buit.");
            throw new IllegalStateException("No hi ha drons al magatzem");
        }

        Drone d = this.warehouse.pop();
        d.setInMaintenance(false);
        logger.info("Final repairDrone - Dron reparat: " + d.getId());
        return d;
    }

    @Override
    public void addFlightPlan(FlightPlan plan) {
        logger.info("Inici addFlightPlan - planId: " + plan.getId());
        Drone d = this.drons.get(plan.getDronId());
        Pilot p = this.pilots.get(plan.getPilotId());

        if (d == null || p == null) {
            logger.error("Error: Dron o Pilot no existeix.");
            throw new IllegalArgumentException("Dron o Pilot invàlids");
        }

        if (d.isInMaintenance()) {
            logger.error("Error: El dron està en manteniment.");
            throw new IllegalStateException("El dron no està operatiu");
        }

        List<FlightPlan> dronPlans = this.flightPlansByDron.get(d.getId());
        for (FlightPlan fp : dronPlans) {
            if (fp.getDateTime().equals(plan.getDateTime())) {
                logger.error("Error: Conflicte d'horari.");
                throw new IllegalStateException("Conflicte d'horari per al dron");
            }
        }

        this.flightPlansByDron.get(d.getId()).add(plan);
        this.flightPlansByPilot.get(p.getId()).add(plan);
        d.setFlightHours(d.getFlightHours() + plan.getEstimatedHours());
        p.setFlightHours(p.getFlightHours() + plan.getEstimatedHours());
        logger.info("Final addFlightPlan - Pla creat.");
    }

    @Override
    public List<FlightPlan> getFlightPlansByPilot(String pilotId) {
        logger.info("Inici getFlightPlansByPilot");
        List<FlightPlan> plans = this.flightPlansByPilot.get(pilotId);
        return plans == null ? new LinkedList<>() : plans;
    }

    @Override
    public List<FlightPlan> getFlightPlansByDrone(String droneId) {
        logger.info("Inici getFlightPlansByDrone");
        List<FlightPlan> plans = this.flightPlansByDron.get(droneId);
        return plans == null ? new LinkedList<>() : plans;
    }

    @Override
    public int numDrones() { return this.drons.size(); }

    @Override
    public int numPilots() { return this.pilots.size(); }

    @Override
    public int numDronesInWarehouse() { return this.warehouse.size(); }

    @Override
    public void clear() {
        this.drons.clear();
        this.pilots.clear();
        this.warehouse.clear();
        this.flightPlansByDron.clear();
        this.flightPlansByPilot.clear();
        logger.info("Sistema netejat.");
    }
} // La clau final que faltava per tancar la classe