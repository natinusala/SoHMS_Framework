package Application;

import Crosscutting.Precondition;
import mservice.MServiceSpecification;
import ProductManagement.ProductionOrder;
import ProductManagement.ProductionProcess;

import java.util.ArrayList;

public class ProductionProcessImpl implements ProductionProcess {
    private ArrayList<MServiceSpecification> services;
    private int state;

    public ProductionProcessImpl(ArrayList<MServiceSpecification> s)
    {
        this.services = s;
        state = 0;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    @Override
    public ArrayList<MServiceSpecification> getStateHist() {
        return null; //Unused
    }

    @Override
    public ArrayList<MServiceSpecification> getAlternatives() {
        return services;
    }

    @Override
    public ArrayList<MServiceSpecification> getAlternatives(ArrayList<MServiceSpecification> stateHist) {
        return null; //Unused
    }

    @Override
    public ArrayList<MServiceSpecification> getAlternatives(int stateID) {
        return null; //Unused
    }

    @Override
    public int evolve(MServiceSpecification serv) {
        if (services.get(state).getMServiceType().getName().equals(serv.getMServiceType().getName()))
            return ++state;
        else
            throw new RuntimeException("Wrong service executed : " + serv.getMServiceType().getName());
    }

    @Override
    public int evolve(int state, MServiceSpecification serv) {
        return 0; //Unused
    }

    @Override
    public ProductionProcess clone() {
        ProductionProcessImpl p = new ProductionProcessImpl((ArrayList<MServiceSpecification>) services.clone());
        p.setState(state);
        return p;
    }

    @Override
    public boolean isTerminated() {
        return state == services.size();
    }

    @Override
    public int getProcessID() {
        return 0; //Unused
    }

    @Override
    public int getStateID() {
        return state;
    }

    @Override
    public double getProgress() {
        return 0; //Unused
    }

    @Override
    public ArrayList<Precondition> getPreconditions() {
        return null; //Unused
    }
}