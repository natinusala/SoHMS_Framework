package Initialization;

import Initialization.Model.*;
import Ontology.ServiceOntology;
import OrdersManagement.ComInterface;
import OrdersManagement.OrderManager;
import ProductManagement.ProductionOrder;
import ProductManagement.ProductionProcess;
import ProductManagement.ProductionProcessImpl;
import ResourceManagement.ResourceHolon;
import com.google.gson.Gson;
import directoryFacilitator.DirectoryFacilitator;
import mservice.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Init {

    private static Gson gson = new Gson();

    private static ConcurrentHashMap<String,ServiceOntology> servOntologies = new ConcurrentHashMap<>(); // these are synchronized collections better than hashtable
    private static ConcurrentHashMap<String, OrderManager> orderManagerDict = new ConcurrentHashMap<>(); //List of Orders
    private static DirectoryFacilitator df;
    private static ArrayList<MService> mServices = new ArrayList<>();
    private static ArrayList<ResourceHolon> resourceCloud = new ArrayList<>();

    private static ComInterface comInterface;

    public static void launchAllOrders()
    {
        for (OrderManager orderManager : orderManagerDict.values())
        {
            orderManager.launch();
        }
    }

    public static ConcurrentHashMap<String, OrderManager> getOrderManagers()
    {
        return orderManagerDict;
    }

    public static void initializeSystems(ComInterface comInterface, String scenarioPath)
    {
        System.out.println("[INIT] Initialization...");

        Init.comInterface = comInterface;
        ScenarioModel scenario = gson.fromJson(readTextFile(scenarioPath), ScenarioModel.class);
        df = new DirectoryFacilitator();

        initServices(scenario);
        initResources(scenario);
        initProducts(scenario);
        initOrders(scenario);

        System.out.println("[INIT] Running system...");

        System.out.println("[DF] Initializing with " + resourceCloud.size() + " resources");
        df.setResourcesDirectory(resourceCloud);
        System.out.println("[DF] All " + resourceCloud.size() + " resources registered");
        df.generateServiceDirectory();
    }

    @SuppressWarnings("SameParameterValue")
    private static String readTextFile(String file) {
        StringBuilder chaine = new StringBuilder();
        try {
            InputStream ips = new FileInputStream(file);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne = br.readLine()) != null)
                chaine.append(ligne).append("\n");
            br.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return chaine.toString();
    }

    private static void initServices(ScenarioModel scenario)
    {
        System.out.println("Found " + scenario.servicesOntologies.size() + " services ontologie(s)");

        for (ServiceOntologyModel serviceOntology : scenario.servicesOntologies)
        {
            System.out.println("    " + serviceOntology.name + " : contains " + serviceOntology.services.size() + " service(s)");

            ServiceOntology s = new ServiceOntology();
            s.setName(serviceOntology.name);

            for (Map.Entry<String, ServiceModel> entry : serviceOntology.services.entrySet())
            {
                System.out.println("        - " + entry.getKey());

                //Service
                ServiceModel serviceModel = entry.getValue();

                MService service = new MService(
                        entry.getKey(),
                        serviceModel.ontology,
                        serviceModel.category,
                        serviceModel.taxonomy,
                        serviceModel.description
                );

                //Parameters
                for (ParameterModel parameter : serviceModel.parameters)
                {
                    SParameter p;
                    if (parameter.dataType != null && !parameter.dataType.isEmpty())
                    {
                        p = new SParameter(parameter.name, parameter.dataType);
                    }
                    else
                    {
                        p = new SParameter(parameter.name);
                    }
                    service.addParameter(p);
                }

                //Attributes
                for (AttributeModel attribute : serviceModel.attributes)
                {
                    SParameter p;
                    if (attribute.dataType != null && !attribute.dataType.isEmpty())
                    {
                        p = new SParameter(attribute.name, attribute.dataType);
                    }
                    else
                    {
                        p = new SParameter(attribute.name);
                    }
                    service.addAttribute(p);
                }

                mServices.add(service);
                s.addService(service);
                servOntologies.put(s.getName(), s);
            }
        }
    }

    private static void initResources(ScenarioModel scenario)
    {
        System.out.println("Found " + scenario.resources.size() + " resource(s)");

        for (ResourceModel resource : scenario.resources)
        {
            System.out.println("    " + resource.name + " : offers " + resource.offeredServices.size() + " service(s)");

            //Inputs outputs
            ArrayList<String> inputs = new ArrayList<>();
            for (PortModel input : resource.inputPorts)
            {
                inputs.add(input.value);
            }

            ArrayList<String> outputs = new ArrayList<>();
            for (PortModel output : resource.inputPorts)
            {
                outputs.add(output.value);
            }

            //Offered services
            MService selfService = null;
            ArrayList<MServiceImplentation> offeredServices = new ArrayList<>();
            for (OfferedServiceModel service : resource.offeredServices)
            {
                System.out.println("        - " + service.service);

                for (MService mService : mServices) {
                    if (mService.getName().trim().equals(service.service)) {
                        selfService = mService;
                    }
                }

                //Profile parameters
                ArrayList<ParametersProfile> parametersProfiles = new ArrayList<>();
                if (service.parametersProfile != null) {
                    for (ParameterProfileModel parametersProfileModel : service.parametersProfile) {
                        //Parameters profile
                        ArrayList<ProfileParameter> profileParameters = new ArrayList<>();

                        if (parametersProfileModel.profileParameters != null)
                        {
                            for (ProfileParameterModel profileParameter : parametersProfileModel.profileParameters) {
                                ArrayList<String> values = new ArrayList<>();

                                for (RangeValueModel value : profileParameter.rangeValues) {
                                    values.add(value.value);
                                }

                                ProfileParameter proparameter = new ProfileParameter(
                                        profileParameter.name,
                                        profileParameter.dataType,
                                        profileParameter.rangeType,
                                        values
                                );
                                profileParameters.add(proparameter);
                            }
                        }

                        //Methods
                        ArrayList<Integer> ids = new ArrayList<>();
                        for (MethodModel method : parametersProfileModel.methods) {
                            ids.add(method.id);
                        }
                        ParametersProfile pp = new ParametersProfile(profileParameters, ids);
                        parametersProfiles.add(pp);
                    }
                }

                //Associated methods
                HashSet<ProcessMethod> processMethods = new HashSet<>();
                for (MethodModel method : service.methods)
                {
                    ProcessMethod processMethod = new ProcessMethod(
                            method.processType,
                            method.id,
                            method.setupId
                    );
                    processMethods.add(processMethod);
                }

                MServiceImplentation mServiceImplentation = new MServiceImplentation(
                        selfService,
                        parametersProfiles,
                        processMethods,
                        inputs,
                        outputs,
                        service.averageCost
                );
                offeredServices.add(mServiceImplentation);
            }

            //Resource holon
            ResourceHolon rh = new ResourceHolon(
                    comInterface,
                    resource.name,
                    resource.technology,
                    resource.category,
                    resource.description,
                    inputs,
                    outputs,
                    offeredServices
            );
            rh.setPosition(resource.position);
            resourceCloud.add(rh);
        }
    }

    private static void initProducts(ScenarioModel scenario)
    {
        System.out.println("Found " + scenario.products.size() + " product(s)");

        for (ProductModel product : scenario.products)
        {
            System.out.println("    " + product.name + " (#" + product.id + ") : needs " + product.services.size() + " services");
            for (String service : product.services)
            {
                System.out.println("        - " + service);
            }
        }

        //TODO Init products?
    }

    private static void initOrders(ScenarioModel scenario)
    {
        System.out.println("Found " + scenario.orders.size() + " order(s)");

        for (OrderModel orderModel : scenario.orders)
        {
            System.out.println("    - Order #" + orderModel.id + " -> Product #" + orderModel.product + " x" + orderModel.numOfUnits);

            ArrayList<MServiceSpecification> serviceSpecifications = new ArrayList<>();

            ProductModel p = null;
            for (ProductModel tmp : scenario.products) {
                if (tmp.id == orderModel.product) {
                    p = tmp;
                    break;
                }
            }

            if (p == null) {
                System.out.println("Product " + orderModel.product+ " not found!");
                return;
            }

            for (String s : p.services)
            {
                for (MService service : mServices) {
                    if (service.getName().equals(s)) {
                        MServiceSpecification spec = new MServiceSpecification(service);
                        spec.setMService(service);
                        spec.setParameters(service.getParameters());
                        serviceSpecifications.add(spec);
                    }
                }
            }

            ProductionProcess process = new ProductionProcessImpl(serviceSpecifications);

            ProductionOrder order = new ProductionOrder(
                    orderModel.numOfUnits,
                    orderModel.priority,
                    orderModel.maxParallelUnits,
                    process,
                    "" //Unused TODO Move that to framework
            );

            OrderManager manager = new OrderManager(order, null);
            manager.setComInterface(comInterface);
            manager.setDf(df);

            orderManagerDict.put(Integer.toString(orderModel.id), manager);
        }
    }

}
