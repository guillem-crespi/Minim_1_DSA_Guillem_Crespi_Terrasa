package edu.upc.dsa.services;

import edu.upc.dsa.domain.models.Pilot;
import edu.upc.dsa.domain.repository.DroneManager;
import edu.upc.dsa.domain.repository.DroneManagerImpl;
import edu.upc.dsa.domain.models.Drone;
import edu.upc.dsa.domain.models.FlightPlan;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/drones", description = "Endpoint del servei de Drons")
@Path("/drones")
public class DroneService {

    private DroneManager manager;

    // Constructor: Agafem la instància Singleton del manager
    public DroneService() {
        this.manager = DroneManagerImpl.getInstance();
    }

    // ==============================================================
    // RUTA EXTRA (Pujar nota): Afegir dron manualment des del Postman
    // ==============================================================
    @POST
    @ApiOperation(value = "Afegeix un dron manualment (Ruta Extra)")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDrone(Drone dron) {
        this.manager.addDrone(dron.getId(), dron.getName(), dron.getManufacturer(), dron.getModel());
        return Response.status(201).build();
    }

    // ==============================================================
    // 1. Llistar drons ordenats per hores de vol (Descendent)
    // ==============================================================
    @GET
    @ApiOperation(value = "Llistar drons per hores de vol")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Drone.class, responseContainer = "List")
    })
    @Path("/listByHours")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDronesByHours() {
        List<Drone> drons = this.manager.getDronesByFlightHours();

        // Embolcallem la llista perquè Jersey la converteixi bé a JSON
        GenericEntity<List<Drone>> entity = new GenericEntity<List<Drone>>(drons) {
        };
        return Response.status(200).entity(entity).build();
    }

    // ==============================================================
    // 2. Enviar un dron al magatzem de reparació
    // ==============================================================
    @POST
    @ApiOperation(value = "Enviar dron al magatzem")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful")
    })
    @Path("/warehouse/{id}")
    public Response storeInWarehouse(@PathParam("id") String id) {
        this.manager.storeDroneInWarehouse(id);
        return Response.status(201).build();
    }

    // ==============================================================
    // 3. Reparar dron (Extreure del magatzem en format LIFO)
    // ==============================================================
    @PUT
    @ApiOperation(value = "Reparar dron del magatzem (LIFO)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Drone.class),
            @ApiResponse(code = 404, message = "El magatzem està buit")
    })
    @Path("/warehouse/repair")
    @Produces(MediaType.APPLICATION_JSON)
    public Response repairDrone() {
        Drone d = this.manager.repairDrone();
        if (d == null) {
            return Response.status(404).build();
        }
        return Response.status(200).entity(d).build();
    }

    // ==============================================================
    // 4. Afegir Pla de Vol i actualitzar hores
    // ==============================================================
    @POST
    @ApiOperation(value = "Afegir pla de vol")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful")
    })
    @Path("/flightplan")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFlightPlan(FlightPlan plan) {
        this.manager.addFlightPlan(plan);
        return Response.status(201).build();
    }
    // ==============================================================
    // RUTA EXTRA 2: Afegir pilot manualment des del Postman
    // ==============================================================
    @POST
    @ApiOperation(value = "Afegeix un pilot manualment")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful")
    })
    @Path("/pilot")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPilot(Pilot pilot) {
        // Assegura't que els mètodes getId(), getName() i getSurname() existeixen al teu model Pilot
        this.manager.addPilot(pilot.getId(), pilot.getName(), pilot.getSurname());
        return Response.status(201).build();
    }
}