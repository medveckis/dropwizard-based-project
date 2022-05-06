package io.medveckis.web.resource;

import io.dropwizard.hibernate.UnitOfWork;
import io.medveckis.dao.CategoryDAO;
import io.medveckis.web.dto.CategoryData;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;


@Path(value = "/categories")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    private final CategoryDAO categoryDAO;

    public CategoryResource(CategoryDAO categoryService) {
        this.categoryDAO = categoryService;
    }

    @GET
    @UnitOfWork
    public List<CategoryData> getAllCategories() {
        return categoryDAO.findAll()
                .stream()
                .map(category -> new CategoryData(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }
}
