package Application.model;

import Application.Application;

import java.util.List;

public class ResourceModel {
    public String type;
    public String name;
    public String category;
    public String description;
    public String technology;

    public List<PortModel> inputPorts;
    public List<PortModel> outputPorts;

    public List<OfferedServiceModel> offeredServices;
}
