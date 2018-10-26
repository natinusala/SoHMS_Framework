package Application.model;

import java.util.List;

public class ServiceModel {
    public String id;
    public String ontology;
    public String category;
    public String taxonomy;
    public String description;

    public List<ParameterModel> parameters;
    public List<AttributeModel> attributes;
}
