package com.example.erillaminventory.controller;

import com.example.erillaminventory.dto.ProductDTO;
import com.example.erillaminventory.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    private ProductService productService;

    @GET
    public Response getAllProducts(@QueryParam("page") int page, @QueryParam("size") int size) {
        List<ProductDTO> products = productService.findAll(page, size);
        return Response.ok(products).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        Optional<ProductDTO> product = productService.findById(id);
        if(product.isPresent()) {
            return Response.ok(product.get()).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Producto no existe.")
                    .build();
        }
    }

    @POST
    public Response createProduct(ProductDTO productDTO) {
        try {
            productService.save(productDTO);
            return Response.status(Response.Status.CREATED)
                    .entity("Producto registrado con éxito.")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al registrar el producto: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, ProductDTO productDTO) {
        Optional<ProductDTO> existingProduct = productService.findById(id);
        if (existingProduct.isPresent()) {
            productDTO.setId(id);
            try {
                productService.update(productDTO);
                return Response.ok("Producto actualizado con éxito.").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error al actualizar el producto: " + e.getMessage())
                        .build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Producto no existe.")
                    .build();
        }
    }

    @PUT
    @Path("/{id}/stock")
    public Response updateProductStock(@PathParam("id") Long id, @QueryParam("stock") int stock) {
        Optional<ProductDTO> existingProduct = productService.findById(id);
        if (existingProduct.isPresent()) {
            try {
                productService.updateStock(id, stock);
                return Response.ok("Stock actualizado con éxito.").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error al actualizar el stock: " + e.getMessage())
                        .build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Producto no existe.")
                    .build();
        }
    }

    @GET
    @Path("/search")
    public Response getProducts(@QueryParam("name") String name, @QueryParam("category") String category) {
        try {
            List<ProductDTO> products = productService.getNameAndCategory(name, category);
            if (products.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontraron productos para el nombre y categoría dados.")
                        .build();
            }
            return Response.ok(products).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocurrió un error al procesar la solicitud: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        try {
            boolean deleted = productService.deleteById(id);
            if (deleted) {
                return Response.ok()
                        .entity("Producto eliminado con éxito.")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Producto con ID " + id + " no encontrado.")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocurrió un error al eliminar el producto: " + e.getMessage())
                    .build();
        }
    }

}
