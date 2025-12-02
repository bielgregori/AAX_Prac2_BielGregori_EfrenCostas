package aar.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import aar.models.Bolsa;
import aar.services.BolsaService;
import aar.repository.BolsaDaoImpl;

import java.util.List;

@Path("/bolsa")
public class BolsaController {

    private final BolsaService bolsaService;

    public BolsaController() {
        //Wiring manual
        this.bolsaService = new BolsaService(new BolsaDaoImpl());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Bolsa> getAllBolsas() {
        return bolsaService.getAllBolsas();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Bolsa getBolsaById(@PathParam("id") Long id) {
        Bolsa bolsa = bolsaService.getBolsaById(id);
        if (bolsa == null) {
            throw new NotFoundException("Bolsa not found with id " + id);
        }
        return bolsa;
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    public Response createBolsa(
            @FormParam("name") String nombreBolsa) {
        
        Bolsa bolsa = new Bolsa(nombreBolsa);
        bolsaService.createBolsa(bolsa);
        return Response.status(Response.Status.CREATED).entity(bolsa).build();
    }

    /*
    @PUT
    @Path("/{id}")
    public Response updateBolsa(@PathParam("id") Long id, Bolsa bolsa) {
        Bolsa existing = bolsaService.getBolsaById(id);
        if (existing == null) {
            throw new NotFoundException("Bolsa not found with id " + id);
        }

        existing.setNombreBolsa(bolsa.getNombreBolsa());

        bolsaService.updateBolsa(existing);
        return Response.ok(existing).build();
    }
     */
    @DELETE
    @Path("/{id}")
    public Response deleteBolsa(@PathParam("id") Long id) {
        Bolsa existing = bolsaService.getBolsaById(id);
        if (existing == null) {
            throw new NotFoundException("Bolsa not found with id " + id);
        }

        bolsaService.deleteBolsa(id);
        return Response.noContent().build();
    }
}

