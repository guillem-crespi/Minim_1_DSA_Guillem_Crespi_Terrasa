package edu.upc.dsa.services;

import edu.upc.dsa.domain.models.Drone;
import edu.upc.dsa.domain.models.FlightPlan;
import edu.upc.dsa.domain.repository.DroneManager;
import edu.upc.dsa.domain.repository.DroneManagerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/drones", description = "Endpoint to Drone Management Service")
@Path("/drones")
public class DroneService {

    private DroneManager manager;

    public DroneService() {
        this.manager = DroneManagerImpl.getInstance();

        // Dades inicials per poder fer proves ràpides a Swagger
        if (manager.numDrones() == 0) {
            manager.addDrone("D1", "Mavic", "DJI", "Pro 3");
            manager.addDrone("D2", "Phantom", "DJI", "4 RTK");
            manager.addPilot("P1", "Guillem", "Crespi");
            manager.addPilot("P2", "Laura", "Gomez");
        }
    }

    @GET
    @ApiOperation(value = "Llistat de drons per hores de vol", notes = "Ordenat descendentment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Drone.class, responseContainer="List"),
    })
    @Path("/listByHours")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDronesByFlightHours() {
        List<Drone> drones = this.manager.getDronesByFlightHours();
        GenericEntity<List<Drone>> entity = new GenericEntity<List<Drone>>(drones) {};
        return Response.status(200).entity(entity).build();
    }

    @POST
    @ApiOperation(value = "Guardar dron al magatzem", notes = "Posa el dron en manteniment")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Dron no trobat"),
            @ApiResponse(code = 409, message = "Dron ja està al magatzem")
    })
    @Path("/warehouse/{droneId}")
    public Response storeDroneInWarehouse(@PathParam("droneId") String droneId) {
        try {
            this.manager.storeDroneInWarehouse(droneId);
            return Response.status(201).build();
        } catch (IllegalArgumentException e) {
            return Response.status(404).build();
        } catch (IllegalStateException e) {
            return Response.status(409).build();
        }
    }

    @PUT
    @ApiOperation(value = "Reparar dron del magatzem", notes = "Treu el dron més proper a la porta (LIFO)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Drone.class),
            @ApiResponse(code = 404, message = "Magatzem buit")
    })
    @Path("/warehouse/repair")
    @Produces(MediaType.APPLICATION_JSON)
    public Response repairDrone() {
        try {
            Drone d = this.manager.repairDrone();
            return Response.status(200).entity(d).build();
        } catch (IllegalStateException e) {
            return Response.status(404).build(); // Magatzem buit
        }
    }

    @POST
    @ApiOperation(value = "Crear un pla de vol", notes = "Afegeix un pla de vol a un dron i pilot")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Dron o Pilot no trobats"),
            @ApiResponse(code = 409, message = "Conflicte d'estat o horari")
    })
    @Path("/flightplan")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFlightPlan(FlightPlan plan) {
        try {
            this.manager.addFlightPlan(plan);
            return Response.status(201).build();
        } catch (IllegalArgumentException e) {
            return Response.status(404).build();
        } catch (IllegalStateException e) {
            return Response.status(409).build();
        }
    }
}