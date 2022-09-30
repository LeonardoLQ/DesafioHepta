package com.hepta.funcionarios.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hepta.funcionarios.entity.Funcionario;
import com.hepta.funcionarios.persistence.FuncionarioDAO;

@Path("/funcionarios")
public class FuncionarioService {

    @Context
    private HttpServletRequest request;

    @Context
    private HttpServletResponse response;

    private FuncionarioDAO dao;

    public FuncionarioService() {
        dao = new FuncionarioDAO();
    }

    protected void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Adiciona novo Funcionario
     * 
     * @param Funcionario: Novo Funcionario
     * @return response 200 (OK) - Conseguiu adicionar
     */
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response FuncionarioCreate(Funcionario funcionario) {
    	
    	try {
    		funcionario.setId(null);
			dao.save(funcionario);
			
			return Response.status(Status.CREATED).build();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
    }

    /**
     * Lista todos os Funcionarios
     * 
     * @return response 200 (OK) - Conseguiu listar
     */
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response FuncionarioRead() {
        List<Funcionario> Funcionarios = new ArrayList<>();
        try {
            Funcionarios = dao.getAll();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar Funcionarios").build();
        }

        GenericEntity<List<Funcionario>> entity = new GenericEntity<List<Funcionario>>(Funcionarios) {
        };
        return Response.status(Status.OK).entity(entity).build();
    }

    /**
     * Atualiza um Funcionario
     * 
     * @param id:          id do Funcionario
     * @param Funcionario: Funcionario atualizado
     * @return response 200 (OK) - Conseguiu atualizar
     */
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public Response FuncionarioUpdate(@PathParam("id") Integer id, Funcionario Funcionario) {
    	try {
    		if (dao.find(id)==null) {
    			return Response.status(Status.NOT_FOUND).build();
    		}
    		Funcionario.setId(id);
    		Funcionario funcionario = dao.update(Funcionario);
    		return Response.status(Status.OK).entity(funcionario).build();
    		
    	}catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao Atualizar Funcionarios").build();
    	}
    }

    /**
     * Remove um Funcionario
     * 
     * @param id: id do Funcionario
     * @return response 200 (OK) - Conseguiu remover
     */
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    public Response FuncionarioDelete(@PathParam("id") Integer id) {
        
    	try {
    		if (dao.find(id) == null) {
    			return Response.status(Status.NOT_FOUND).build();
    		}
    		dao.delete(id);
    			return Response.status(Status.NO_CONTENT).build();
    		
    	}catch (Exception e) {
    			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    	}
        
    }

    /**
     * Métodos simples apenas para testar o REST
     * @return
     */
    @Path("/teste")
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public String TesteJersey() {
        return "Funcionando.";
    }

}
