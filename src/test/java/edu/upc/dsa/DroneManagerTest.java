package edu.upc.dsa;

import edu.upc.dsa.domain.models.Drone;
import edu.upc.dsa.domain.models.FlightPlan;
import edu.upc.dsa.domain.models.Position;
import edu.upc.dsa.domain.repository.DroneManager;
import edu.upc.dsa.domain.repository.DroneManagerImpl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DroneManagerTest {

    private DroneManager manager;

    @Before
    public void setUp() {
        manager = DroneManagerImpl.getInstance();
        manager.clear();

        manager.addDrone("D1", "Mavic", "DJI", "Pro 3");
        manager.addDrone("D2", "Phantom", "DJI", "4 RTK");
        manager.addPilot("P1", "Guillem", "Crespi");

        System.out.println("\n--- SETUP: Dades inicials carregades ---");
    }

    @After
    public void tearDown() {
        manager.clear();
    }

    @Test
    public void test1_LlistarDrons() {
        System.out.println("Executant Test 1: Llistar drons...");
        List<Drone> llista = manager.getDronesByFlightHours();

        Assert.assertEquals("Hi hauria d'haver 2 drons inicials", 2, llista.size());
        System.out.println("-> OK: Llista obtinguda correctament.");
    }

    @Test
    public void test2_EnviarAlMagatzem() {
        System.out.println("Executant Test 2: Enviar al magatzem...");
        manager.storeDroneInWarehouse("D1");

        // Comprovem que ara hi ha 1 dron al magatzem
        Assert.assertEquals(1, manager.numDronesInWarehouse());
        System.out.println("-> OK: Dron D1 enviat a manteniment.");
    }

    @Test
    public void test3_RepararDronLIFO() {
        System.out.println("Executant Test 3: Reparar dron (LIFO)...");
        manager.storeDroneInWarehouse("D1");
        manager.storeDroneInWarehouse("D2");

        Drone reparat = manager.repairDrone();

        Assert.assertEquals("El dron reparat hauria de ser el D2", "D2", reparat.getId());
        Assert.assertFalse("El dron ja no hauria d'estar en manteniment", reparat.isInMaintenance());
        System.out.println("-> OK: S'ha reparat l'últim dron en entrar (D2).");
    }

    @Test
    public void test4_CrearPlaDeVol() {
        System.out.println("Executant Test 4: Crear pla de vol i sumar hores...");
        FlightPlan plan = new FlightPlan();
        plan.setId("FP001");
        plan.setDronId("D2");
        plan.setPilotId("P1");
        plan.setEstimatedHours(2.5);
        plan.setRoute(new ArrayList<Position>());

        manager.addFlightPlan(plan);
        List<Drone> llista = manager.getDronesByFlightHours();

        Assert.assertEquals("El D2 ha de ser el primer de la llista", "D2", llista.get(0).getId());
        Assert.assertEquals(2.5, llista.get(0).getFlightHours(), 0.01);
        System.out.println("-> OK: Pla de vol afegit i hores sumades al D2.");
    }
}