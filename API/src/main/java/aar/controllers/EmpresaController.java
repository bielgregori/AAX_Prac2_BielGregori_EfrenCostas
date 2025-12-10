package aar.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import aar.models.Bolsa;
import aar.models.Empresa;
import aar.services.BolsaService;
import aar.services.EmpresaService;
import aar.repository.BolsaDaoImpl;
import aar.repository.EmpresaDaoImpl;


import java.util.List;

@Path("/empresa")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController() {
        this.empresaService = new EmpresaService(new EmpresaDaoImpl());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Empresa> getAllempresas() {
        return empresaService.getAllEmpresas();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Empresa getEmpresaById(@PathParam("id") Long id) {
        Empresa empresa = empresaService.getEmpresaById(id);
        if (empresa == null) {
            throw new NotFoundException("Empresa not found with id " + id);
        }
        return empresa;
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    public Response createEmpresa(
            @FormParam("nombreEmpresa") String nombreEmpresa,
            @FormParam("icono") String icono,
            @FormParam("bolsa_id") Long bolsaId) {

        BolsaService bolsaService = new BolsaService(new BolsaDaoImpl());
        Bolsa bolsa = bolsaService.getBolsaById(bolsaId);

        if (bolsa == null) {
            throw new NotFoundException("Bolsa not found with id " + bolsaId);
        }

        Empresa empresa = new Empresa(nombreEmpresa, icono);
        empresa.setBolsa(bolsa);
        bolsa.agregarEmpresa(empresa);
        
        empresaService.createEmpresa(empresa);
        return Response.status(Response.Status.CREATED).entity(empresa).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateEmpresa(@PathParam("id") Long id, Empresa empresa) {
        Empresa existing = empresaService.getEmpresaById(id);
        if (existing == null) {
            throw new NotFoundException("Empresa not found with id " + id);
        }

        existing.setNombreEmpresa(empresa.getNombreEmpresa());
        existing.setIcono(empresa.getIcono());
        existing.setPrecioAccion(empresa.getPrecioAccion());

        empresaService.updateEmpresa(existing);
        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEmpresa(@PathParam("id") Long id) {
        Empresa existing = empresaService.getEmpresaById(id);
        if (existing == null) {
            throw new NotFoundException("Empresa not found with id " + id);
        }

        empresaService.deleteEmpresa(id);
        return Response.noContent().build();
    }
}

